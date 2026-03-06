package id.ac.ui.cs.advprog.jsonauthservice.service.impl;

import id.ac.ui.cs.advprog.jsonauthservice.dto.account.AccountResponseDTO;
import id.ac.ui.cs.advprog.jsonauthservice.model.Account;
import id.ac.ui.cs.advprog.jsonauthservice.model.AccountStatus;
import id.ac.ui.cs.advprog.jsonauthservice.repository.AccountRepository;
import id.ac.ui.cs.advprog.jsonauthservice.service.AccountService;
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
}

