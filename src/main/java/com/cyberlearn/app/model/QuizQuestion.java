package com.cyberlearn.app.model;

import java.util.List;

public class QuizQuestion {
    private String prompt;
    private List<String> options;
    private int correctIndex;

    public QuizQuestion() {}

    public QuizQuestion(String prompt, List<String> options, int correctIndex) {
        this.prompt = prompt;
        this.options = options;
        this.correctIndex = correctIndex;
    }

    public String getPrompt() { return prompt; }
    public List<String> getOptions() { return options; }
    public int getCorrectIndex() { return correctIndex; }
}
