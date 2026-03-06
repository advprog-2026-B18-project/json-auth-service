package id.ac.ui.cs.advprog.jsonauthservice.controller;

import id.ac.ui.cs.advprog.jsonauthservice.dto.admin.KycListResponseDTO;
import id.ac.ui.cs.advprog.jsonauthservice.dto.admin.KycReviewRequestDTO;
import id.ac.ui.cs.advprog.jsonauthservice.dto.common.ApiResponse;
import id.ac.ui.cs.advprog.jsonauthservice.model.KycStatus;
import id.ac.ui.cs.advprog.jsonauthservice.service.AdminKycService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import id.ac.ui.cs.advprog.jsonauthservice.dto.profile.KycStatusResponseDTO;

@RestController
@RequestMapping("/admin/kyc")
@PreAuthorize("hasRole('ADMIN')")
public class AdminKycController {

    private final AdminKycService adminKycService;

    public AdminKycController(AdminKycService adminKycService) {
        this.adminKycService = adminKycService;
    }

    @GetMapping
    public ApiResponse<KycListResponseDTO> listKyc(
            @RequestParam(value = "status", required = false) String statusParam,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "limit", defaultValue = "10") int limit
    ) {
        KycStatus status = null;
        if (statusParam != null && !statusParam.isBlank()) {
            status = KycStatus.valueOf(statusParam.toUpperCase());
        }

        KycListResponseDTO result = adminKycService.listSubmissions(status, page, limit);
        ApiResponse<KycListResponseDTO> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("KYC submissions fetched");
        response.setData(result);
        return response;
    }

    @PatchMapping("/{kycId}/review")
    public ApiResponse<KycStatusResponseDTO> reviewKyc(
            @PathVariable("kycId") java.util.UUID kycId,
            @RequestBody KycReviewRequestDTO request
    ) {
        KycStatusResponseDTO result = adminKycService.reviewSubmission(kycId, request);
        ApiResponse<KycStatusResponseDTO> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("KYC reviewed");
        response.setData(result);
        return response;
    }
}

