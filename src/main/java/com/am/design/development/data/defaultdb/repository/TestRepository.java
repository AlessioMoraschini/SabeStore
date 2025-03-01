package com.am.design.development.data.defaultdb.repository;

import com.am.design.development.data.defaultdb.entity.TestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestRepository extends JpaRepository<TestEntity, Long> {

    List<TestEntity> getByTest(String name);
}
