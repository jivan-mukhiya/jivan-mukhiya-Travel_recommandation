package com.texas.traveldestinationrecommendation.algorithm;

import com.texas.traveldestinationrecommendation.model.Destination;
import com.texas.traveldestinationrecommendation.model.User;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ContentBasedRecommender implements RecommendationStrategy {
    @Override
    public List<Destination> recommend(User user, List<Destination> allDestinations) {
        Map<Destination, Double> destinationScores = new HashMap<>();

        for (Destination destination : allDestinations) {
            double score = calculateContentSimilarity(user, destination);
            destinationScores.put(destination, score);
        }

        return destinationScores.entrySet().stream()
                .sorted(Map.Entry.<Destination, Double>comparingByValue().reversed())
                .limit(5)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private double calculateContentSimilarity(User user, Destination destination) {
        double score = 0.0;

        // Season preference matching
        if (user.getSeasonPreference() != null && destination.getBestSeasonToVisit() != null) {
            if (user.getSeasonPreference().equalsIgnoreCase(destination.getBestSeasonToVisit())) {
                score += 0.3;
            }
        }

        // Budget matching
        if (user.getBudgetMin() != null && user.getBudgetMax() != null) {
            double budgetMid = (user.getBudgetMin() + user.getBudgetMax()) / 2;
            double budgetDiff = Math.abs(budgetMid - destination.getCostPerDay());
            double budgetScore = 1.0 - (budgetDiff / budgetMid);
            score += Math.max(0, budgetScore * 0.4);
        }

        // Travel type preference matching
        if (user.getTravelTypePreference() != null && destination.getType() != null) {
            if (user.getTravelTypePreference().equalsIgnoreCase(destination.getType())) {
                score += 0.2;
            }
        }

        // Activity tags matching
        if (user.getPreferences() != null && destination.getActivityTags() != null) {
            long matchingTags = user.getPreferences().stream()
                    .filter(pref -> destination.getActivityTags().contains(pref))
                    .count();
            score += (matchingTags * 0.1);
        }

        return score;
    }
}
