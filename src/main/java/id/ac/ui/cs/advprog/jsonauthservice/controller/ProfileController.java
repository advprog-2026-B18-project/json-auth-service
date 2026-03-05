package id.ac.ui.cs.advprog.jsonauthservice.controller;

import id.ac.ui.cs.advprog.jsonauthservice.dto.common.ApiResponse;
import id.ac.ui.cs.advprog.jsonauthservice.dto.profile.KycStatusResponseDTO;
import id.ac.ui.cs.advprog.jsonauthservice.dto.profile.KycSubmissionRequestDTO;
import id.ac.ui.cs.advprog.jsonauthservice.dto.profile.ProfileResponseDTO;
import id.ac.ui.cs.advprog.jsonauthservice.dto.profile.PublicProfileResponseDTO;
import id.ac.ui.cs.advprog.jsonauthservice.dto.profile.UpdateProfileRequestDTO;
import id.ac.ui.cs.advprog.jsonauthservice.security.CustomUserDetails;
import id.ac.ui.cs.advprog.jsonauthservice.service.ProfileService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<ProfileResponseDTO> getMyProfile(@AuthenticationPrincipal CustomUserDetails userDetails) {
        UUID accountId = userDetails.getId();
        ProfileResponseDTO profile = profileService.getMyProfile(accountId);

        ApiResponse<ProfileResponseDTO> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("Profile fetched");
        response.setData(profile);
        return response;
    }

    @PatchMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<ProfileResponseDTO> updateMyProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody UpdateProfileRequestDTO request
    ) {
        UUID accountId = userDetails.getId();
        ProfileResponseDTO profile = profileService.updateMyProfile(accountId, request);

        ApiResponse<ProfileResponseDTO> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("Profile updated");
        response.setData(profile);
        return response;
    }

    @GetMapping("/{username}")
    public ApiResponse<PublicProfileResponseDTO> getPublicProfile(@PathVariable String username) {
        PublicProfileResponseDTO profile = profileService.getPublicProfile(username);
        ApiResponse<PublicProfileResponseDTO> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("Profile fetched");
        response.setData(profile);
        return response;
    }

    @PostMapping("/me/kyc")
    @PreAuthorize("hasRole('TITIPERS')")
    public ApiResponse<ProfileResponseDTO> submitKyc(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody KycSubmissionRequestDTO request
    ) {
        UUID accountId = userDetails.getId();
        ProfileResponseDTO profile = profileService.submitKyc(accountId, request);

        ApiResponse<ProfileResponseDTO> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("KYC submitted");
        response.setData(profile);
        return response;
    }

    @GetMapping("/me/kyc")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<KycStatusResponseDTO> getMyKycStatus(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        UUID accountId = userDetails.getId();
        KycStatusResponseDTO status = profileService.getMyKycStatus(accountId);

        ApiResponse<KycStatusResponseDTO> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("KYC status fetched");
        response.setData(status);
        return response;
    }
}

