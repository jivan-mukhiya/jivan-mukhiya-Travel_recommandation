package com.texas.traveldestinationrecommendation.repository;

import com.texas.traveldestinationrecommendation.dto.DestinationRatingDto;
import com.texas.traveldestinationrecommendation.model.Destination;
import com.texas.traveldestinationrecommendation.model.DestinationRating;
import com.texas.traveldestinationrecommendation.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DestinationRatingRepository extends JpaRepository<DestinationRating, Long> {
    List<DestinationRating> findByDestination(Destination destination);
    List<DestinationRating> findByDestination_DestinationId(Long destinationId);
    List<DestinationRating> findByUser(User similarUser);
}
