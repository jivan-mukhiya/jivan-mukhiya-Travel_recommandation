package com.texas.traveldestinationrecommendation.algorithm;

import com.texas.traveldestinationrecommendation.model.Destination;
import com.texas.traveldestinationrecommendation.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class HybridRecommender implements RecommendationStrategy {
    private final ContentBasedRecommender contentBased;
    private final CollaborativeRecommender collaborative;


    @Override
    public List<Destination> recommend(User user, List<Destination> allDestinations) {
        List<Destination> contentBasedRecs = contentBased.recommend(user, allDestinations);
        List<Destination> collaborativeRecs = collaborative.recommend(user, allDestinations);

        Map<Destination, Double> hybridScores = new HashMap<>();

        // Add content-based scores with 60% weight
        for (int i = 0; i < contentBasedRecs.size(); i++) {
            Destination dest = contentBasedRecs.get(i);
            double score = (contentBasedRecs.size() - i) * 0.6;
            hybridScores.put(dest, score);
        }

        // Add collaborative scores with 40% weight
        for (int i = 0; i < collaborativeRecs.size(); i++) {
            Destination dest = collaborativeRecs.get(i);
            double score = (collaborativeRecs.size() - i) * 0.4;
            hybridScores.merge(dest, score, Double::sum);
        }

        return hybridScores.entrySet().stream()
                .sorted(Map.Entry.<Destination, Double>comparingByValue().reversed())
                .limit(6)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
