package com.project.CheatingDetectionProject.Dtos;

import java.util.List;

public class QuestionAnalysis {

    private int qnumber;
    private double levenshteinSimilarity;
    private double sentenceTransformerSimilarity;
    private double timeRisk;
    private double timeMin;
    private List<String> similarWords;

    public int getQnumber() {
        return qnumber;
    }

    public void setQnumber(int qnumber) {
        this.qnumber = qnumber;
    }

    public double getLevenshteinSimilarity() {
        return levenshteinSimilarity;
    }

    public void setLevenshteinSimilarity(double levenshteinSimilarity) {
        this.levenshteinSimilarity = levenshteinSimilarity;
    }

    public double getSentenceTransformerSimilarity() {
        return sentenceTransformerSimilarity;
    }

    public void setSentenceTransformerSimilarity(double sentenceTransformerSimilarity) {
        this.sentenceTransformerSimilarity = sentenceTransformerSimilarity;
    }

    public double getTimeRisk() {
        return timeRisk;
    }

    public void setTimeRisk(double timeRisk) {
        this.timeRisk = timeRisk;
    }

    public double getTimeMin() {
        return timeMin;
    }

    public void setTimeMin(double timeMin) {
        this.timeMin = timeMin;
    }

    public List<String> getSimilarWords() {
        return similarWords;
    }

    public void setSimilarWords(List<String> similarWords) {
        this.similarWords = similarWords;
    }
}