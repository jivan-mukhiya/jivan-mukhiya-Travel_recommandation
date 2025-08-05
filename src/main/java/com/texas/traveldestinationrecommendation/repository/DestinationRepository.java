package com.texas.traveldestinationrecommendation.repository;

import com.texas.traveldestinationrecommendation.model.Destination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface DestinationRepository extends JpaRepository<Destination, Long> {

    Destination findByName(String name);

        // Fetch all destinations with their activity tags
        @Query("SELECT d FROM Destination d LEFT JOIN FETCH d.activityTags")
        List<Destination> findAllWithActivityTags();

        // Default method to process in Java
        default Map<String, Long> countByActivityTags() {
            return findAllWithActivityTags().stream()
                    .flatMap(destination -> destination.getActivityTags().stream())
                    .collect(Collectors.groupingBy(
                            tag -> tag,
                            Collectors.counting()
                    ));
        }

    @Query("SELECT d.bestSeasonToVisit, COUNT(d) FROM Destination d " +
            "WHERE d.bestSeasonToVisit IS NOT NULL " +  // Add this filter
            "GROUP BY d.bestSeasonToVisit")
    List<Object[]> countDestinationsBySeason();


    // Count destinations added in the last 30 days
    @Query("SELECT COUNT(d) FROM Destination d WHERE d.localDateTime >= :cutoffDate")
    long countNewDestinationsSince(LocalDateTime cutoffDate);

    // Count total ratings across all destinations
    @Query("SELECT COALESCE(SUM(SIZE(d.ratings)), 0) FROM Destination d")
    long countTotalRatings();


    }

