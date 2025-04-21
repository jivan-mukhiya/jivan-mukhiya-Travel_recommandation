package com.texas.traveldestinationrecommendation.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DestinationDto {

    private Long destinationId;

    private String name;
    private String type;
    private double costPerDay;
    private String bestSeasonToVisit;
    private double averageRating;
    private List<String> recommendedFor;
    private LocalDateTime addedTime;
    private int popularityScore;
    private List<String> activityTags;
}
