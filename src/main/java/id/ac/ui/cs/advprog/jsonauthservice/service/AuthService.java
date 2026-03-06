package id.ac.ui.cs.advprog.jsonauthservice.service;

import id.ac.ui.cs.advprog.jsonauthservice.dto.auth.AuthResponseDTO;
import id.ac.ui.cs.advprog.jsonauthservice.dto.auth.LoginRequestDTO;
import id.ac.ui.cs.advprog.jsonauthservice.dto.auth.RefreshTokenRequestDTO;
import id.ac.ui.cs.advprog.jsonauthservice.dto.auth.RefreshTokenResponseDTO;
import id.ac.ui.cs.advprog.jsonauthservice.dto.auth.RegisterRequestDTO;
import id.ac.ui.cs.advprog.jsonauthservice.dto.auth.RegisterResponseDTO;

public interface AuthService {
    RegisterResponseDTO register(RegisterRequestDTO request);
    AuthResponseDTO login(LoginRequestDTO request);
    RefreshTokenResponseDTO refreshToken(RefreshTokenRequestDTO request);
    void logout(RefreshTokenRequestDTO request);
}

