package com.texas.traveldestinationrecommendation.services;

import com.texas.traveldestinationrecommendation.dto.DestinationDto;
import com.texas.traveldestinationrecommendation.model.Destination;

import java.util.List;

public interface DestinationServices {

    List<DestinationDto> getAllDestinations();
    Destination getDestination(Long id);
    Destination addDestination(Destination destination);
    void deleteDestination(Long id);
    Destination updateDestination(Destination destination,Long id);
}
