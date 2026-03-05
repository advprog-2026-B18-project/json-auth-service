package id.ac.ui.cs.advprog.jsonauthservice.dto.profile;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import id.ac.ui.cs.advprog.jsonauthservice.model.AccountStatus;
import id.ac.ui.cs.advprog.jsonauthservice.model.Role;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PublicProfileResponseDTO {
    @JsonProperty("user_id")
    private UUID userId;

    private String username;

    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("profile_picture_url")
    private String profilePictureUrl;

    private Role role;

    @JsonProperty("member_since")
    private OffsetDateTime memberSince;

    private AccountStatus status;

    private PublicJastiperStatsDTO stats;

    private Double rating;

    private List<String> badges;
}
