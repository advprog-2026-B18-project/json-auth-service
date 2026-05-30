package id.ac.ui.cs.advprog.jsonauthservice.dto.internal;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InternalRatingRequestDTO {
    @JsonProperty("order_id")
    private String orderId;

    @NotNull
    private Double rating;

    @JsonProperty("is_completed")
    private Boolean isCompleted;
}