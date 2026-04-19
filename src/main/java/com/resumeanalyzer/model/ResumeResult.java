package com.resumeanalyzer.model;

import java.util.*;

public class ResumeResult {

    private int score;
    private String feedback;
    private Map<String, Integer> categories;
    private Map<String, List<String>> skills;
    private String roadmap;

    public ResumeResult(int score, String feedback,
                        Map<String, Integer> categories,
                        Map<String, List<String>> skills,
                        String roadmap) {
        this.score = score;
        this.feedback = feedback;
        this.categories = categories;
        this.skills = skills;
        this.roadmap = roadmap;
    }

    public int getScore() { return score; }
    public String getFeedback() { return feedback; }
    public Map<String, List<String>> getSkills() { return skills; }
    public String getRoadmap() { return roadmap; }
}