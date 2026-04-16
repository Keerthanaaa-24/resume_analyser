package com.resumeanalyzer.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class AIService {

    @Value("${openai.api.key}")
    private String apiKey;

    private final WebClient webClient =
            WebClient.create("https://api.openai.com/v1/chat/completions");

    public String getAIAnalysis(String resumeText) {

        try {
            Map<String, Object> request = Map.of(
                    "model", "gpt-4o-mini",
                    "messages", List.of(
                            Map.of("role", "user",
                                   "content", "Give 3 short suggestions to improve this resume:\n" + resumeText)
                    )
            );

            Map response = webClient.post()
                    .header("Authorization", "Bearer " + apiKey)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            List choices = (List) response.get("choices");
            Map choice = (Map) choices.get(0);
            Map message = (Map) choice.get("message");

            return message.get("content").toString();

        } catch (Exception e) {
            return "AI feedback unavailable.";
        }
    }
}