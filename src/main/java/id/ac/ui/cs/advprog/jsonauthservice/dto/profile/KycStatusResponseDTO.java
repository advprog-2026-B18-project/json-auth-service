package id.ac.ui.cs.advprog.jsonauthservice.dto.profile;

import com.fasterxml.jackson.annotation.JsonProperty;
import id.ac.ui.cs.advprog.jsonauthservice.model.KycStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Setter
@Getter
public class KycStatusResponseDTO {
    @JsonProperty("kyc_id")
    private UUID kycId;

    private KycStatus status;

    @JsonProperty("submitted_at")
    private OffsetDateTime submittedAt;

    @JsonProperty("reviewed_at")
    private OffsetDateTime reviewedAt;

    @JsonProperty("rejection_reason")
    private String rejectionReason;
}
