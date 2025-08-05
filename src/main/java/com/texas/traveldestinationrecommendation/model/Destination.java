package com.texas.traveldestinationrecommendation.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "destinations")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Destination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long destinationId;

    private String name;
    private String type;
    private double costPerDay;
    private String bestSeasonToVisit;
    private double averageRating;
    private int popularityScore;
    private String description;
    @ElementCollection
    private List<String> recommendedFor;
    private LocalDateTime localDateTime;

    @ElementCollection
    private List<String> activityTags;

    @OneToMany(mappedBy = "destination", cascade = CascadeType.ALL)
    private List<DestinationRating> ratings = new ArrayList<>();

    private String imagePath;


   }
