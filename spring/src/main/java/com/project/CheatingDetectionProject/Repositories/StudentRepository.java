package com.project.CheatingDetectionProject.Repositories;

import com.project.CheatingDetectionProject.Models.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
