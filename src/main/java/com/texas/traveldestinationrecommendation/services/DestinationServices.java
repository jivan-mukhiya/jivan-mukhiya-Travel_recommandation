package com.texas.traveldestinationrecommendation.services;

import com.texas.traveldestinationrecommendation.dto.DestinationDto;
import com.texas.traveldestinationrecommendation.model.Destination;

import java.io.IOException;
import java.util.List;

public interface DestinationServices {

    List<DestinationDto> getAllDestinations();
    DestinationDto getDestination(Long id);
    DestinationDto addDestination(DestinationDto destinationDto);
    void deleteDestination(Long id);
    DestinationDto updateDestination(DestinationDto destinationDto,Long id);
}
