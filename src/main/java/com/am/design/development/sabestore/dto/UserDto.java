package com.am.design.development.sabestore.dto;

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
    Integer id;
    @NotNull
    @NotBlank
    @Pattern(regexp = ".{1,255}", message = "Invalid name length, must be between 1 and 255 characters")
    String name;
    @NotNull
    @NotBlank
    @Pattern(regexp = ".{1,255}", message = "Invalid surname length, must be between 1 and 255 characters")
    String surname;
    @Min(0)
    @Max(150)
    Integer age;
}
