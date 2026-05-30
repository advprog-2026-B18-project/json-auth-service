package id.ac.ui.cs.advprog.jsonauthservice.service.impl;

import id.ac.ui.cs.advprog.jsonauthservice.dto.account.AccountResponseDTO;
import id.ac.ui.cs.advprog.jsonauthservice.dto.internal.InternalRatingResponseDTO;
import id.ac.ui.cs.advprog.jsonauthservice.model.Account;
import id.ac.ui.cs.advprog.jsonauthservice.model.AccountStatus;
import id.ac.ui.cs.advprog.jsonauthservice.model.Role;
import id.ac.ui.cs.advprog.jsonauthservice.repository.AccountRepository;
import id.ac.ui.cs.advprog.jsonauthservice.service.AccountService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account getById(UUID id) {
        return accountRepository.findById(id).orElseThrow();
    }

    @Override
    public Account getByEmail(String email) {
        return accountRepository.findByEmail(email).orElseThrow();
    }

    @Override
    public AccountResponseDTO toDTO(Account account) {
        AccountResponseDTO dto = new AccountResponseDTO();
        dto.setId(account.getId());
        dto.setEmail(account.getEmail());
        dto.setRole(account.getRole());
        dto.setUsername(account.getUsername());
        dto.setStatus(account.getStatus());
        return dto;
    }

    @Override
    public void banAccount(UUID id) {
        Account account = getById(id);
        account.setStatus(AccountStatus.BANNED);
        accountRepository.save(account);
    }

    @Override
    public void activateAccount(UUID id) {
        Account account = getById(id);
        account.setStatus(AccountStatus.ACTIVE);
        accountRepository.save(account);
    }

    @Override
    public void markPendingVerification(UUID id) {
        Account account = getById(id);
        account.setStatus(AccountStatus.PENDING_VERIFICATION);
        accountRepository.save(account);
    }

    @Override
    public InternalRatingResponseDTO updateRating(UUID userId, Double rating, Boolean isCompleted) {
        Account account = accountRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Jastiper not found"));

        if (account.getRole() != Role.JASTIPER) {
            throw new EntityNotFoundException("Jastiper not found");
        }

        // ratingCount tracks number of ratings for avgRating calculation,
        // separate from totalOrders/completedOrders which track order lifecycle
        account.setRatingCount(account.getRatingCount() + 1);

        int count = account.getRatingCount();
        double currentTotalScore = account.getAvgRating() * (count - 1);
        double newAvgRating = (currentTotalScore + rating) / count;
        account.setAvgRating(Math.round(newAvgRating * 10.0) / 10.0);

        accountRepository.save(account);

        return new InternalRatingResponseDTO(
                account.getId(),
                account.getAvgRating(),
                account.getTotalOrders(),
                account.getCompletedOrders()
        );
    }

    @Override
    public void handleOrderEvent(UUID userId, String event) {
        Account account = accountRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Jastiper not found"));

        switch (event) {
            case "CREATED" -> account.setTotalOrders(account.getTotalOrders() + 1);
            case "COMPLETED" -> account.setCompletedOrders(account.getCompletedOrders() + 1);
            default -> throw new IllegalArgumentException("Unknown event: " + event);
        }

        accountRepository.save(account);
    }
}

