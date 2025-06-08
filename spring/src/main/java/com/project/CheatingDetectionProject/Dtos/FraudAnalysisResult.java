package com.project.CheatingDetectionProject.Dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class FraudAnalysisResult {
    private String student1;
    private String student2;
    private List<QuestionAnalysis> questionAnalyses;

    public String getStudent1() {
        return student1;
    }

    public void setStudent1(String student1) {
        this.student1 = student1;
    }

    public String getStudent2() {
        return student2;
    }

    public void setStudent2(String student2) {
        this.student2 = student2;
    }

    public List<QuestionAnalysis> getQuestionAnalyses() {
        return questionAnalyses;
    }

    public void setQuestionAnalyses(List<QuestionAnalysis> questionAnalyses) {
        this.questionAnalyses = questionAnalyses;
    }
}