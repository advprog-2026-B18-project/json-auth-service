package id.ac.ui.cs.advprog.jsonauthservice.service.impl;

import id.ac.ui.cs.advprog.jsonauthservice.dto.admin.KycListItemDTO;
import id.ac.ui.cs.advprog.jsonauthservice.dto.admin.KycListResponseDTO;
import id.ac.ui.cs.advprog.jsonauthservice.dto.admin.KycReviewRequestDTO;
import id.ac.ui.cs.advprog.jsonauthservice.dto.admin.PaginationDTO;
import id.ac.ui.cs.advprog.jsonauthservice.dto.profile.KycStatusResponseDTO;
import id.ac.ui.cs.advprog.jsonauthservice.model.Account;
import id.ac.ui.cs.advprog.jsonauthservice.model.AccountStatus;
import id.ac.ui.cs.advprog.jsonauthservice.model.KYCSubmission;
import id.ac.ui.cs.advprog.jsonauthservice.model.KycStatus;
import id.ac.ui.cs.advprog.jsonauthservice.model.Role;
import id.ac.ui.cs.advprog.jsonauthservice.repository.KYCSubmissionRepository;
import id.ac.ui.cs.advprog.jsonauthservice.service.AdminKycService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AdminKycServiceImpl implements AdminKycService {

    private final KYCSubmissionRepository kycSubmissionRepository;

    public AdminKycServiceImpl(KYCSubmissionRepository kycSubmissionRepository) {
        this.kycSubmissionRepository = kycSubmissionRepository;
    }

    @Override
    public KycListResponseDTO listSubmissions(KycStatus status, int page, int limit) {
        int pageIndex = Math.max(page, 1) - 1;
        int pageSize = Math.max(limit, 1);
        Pageable pageable = PageRequest.of(pageIndex, pageSize);

        Page<KYCSubmission> submissionsPage = (status != null)
                ? kycSubmissionRepository.findByStatus(status, pageable)
                : kycSubmissionRepository.findAll(pageable);

        List<KycListItemDTO> items = submissionsPage.getContent().stream()
                .map(this::toDto)
                .collect(Collectors.toList());

        PaginationDTO pagination = new PaginationDTO();
        pagination.setPage(page);
        pagination.setLimit(limit);
        pagination.setTotal(submissionsPage.getTotalElements());

        KycListResponseDTO response = new KycListResponseDTO();
        response.setData(items);
        response.setPagination(pagination);
        return response;
    }

    private KycListItemDTO toDto(KYCSubmission submission) {
        KycListItemDTO dto = new KycListItemDTO();
        dto.setKycId(submission.getId());
        dto.setUserId(submission.getUser().getId());
        dto.setUsername(submission.getUser().getUsername());
        dto.setFullNameKtp(submission.getFullNameKtp());
        dto.setStatus(submission.getStatus());
        dto.setSubmittedAt(submission.getSubmittedAt());
        return dto;
    }

    @Override
    public KycStatusResponseDTO reviewSubmission(UUID kycId, KycReviewRequestDTO request) {
        KYCSubmission submission = kycSubmissionRepository.findById(kycId)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("KYC submission not found"));

        if (submission.getStatus() != KycStatus.PENDING_VERIFICATION) {
            throw new IllegalArgumentException("KYC already reviewed");
        }

        if (request.getAction() == KycReviewRequestDTO.Action.REJECT &&
                (request.getRejectionReason() == null || request.getRejectionReason().isBlank())) {
            throw new IllegalArgumentException("Rejection reason is required when rejecting KYC");
        }

        submission.setReviewedAt(OffsetDateTime.now());

        Account user = submission.getUser();

        if (request.getAction() == KycReviewRequestDTO.Action.APPROVE) {
            submission.setStatus(KycStatus.APPROVED);
            submission.setRejectionReason(null);
            user.setRole(Role.JASTIPER);
            user.setStatus(AccountStatus.ACTIVE);
        } else {
            submission.setStatus(KycStatus.REJECTED);
            submission.setRejectionReason(request.getRejectionReason());
            // keep user role/status unchanged
        }

        kycSubmissionRepository.save(submission);

        KycStatusResponseDTO dto = new KycStatusResponseDTO();
        dto.setKycId(submission.getId());
        dto.setStatus(submission.getStatus());
        dto.setSubmittedAt(submission.getSubmittedAt());
        dto.setReviewedAt(submission.getReviewedAt());
        dto.setRejectionReason(submission.getRejectionReason());
        return dto;
    }
}

