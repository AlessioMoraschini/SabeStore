package com.am.design.development.sabestore.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Generated;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name="SABE_USER")
public class UserEntity {
    @Id
    @Generated()
    private Integer id;
    @Column
    private String name;
    @Column
    private String surname;
    @Column
    private Integer age;
}