package com.texas.traveldestinationrecommendation.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    private LocalDate dob;
    private String profession;
    private Double budgetMin;
    private Double budgetMax;
    private String travelTypePreference;
    private String seasonPreference;

    @ElementCollection
    private List<String> pastVisitedDestinations;

    @ElementCollection
    private List<String> preferences;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="login_id")
    private UserLogin userLogin;
}
