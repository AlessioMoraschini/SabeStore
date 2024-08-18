package com.am.design.development.sabestore.data.repository;

import com.am.design.development.sabestore.data.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    List<UserEntity> getByName(String name);
    List<UserEntity> getByNameAndSurname(String name, String surname);
    List<UserEntity> getByNameAndSurnameAndAge(String name, String surname, Integer age);

}
