package com.texas.traveldestinationrecommendation.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String name;
    private String gender;
    private int age;
    private String profession;
    private double budgetMin;
    private double budgetMax;
    private String travelTypePreference;
    private String seasonPreference;

    @ElementCollection
    private List<String> pastVisitedDestinations;

    @ElementCollection
    private List<String> preferences;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<DestinationRating> userRatings = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    private UserLogin userLogin;
}
