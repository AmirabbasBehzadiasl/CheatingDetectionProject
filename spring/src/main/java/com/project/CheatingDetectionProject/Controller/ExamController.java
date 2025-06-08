package com.project.CheatingDetectionProject.Controller;

import com.project.CheatingDetectionProject.Dtos.FraudAnalysisResult;
import com.project.CheatingDetectionProject.Dtos.StudentResponse;
import com.project.CheatingDetectionProject.Services.FraudDetectionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exams")
@CrossOrigin(origins = "http://127.0.0.1:5500/")
public class ExamController {

    private final FraudDetectionService fraudDetectionService;

    public ExamController(FraudDetectionService fraudDetectionService) {
        this.fraudDetectionService = fraudDetectionService;
    }

    /**
     * Endpoint for submitting student responses after an exam.
     * This method will be called when the user clicks "End Exam" or "Submit".
     * @param studentResponse The DTO containing student's name and their answers.
     * @return ResponseEntity indicating success or failure.
     */
    @PostMapping("/submit")
    public ResponseEntity<String> submitExamResponses(@RequestBody StudentResponse studentResponse) {
        try {
            fraudDetectionService.saveStudentResponses(studentResponse);
            return new ResponseEntity<>("Student responses submitted successfully!", HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error submitting student responses: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint for triggering the fraud analysis.
     * This method will be called when the user clicks "Analyze Cheating".
     * @return ResponseEntity containing a list of fraud analysis results.
     */
    @GetMapping("/analyze")
    public ResponseEntity<List<FraudAnalysisResult>> analyzeExamResponses() {
        try {
            List<FraudAnalysisResult> results = fraudDetectionService.analyze();
            if (results.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(results, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}