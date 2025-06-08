package com.project.CheatingDetectionProject.Services;

import com.project.CheatingDetectionProject.Dtos.FraudAnalysisResult;
import com.project.CheatingDetectionProject.Dtos.QuestionAnalysis;
import com.project.CheatingDetectionProject.Dtos.QuestionResponse;
import com.project.CheatingDetectionProject.Dtos.StudentResponse;
import com.project.CheatingDetectionProject.Exceptions.AlreadyExistException;
import com.project.CheatingDetectionProject.Exceptions.NotFoundException;
import com.project.CheatingDetectionProject.Mapper.StudentMapper;
import com.project.CheatingDetectionProject.Models.*;
import com.project.CheatingDetectionProject.Repositories.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service for fraud detection by processing and analyzing exam responses.
 */
@Service
@Transactional
public class FraudDetectionService {
    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final SimilarityService similarityService;
//    private final SentenceTransformerClient sentenceTransformerClient;

    public FraudDetectionService(StudentRepository studentRepository,
                                 StudentMapper studentMapper,
                                 SimilarityService similarityService

                                 ) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
        this.similarityService = similarityService;
//        this.sentenceTransformerClient = sentenceTransformerClient;
    }

    /**
     * Delete a single student with her/his exam
     * **/
    public void deleteStudentByName(Long id) {
        Student student = studentRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException("Student with id "+ id + " not found"));
        studentRepository.delete(student);
    }

    /**
     * Saves a single student's exam responses to the database.
     * @param studentResponse Input DTO containing a student's responses
     */

    public void saveStudentResponses(StudentResponse studentResponse) {
        Optional<Student> existingStudent = studentRepository.findStudentByName(studentResponse.getName());

        if (existingStudent.isPresent()) {
            throw new AlreadyExistException("Student with name " + studentResponse.getName() + " already exists.");
        }
        Student newStudent = studentMapper.toStudent(studentResponse);

        List<Answers> answers = new ArrayList<>();
        for (QuestionResponse qr : studentResponse.getResponses()) {
            Answers answer = studentMapper.toAnswer(qr, newStudent);
            answers.add(answer);
        }
        newStudent.setAnswers(answers);

        studentRepository.save(newStudent);
    }

    /**
     * Analyzes stored responses for fraud detection.
     * @return List of fraud analysis results for participant pairs
     **/

    public List<FraudAnalysisResult> analyze() {
        List<Student> students = studentRepository.findAll();
        List<FraudAnalysisResult> results = new ArrayList<>();

        for (int i = 0; i < students.size(); i++) {
            for (int j = i + 1; j < students.size(); j++) {
                Student student1 = students.get(i);
                Student student2 = students.get(j);

                FraudAnalysisResult result = new FraudAnalysisResult();
                result.setStudent1(student1.getName());
                result.setStudent2(student2.getName());
                List<QuestionAnalysis> questionAnalyses = new ArrayList<>();

                for (Answers answer1 : student1.getAnswers()) {
                    for (Answers answer2 : student2.getAnswers()) {
                        if (answer1.getQnumber().equals(answer2.getQnumber())) {
                            QuestionAnalysis analysis = new QuestionAnalysis();
                            analysis.setQnumber(answer1.getQnumber());
                            analysis.setLevenshteinSimilarity(
                                    similarityService.calculateLevenshteinSimilarity(
                                            answer1.getDescription(), answer2.getDescription()));
//                            analysis.setSentenceTransformerSimilarity(
//                                    sentenceTransformerClient.getSimilarity(
//                                            answer1.getDescription(), answer2.getDescription()));
                            analysis.setTimeRisk(calculateTimeRisk(answer1.getTimeTaken(), answer2.getTimeTaken()));
                            analysis.setTimeMin(calculateTimeMin(answer1.getTimeTaken()));
                            analysis.setSimilarWords(findSimilarWords(answer1.getDescription(), answer2.getDescription()));
                            questionAnalyses.add(analysis);
                        }
                    }
                }
                result.setQuestionAnalyses(questionAnalyses);
                results.add(result);
            }
        }

        return results;
    }

    private double calculateTimeRisk(int time1, int time2) {
        return Math.abs(time1 - time2) < 5 ? 0.8 : 0.2;
    }

    private double calculateTimeMin(int time) {
        return time < 10 ? 0.9 : (time < 20 ? 0.5 : 0.1);
    }

    private List<String> findSimilarWords(String s1, String s2) {
        List<String> words1 = List.of(s1.toLowerCase().split("\\s+"));
        List<String> words2 = List.of(s2.toLowerCase().split("\\s+"));
        return words1.stream().filter(words2::contains).toList();
    }
}