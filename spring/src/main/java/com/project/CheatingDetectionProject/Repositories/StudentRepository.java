package com.project.CheatingDetectionProject.Repositories;

import com.project.CheatingDetectionProject.Models.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    void deleteByName(String name);

    Optional<Student> findStudentByName(String studentName);
}
