package com.texas.traveldestinationrecommendation.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String activityType; // ADD_DESTINATION, UPDATE_PROFILE, ADD_RATING
    private String username;
    private String details;
    private LocalDateTime timestamp;


    public ActivityLog(String activityType, String username, String details) {
        this.activityType = activityType;
        this.username = username;
        this.details = details;
        this.timestamp = LocalDateTime.now();
    }
}
