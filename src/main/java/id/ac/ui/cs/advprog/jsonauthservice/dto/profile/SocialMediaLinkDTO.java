package id.ac.ui.cs.advprog.jsonauthservice.dto.profile;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SocialMediaLinkDTO {
    @NotBlank
    private String platform;

    @NotBlank
    private String url;
}
