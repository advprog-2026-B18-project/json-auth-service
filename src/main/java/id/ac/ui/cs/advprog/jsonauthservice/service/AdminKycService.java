package id.ac.ui.cs.advprog.jsonauthservice.service;

import id.ac.ui.cs.advprog.jsonauthservice.dto.admin.KycListResponseDTO;
import id.ac.ui.cs.advprog.jsonauthservice.dto.admin.KycReviewRequestDTO;
import id.ac.ui.cs.advprog.jsonauthservice.dto.profile.KycStatusResponseDTO;
import id.ac.ui.cs.advprog.jsonauthservice.model.KycStatus;

public interface AdminKycService {

    KycListResponseDTO listSubmissions(KycStatus status, int page, int limit);
    KycStatusResponseDTO reviewSubmission(java.util.UUID kycId, KycReviewRequestDTO request);
}

