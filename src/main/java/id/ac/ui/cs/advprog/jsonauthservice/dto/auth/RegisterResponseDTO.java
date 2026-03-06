package id.ac.ui.cs.advprog.jsonauthservice.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import id.ac.ui.cs.advprog.jsonauthservice.model.Role;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Setter
@Getter
public class RegisterResponseDTO {
    @JsonProperty("user_id")
    private UUID userId;

    private String email;

    private Role role;

    @JsonProperty("created_at")
    private OffsetDateTime createdAt;
}
