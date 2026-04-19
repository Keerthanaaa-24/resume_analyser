package com.resumeanalyzer.controller;

import com.resumeanalyzer.model.ResumeResult;
import com.resumeanalyzer.service.ResumeService;
import com.resumeanalyzer.service.AIService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@CrossOrigin
public class ResumeController {

    @Autowired
    private ResumeService service;

    @Autowired
    private AIService aiService;

    @PostMapping("/analyze")
    public ResumeResult analyze(
            @RequestParam("file") MultipartFile file,
            @RequestParam("role") String role) {

        try {
            // Extract resume text
            String text = service.extractText(file);

            // Get role skills
            List<String> roleSkills = service.getRoleSkills(role);

            // Match skills
            Map<String, List<String>> skills =
                    service.matchSkills(text, roleSkills);

            // Score
            int score = service.calculateRoleScore(
                    skills.get("matched"), roleSkills);

            // Missing skills string
            String missing = String.join(", ", skills.get("missing"));

            // 🔥 AI Feedback
            String feedback = aiService.generateAIResponse(
                    role,
                    score,
                    missing
            );

            return new ResumeResult(
                    score,
                    feedback,
                    Map.of(),
                    skills,
                    "" // roadmap not used now
            );

        } catch (Exception e) {
            e.printStackTrace();

            return new ResumeResult(
                    0,
                    "Error analyzing resume",
                    Map.of(),
                    Map.of("matched", List.of(), "missing", List.of()),
                    ""
            );
        }
    }
}