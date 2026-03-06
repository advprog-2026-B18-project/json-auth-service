package id.ac.ui.cs.advprog.jsonauthservice.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RefreshTokenRequestDTO {
    @NotBlank
    @JsonProperty("refresh_token")
    private String refreshToken;
}
