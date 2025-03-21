package com.am.design.development.data.userdb.repository;

import com.am.design.development.data.userdb.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Page<UserEntity> findAll(Pageable pageable);

    List<UserEntity> findByName(String name);
    List<UserEntity> findByNameAndSurname(String name, String surname);
    List<UserEntity> findByNameAndSurnameAndAge(String name, String surname, Integer age);

    UserEntity findByMail(String mail);

    @Query("SELECT u FROM SABE_USER u LEFT JOIN FETCH u.roles WHERE u.mail = :mail")
    UserEntity findByMailWithRoles(@Param("mail") String mail);

}
