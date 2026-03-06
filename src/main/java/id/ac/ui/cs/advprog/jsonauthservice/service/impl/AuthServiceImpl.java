package id.ac.ui.cs.advprog.jsonauthservice.service.impl;

import id.ac.ui.cs.advprog.jsonauthservice.dto.auth.AuthResponseDTO;
import id.ac.ui.cs.advprog.jsonauthservice.dto.auth.RegisterResponseDTO;
import id.ac.ui.cs.advprog.jsonauthservice.dto.auth.LoginRequestDTO;
import id.ac.ui.cs.advprog.jsonauthservice.dto.auth.RefreshTokenRequestDTO;
import id.ac.ui.cs.advprog.jsonauthservice.dto.auth.RefreshTokenResponseDTO;
import id.ac.ui.cs.advprog.jsonauthservice.dto.auth.RegisterRequestDTO;
import id.ac.ui.cs.advprog.jsonauthservice.model.Account;
import id.ac.ui.cs.advprog.jsonauthservice.dto.account.AccountResponseDTO;
import id.ac.ui.cs.advprog.jsonauthservice.repository.AccountRepository;
import id.ac.ui.cs.advprog.jsonauthservice.model.AccountStatus;
import id.ac.ui.cs.advprog.jsonauthservice.model.Role;
import id.ac.ui.cs.advprog.jsonauthservice.security.JwtTokenProvider;
import id.ac.ui.cs.advprog.jsonauthservice.security.RevokedTokenStore;
import id.ac.ui.cs.advprog.jsonauthservice.service.AccountService;
import id.ac.ui.cs.advprog.jsonauthservice.service.AuthService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AccountService accountService;
    private final RevokedTokenStore revokedTokenStore;

    public AuthServiceImpl(AccountRepository accountRepository,
                           PasswordEncoder passwordEncoder,
                           JwtTokenProvider jwtTokenProvider,
                           AccountService accountService,
                           RevokedTokenStore revokedTokenStore) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.accountService = accountService;
        this.revokedTokenStore = revokedTokenStore;
    }

    @Override
    public RegisterResponseDTO register(RegisterRequestDTO request) {
        if (!request.getPassword().equals(request.getPasswordConfirmation())) {
            throw new IllegalArgumentException("Password mismatch");
        }

        if (!request.getPassword().matches("^(?=.*[A-Za-z])(?=.*\\d).+$")) {
            throw new IllegalArgumentException("Password must contain letters and numbers");
        }

        if (accountRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }

        if (request.getRole() == Role.ADMIN) {
            throw new IllegalArgumentException("Cannot request admin role");
        }

        Account account = new Account();
        account.setEmail(request.getEmail());
        account.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        account.setRole(request.getRole());
        account.setUsername(null);
        account.setStatus((request.getRole() == Role.JASTIPER) ? AccountStatus.PENDING_VERIFICATION : AccountStatus.ACTIVE);

        Account saved = accountRepository.save(account);

        RegisterResponseDTO response = new RegisterResponseDTO();
        response.setUserId(saved.getId());
        response.setEmail(saved.getEmail());
        response.setRole(saved.getRole());
        response.setCreatedAt(saved.getCreatedAt());
        return response;
    }

    @Override
    public AuthResponseDTO login(LoginRequestDTO request) {
        Account account = accountRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), account.getPasswordHash())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        if (account.getStatus() != null && account.getStatus() == AccountStatus.BANNED) {
            throw new AccessDeniedException("Account is banned");
        }

        String refreshToken = jwtTokenProvider.generateAccessToken(
                account.getId(),
                account.getEmail(),
                account.getRole()
        );

        return buildAuthResponse(account, refreshToken);
    }

    @Override
    public RefreshTokenResponseDTO refreshToken(RefreshTokenRequestDTO request) {
        String token = request.getRefreshToken();

        if (revokedTokenStore.isRevoked(token)) {
            throw new BadCredentialsException("Invalid refresh token");
        }

        if (!jwtTokenProvider.validateToken(token)) {
            throw new BadCredentialsException("Invalid refresh token");
        }

        String accountId = jwtTokenProvider.getAccountIdFromToken(token);
        Account account = accountService.getById(UUID.fromString(accountId));

        String newRefreshToken = jwtTokenProvider.generateAccessToken(
                account.getId(),
                account.getEmail(),
                account.getRole()
        );

        RefreshTokenResponseDTO response = new RefreshTokenResponseDTO();
        response.setRefreshToken(newRefreshToken);
        response.setExpiresIn(jwtTokenProvider.getJwtExpirationMs() / 1000);
        return response;
    }

    @Override
    public void logout(RefreshTokenRequestDTO request) {
        String token = request.getRefreshToken();
        revokedTokenStore.revoke(token);
    }

    private AuthResponseDTO buildAuthResponse(Account account, String refreshToken) {
        AccountResponseDTO accountResponseDTO = accountService.toDTO(account);
        AuthResponseDTO responseDTO = new AuthResponseDTO();
        responseDTO.setRefreshToken(refreshToken);
        responseDTO.setExpiresIn(jwtTokenProvider.getJwtExpirationMs() / 1000);
        responseDTO.setUser(accountResponseDTO);
        return responseDTO;
    }
}

