package com.project.CheatingDetectionProject.Dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * for representing a single question's response
 * **/
public class QuestionResponse {

    @NotNull(message = "Question number cannot be null")
    @Min(value = 1, message = "Question number must be positive")
    private Integer qnumber;

    @NotBlank(message = "Description cannot be blank")
    private String description;

    @NotNull(message = "Time taken cannot be null")
    @Min(value = 0, message = "Time taken cannot be negative")
    private Integer timeTaken;

    public Integer getQnumber() {
        return qnumber;
    }

    public void setQnumber(Integer qnumber) {
        this.qnumber = qnumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(Integer timeTaken) {
        this.timeTaken = timeTaken;
    }
}