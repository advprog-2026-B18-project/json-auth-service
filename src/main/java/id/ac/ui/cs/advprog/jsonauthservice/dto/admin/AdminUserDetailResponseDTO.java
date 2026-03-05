package id.ac.ui.cs.advprog.jsonauthservice.dto.admin;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import id.ac.ui.cs.advprog.jsonauthservice.dto.jastiper.JastiperStats;
import id.ac.ui.cs.advprog.jsonauthservice.model.AccountStatus;
import id.ac.ui.cs.advprog.jsonauthservice.model.KycStatus;
import id.ac.ui.cs.advprog.jsonauthservice.model.Role;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdminUserDetailResponseDTO {
    @JsonProperty("user_id")
    private UUID userId;

    private String email;

    private String username;

    @JsonProperty("full_name")
    private String fullName;

    private Role role;

    private AccountStatus status;

    @JsonProperty("profile_picture_url")
    private String profilePictureUrl;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("created_at")
    private OffsetDateTime createdAt;

    @JsonProperty("kyc_id")
    private UUID kycId;

    @JsonProperty("kyc_status")
    private KycStatus kycStatus;

    @JsonProperty("kyc_submitted_at")
    private OffsetDateTime kycSubmittedAt;

    @JsonProperty("kyc_reviewed_at")
    private OffsetDateTime kycReviewedAt;

    @JsonProperty("kyc_rejection_reason")
    private String kycRejectionReason;

    private JastiperStats stats;
}
