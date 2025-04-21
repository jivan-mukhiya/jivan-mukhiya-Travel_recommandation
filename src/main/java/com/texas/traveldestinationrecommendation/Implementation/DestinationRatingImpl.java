package com.texas.traveldestinationrecommendation.Implementation;

import com.texas.traveldestinationrecommendation.dto.DestinationRatingDto;
import com.texas.traveldestinationrecommendation.model.Destination;
import com.texas.traveldestinationrecommendation.model.DestinationRating;
import com.texas.traveldestinationrecommendation.repository.DestinationRatingRepository;
import com.texas.traveldestinationrecommendation.repository.DestinationRepository;
import com.texas.traveldestinationrecommendation.services.DestinationRatingServices;
import com.texas.traveldestinationrecommendation.services.DestinationServices;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class DestinationRatingImpl implements DestinationRatingServices {

    private final DestinationRatingRepository destinationRatingRepository;
    private final DestinationRepository destinationRepository;
    private final DestinationServices destinationServices;

    @Override
    public List<DestinationRatingDto> getAllDestinationsRating() {
        return destinationRatingRepository.findAll().stream().map(des -> {
            DestinationRatingDto destinationRatingDto = new DestinationRatingDto();
            destinationRatingDto.setId(des.getId());
            destinationRatingDto.setDestinationName(des.getDestination().getName());
            destinationRatingDto.setRating(des.getRating());
            destinationRatingDto.setFeedback(des.getFeedback());
            destinationRatingDto.setUserName(des.getUser().getName());
            return destinationRatingDto;
        }).sorted(Comparator.comparing(DestinationRatingDto::getId).
                reversed()).collect(Collectors.toList());
    }

    @Override
    public void deleteDestinationRating(Long id) {

        destinationRatingRepository.findById(id).ifPresentOrElse(destinationRatingRepository :: delete,()->{
               throw new IllegalArgumentException("Destination Rating does not exist :"+id);
        });
    }

    @Override
    public DestinationRating getDestinationRating(Long id) {
        return destinationRatingRepository.findById(id).orElseThrow(()->
                new IllegalArgumentException("Destination Rating does not exist :"+id));
    }

    @Override
    public DestinationRating updateDestination(DestinationRating destinationRating) {
        return null;
    }

    @Override
    @Transactional
    public DestinationRating addDestination(DestinationRating destinationRating) {

        if(destinationRating.getRating() <0 || destinationRating.getRating() >5){
            throw new IllegalArgumentException("Rating must be 0 to 5");
        }

        DestinationRating saveRating = destinationRatingRepository.save(destinationRating);

        Destination destination=destinationServices.getDestination(saveRating.getDestination().getDestinationId());

        List<DestinationRating> ratings=destination.getRatings();
        double averageRating=ratings.stream().mapToDouble(DestinationRating::getRating)
                .average()
                .orElse(0.0);
        averageRating=Math.max(0,Math.min(5,averageRating));

        int ratingCount=ratings.size();
        double averageRatingWeight=averageRating*10;
        double ratingWeight=Math.min(ratingCount*2, 50);

        int popularityScore=(int) Math.round(averageRatingWeight+ratingWeight);
        popularityScore=Math.max(0,Math.min(100,popularityScore));

        destination.setAverageRating(averageRating);
        destination.setPopularityScore(popularityScore);

        destinationServices.updateDestination(destination,destination.getDestinationId());

        return saveRating;
    }
}
