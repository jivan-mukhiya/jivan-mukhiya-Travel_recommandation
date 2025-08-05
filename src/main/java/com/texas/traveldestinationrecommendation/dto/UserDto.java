package com.texas.traveldestinationrecommendation.dto;

import com.texas.traveldestinationrecommendation.model.DestinationRating;
import com.texas.traveldestinationrecommendation.model.UserLogin;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class UserDto {

    private Long userId;

    private String name;
    private String gender;
    private LocalDate dob;
    private String profession;
    private Double budgetMin;
    private Double budgetMax;
    private String travelTypePreference;
    private String seasonPreference;
    private List<String> pastVisitedDestinations;
    private List<String> preferences;
    private String UserEmail;
    private String UserName;



}
