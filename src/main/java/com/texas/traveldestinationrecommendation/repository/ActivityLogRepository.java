package com.texas.traveldestinationrecommendation.repository;

import com.texas.traveldestinationrecommendation.model.ActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {
    List<ActivityLog> findTop3ByOrderByTimestampDesc();
}

