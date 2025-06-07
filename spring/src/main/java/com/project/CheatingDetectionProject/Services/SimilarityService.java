package com.project.CheatingDetectionProject.Services;

import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.stereotype.Service;

@Service
public class SimilarityService {

    public double calculateLevenshteinSimilarity(String a, String b) {
        LevenshteinDistance distance = new LevenshteinDistance();
        int rawDistance = distance.apply(a, b);
        int maxLength = Math.max(a.length(), b.length());

        if (maxLength == 0) return 1.0;

        return 1.0 - ((double) rawDistance / maxLength);
    }

}
