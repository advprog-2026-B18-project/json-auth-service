package id.ac.ui.cs.advprog.jsonauthservice.dto.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KycReviewRequestDTO {
    @NotNull
    private Action action;

    @JsonProperty("rejection-reason")
    private String rejectionReason;

    public enum Action {
        APPROVE,
        REJECT
    }
}
