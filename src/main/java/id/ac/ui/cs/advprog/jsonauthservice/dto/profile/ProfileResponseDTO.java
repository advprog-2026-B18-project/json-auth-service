package id.ac.ui.cs.advprog.jsonauthservice.dto.profile;

import com.fasterxml.jackson.annotation.JsonProperty;
import id.ac.ui.cs.advprog.jsonauthservice.model.AccountStatus;
import id.ac.ui.cs.advprog.jsonauthservice.model.KycStatus;
import id.ac.ui.cs.advprog.jsonauthservice.model.Role;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Setter
@Getter
public class ProfileResponseDTO {
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

    @JsonProperty("kyc_status")
    private KycStatus kycStatus;
}
