package vn.edu.hcmute.example4;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

record RegisterRequest(
        @NotBlank @Email String email,
        @NotBlank @Size(min = 6, max = 72) String password,
        @NotBlank @Size(max = 100) String fullName) {}

record LoginRequest(@NotBlank @Email String email, @NotBlank String password) {}

record LoginResponse(String token, long expiresIn) {}

record UserResponse(Long id, String email, String fullName, String role) {
    static UserResponse from(AppUser user) {
        return new UserResponse(user.getId(), user.getEmail(), user.getFullName(), user.getRole());
    }
}
