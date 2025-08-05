package com.texas.traveldestinationrecommendation.restcontroller;

import com.texas.traveldestinationrecommendation.Implementation.ActivityLogger;
import com.texas.traveldestinationrecommendation.model.ActivityLog;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/activities")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class ActivityRestController {

    private final ActivityLogger activityLogService;

    @GetMapping("/recent")
    public ResponseEntity<List<Map<String, Object>>> getRecentActivities() {
        List<ActivityLog> logs = activityLogService.getRecentActivities();

        List<Map<String, Object>> activities = logs.stream()
                .map(this::convertToFrontendFormat)
                .collect(Collectors.toList());

        return ResponseEntity.ok(activities);
    }

    private Map<String, Object> convertToFrontendFormat(ActivityLog log) {
        Map<String, Object> activity = new HashMap<>();

        // Map activity type to icon and color
        switch(log.getActivityType()) {
            case "ADD_DESTINATION":
                activity.put("icon", "FiPlusCircle");
                activity.put("color", "blue");
                break;
            case "UPDATE_PROFILE":
                activity.put("icon", "FiUsers");
                activity.put("color", "green");
                break;
            case "ADD_REVIEW":
            case "ADD_RATING":
                activity.put("icon", "FiStar");
                activity.put("color", "yellow");
                break;
            default:
                activity.put("icon", "FiInfo");
                activity.put("color", "gray");
        }

        // Format description with HTML for bold text
        String boldUsername = log.getUsername().equals("System")
                ? "System"
                : "<span class=\"font-medium\">" + log.getUsername() + "</span>";

        // Create title based on activity type
        String title;
        switch(log.getActivityType()) {
            case "ADD_DESTINATION":
                title = boldUsername + " added a new destination \"" + log.getDetails() + "\"";
                break;
            case "UPDATE_PROFILE":
                title = boldUsername + " updated profile information";
                break;
            case "ADD_REVIEW":
                title = boldUsername + " reviewed destination \"" + log.getDetails() + "\"";
                break;
            case "ADD_RATING":
                title = boldUsername + " rated destination \"" + log.getDetails() + "\"";
                break;
            default:
                title = boldUsername + " performed an action: " + log.getActivityType();
        }

        activity.put("title", title);
        activity.put("time", formatTimeAgo(log.getTimestamp()));

        return activity;
    }

    private String formatTimeAgo(LocalDateTime timestamp) {
        Duration duration = Duration.between(timestamp, LocalDateTime.now());
        long seconds = duration.getSeconds();

        if (seconds < 60) return "Just now";
        if (seconds < 3600) return (seconds / 60) + " minutes ago";
        if (seconds < 86400) return (seconds / 3600) + " hours ago";
        return (seconds / 86400) + " days ago";
    }

}
