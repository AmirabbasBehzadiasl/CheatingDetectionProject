package com.project.CheatingDetectionProject.Services;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class SentenceTransformerClient {
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String API_URL = "http://localhost:8000/similarity";

    public double getSimilarity(String text1, String text2) {
        Map<String, String> request = new HashMap<>();
        request.put("text1", text1);
        request.put("text2", text2);

        ResponseEntity<Map> response = restTemplate.postForEntity(API_URL, request, Map.class);
        Map responseBody = response.getBody();

        if (responseBody != null && responseBody.containsKey("similarity")) {
            return ((Number) responseBody.get("similarity")).doubleValue();
        } else {
            throw new RuntimeException("Invalid response from SentenceTransformer service");
        }
    }
}