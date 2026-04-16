package com.resumeanalyzer.model;

import java.util.Map;
import java.util.List;

public class ResumeResult {

    private int score;
    private String feedback;
    private Map<String, Integer> categories;
    private Map<String, List<String>> skills;

    public ResumeResult(int score, String feedback,
                        Map<String, Integer> categories,
                        Map<String, List<String>> skills) {
        this.score = score;
        this.feedback = feedback;
        this.categories = categories;
        this.skills = skills;
    }

    public int getScore() { return score; }
    public String getFeedback() { return feedback; }
    public Map<String, Integer> getCategories() { return categories; }
    public Map<String, List<String>> getSkills() { return skills; }
}