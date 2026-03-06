package id.ac.ui.cs.advprog.jsonauthservice.service;

import id.ac.ui.cs.advprog.jsonauthservice.dto.admin.AdminUserDetailResponseDTO;
import id.ac.ui.cs.advprog.jsonauthservice.dto.admin.AdminUserListResponseDTO;
import id.ac.ui.cs.advprog.jsonauthservice.model.AccountStatus;
import id.ac.ui.cs.advprog.jsonauthservice.model.Role;

public interface AdminUserService {

    AdminUserListResponseDTO listUsers(
            AccountStatus status,
            Role role,
            String search,
            int page,
            int limit,
            String sortBy,
            String order
    );

    AdminUserDetailResponseDTO getUserDetail(java.util.UUID userId);
}

