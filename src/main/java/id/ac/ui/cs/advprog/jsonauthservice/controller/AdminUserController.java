package id.ac.ui.cs.advprog.jsonauthservice.controller;

import id.ac.ui.cs.advprog.jsonauthservice.dto.admin.AdminUserDetailResponseDTO;
import id.ac.ui.cs.advprog.jsonauthservice.dto.admin.AdminUserListResponseDTO;
import id.ac.ui.cs.advprog.jsonauthservice.dto.common.ApiResponse;
import id.ac.ui.cs.advprog.jsonauthservice.model.AccountStatus;
import id.ac.ui.cs.advprog.jsonauthservice.model.Role;
import id.ac.ui.cs.advprog.jsonauthservice.service.AdminUserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    private final AdminUserService adminUserService;

    public AdminUserController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @GetMapping
    public ApiResponse<AdminUserListResponseDTO> listUsers(
            @RequestParam(value = "status", required = false) String statusParam,
            @RequestParam(value = "role", required = false) String roleParam,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "limit", defaultValue = "10") int limit,
            @RequestParam(value = "sort_by", required = false) String sortBy,
            @RequestParam(value = "order", defaultValue = "desc") String order
    ) {
        AccountStatus status = null;
        if (statusParam != null && !statusParam.isBlank()) {
            status = AccountStatus.valueOf(statusParam.toUpperCase());
        }

        Role role = null;
        if (roleParam != null && !roleParam.isBlank()) {
            role = Role.valueOf(roleParam.toUpperCase());
        }

        AdminUserListResponseDTO result = adminUserService.listUsers(
                status, role, search, page, limit, sortBy, order
        );
        ApiResponse<AdminUserListResponseDTO> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("Users fetched");
        response.setData(result);
        return response;
    }

    @GetMapping("/{userId}")
    public ApiResponse<AdminUserDetailResponseDTO> getUserDetail(
            @PathVariable("userId") java.util.UUID userId
    ) {
        AdminUserDetailResponseDTO result = adminUserService.getUserDetail(userId);
        ApiResponse<AdminUserDetailResponseDTO> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("User detail fetched");
        response.setData(result);
        return response;
    }
}

