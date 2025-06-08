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
    @Mapping(source = "answers", target = "answers",ignore = true)
    Student toStudent(StudentResponse studentResponse);

    /**
     * Maps QuestionResponse to Answers entity.
     * @param questionResponse Input DTO for a response
     * @return Answers entity
     */
    @Mapping(source = "qnumber", target = "qnumber")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "startTime", target = "startTime")
    @Mapping(source = "endTime", target = "endTime")
    @Mapping(target = "student" , ignore = true)
    Answers toAnswer(QuestionResponse questionResponse);


}