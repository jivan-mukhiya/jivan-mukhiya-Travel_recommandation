package com.texas.traveldestinationrecommendation.Implementation;

import com.texas.traveldestinationrecommendation.model.ActivityLog;
import com.texas.traveldestinationrecommendation.repository.ActivityLogRepository;

import com.texas.traveldestinationrecommendation.model.ActivityLog;
import com.texas.traveldestinationrecommendation.repository.ActivityLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ActivityLogger {

    private final ActivityLogRepository activityLogRepository;

    @Autowired
    public ActivityLogger(ActivityLogRepository activityLogRepository) {
        this.activityLogRepository = activityLogRepository;
    }

    @Transactional
    public void logActivity(String activityType, String username, String details) {
        ActivityLog log = new ActivityLog(activityType, username, details);
        activityLogRepository.save(log);
    }
    public List<ActivityLog> getRecentActivities() {
        return activityLogRepository.findTop3ByOrderByTimestampDesc();
    }
}
