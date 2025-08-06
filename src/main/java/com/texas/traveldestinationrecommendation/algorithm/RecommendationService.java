package com.texas.traveldestinationrecommendation.algorithm;

import com.texas.traveldestinationrecommendation.dto.DestinationDto;
import com.texas.traveldestinationrecommendation.model.Destination;
import com.texas.traveldestinationrecommendation.model.Recommendation;
import com.texas.traveldestinationrecommendation.model.User;
import com.texas.traveldestinationrecommendation.repository.DestinationRepository;
import com.texas.traveldestinationrecommendation.repository.RecommendationRepository;
import com.texas.traveldestinationrecommendation.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationService {
    private static final int MAX_RECOMMENDATIONS = 8;

    private final DestinationRepository destinationRepository;
    private final UserRepository userRepository;
    private final RecommendationRepository recommendationRepository;
    private final ContentBasedRecommender contentBased;
    private final CollaborativeRecommender collaborative;
    private final HybridRecommender hybrid;

    public List<DestinationDto> getRecommendationsForUser(Long userId) {

        User user = userRepository.findUserByUserLogin_Id(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found with id: " + userId
                ));

        List<Destination> allDestinations = destinationRepository.findAll();

        // Get recommendations from all strategies
        List<Destination> contentBasedRecs = contentBased.recommend(user, allDestinations);
        List<Destination> collaborativeRecs = collaborative.recommend(user, allDestinations);
        List<Destination> hybridRecs = hybrid.recommend(user, allDestinations);

        // Combine with priority: Hybrid > Content > Collaborative
        Set<Destination> finalRecommendations = new LinkedHashSet<>();
        finalRecommendations.addAll(hybridRecs);
        finalRecommendations.addAll(contentBasedRecs);
        finalRecommendations.addAll(collaborativeRecs);

        // Convert to DTOs
        List<DestinationDto> result = finalRecommendations.stream()
                .map(this::convertToDto)
                .limit(MAX_RECOMMENDATIONS)
                .collect(Collectors.toList());
        saveRecommendations(user.getUserId(), result);

        return result;
    }

    private DestinationDto convertToDto(Destination destination) {
        return DestinationDto.builder()
                .destinationId(destination.getDestinationId())
                .name(destination.getName())
                .type(destination.getType())
                .costPerDay(destination.getCostPerDay())
                .bestSeasonToVisit(destination.getBestSeasonToVisit())
                .averageRating(destination.getAverageRating())
                .recommendedFor(new ArrayList<>(destination.getRecommendedFor()))
                .addedTime(destination.getLocalDateTime())
                .popularityScore(destination.getPopularityScore())
                .activityTags(new ArrayList<>(destination.getActivityTags()))
                .description(destination.getDescription())
                .imagePath(destination.getImagePath())
                .build();
    }

    private void saveRecommendations(Long userId, List<DestinationDto> recommendations) {
        User user = userRepository.findUserByUserLogin_Id(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found with id: " + userId
                ));
        LocalDateTime now = LocalDateTime.now();

        recommendations.forEach(dto -> {
            Destination destination = destinationRepository.findById(dto.getDestinationId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Destination not found with id: " + dto.getDestinationId()
                    ));

            Recommendation rec = new Recommendation();
            rec.setUser(user);
            rec.setDestination(destination);
            rec.setRecommendedAt(now);
            rec.setRecommendationScore(calculateScore(recommendations.indexOf(dto)));

            recommendationRepository.save(rec);
        });
    }

    private double calculateScore(int position) {
        return 1.0 - (position * 0.02); // Higher score for earlier positions
    }
}
