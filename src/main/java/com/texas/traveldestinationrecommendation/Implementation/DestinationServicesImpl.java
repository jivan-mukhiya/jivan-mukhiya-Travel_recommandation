package com.texas.traveldestinationrecommendation.Implementation;

import com.texas.traveldestinationrecommendation.dto.DestinationDto;
import com.texas.traveldestinationrecommendation.model.Destination;
import com.texas.traveldestinationrecommendation.repository.DestinationRepository;
import com.texas.traveldestinationrecommendation.services.DestinationServices;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DestinationServicesImpl implements DestinationServices {

    private final DestinationRepository destinationRepository;
    private final ImageStorageService imageStorageService;
    private final ActivityLogger activityLogger;

    @Override
    public List<DestinationDto> getAllDestinations() {
        return destinationRepository.findAll().stream().map(des->{
            DestinationDto destinationDto = new DestinationDto();

            destinationDto.setDestinationId(des.getDestinationId());
            destinationDto.setName(des.getName());
            destinationDto.setType(des.getType());
            destinationDto.setCostPerDay(des.getCostPerDay());
            destinationDto.setRecommendedFor(des.getRecommendedFor());
            destinationDto.setAverageRating(des.getAverageRating());
            destinationDto.setActivityTags(des.getActivityTags());
            destinationDto.setAddedTime(des.getLocalDateTime());
            destinationDto.setBestSeasonToVisit(des.getBestSeasonToVisit());
            destinationDto.setPopularityScore(des.getPopularityScore());
            destinationDto.setDescription(des.getDescription());
            destinationDto.setDestinationId(des.getDestinationId());
                    destinationDto.setImagePath(des.getImagePath());
            return destinationDto;
        })
                .sorted(Comparator.comparing(DestinationDto::getDestinationId).reversed()) // sort by ID in descending order
                .collect(Collectors.toList());
    }

    @Override
    public DestinationDto getDestination(Long id) {

        Destination des=destinationRepository.findById(id).orElseThrow(()->
                new IllegalArgumentException("Destination not found id:"+id));

        return ConvertToDestinationDto(des);
    }

    @Override
    public DestinationDto addDestination(DestinationDto destinationDto) {
        if(destinationRepository.findByName(destinationDto.getName()) != null){
            throw new IllegalArgumentException("Destination already exists");
        }

        Destination destination = new Destination();
        destination.setName(destinationDto.getName());
        destination.setType(destinationDto.getType());
        destination.setCostPerDay(destinationDto.getCostPerDay());
        destination.setBestSeasonToVisit(destinationDto.getBestSeasonToVisit());
        destination.setRecommendedFor(destinationDto.getRecommendedFor());
        destination.setActivityTags(destinationDto.getActivityTags());
        destination.setDescription(destinationDto.getDescription());

        if(destinationDto.getImageFile() != null && !destinationDto.getImageFile().isEmpty()){
            String imagePath = imageStorageService.storeImage(destinationDto.getImageFile());
            destination.setImagePath(imagePath);
        }

        destination.setLocalDateTime(LocalDateTime.now());
        destination.setPopularityScore(0);
        destination.setAverageRating(0.0);

        Destination savedDestination = destinationRepository.save(destination);
        String currentUser="Admin";
        activityLogger.logActivity(  "ADD_DESTINATION",
                currentUser,
                "Added destination: " + destination.getName());

        return ConvertToDestinationDto(savedDestination);
    }

    @Override
    public void deleteDestination(Long id) {
        destinationRepository.findById(id).ifPresent(destination -> {
            if (destination.getImagePath() != null) {
                try {
                    imageStorageService.deleteImage(destination.getImagePath());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            destinationRepository.delete(destination);
            String username = "Admin";
            activityLogger.logActivity("DELETE_DESTINATION", username, destination.getName());
        });

    }

    @Override
    public DestinationDto updateDestination(DestinationDto destinationDto, Long id) {


        return destinationRepository.findById(id).map(des -> {
            String originalName = des.getName();

            des.setName(destinationDto.getName());
            des.setType(destinationDto.getType());
            des.setCostPerDay(destinationDto.getCostPerDay());
            des.setRecommendedFor(destinationDto.getRecommendedFor());
            des.setAverageRating(destinationDto.getAverageRating());
            des.setActivityTags(destinationDto.getActivityTags());
            des.setBestSeasonToVisit(destinationDto.getBestSeasonToVisit());
            des.setPopularityScore(destinationDto.getPopularityScore());
            des.setDescription(destinationDto.getDescription());
            if (destinationDto.getImageFile() != null && !destinationDto.getImageFile().isEmpty()) {
                // Delete old image if exists
                if (des.getImagePath() != null) {
                    try {
                        imageStorageService.deleteImage(des.getImagePath());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                // Store new image
                String imagePath = imageStorageService.storeImage(destinationDto.getImageFile());
                des.setImagePath(imagePath);
            }

            Destination updateDestination = destinationRepository.save(des);

            String username = "Admin";
            String details = originalName.equals(updateDestination.getName())
                    ? originalName
                    : originalName + " to " + updateDestination.getName();

            activityLogger.logActivity("UPDATE_DESTINATION", username, details);

            return ConvertToDestinationDto(updateDestination);
        }).orElseThrow(() -> new IllegalArgumentException("Destination not found id: " + id));
    }

    private DestinationDto ConvertToDestinationDto(Destination destination) {
        DestinationDto destinationDto = new DestinationDto();

        destinationDto.setDestinationId(destination.getDestinationId());
        destinationDto.setName(destination.getName());
        destinationDto.setType(destination.getType());
        destinationDto.setCostPerDay(destination.getCostPerDay());
        destinationDto.setRecommendedFor(destination.getRecommendedFor());
        destinationDto.setAverageRating(destination.getAverageRating());
        destinationDto.setActivityTags(destination.getActivityTags());
        destinationDto.setBestSeasonToVisit(destination.getBestSeasonToVisit());
        destinationDto.setAddedTime(destination.getLocalDateTime());
        destinationDto.setPopularityScore(destination.getPopularityScore());
        destinationDto.setDescription(destination.getDescription());
        destinationDto.setImagePath(destination.getImagePath());
        return destinationDto;
    }



    public Map<String,Long> getActivityTagStatistics(){
        return destinationRepository.countByActivityTags();
    }


    public Map<String, Long> countBySeason() {
        List<Object[]> result = destinationRepository.countDestinationsBySeason();
        Map<String, Long> seasonCount = new HashMap<>();

        for(Object[] row : result) {
            String season = (String) row[0];
            // Skip null keys
            if(season != null) {
                seasonCount.put(season, (Long) row[1]);
            }
        }
        return seasonCount;
    }

}
