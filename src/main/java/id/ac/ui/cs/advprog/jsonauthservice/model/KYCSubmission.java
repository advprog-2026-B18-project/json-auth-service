package id.ac.ui.cs.advprog.jsonauthservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "kyc_submission")
public class KYCSubmission {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "kyc_id")
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private Account user;

    @Column(name = "full_name_ktp", nullable = false)
    private String fullNameKtp;

    @Column(name = "ktp_number", nullable = false, length = 16)
    private String ktpNumber;

    @Column(name = "ktp_photo_url", nullable = false)
    private String ktpPhotoUrl;

    @Column(name = "selfie_with_ktp_url", nullable = false)
    private String selfieWithKtpUrl;

    @ElementCollection
    @CollectionTable(
            name = "kyc_social_media_link",
            joinColumns = @JoinColumn(name = "kyc_id")
    )
    private List<SocialMediaLink> socialMediaLinks;

    @Column(columnDefinition = "text")
    private String bio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private KycStatus status = KycStatus.PENDING_VERIFICATION;

    @Column(name = "rejection_reason")
    private String rejectionReason;

    @ManyToOne
    @JoinColumn(name = "reviewed_by")
    private Account reviewedBy;

    @Column(name = "submitted_at", nullable = false, updatable = false)
    private OffsetDateTime submittedAt;

    @Column(name = "reviewed_at")
    private OffsetDateTime reviewedAt;

    @PrePersist
    public void onSubmit() {
        this.submittedAt = OffsetDateTime.now();
    }
}
