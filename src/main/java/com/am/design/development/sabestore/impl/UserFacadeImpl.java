package com.am.design.development.sabestore.impl;

import com.am.design.development.data.userdb.entity.RoleEntity;
import com.am.design.development.data.userdb.entity.UserEntity;
import com.am.design.development.data.userdb.repository.RoleRepository;
import com.am.design.development.data.userdb.repository.UserRepository;
import com.am.design.development.dto.UserRole;
import com.am.design.development.sabestore.dto.UserDto;
import com.am.design.development.sabestore.dto.UserDtoFull;
import com.am.design.development.sabestore.facade.UserFacade;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserFacadeImpl implements UserFacade {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserFacadeImpl.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    @Transactional(transactionManager = "userDbTransactionManager", readOnly = true)
    // TODO with pagination!
    public List<UserDto> getUsers() {
        List<UserDto> userDtos = new ArrayList<>();

        for (UserEntity ent : userRepository.findAll()) {
            UserDtoFull dto = new UserDtoFull();
            BeanUtils.copyProperties(ent, dto);
            dto.setRoles(ent.getRoles().stream()
                    .map(RoleEntity::getRole)
                    .collect(Collectors.toSet()));
            dto.setPassword("********");
            userDtos.add(dto);
        }
        return userDtos;
    }

    @Override
    @Transactional(transactionManager = "userDbTransactionManager", readOnly = true)
    public UserDto getCurrentUserDetails(String mail) {
        UserEntity ent = userRepository.findByMail(mail);
        UserDtoFull dto = new UserDtoFull();
        BeanUtils.copyProperties(ent, dto);
        dto.setRoles(ent.getRoles().stream()
                .map(RoleEntity::getRole)
                .collect(Collectors.toSet()));
        dto.setPassword("********");
        return dto;
    }

    @Override
    @Transactional(transactionManager = "userDbTransactionManager")
    public UserDto addUser(UserDto userDto) {
        var userFromDb = userRepository.findByMail(userDto.getMail());
        if (userFromDb != null) {
            throw new RuntimeException("At least one User already found in DB with same mail");
        }

        var roleEntities = userDto.getRoles().stream()
                .map(role -> roleRepository.getByRole(role))
                .peek(roleEntity -> {
                    if (roleEntity == null) {
                        throw new RuntimeException("Role not found! You can only specify on of the following roles: "
                                + Arrays.toString(UserRole.values()));
                    }
                })
                .collect(Collectors.toSet());

        String hashedPsw = BCrypt.hashpw(userDto.getPassword(), BCrypt.gensalt());

        var userEntity = UserEntity.builder()
                .age(userDto.getAge())
                .surname(userDto.getSurname())
                .name(userDto.getName())
                .roles(roleEntities)
                .password(hashedPsw)
                .mail(userDto.getMail())
                .build();

        UserDtoFull response = new UserDtoFull();
        BeanUtils.copyProperties(userRepository.save(userEntity), response);
        response.setRoles(roleEntities.stream()
                .map(RoleEntity::getRole)
                .collect(Collectors.toSet()));
        response.setPassword("********");
        return response;
    }

    @Override
    @Transactional(transactionManager = "userDbTransactionManager")
    public UserDto updateUser(UserDto userDto, boolean isSuperUser) {
        var userFromDb = userRepository.findByMail(userDto.getMail());
        if (userFromDb == null) {
            throw new RuntimeException("User not found in DB");
        }

        // Reset roles before to update
        if (isSuperUser) {
            userFromDb.setRoles(new HashSet<>());

            var roleEntities = userDto.getRoles().stream()
                    .map(role -> roleRepository.getByRole(role))
                    .peek(roleEntity -> {
                        if (roleEntity == null) {
                            throw new RuntimeException("Role not found! You can only specify on of the following roles: "
                                    + Arrays.toString(UserRole.values()));
                        }
                    })
                    .collect(Collectors.toSet());
            userFromDb.setRoles(roleEntities);
        }

        String hashedPsw = BCrypt.hashpw(userDto.getPassword(), BCrypt.gensalt());

        userFromDb.setAge(userDto.getAge());
        userFromDb.setSurname(userDto.getSurname());
        userFromDb.setName(userDto.getName());
        userFromDb.setPassword(hashedPsw);
        userFromDb.setMail(userDto.getMail());

        UserDtoFull response = new UserDtoFull();
        BeanUtils.copyProperties(userRepository.save(userFromDb), response);
        response.setRoles(userFromDb.getRoles().stream()
                .map(RoleEntity::getRole)
                .collect(Collectors.toSet()));
        response.setPassword("********");
        return response;
    }

    @Override
    @Transactional(transactionManager = "userDbTransactionManager")
    public UserDto removeById(Long id) {
        var found = userRepository.getReferenceById(id);

        userRepository.deleteById(id);

        UserDtoFull dto = new UserDtoFull();
        try {
            found.getId(); // Will throw the exception if there is no entity found
            BeanUtils.copyProperties(found, dto);
            dto.setRoles(found.getRoles().stream()
                    .map(RoleEntity::getRole)
                    .collect(Collectors.toSet()));
            dto.setPassword("********");
        } catch (EntityNotFoundException e) {
            LOGGER.error("User not found by id: {}", id);
            throw new EntityNotFoundException("User not found by id: " + id);
        }

        return dto;
    }

    // TODO with pagination!
    @Override
    @Transactional(transactionManager = "userDbTransactionManager", readOnly = true)
    public List<UserDto> getUsersByFilter(UserDto userFilter) {
        return null;
    }
}
