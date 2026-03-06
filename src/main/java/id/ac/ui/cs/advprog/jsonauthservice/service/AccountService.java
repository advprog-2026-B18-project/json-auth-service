package id.ac.ui.cs.advprog.jsonauthservice.service;

import id.ac.ui.cs.advprog.jsonauthservice.dto.account.AccountResponseDTO;
import id.ac.ui.cs.advprog.jsonauthservice.model.Account;

import java.util.UUID;

public interface AccountService {
    Account getById(UUID id);
    Account getByEmail(String email);
    AccountResponseDTO toDTO(Account account);

    void banAccount(UUID id);
    void activateAccount(UUID id);
    void markPendingVerification(UUID id);
}

