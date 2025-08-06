package com.texas.traveldestinationrecommendation.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;


import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
    private String description;

    private String imagePath;
    private MultipartFile imageFile; // For receiving file uploads

    
}
