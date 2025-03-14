package com.am.design.development.data.userdb.entity;

import com.am.design.development.dto.UserVerificationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name="SABE_USER")
@SequenceGenerator(name = "SABE_USER_SEQ", allocationSize = 1)
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SABE_USER_SEQ")
    private Long id;
    @Column
    private String name;
    @Column
    private String surname;
    @Column
    private String mail;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<RoleEntity> roles = new HashSet<>();
    @Column
    private String password;
    @Column(name = "RANDOM_IDENTIFIER")
    String randomIdentifier;
    @Column(name = "VERIFICATION_STATUS")
    @Enumerated(EnumType.STRING)
    UserVerificationStatus verificationStatus;
    @Column
    private Integer age;
}