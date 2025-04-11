package com.texas.traveldestinationrecommendation.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
    private String recommendedFor;

    @ElementCollection
    private List<String> activityTags;

    @OneToMany(mappedBy = "destination", cascade = CascadeType.ALL)
    private List<DestinationRating> ratings = new ArrayList<>();

   }
