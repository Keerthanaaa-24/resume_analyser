package com.resumeanalyzer.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.*;
import java.nio.charset.StandardCharsets;

@Service
public class AIService {

    @Value("${openai.api.key:}")
    private String apiKey;

    public String generateAIResponse(String role, int score, String missingSkills) {

        // ✅ Fallback (no API)
        if (apiKey == null || apiKey.isEmpty()) {
            return "Your ATS score is " + score +
                    ". Improve by adding: " + missingSkills +
                    ". Add projects and measurable achievements.";
        }

        try {
            String prompt = "You are a resume expert. Analyze this:\n" +
                    "Role: " + role + "\n" +
                    "ATS Score: " + score + "\n" +
                    "Missing Skills: " + missingSkills + "\n\n" +
                    "Give professional feedback and improvement tips.";

            String requestBody = """
            {
              "model": "gpt-4.1-mini",
              "input": "%s"
            }
            """.formatted(prompt.replace("\"", "\\\""));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.openai.com/v1/responses"))
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            return response.body();

        } catch (Exception e) {
            e.printStackTrace();
            return "AI feedback unavailable. Improve by adding: " + missingSkills;
        }
    }
}