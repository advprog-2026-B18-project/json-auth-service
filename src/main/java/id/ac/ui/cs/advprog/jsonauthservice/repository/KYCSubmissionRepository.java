package id.ac.ui.cs.advprog.jsonauthservice.repository;

import id.ac.ui.cs.advprog.jsonauthservice.model.Account;
import id.ac.ui.cs.advprog.jsonauthservice.model.KYCSubmission;
import id.ac.ui.cs.advprog.jsonauthservice.model.KycStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface KYCSubmissionRepository extends JpaRepository<KYCSubmission, UUID> {

    Optional<KYCSubmission> findTopByUserOrderBySubmittedAtDesc(Account user);

    boolean existsByUserAndStatus(Account user, KycStatus status);

    Page<KYCSubmission> findByStatus(KycStatus status, Pageable pageable);
}

