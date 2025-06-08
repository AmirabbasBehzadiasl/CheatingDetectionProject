package com.project.CheatingDetectionProject.Dtos;

import java.util.List;

public class FraudAnalysisResult {
    private String participant1;
    private String participant2;
    private List<QuestionAnalysis> questionAnalyses;

    public String getParticipant1() {
        return participant1;
    }

    public void setParticipant1(String participant1) {
        this.participant1 = participant1;
    }

    public String getParticipant2() {
        return participant2;
    }

    public void setParticipant2(String participant2) {
        this.participant2 = participant2;
    }

    public List<QuestionAnalysis> getQuestionAnalyses() {
        return questionAnalyses;
    }

    public void setQuestionAnalyses(List<QuestionAnalysis> questionAnalyses) {
        this.questionAnalyses = questionAnalyses;
    }
}