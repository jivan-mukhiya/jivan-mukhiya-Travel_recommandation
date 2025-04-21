package com.texas.traveldestinationrecommendation.Implementation;

import com.texas.traveldestinationrecommendation.dto.DestinationDto;
import com.texas.traveldestinationrecommendation.model.Destination;
import com.texas.traveldestinationrecommendation.repository.DestinationRepository;
import com.texas.traveldestinationrecommendation.services.DestinationServices;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
            return destinationDto;
        })
                .sorted(Comparator.comparing(DestinationDto::getDestinationId).reversed()) // sort by ID in descending order
                .collect(Collectors.toList());
    }

    @Override
    public Destination getDestination(Long id) {

        return  destinationRepository.findById(id).orElseThrow(()->
                new IllegalArgumentException("Destination not found id:"+id));
    }

    @Override
    public Destination addDestination(Destination destination) {
        if(destinationRepository.findByName(destination.getName()) == null){
            destination.setLocalDateTime(LocalDateTime.now());
            destination.setPopularityScore(0);
            destination.setAverageRating(0.0);
            return destinationRepository.save(destination);
        }
        throw new IllegalArgumentException("Destination already exists");
    }

    @Override
    public void deleteDestination(Long id) {

        destinationRepository.findById(id).ifPresentOrElse(destinationRepository ::delete,()->{
            throw new IllegalArgumentException("Destination not found id:"+id);
        });

    }

    @Override
    public Destination updateDestination(Destination destination, Long id) {

        return destinationRepository.findById(id).map(des->{

            des.setName(destination.getName());
            des.setType(destination.getType());
            des.setCostPerDay(destination.getCostPerDay());
            des.setRecommendedFor(destination.getRecommendedFor());
            des.setAverageRating(destination.getAverageRating());
            des.setActivityTags(destination.getActivityTags());
            des.setBestSeasonToVisit(destination.getBestSeasonToVisit());
            des.setPopularityScore(destination.getPopularityScore());
            return destinationRepository.save(des);
        }).orElseThrow(()->
                new IllegalArgumentException("Destination not found id:" + id));
    }



    //convert to Destination on Dto
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
        return destinationDto;
    }


    //count By activityTags
    public Map<String,Long> getActivityTagStatistics(){
        return destinationRepository.countByActivityTags();
    }

    //count by best season to visit
    public Map<String, Long> countBySeason(){

        List<Object[]> result=destinationRepository.countDestinationsBySeason();
        Map<String,Long> seasonCount=new HashMap<>();
        for(Object[] row:result){
            seasonCount.put((String)row[0],(Long)row[1]);
        }
        return seasonCount;
    }

}
