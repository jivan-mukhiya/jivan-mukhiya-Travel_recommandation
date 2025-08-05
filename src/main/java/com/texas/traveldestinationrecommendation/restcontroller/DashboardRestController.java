package com.texas.traveldestinationrecommendation.restcontroller;

import com.texas.traveldestinationrecommendation.Implementation.DashboardService;
import com.texas.traveldestinationrecommendation.dto.DashboardStatsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/destination")
@CrossOrigin(origins = "http://localhost:5173")
public class DashboardRestController {

    private final DashboardService dashboardService;

    public DashboardRestController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/stats")
    public List<DashboardStatsDTO> getDashboardStats() {
        return dashboardService.getDashboardStats();
    }
}
