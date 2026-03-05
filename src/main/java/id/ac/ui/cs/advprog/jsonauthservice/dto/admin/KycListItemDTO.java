package id.ac.ui.cs.advprog.jsonauthservice.dto.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import id.ac.ui.cs.advprog.jsonauthservice.model.KycStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Setter
@Getter
public class KycListItemDTO {
    @JsonProperty("kyc_id")
    private UUID kycId;

    @JsonProperty("user_id")
    private UUID userId;

    private String username;

    @JsonProperty("full_name_ktp")
    private String fullNameKtp;

    private KycStatus status;

    @JsonProperty("submitted_at")
    private OffsetDateTime submittedAt;
}
