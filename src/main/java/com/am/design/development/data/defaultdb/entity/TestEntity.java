package com.am.design.development.data.defaultdb.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name="DEF_TEST_TABLE")
@SequenceGenerator(name = "DEF_TEST_TABLE_SEQ", allocationSize = 1)
public class TestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DEF_TEST_TABLE_SEQ")
    private Long id;
    @Column
    private String test;
}