package id.ac.ui.cs.advprog.jsonauthservice.dto.jastiper;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JastiperStats {
    private long totalOrders;
    private long completedOrders;
    private double successRate;
    private double avgRating;
    private long totalReviews;
}
