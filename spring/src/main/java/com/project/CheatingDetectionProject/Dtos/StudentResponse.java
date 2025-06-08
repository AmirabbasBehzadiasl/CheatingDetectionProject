package com.project.CheatingDetectionProject.Dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

import java.util.List;

/**
 * DTO for representing a single student's exam responses.
 **/

public class StudentResponse {

    @NotBlank(message = "Student name is required")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "name should have only lowercase alphabet characters")
    private String name;

    @NotEmpty(message = "answers cannot be empty")
    private List<QuestionResponse> answers;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<QuestionResponse> getAnswers() {
        return answers;
    }

    public void setAnswers(List<QuestionResponse> answers) {
        this.answers = answers;
    }
}