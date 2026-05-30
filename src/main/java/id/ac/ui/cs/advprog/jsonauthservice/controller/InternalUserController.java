package id.ac.ui.cs.advprog.jsonauthservice.controller;

import id.ac.ui.cs.advprog.jsonauthservice.dto.internal.InternalOrderEventRequestDTO;
import id.ac.ui.cs.advprog.jsonauthservice.dto.internal.InternalRatingRequestDTO;
import id.ac.ui.cs.advprog.jsonauthservice.dto.internal.InternalRatingResponseDTO;
import id.ac.ui.cs.advprog.jsonauthservice.model.Account;
import id.ac.ui.cs.advprog.jsonauthservice.model.AccountStatus;
import id.ac.ui.cs.advprog.jsonauthservice.repository.AccountRepository;
import id.ac.ui.cs.advprog.jsonauthservice.service.AccountService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/internal/users")
public class InternalUserController {

    private final AccountRepository accountRepository;
    private final AccountService accountService;

    public InternalUserController(AccountRepository accountRepository, AccountService accountService) {
        this.accountRepository = accountRepository;
        this.accountService = accountService;
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
    public ResponseEntity<InternalRatingResponseDTO> updateRating(
            @PathVariable UUID userId,
            @Valid @RequestBody InternalRatingRequestDTO request) {
        InternalRatingResponseDTO response = accountService.updateRating(
                userId, request.getRating(), request.getIsCompleted());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{userId}/order-event")
    public ResponseEntity<Void> handleOrderEvent(
            @PathVariable UUID userId,
            @Valid @RequestBody InternalOrderEventRequestDTO request) {
        accountService.handleOrderEvent(userId, request.getEvent());
        return ResponseEntity.ok().build();
    }
}