package com.project.CheatingDetectionProject.Services;

import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.stereotype.Service;

/**
 * Service for calculating Levenshtein similarity between two strings.
 */
@Service
public class SimilarityService {

    /**
     * Calculates normalized Levenshtein similarity between two strings (0.0 to 1.0).
     * @param a First string
     * @param b Second string
     * @return Similarity score (1.0 = identical, 0.0 = completely different)
     * @throws IllegalArgumentException if inputs are null
     */
    public double calculateLevenshteinSimilarity(String a, String b) {
        if (a == null || b == null) {
            throw new IllegalArgumentException("Input strings cannot be null");
        }

        LevenshteinDistance distance = new LevenshteinDistance();
        int rawDistance = distance.apply(a, b);
        int maxLength = Math.max(a.length(), b.length());

        return maxLength == 0 ? 1.0 : 1.0 - ((double) rawDistance / maxLength);
    }
}