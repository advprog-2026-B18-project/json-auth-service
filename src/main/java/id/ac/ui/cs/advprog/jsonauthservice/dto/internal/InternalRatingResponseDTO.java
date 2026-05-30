package id.ac.ui.cs.advprog.jsonauthservice.dto.internal;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InternalRatingResponseDTO {
    @JsonProperty("user_id")
    private UUID userId;

    @JsonProperty("avg_rating")
    private Double avgRating;

    @JsonProperty("total_orders")
    private Integer totalOrders;

    @JsonProperty("completed_orders")
    private Integer completedOrders;
}
