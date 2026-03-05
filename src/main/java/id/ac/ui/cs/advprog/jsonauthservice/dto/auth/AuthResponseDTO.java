package id.ac.ui.cs.advprog.jsonauthservice.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import id.ac.ui.cs.advprog.jsonauthservice.dto.account.AccountResponseDTO;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthResponseDTO {
    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("expires_in")
    private long expiresIn;

    @JsonProperty("token_type")
    private String tokenType = "Bearer";

    @JsonProperty("user")
    private AccountResponseDTO user;
}
