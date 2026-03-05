package id.ac.ui.cs.advprog.jsonauthservice.service.impl;

import id.ac.ui.cs.advprog.jsonauthservice.dto.profile.KycStatusResponseDTO;
import id.ac.ui.cs.advprog.jsonauthservice.dto.profile.KycSubmissionRequestDTO;
import id.ac.ui.cs.advprog.jsonauthservice.dto.profile.ProfileResponseDTO;
import id.ac.ui.cs.advprog.jsonauthservice.dto.profile.PublicJastiperStatsDTO;
import id.ac.ui.cs.advprog.jsonauthservice.dto.profile.PublicProfileResponseDTO;
import id.ac.ui.cs.advprog.jsonauthservice.dto.profile.UpdateProfileRequestDTO;
import id.ac.ui.cs.advprog.jsonauthservice.model.Account;
import id.ac.ui.cs.advprog.jsonauthservice.model.AccountStatus;
import id.ac.ui.cs.advprog.jsonauthservice.model.KYCSubmission;
import id.ac.ui.cs.advprog.jsonauthservice.model.KycStatus;
import id.ac.ui.cs.advprog.jsonauthservice.model.Role;
import id.ac.ui.cs.advprog.jsonauthservice.model.SocialMediaLink;
import id.ac.ui.cs.advprog.jsonauthservice.repository.AccountRepository;
import id.ac.ui.cs.advprog.jsonauthservice.repository.KYCSubmissionRepository;
import id.ac.ui.cs.advprog.jsonauthservice.service.ProfileService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProfileServiceImpl implements ProfileService {

    private final AccountRepository accountRepository;
    private final KYCSubmissionRepository kycSubmissionRepository;

    public ProfileServiceImpl(AccountRepository accountRepository,
                              KYCSubmissionRepository kycSubmissionRepository) {
        this.accountRepository = accountRepository;
        this.kycSubmissionRepository = kycSubmissionRepository;
    }

    @Override
    public ProfileResponseDTO getMyProfile(UUID accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow();
        ProfileResponseDTO dto = new ProfileResponseDTO();
        dto.setUserId(account.getId());
        dto.setEmail(account.getEmail());
        dto.setUsername(account.getUsername());
        dto.setFullName(account.getFullName());
        dto.setRole(account.getRole());
        dto.setStatus(account.getStatus());
        dto.setProfilePictureUrl(account.getProfilePictureUrl());
        dto.setPhoneNumber(account.getPhoneNumber());
        dto.setCreatedAt(account.getCreatedAt());

        kycSubmissionRepository.findTopByUserOrderBySubmittedAtDesc(account)
                .map(KYCSubmission::getStatus)
                .ifPresent(dto::setKycStatus);

        return dto;
    }

    @Override
    public ProfileResponseDTO updateMyProfile(UUID accountId, UpdateProfileRequestDTO request) {
        Account account = accountRepository.findById(accountId).orElseThrow();
        String requestedUsername = request.getUsername();

        if (requestedUsername == null || requestedUsername.isBlank()) {
            // Auto-generate from email local part if username is missing
            String localPart = account.getEmail().split("@")[0];
            String base = localPart.replaceAll("[^A-Za-z0-9_]", "_");
            if (base.length() > 30) {
                base = base.substring(0, 30);
            }
            String candidate = base.isEmpty() ? "user" : base;
            int suffix = 0;
            while (accountRepository.existsByUsername(candidate)) {
                suffix++;
                String withSuffix = base;
                String suffixStr = String.valueOf(suffix);
                if (withSuffix.length() + suffixStr.length() > 30) {
                    withSuffix = withSuffix.substring(0, 30 - suffixStr.length());
                }
                candidate = withSuffix + suffixStr;
            }
            account.setUsername(candidate);
        } else {
            // Explicit username: ensure uniqueness if changed
            if (!requestedUsername.equals(account.getUsername())
                    && accountRepository.existsByUsername(requestedUsername)) {
                throw new IllegalArgumentException("Username already taken");
            }
            account.setUsername(requestedUsername);
        }

        if (request.getFullName() != null) {
            account.setFullName(request.getFullName());
        }
        if (request.getPhoneNumber() != null) {
            account.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getProfilePictureUrl() != null) {
            account.setProfilePictureUrl(request.getProfilePictureUrl());
        }
        accountRepository.save(account);
        return getMyProfile(accountId);
    }

    @Override
    public PublicProfileResponseDTO getPublicProfile(String username) {
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Profile not found"));

        if (account.getStatus() == AccountStatus.BANNED) {
            throw new AccessDeniedException("Profile not available");
        }

        PublicProfileResponseDTO dto = new PublicProfileResponseDTO();
        dto.setUserId(account.getId());
        dto.setUsername(account.getUsername());
        dto.setFullName(account.getFullName());
        dto.setProfilePictureUrl(account.getProfilePictureUrl());
        dto.setRole(account.getRole());
        dto.setMemberSince(account.getCreatedAt());
        dto.setStatus(account.getStatus());

        if (account.getRole() == Role.JASTIPER) {
            PublicJastiperStatsDTO stats = new PublicJastiperStatsDTO();
            stats.setTotalOrders(0);
            stats.setSuccessRate(0.0);
            stats.setAvgRating(0.0);
            dto.setStats(stats);
            dto.setRating(0.0);
            dto.setBadges(java.util.List.of());
        }

        return dto;
    }

    @Override
    public ProfileResponseDTO submitKyc(UUID accountId, KycSubmissionRequestDTO request) {
        Account account = accountRepository.findById(accountId).orElseThrow();

        if (account.getRole() == Role.JASTIPER) {
            throw new IllegalArgumentException("Already a JASTIPER");
        }

        if (account.getUsername() == null || account.getUsername().isBlank()
                || account.getFullName() == null || account.getFullName().isBlank()) {
            throw new IllegalArgumentException("User must have username and full_name before submitting KYC");
        }

        if (kycSubmissionRepository.existsByUserAndStatus(account, KycStatus.PENDING_VERIFICATION)) {
            throw new IllegalArgumentException("KYC submission already pending");
        }

        KYCSubmission submission = new KYCSubmission();
        submission.setUser(account);
        submission.setFullNameKtp(request.getFullNameKtp());
        submission.setKtpNumber(request.getKtpNumber());
        submission.setKtpPhotoUrl(request.getKtpPhotoUrl());
        submission.setSelfieWithKtpUrl(request.getSelfieWithKtpUrl());

        java.util.List<SocialMediaLink> links = new java.util.ArrayList<>();
        request.getSocialMediaLinks().forEach(linkDto -> {
            SocialMediaLink link = new SocialMediaLink();
            link.setPlatform(linkDto.getPlatform());
            link.setUrl(linkDto.getUrl());
            links.add(link);
        });
        submission.setSocialMediaLinks(links);
        submission.setBio(request.getBio());
        submission.setStatus(KycStatus.PENDING_VERIFICATION);

        kycSubmissionRepository.save(submission);

        account.setStatus(AccountStatus.PENDING_VERIFICATION);
        accountRepository.save(account);

        return getMyProfile(accountId);
    }

    @Override
    public KycStatusResponseDTO getMyKycStatus(UUID accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow();

        KYCSubmission submission = kycSubmissionRepository.findTopByUserOrderBySubmittedAtDesc(account)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("KYC submission not found"));

        KycStatusResponseDTO dto = new KycStatusResponseDTO();
        dto.setKycId(submission.getId());
        dto.setStatus(submission.getStatus());
        dto.setSubmittedAt(submission.getSubmittedAt());
        dto.setReviewedAt(submission.getReviewedAt());
        dto.setRejectionReason(submission.getRejectionReason());
        return dto;
    }
}

