package com.resumeanalyzer.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class ResumeService {

    // 🔹 Extract text (PDF + DOCX SAFE)
    public String extractText(MultipartFile file) {

        try {
            String name = file.getOriginalFilename();

            if (name == null) return "";

            if (name.endsWith(".pdf")) {
                PDDocument doc = PDDocument.load(file.getInputStream());
                return new PDFTextStripper().getText(doc);
            }

            else if (name.endsWith(".docx")) {

                XWPFDocument doc = new XWPFDocument(file.getInputStream());
                StringBuilder text = new StringBuilder();

                for (XWPFParagraph para : doc.getParagraphs()) {
                    text.append(para.getText()).append("\n");
                }

                return text.toString();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    // 🔹 Role Skills
    public List<String> getRoleSkills(String role) {

        Map<String, List<String>> roles = new HashMap<>();

        roles.put("genai", Arrays.asList(
                "python","machine learning","deep learning",
                "nlp","transformers","docker","aws","git"
        ));

        roles.put("backend", Arrays.asList(
                "java","spring","api","mysql","docker"
        ));

        roles.put("frontend", Arrays.asList(
                "html","css","javascript","react"
        ));

        return roles.getOrDefault(role, new ArrayList<>());
    }

    // 🔹 Match Skills
    public Map<String, List<String>> matchSkills(String text, List<String> roleSkills) {

        text = text.toLowerCase();

        List<String> matched = new ArrayList<>();
        List<String> missing = new ArrayList<>();

        for (String skill : roleSkills) {
            if (text.contains(skill)) matched.add(skill);
            else missing.add(skill);
        }

        Map<String, List<String>> result = new HashMap<>();
        result.put("matched", matched);
        result.put("missing", missing);

        return result;
    }

    // 🔹 Score
    public int calculateRoleScore(List<String> matched, List<String> total) {

        if (total.isEmpty()) return 0;

        return (int)(((double) matched.size() / total.size()) * 100);
    }

    // 🔹 Feedback
    public String generateRoleFeedback(String role, List<String> missing) {

        if (missing.isEmpty()) {
            return "🔥 Perfect match for " + role;
        }

        return "⚠️ Improve by adding: " + String.join(", ", missing);
    }
}