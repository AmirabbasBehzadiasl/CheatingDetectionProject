package com.project.CheatingDetectionProject.Repositories;

import com.project.CheatingDetectionProject.Models.Answers;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answers, Long> {
}
