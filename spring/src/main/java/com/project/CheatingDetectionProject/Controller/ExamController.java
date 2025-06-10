package com.project.CheatingDetectionProject.Controller;

import com.project.CheatingDetectionProject.Dtos.FraudAnalysisResult;
import com.project.CheatingDetectionProject.Dtos.StudentResponse;
import com.project.CheatingDetectionProject.Services.FraudDetectionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exams")
@CrossOrigin(origins = {"http://127.0.0.1:5500", "http://localhost:5500"})
public class ExamController {

    private final FraudDetectionService fraudDetectionService;

    public ExamController(FraudDetectionService fraudDetectionService) {
        this.fraudDetectionService = fraudDetectionService;
    }

    /**
     * Endpoint for submitting student responses after an exam.
     * This method will be called when the user clicks "End Exam" or "Submit".
     *
     * @param studentResponse The DTO containing student's name and their answers.
     * @return ResponseEntity indicating success or failure.
     */

    @PostMapping("/submit")
    public ResponseEntity<String> submitExamResponses(@Valid @RequestBody StudentResponse studentResponse) {
            fraudDetectionService.saveStudentResponses(studentResponse);
            return new ResponseEntity<>("Student responses submitted successfully!", HttpStatus.CREATED);
    }

    /**
     * Endpoint for deleting a student's exam responses by their ID.
     * This method will remove all recorded responses associated with the given student ID.
     * If the student with the specified ID is not found, an appropriate error will be returned.
     *
     * @param id The unique identifier (ID) of the student whose responses are to be deleted.
     * @return ResponseEntity with HttpStatus.NO_CONTENT if deletion is successful,
     * or an appropriate error status (e.g., HttpStatus.NOT_FOUND) if the student is not found,
     * or HttpStatus.INTERNAL_SERVER_ERROR for other issues.
     */

    @DeleteMapping("/deleteStudentById")
    public ResponseEntity<?> deleteExamResponses(@RequestParam Long id) {
        fraudDetectionService.deleteStudentByName(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Endpoint for triggering the fraud analysis.
     * This method will be called when the user clicks "Analyze Cheating".
     *
     * @return ResponseEntity containing a list of fraud analysis results.
     */
    @GetMapping("/analyze")
    public ResponseEntity<List<FraudAnalysisResult>> analyzeExamResponses() {
        List<FraudAnalysisResult> results = fraudDetectionService.analyze();
        if (results.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(results, HttpStatus.OK);
    }
}