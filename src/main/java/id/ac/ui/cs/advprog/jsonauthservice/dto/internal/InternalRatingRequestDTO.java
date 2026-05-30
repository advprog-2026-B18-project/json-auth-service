package id.ac.ui.cs.advprog.jsonauthservice.dto.internal;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InternalRatingRequestDTO {
    @JsonProperty("order_id")
    private String orderId;

    @NotNull
    @DecimalMin(value = "1.0", message = "Rating must be at least 1.0")
    @DecimalMax(value = "5.0", message = "Rating must be at most 5.0")
    private Double rating;

    @JsonProperty("is_completed")
    private Boolean isCompleted;
}