package id.ac.ui.cs.advprog.jsonauthservice.service.impl;

import id.ac.ui.cs.advprog.jsonauthservice.repository.AccountRepository;
import id.ac.ui.cs.advprog.jsonauthservice.service.UsernameGeneratorStrategy;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class EmailUsernameGeneratorStrategy implements UsernameGeneratorStrategy {

    private final AccountRepository accountRepository;

    public EmailUsernameGeneratorStrategy(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public String generateUsername(String email) {
        String baseUsername = email.split("@")[0].replaceAll("[^a-zA-Z0-9_]", "");
        String finalUsername = baseUsername;

        Random random = new Random();
        while (accountRepository.existsByUsername(finalUsername)) {
            finalUsername = baseUsername + random.nextInt(10000);
        }

        return finalUsername;
    }
}