package com.texas.traveldestinationrecommendation.algorithm;

import com.texas.traveldestinationrecommendation.model.Destination;
import com.texas.traveldestinationrecommendation.model.User;

import java.util.List;

public interface RecommendationStrategy {
    List<Destination> recommend(User user, List<Destination> allDestinations);
}
