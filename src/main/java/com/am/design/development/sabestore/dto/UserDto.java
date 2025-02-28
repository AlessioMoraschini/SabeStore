package com.am.design.development.sabestore.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    @NotNull
    @NotBlank
    @Pattern(regexp = ".{1,255}", message = "Invalid name length, must be between 1 and 255 characters")
    @Schema(description = "Name of the user", example = "Mario")
    String name;

    @NotNull
    @NotBlank
    @Pattern(regexp = ".{1,255}", message = "Invalid surname length, must be between 1 and 255 characters")
    @Schema(description = "Surname of the user", example = "Bianchi")
    String surname;

    @Min(0)
    @Max(150)
    @Schema(description = "Age of the user", example = "25")
    Integer age;
}
