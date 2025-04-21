package com.texas.traveldestinationrecommendation.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DestinationRatingDto {

    private Long id;

    private double rating;
    private String feedback;
    private String userName;
    private String destinationName;
}
