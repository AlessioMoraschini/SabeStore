package com.am.design.development.data.userdb.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Integer age;
}