package com.project.CheatingDetectionProject.Dtos;

import java.util.List;

public class QuestionAnalysis {

    private int qnumber;
    private double levenshteinSimilarity;
    private double sentenceTransformerSimilarity;
    private double SuspiciousTimeDifference;
    private double SuspiciousTimeStudent1;
    private double SuspiciousTimeStudent2;
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

    public double getSuspiciousTimeDifference() {
        return SuspiciousTimeDifference;
    }

    public void setSuspiciousTimeDifference(double suspiciousTimeDifference) {
        this.SuspiciousTimeDifference = suspiciousTimeDifference;
    }

    public double getSuspiciousTimeStudent1() {
        return SuspiciousTimeStudent1;
    }

    public void setSuspiciousTimeStudent1(double suspiciousTimeStudent1) {
        this.SuspiciousTimeStudent1 = suspiciousTimeStudent1;
    }

    public List<String> getSimilarWords() {
        return similarWords;
    }

    public void setSimilarWords(List<String> similarWords) {
        this.similarWords = similarWords;
    }

    public double getSuspiciousTimeStudent2() {
        return SuspiciousTimeStudent2;
    }

    public void setSuspiciousTimeStudent2(double suspiciousTimeStudent2) {
        SuspiciousTimeStudent2 = suspiciousTimeStudent2;
    }
}