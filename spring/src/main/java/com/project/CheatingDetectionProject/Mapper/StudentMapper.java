package com.project.CheatingDetectionProject.Mapper;


import com.project.CheatingDetectionProject.Dtos.QuestionResponse;
import com.project.CheatingDetectionProject.Dtos.StudentResponse;
import com.project.CheatingDetectionProject.Models.Answers;
import com.project.CheatingDetectionProject.Models.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

/**
 * Mapper for converting StudentResponse DTO to Student and Answers entities.
 */
@Mapper(componentModel = "spring")
public interface StudentMapper {

    /**
     * Maps StudentResponse to a Student entity.
     * @param studentResponse Input DTO for a single student
     * @return Student entity with associated Answers
     */
    @Mapping(source = "name", target = "name")
    @Mapping(source = "responses", target = "answers",ignore = true)
    Student toStudent(StudentResponse studentResponse);

    /**
     * Maps QuestionResponse to Answers entity.
     * @param questionResponse Input DTO for a response
     * @param student Associated Student entity
     * @return Answers entity
     */
    @Mapping(source = "questionResponse.qnumber", target = "qnumber")
    @Mapping(source = "questionResponse.description", target = "description")
    @Mapping(source = "questionResponse.timeTaken", target = "timeTaken")
    @Mapping(source = "student", target = "student" , ignore = true)
    Answers toAnswer(QuestionResponse questionResponse, Student student);


}