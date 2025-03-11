package com.am.design.development.security;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {
    @Schema(description = "Mail of the user", example = "xxx.y@mail.com")
    @Email
    private String username;
    @Schema(description = "Plain text password for the user", example = "Ajdaniauh")
    @NotBlank
    private String password;
}
