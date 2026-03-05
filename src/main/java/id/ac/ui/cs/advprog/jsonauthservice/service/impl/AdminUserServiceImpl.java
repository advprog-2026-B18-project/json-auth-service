package id.ac.ui.cs.advprog.jsonauthservice.service.impl;

import id.ac.ui.cs.advprog.jsonauthservice.dto.admin.AdminUserDetailResponseDTO;
import id.ac.ui.cs.advprog.jsonauthservice.dto.admin.AdminUserListItemDTO;
import id.ac.ui.cs.advprog.jsonauthservice.dto.admin.AdminUserListResponseDTO;
import id.ac.ui.cs.advprog.jsonauthservice.dto.admin.PaginationDTO;
import id.ac.ui.cs.advprog.jsonauthservice.dto.jastiper.JastiperStats;
import id.ac.ui.cs.advprog.jsonauthservice.model.Account;
import id.ac.ui.cs.advprog.jsonauthservice.model.AccountStatus;
import id.ac.ui.cs.advprog.jsonauthservice.model.KYCSubmission;
import id.ac.ui.cs.advprog.jsonauthservice.model.Role;
import id.ac.ui.cs.advprog.jsonauthservice.repository.AccountRepository;
import id.ac.ui.cs.advprog.jsonauthservice.repository.KYCSubmissionRepository;
import id.ac.ui.cs.advprog.jsonauthservice.service.AdminUserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class AdminUserServiceImpl implements AdminUserService {

    private final AccountRepository accountRepository;

    private final KYCSubmissionRepository kycSubmissionRepository;

    public AdminUserServiceImpl(AccountRepository accountRepository,
                                KYCSubmissionRepository kycSubmissionRepository) {
        this.accountRepository = accountRepository;
        this.kycSubmissionRepository = kycSubmissionRepository;
    }

    @Override
    public AdminUserListResponseDTO listUsers(
            AccountStatus status,
            Role role,
            String search,
            int page,
            int limit,
            String sortBy,
            String order
    ) {
        int pageIndex = Math.max(page, 1) - 1;
        int pageSize = Math.max(limit, 1);

        String sortProperty;
        if ("email".equalsIgnoreCase(sortBy)) {
            sortProperty = "email";
        } else if ("username".equalsIgnoreCase(sortBy)) {
            sortProperty = "username";
        } else if ("full_name".equalsIgnoreCase(sortBy)) {
            sortProperty = "fullName";
        } else if ("role".equalsIgnoreCase(sortBy)) {
            sortProperty = "role";
        } else if ("status".equalsIgnoreCase(sortBy)) {
            sortProperty = "status";
        } else {
            sortProperty = "createdAt";
        }

        Sort.Direction direction = "asc".equalsIgnoreCase(order) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(pageIndex, pageSize, Sort.by(direction, sortProperty));

        Specification<Account> spec = buildSpec(status, role, search);
        Page<Account> pageResult = accountRepository.findAll(spec, pageable);

        List<AdminUserListItemDTO> items = pageResult.getContent().stream()
                .map(this::toDto)
                .collect(Collectors.toList());

        PaginationDTO pagination = new PaginationDTO();
        pagination.setPage(page);
        pagination.setLimit(limit);
        pagination.setTotal(pageResult.getTotalElements());

        AdminUserListResponseDTO response = new AdminUserListResponseDTO();
        response.setData(items);
        response.setPagination(pagination);
        return response;
    }

    private Specification<Account> buildSpec(AccountStatus status, Role role, String search) {
        return (root, query, cb) -> {
            List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>();

            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            if (role != null) {
                predicates.add(cb.equal(root.get("role"), role));
            }
            if (search != null && !search.isBlank()) {
                String like = "%" + search.toLowerCase(Locale.ROOT) + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("email")), like),
                        cb.like(cb.lower(root.get("username")), like)
                ));
            }

            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };
    }

    private AdminUserListItemDTO toDto(Account account) {
        AdminUserListItemDTO dto = new AdminUserListItemDTO();
        dto.setUserId(account.getId());
        dto.setEmail(account.getEmail());
        dto.setUsername(account.getUsername());
        dto.setFullName(account.getFullName());
        dto.setRole(account.getRole());
        dto.setStatus(account.getStatus());
        dto.setCreatedAt(account.getCreatedAt());
        return dto;
    }

    @Override
    public AdminUserDetailResponseDTO getUserDetail(java.util.UUID userId) {
        Account account = accountRepository.findById(userId)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("User not found"));

        AdminUserDetailResponseDTO dto = new AdminUserDetailResponseDTO();
        dto.setUserId(account.getId());
        dto.setEmail(account.getEmail());
        dto.setUsername(account.getUsername());
        dto.setFullName(account.getFullName());
        dto.setRole(account.getRole());
        dto.setStatus(account.getStatus());
        dto.setProfilePictureUrl(account.getProfilePictureUrl());
        dto.setPhoneNumber(account.getPhoneNumber());
        dto.setCreatedAt(account.getCreatedAt());

        kycSubmissionRepository.findTopByUserOrderBySubmittedAtDesc(account).ifPresent(kyc -> {
            dto.setKycId(kyc.getId());
            dto.setKycStatus(kyc.getStatus());
            dto.setKycSubmittedAt(kyc.getSubmittedAt());
            dto.setKycReviewedAt(kyc.getReviewedAt());
            dto.setKycRejectionReason(kyc.getRejectionReason());
        });

        if (account.getRole() == Role.JASTIPER) {
            JastiperStats stats = new JastiperStats();
            stats.setTotalOrders(0);
            stats.setCompletedOrders(0);
            stats.setSuccessRate(0.0);
            stats.setAvgRating(0.0);
            stats.setTotalReviews(0);
            dto.setStats(stats);
        }

        return dto;
    }
}

