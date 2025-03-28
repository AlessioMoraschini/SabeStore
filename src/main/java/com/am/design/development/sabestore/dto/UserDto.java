package com.am.design.development.sabestore.dto;

import com.am.design.development.dto.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    @NotNull(message = "Invalid id, must not be null")
    @NotBlank(message = "Invalid id, must not be empty")
    @Pattern(regexp = ".{1,255}", message = "Invalid name length, must be between 1 and 255 characters")
    @Schema(description = "Name of the user", example = "Mario")
    String name;

    @NotNull(message = "Invalid surname, must not be null")
    @NotBlank(message = "Invalid surname, must not be empty")
    @Pattern(regexp = ".{1,255}", message = "Invalid surname length, must be between 1 and 255 characters")
    @Schema(description = "Surname of the user", example = "Bianchi")
    String surname;

    @Pattern(regexp = ".{1,255}", message = "Invalid surname length, must be between 1 and 255 characters")
    @Email(message = "Invalid Role, must be a mail format!")
    @Schema(description = "Mail of the user", example = "test@testuser.com")
    String mail;

    @Schema(description = "Comma separated list of non repeated Roles of the user, can be USER/SUPERUSER", example = "USER,SUPERUSER")
    @NotEmpty(message = "Invalid roles, must contain at least one role")
    Set<UserRole> roles;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()])[A-Za-z\\d!@#$%^&*()]{10,}$",
            message = "Invalid password, must contain at least one uppercase letter, one lowercase letter, one digit, one special character, and be at least 10 characters long")
    @Schema(description = "Plain text password for the user", example = "Ajdaniauh")
    String password;

    @Min(value = 0, message = "Invalid age, must be between 0 and 150")
    @Max(value = 150, message = "Invalid age, must be between 0 and 150")
    @Schema(description = "Age of the user", example = "25")
    Integer age;
}
