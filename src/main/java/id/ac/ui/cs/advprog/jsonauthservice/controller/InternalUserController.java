package id.ac.ui.cs.advprog.jsonauthservice.controller;

import id.ac.ui.cs.advprog.jsonauthservice.dto.internal.InternalRatingRequestDTO;
import id.ac.ui.cs.advprog.jsonauthservice.model.Account;
import id.ac.ui.cs.advprog.jsonauthservice.model.AccountStatus;
import id.ac.ui.cs.advprog.jsonauthservice.model.Role;
import id.ac.ui.cs.advprog.jsonauthservice.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/internal/users")
public class InternalUserController {

    private final AccountRepository accountRepository;

    public InternalUserController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @GetMapping("/{userId}/validate")
    public ResponseEntity<?> validateUser(@PathVariable UUID userId) {
        Account account = accountRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Map<String, Object> response = new HashMap<>();
        response.put("user_id", account.getId());
        response.put("role", account.getRole().name());
        response.put("status", account.getStatus().name());
        response.put("is_active", account.getStatus() == AccountStatus.ACTIVE);
        response.put("username", account.getUsername());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{userId}/rating")
    public ResponseEntity<?> updateRating(@PathVariable UUID userId, @RequestBody InternalRatingRequestDTO request) {
        Account account = accountRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Jastiper not found"));

        if (account.getRole() != Role.JASTIPER) {
            return ResponseEntity.status(404).body(Map.of("message", "Jastiper not found"));
        }

        account.setTotalOrders(account.getTotalOrders() + 1);

        if (Boolean.TRUE.equals(request.getIsCompleted())) {
            account.setCompletedOrders(account.getCompletedOrders() + 1);
        }

        double currentTotalScore = account.getAvgRating() * (account.getTotalOrders() - 1);
        double newAvgRating = (currentTotalScore + request.getRating()) / account.getTotalOrders();

        account.setAvgRating(Math.round(newAvgRating * 10.0) / 10.0);

        accountRepository.save(account);

        Map<String, Object> response = new HashMap<>();
        response.put("user_id", account.getId());
        response.put("avg_rating", account.getAvgRating());
        response.put("total_orders", account.getTotalOrders());
        response.put("completed_orders", account.getCompletedOrders());

        return ResponseEntity.ok(response);
    }
}