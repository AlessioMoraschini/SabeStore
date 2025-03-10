package com.am.design.development.data.userdb.repository;

import com.am.design.development.data.userdb.entity.RoleEntity;
import com.am.design.development.dto.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    RoleEntity getByRole(UserRole role);
}
