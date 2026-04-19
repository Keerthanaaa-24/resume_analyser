package com.resumeanalyzer.service;

import org.springframework.stereotype.Service;

@Service
public class AIService {

    public String analyze(String text) {

        // 🔒 Safe fallback (no API required)
        return "AI analysis is currently disabled. Improve your resume by adding more relevant skills and projects.";
    }
}