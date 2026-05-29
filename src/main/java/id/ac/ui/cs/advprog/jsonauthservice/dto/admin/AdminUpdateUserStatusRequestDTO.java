package id.ac.ui.cs.advprog.jsonauthservice.dto.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AdminUpdateUserStatusRequestDTO {
    @NotNull(message = "Action is required")
    private Action action;

    @NotBlank(message = "Reason is required")
    private String reason;

    public enum Action {
        BAN, UNBAN, DEMOTE_TO_TITIPERS
    }
}