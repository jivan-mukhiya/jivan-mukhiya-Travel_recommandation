package com.texas.traveldestinationrecommendation.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DestinationRatingDto {

    private Long id;
    private Long destinationId;
    private String destinationName;
    private double rating;
    private String feedback;
    private Long userId;
    private String userName;
}
