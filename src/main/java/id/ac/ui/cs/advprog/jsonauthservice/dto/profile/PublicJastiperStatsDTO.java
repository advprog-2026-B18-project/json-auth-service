package id.ac.ui.cs.advprog.jsonauthservice.dto.profile;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PublicJastiperStatsDTO {
    @JsonProperty("total_orders")
    private long totalOrders;

    @JsonProperty("success_rate")
    private double successRate;

    @JsonProperty("avg_rating")
    private double avgRating;
}

