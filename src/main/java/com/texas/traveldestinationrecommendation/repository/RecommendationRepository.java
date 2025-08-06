package com.texas.traveldestinationrecommendation.repository;

import com.texas.traveldestinationrecommendation.model.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {
}
