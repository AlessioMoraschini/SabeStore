package com.am.design.development.sabestore.dto;

import com.am.design.development.dto.UserVerificationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDtoFull extends UserDto {
    Long id;
    String randomIdentifier;
    UserVerificationStatus verificationStatus;
}
