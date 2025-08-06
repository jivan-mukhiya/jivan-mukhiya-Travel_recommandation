package com.texas.traveldestinationrecommendation.algorithm;

import com.texas.traveldestinationrecommendation.model.Destination;
import com.texas.traveldestinationrecommendation.model.DestinationRating;
import com.texas.traveldestinationrecommendation.model.User;
import com.texas.traveldestinationrecommendation.repository.DestinationRatingRepository;
import com.texas.traveldestinationrecommendation.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CollaborativeRecommender implements RecommendationStrategy {


    private final UserRepository userRepository;
    private final DestinationRatingRepository ratingRepository;

    @Override
    public List<Destination> recommend(User user, List<Destination> allDestinations) {
        List<User> allUsers = userRepository.findAll();
        Map<User, Double> similarUsers = new HashMap<>();

        for (User otherUser : allUsers) {
            if (!otherUser.getUserId().equals(user.getUserId())) {
                double similarity = calculateUserSimilarity(user, otherUser);
                if (similarity > 0.3) {
                    similarUsers.put(otherUser, similarity);
                }
            }
        }

        Map<Destination, Double> destinationScores = new HashMap<>();

        for (Map.Entry<User, Double> entry : similarUsers.entrySet()) {
            User similarUser = entry.getKey();
            double similarityScore = entry.getValue();

            List<DestinationRating> ratings = ratingRepository.findByUser(similarUser);
            for (DestinationRating rating : ratings) {
                if (rating.getRating() >= 4.0) {
                    Destination dest = rating.getDestination();
                    if (!user.getPastVisitedDestinations().contains(dest.getName())) {
                        destinationScores.merge(dest, similarityScore * rating.getRating(), Double::sum);
                    }
                }
            }
        }

        return destinationScores.entrySet().stream()
                .sorted(Map.Entry.<Destination, Double>comparingByValue().reversed())
                .limit(5)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private double calculateUserSimilarity(User user1, User user2) {
        double similarity = 0.0;

        if (user1.getGender() != null && user2.getGender() != null &&
                user1.getGender().equals(user2.getGender())) {
            similarity += 0.1;
        }

        if (user1.getProfession() != null && user2.getProfession() != null &&
                user1.getProfession().equals(user2.getProfession())) {
            similarity += 0.1;
        }

        if (user1.getTravelTypePreference() != null && user2.getTravelTypePreference() != null &&
                user1.getTravelTypePreference().equals(user2.getTravelTypePreference())) {
            similarity += 0.2;
        }

        if (user1.getSeasonPreference() != null && user2.getSeasonPreference() != null &&
                user1.getSeasonPreference().equals(user2.getSeasonPreference())) {
            similarity += 0.1;
        }

        if (user1.getBudgetMin() != null && user1.getBudgetMax() != null &&
                user2.getBudgetMin() != null && user2.getBudgetMax() != null) {
            double overlap = Math.min(user1.getBudgetMax(), user2.getBudgetMax()) -
                    Math.max(user1.getBudgetMin(), user2.getBudgetMin());
            if (overlap > 0) {
                similarity += (overlap / (user1.getBudgetMax() - user1.getBudgetMin())) * 0.2;
            }
        }

        if (user1.getPreferences() != null && user2.getPreferences() != null) {
            Set<String> intersection = new HashSet<>(user1.getPreferences());
            intersection.retainAll(user2.getPreferences());
            Set<String> union = new HashSet<>(user1.getPreferences());
            union.addAll(user2.getPreferences());
            if (!union.isEmpty()) {
                similarity += ((double) intersection.size() / union.size()) * 0.3;
            }
        }

        return similarity;
    }
}
