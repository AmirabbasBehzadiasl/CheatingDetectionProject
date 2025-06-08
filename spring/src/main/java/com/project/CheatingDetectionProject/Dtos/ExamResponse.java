package com.project.CheatingDetectionProject.Dtos;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Map;

public class ExamResponse {

    @NotEmpty(message = "Responses cannot be empty")
    private Map<String, List<QuestionResponse>> responses;

    public Map<String, List<QuestionResponse>> getResponses() {
        return responses;
    }

    public void setResponses(Map<String, List<QuestionResponse>> responses) {
        this.responses = responses;
    }
}