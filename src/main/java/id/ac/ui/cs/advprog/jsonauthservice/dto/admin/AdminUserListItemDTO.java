package id.ac.ui.cs.advprog.jsonauthservice.dto.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import id.ac.ui.cs.advprog.jsonauthservice.model.AccountStatus;
import id.ac.ui.cs.advprog.jsonauthservice.model.Role;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Setter
@Getter
public class AdminUserListItemDTO {
    @JsonProperty("user_id")
    private UUID userId;

    private String email;

    private String username;

    @JsonProperty("full_name")
    private String fullName;

    private Role role;

    private AccountStatus status;

    @JsonProperty("created_at")
    private OffsetDateTime createdAt;
}
