package com.texas.traveldestinationrecommendation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DashboardStatsDTO {
    private String title;
    private String value;
    private String change;
    private String trend;
}
