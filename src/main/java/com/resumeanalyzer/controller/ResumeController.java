package com.resumeanalyzer.controller;

import com.resumeanalyzer.model.ResumeResult;
import com.resumeanalyzer.service.ResumeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@CrossOrigin
public class ResumeController {

    @Autowired
    private ResumeService service;

    @PostMapping("/analyze")
    public ResumeResult analyze(
            @RequestParam("file") MultipartFile file,
            @RequestParam("role") String role) {

        try {
            String text = service.extractText(file);

            if (text == null || text.isEmpty()) {
                return new ResumeResult(
                        0,
                        "❌ Could not read resume file",
                        Map.of(),
                        Map.of("matched", List.of(), "missing", List.of())
                );
            }

            List<String> roleSkills = service.getRoleSkills(role);

            Map<String, List<String>> skills =
                    service.matchSkills(text, roleSkills);

            int score = service.calculateRoleScore(
                    skills.get("matched"), roleSkills);

            String feedback = service.generateRoleFeedback(
                    role, skills.get("missing"));

            return new ResumeResult(score, feedback, Map.of(), skills);

        } catch (Exception e) {
            e.printStackTrace();

            return new ResumeResult(
                    0,
                    "❌ Server error while analyzing resume",
                    Map.of(),
                    Map.of("matched", List.of(), "missing", List.of())
            );
        }
    }
}