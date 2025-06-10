package com.project.CheatingDetectionProject.Dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * for representing a single question's response
 * **/
public class QuestionResponse {

    @NotNull(message = "Question number cannot be null")
    @Min(value = 1, message = "Question number must be positive")
    private Integer qnumber;

    @NotBlank(message = "Description cannot be blank")
    private String description;

    @NotNull(message = "start time is required")
    private LocalDateTime startTime;

    @NotNull(message = "end time is required")
    private LocalDateTime endTime;


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

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

}