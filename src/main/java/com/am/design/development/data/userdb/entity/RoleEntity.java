package com.am.design.development.data.userdb.entity;

import com.am.design.development.dto.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name="USER_ROLE")
@SequenceGenerator(name = "ROLE_SEQ", allocationSize = 1)
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROLE_SEQ")
    private Long id;
    @Column
    @Enumerated(EnumType.STRING)
    private UserRole role;
}