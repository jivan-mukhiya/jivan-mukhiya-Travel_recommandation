package com.texas.traveldestinationrecommendation.services;

import com.texas.traveldestinationrecommendation.dto.DestinationRatingDto;
import com.texas.traveldestinationrecommendation.model.DestinationRating;

import java.util.List;

public interface DestinationRatingServices {

    List<DestinationRatingDto> getAllDestinationsRating();
    void deleteDestinationRating(Long id);
    DestinationRating getDestinationRating(Long id);
    DestinationRating updateDestination(DestinationRating destinationRating);
    DestinationRating addDestination(DestinationRating destinationRating);
}
