package com.texas.traveldestinationrecommendation.Implementation;

import com.texas.traveldestinationrecommendation.dto.DestinationDto;
import com.texas.traveldestinationrecommendation.dto.DestinationRatingDto;
import com.texas.traveldestinationrecommendation.model.Destination;
import com.texas.traveldestinationrecommendation.model.DestinationRating;
import com.texas.traveldestinationrecommendation.model.User;
import com.texas.traveldestinationrecommendation.repository.DestinationRatingRepository;
import com.texas.traveldestinationrecommendation.repository.DestinationRepository;
import com.texas.traveldestinationrecommendation.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final ActivityLogger activityLogger;

    @Override
    public List<DestinationRatingDto> getAllDestinationsRating() {
        return destinationRatingRepository.findAll().stream().map(this::convertToDto)
                .sorted(Comparator.comparing(DestinationRatingDto::getId).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public void deleteDestinationRating(Long id) {
        DestinationRating rating = destinationRatingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Destination Rating does not exist: " + id));

        // Update destination's average rating and popularity before deleting
        updateDestinationRatingStats(rating.getDestination());
        destinationRatingRepository.delete(rating);
        String currentUser = "Admin";
        activityLogger.logActivity(
                "DELETE_RATING",
                currentUser,
                "Deleted rating for \"" + rating.getDestination().getName()
        );
    }

    @Override
    public DestinationRatingDto getDestinationRating(Long id) {
        DestinationRating rating = destinationRatingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Destination Rating does not exist: " + id));
        return convertToDto(rating);
    }

    @Override
    @Transactional
    public DestinationRatingDto updateDestinationRating(DestinationRatingDto ratingDto) {
        DestinationRating rating = destinationRatingRepository.findById(ratingDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Destination Rating does not exist: " + ratingDto.getId()));

        if (ratingDto.getRating() < 0 || ratingDto.getRating() > 5) {
            throw new IllegalArgumentException("Rating must be between 0 and 5");
        }
        double oldRating =rating.getRating();
        String destinationName = rating.getDestination().getName();
        String username = rating.getUser().getName();

        rating.setRating(ratingDto.getRating());
        rating.setFeedback(ratingDto.getFeedback());

        DestinationRating updatedRating = destinationRatingRepository.save(rating);
        updateDestinationRatingStats(rating.getDestination());

        String currentUser = updatedRating.getUser().getName();
        String details = "Updated rating for \"" + destinationName + "\" by " + username +
                " from " + oldRating + " to " + ratingDto.getRating();

        activityLogger.logActivity(
                "UPDATE_RATING",
                currentUser,
                details
        );



        return convertToDto(updatedRating);
    }

    @Override
    @Transactional
    public DestinationRatingDto addDestinationRating(DestinationRatingDto ratingDto) {
        if (ratingDto.getRating() < 0 || ratingDto.getRating() > 5) {
            throw new IllegalArgumentException("Rating must be between 0 and 5");
        }

        Destination destination = destinationRepository.findById(ratingDto.getDestinationId())
                .orElseThrow(() -> new IllegalArgumentException("Destination not found"));

        User user = userRepository.findUserByUserLogin_Id(ratingDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with login ID: " + ratingDto.getUserId()));

        DestinationRating rating = new DestinationRating();
        rating.setDestination(destination);
        rating.setRating(ratingDto.getRating());
        rating.setFeedback(ratingDto.getFeedback());
        rating.setUser(user);

        DestinationRating savedRating = destinationRatingRepository.save(rating);
        updateDestinationRatingStats(destination);

        String currentUser = savedRating.getUser().getName();
        activityLogger.logActivity(
                "ADD_RATING",
                currentUser,
                "Added " + ratingDto.getRating() + " star rating for \"" + destination.getName() + "\""
        );
        return convertToDto(savedRating);
    }

    @Override
    public List<DestinationRatingDto> getDestinationsRatingByDestinationId(Long destinationId) {
        List<DestinationRating> ratings=destinationRatingRepository.findByDestination_DestinationId(destinationId);

        return ratings.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    private DestinationRatingDto convertToDto(DestinationRating rating) {
        DestinationRatingDto dto = new DestinationRatingDto();
        dto.setId(rating.getId());
        dto.setDestinationId(rating.getDestination().getDestinationId());
        dto.setDestinationName(rating.getDestination().getName());
        dto.setRating(rating.getRating());
        dto.setFeedback(rating.getFeedback());
        dto.setUserId(rating.getUser().getUserId());
        dto.setUserName(rating.getUser().getName());
        return dto;
    }

    private void updateDestinationRatingStats(Destination destination) {
        List<DestinationRating> ratings = destinationRatingRepository.findByDestination(destination);

        double averageRating = ratings.stream()
                .mapToDouble(DestinationRating::getRating)
                .average()
                .orElse(0.0);
        averageRating = Math.max(0, Math.min(5, averageRating));

        int ratingCount = ratings.size();
        double averageRatingWeight = averageRating * 10;
        double ratingWeight = Math.min(ratingCount * 2, 50);

        int popularityScore = (int) Math.round(averageRatingWeight + ratingWeight);
        popularityScore = Math.max(0, Math.min(100, popularityScore));

        destination.setAverageRating(averageRating);
        destination.setPopularityScore(popularityScore);


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

        destinationServices.updateDestination(destinationDto, destination.getDestinationId());
    }
}