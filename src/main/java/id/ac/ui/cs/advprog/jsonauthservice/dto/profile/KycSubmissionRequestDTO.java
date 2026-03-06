package id.ac.ui.cs.advprog.jsonauthservice.dto.profile;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class KycSubmissionRequestDTO {
    @JsonProperty("full_name_ktp")
    @NotBlank
    private String fullNameKtp;

    @JsonProperty("ktp_number")
    @NotBlank
    @Pattern(regexp = "^\\d{16}$", message = "KTP number must be 16 digits")
    private String ktpNumber;

    @JsonProperty("ktp_photo_url")
    @NotBlank
    private String ktpPhotoUrl;

    @JsonProperty("selfie_with_ktp_url")
    @NotBlank
    private String selfieWithKtpUrl;

    @JsonProperty("social_media_links")
    @NotEmpty
    @Valid
    private List<SocialMediaLinkDTO> socialMediaLinks;

    private String bio;
}
