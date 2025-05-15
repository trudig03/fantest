package com.project.fanla.controller;

import com.project.fanla.dto.AdminUserDto;
import com.project.fanla.service.AdminUserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/superadmin/users")
@PreAuthorize("hasRole('ROLE_SUPERADMIN')")
public class AdminUserController {

    private final AdminUserService adminUserService;

    public AdminUserController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @GetMapping
    public ResponseEntity<List<AdminUserDto>> getAllAdmins() {
        return ResponseEntity.ok(adminUserService.getAllAdmins());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminUserDto> getAdminById(@PathVariable Long id) {
        return ResponseEntity.ok(adminUserService.getAdminById(id));
    }

    @GetMapping("/team/{teamId}")
    public ResponseEntity<List<AdminUserDto>> getAdminsByTeam(@PathVariable Long teamId) {
        return ResponseEntity.ok(adminUserService.getAdminsByTeam(teamId));
    }

    @PostMapping
    public ResponseEntity<AdminUserDto> createAdmin(@Valid @RequestBody AdminUserDto adminUserDto) {
        AdminUserDto createdAdmin = adminUserService.createAdmin(adminUserDto);
        return new ResponseEntity<>(createdAdmin, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdminUserDto> updateAdmin(@PathVariable Long id, @Valid @RequestBody AdminUserDto adminUserDto) {
        AdminUserDto updatedAdmin = adminUserService.updateAdmin(id, adminUserDto);
        return ResponseEntity.ok(updatedAdmin);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable Long id) {
        adminUserService.deleteAdmin(id);
        return ResponseEntity.noContent().build();
    }
}
