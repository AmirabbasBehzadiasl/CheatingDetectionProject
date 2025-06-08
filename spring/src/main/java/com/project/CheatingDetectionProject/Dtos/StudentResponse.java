package com.project.CheatingDetectionProject.Dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

/**
 * DTO for representing a single student's exam responses.
 */
public class StudentResponse {
    @NotBlank(message = "Student name cannot be blank")
    private String name;

    @NotEmpty(message = "Responses cannot be empty")
    private List<QuestionResponse> responses;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<QuestionResponse> getResponses() {
        return responses;
    }

    public void setResponses(List<QuestionResponse> responses) {
        this.responses = responses;
    }
}