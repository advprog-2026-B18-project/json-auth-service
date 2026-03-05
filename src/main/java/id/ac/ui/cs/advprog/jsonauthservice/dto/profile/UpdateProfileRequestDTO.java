package id.ac.ui.cs.advprog.jsonauthservice.dto.profile;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateProfileRequestDTO {
    @Size(max = 30)
    @Pattern(regexp = "^[A-Za-z0-9_]*$", message = "Username can only contain letters, numbers, and underscore")
    private String username;

    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("profile_picture_url")
    private String profilePictureUrl;
}
