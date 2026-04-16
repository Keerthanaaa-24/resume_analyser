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

    // 🔹 Extract text (PDF + DOCX FIXED)
    public String extractText(MultipartFile file) {

        try {
            String name = file.getOriginalFilename();

            // PDF
            if (name != null && name.endsWith(".pdf")) {
                PDDocument doc = PDDocument.load(file.getInputStream());
                return new PDFTextStripper().getText(doc);
            }

            // DOCX (SAFE — no extractor)
            else if (name != null && name.endsWith(".docx")) {

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

    // 🔹 Role Requirements (EXTENDED 🔥)
    public List<String> getRoleSkills(String role) {

        Map<String, List<String>> roles = new HashMap<>();

        roles.put("genai", Arrays.asList(
                "python","machine learning","deep learning",
                "nlp","transformers","openai","langchain",
                "docker","aws","cloud","git","llm","rag"
        ));

        roles.put("datascience", Arrays.asList(
                "python","pandas","numpy","statistics",
                "machine learning","sql","data visualization"
        ));

        roles.put("ml", Arrays.asList(
                "python","tensorflow","pytorch",
                "deep learning","model deployment"
        ));

        roles.put("backend", Arrays.asList(
                "java","spring","spring boot","api",
                "mysql","mongodb","docker","aws","git"
        ));

        roles.put("frontend", Arrays.asList(
                "html","css","javascript","react","angular","ui","ux"
        ));

        roles.put("fullstack", Arrays.asList(
                "java","spring","react","html","css","api","mysql"
        ));

        roles.put("devops", Arrays.asList(
                "docker","kubernetes","ci/cd","jenkins","linux","aws"
        ));

        roles.put("cloud", Arrays.asList(
                "aws","azure","gcp","cloud architecture","docker"
        ));

        roles.put("mobile", Arrays.asList(
                "android","kotlin","flutter","react native"
        ));

        roles.put("cyber", Arrays.asList(
                "security","ethical hacking","penetration testing","encryption"
        ));

        roles.put("qa", Arrays.asList(
                "testing","selenium","automation testing","jira"
        ));

        roles.put("uiux", Arrays.asList(
                "figma","ui design","ux design","wireframing","prototyping"
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

    // 🔹 Score Calculation
    public int calculateRoleScore(List<String> matched, List<String> total) {

        if (total.isEmpty()) return 0;

        return (int)(((double) matched.size() / total.size()) * 100);
    }

    // 🔹 Feedback
    public String generateRoleFeedback(String role, List<String> missing) {

        if (missing.isEmpty()) {
            return "🔥 Perfect match for " + role + " role!";
        }

        return "⚠️ Improve for " + role + " role by learning: "
                + String.join(", ", missing);
    }
}