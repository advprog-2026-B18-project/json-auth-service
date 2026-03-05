package id.ac.ui.cs.advprog.jsonauthservice.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import id.ac.ui.cs.advprog.jsonauthservice.model.AccountStatus;
import id.ac.ui.cs.advprog.jsonauthservice.model.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class AccountResponseDTO {
    @JsonProperty("user_id")
    private UUID id;

    private String email;

    private Role role;

    private String username;

    private AccountStatus status;
}
