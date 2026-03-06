package id.ac.ui.cs.advprog.jsonauthservice.controller;

import id.ac.ui.cs.advprog.jsonauthservice.dto.auth.AuthResponseDTO;
import id.ac.ui.cs.advprog.jsonauthservice.dto.auth.LoginRequestDTO;
import id.ac.ui.cs.advprog.jsonauthservice.dto.auth.RefreshTokenRequestDTO;
import id.ac.ui.cs.advprog.jsonauthservice.dto.auth.RefreshTokenResponseDTO;
import id.ac.ui.cs.advprog.jsonauthservice.dto.auth.RegisterRequestDTO;
import id.ac.ui.cs.advprog.jsonauthservice.dto.auth.RegisterResponseDTO;
import id.ac.ui.cs.advprog.jsonauthservice.dto.common.ApiResponse;
import id.ac.ui.cs.advprog.jsonauthservice.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ApiResponse<RegisterResponseDTO> register(@Valid @RequestBody RegisterRequestDTO request) {
        RegisterResponseDTO result = authService.register(request);
        ApiResponse<RegisterResponseDTO> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("Registration successful");
        response.setData(result);
        return response;
    }

    @PostMapping("/login")
    public ApiResponse<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        AuthResponseDTO result = authService.login(request);
        ApiResponse<AuthResponseDTO> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("Login successful");
        response.setData(result);
        return response;
    }

    @PostMapping("/refresh-token")
    public ApiResponse<RefreshTokenResponseDTO> refresh(@Valid @RequestBody RefreshTokenRequestDTO request) {
        RefreshTokenResponseDTO result = authService.refreshToken(request);
        ApiResponse<RefreshTokenResponseDTO> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("Token refreshed");
        response.setData(result);
        return response;
    }

    @PostMapping("/logout")
    public ApiResponse<Object> logout(@Valid @RequestBody RefreshTokenRequestDTO request) {
        authService.logout(request);
        ApiResponse<Object> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("Logged out successfully");
        response.setData(null);
        return response;
    }
}

