package com.texas.traveldestinationrecommendation.Implementation;

import com.texas.traveldestinationrecommendation.dto.DashboardStatsDTO;
import com.texas.traveldestinationrecommendation.repository.DestinationRepository;
import com.texas.traveldestinationrecommendation.repository.UserLoginRepository;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class DashboardService {

    private final DestinationRepository destinationRepository;
    private final UserLoginRepository userLoginRepository;

    public DashboardService(DestinationRepository destinationRepository,
                            UserLoginRepository userLoginRepository) {
        this.destinationRepository = destinationRepository;
        this.userLoginRepository = userLoginRepository;
    }

    public List<DashboardStatsDTO> getDashboardStats() {
        List<DashboardStatsDTO> stats = new ArrayList<>();
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
        LocalDateTime monthAgo = LocalDateTime.now().minusDays(30);

        // 1. Total Destinations
        long totalDestinations = destinationRepository.count();
        stats.add(new DashboardStatsDTO(
                "Total Destinations",
                numberFormat.format(totalDestinations),
                calculateChange(totalDestinations, 130),
                "up"
        ));

        // 2. New Destinations (last 30 days)
        long newDestinations = destinationRepository.countNewDestinationsSince(monthAgo);
        stats.add(new DashboardStatsDTO(
                "New Destinations",
                numberFormat.format(newDestinations),
                calculateChange(newDestinations, 20),
                "up"
        ));

        // 4. New Users (last 30 days)
        long newUsers = userLoginRepository.countNewUsersSince(monthAgo);
        stats.add(new DashboardStatsDTO(
                "New Users",
                numberFormat.format(newUsers),
                calculateChange(newUsers, 10),
                "up"
        ));

        // 5. Total Reviews
        long totalReviews = destinationRepository.countTotalRatings();
        stats.add(new DashboardStatsDTO(
                "Total Reviews",
                numberFormat.format(totalReviews),
                calculateChange(totalReviews, 20),
                "up"
        ));



        return stats;
    }

    private String calculateChange(long current, long previous) {
        if (previous == 0) return "+0%";

        double change = ((current - previous) / (double) previous) * 100;
        String sign = change >= 0 ? "+" : "";
        return String.format("%s%d%%", sign, (int) Math.round(change));
    }
}
