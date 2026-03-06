package id.ac.ui.cs.advprog.jsonauthservice.service;

import id.ac.ui.cs.advprog.jsonauthservice.dto.profile.*;

import java.util.UUID;

public interface ProfileService {
    ProfileResponseDTO getMyProfile(UUID accountId);
    ProfileResponseDTO updateMyProfile(UUID accountId, UpdateProfileRequestDTO request);
    PublicProfileResponseDTO getPublicProfile(String username);
    ProfileResponseDTO submitKyc(UUID accountId, KycSubmissionRequestDTO request);
    KycStatusResponseDTO getMyKycStatus(UUID accountId);
}

