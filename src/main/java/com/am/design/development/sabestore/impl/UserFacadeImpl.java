package com.am.design.development.sabestore.impl;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class UserFacadeImpl implements UserFacade {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserFacadeImpl.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<UserDto> getUsers() {
        List<UserDto> userDtos = new ArrayList<>();

        for (UserEntity ent : userRepository.findAll()) {
            UserDtoFull dto = new UserDtoFull();
            BeanUtils.copyProperties(ent, dto);
            dto.setUserRole(ent.getUserRole().getRole());
            dto.setPassword("********");
            userDtos.add(dto);
        }
        return userDtos;
    }

    @Override
    public UserDto addUser(UserDto userDto) {
        var userFromDb = userRepository.getByNameAndSurnameAndAge(userDto.getName(), userDto.getSurname(), userDto.getAge());
        if (userFromDb != null && !userFromDb.isEmpty()) {
            throw new RuntimeException("At least one User already found in DB with same name, surname, and age");
        }

        var roleEntity = roleRepository.getByRole(userDto.getUserRole());
        if (roleEntity == null) {
            throw new RuntimeException("Role not found! You can only specify on of the following roles: "
                    + Arrays.toString(UserRole.values()));
        }

        String hashedPsw = BCrypt.hashpw(userDto.getPassword(), BCrypt.gensalt());

        var userEntity = UserEntity.builder()
                .age(userDto.getAge())
                .surname(userDto.getSurname())
                .name(userDto.getName())
                .userRole(roleEntity)
                .password(hashedPsw)
                .mail(userDto.getMail())
                .build();

        UserDtoFull response = new UserDtoFull();
        BeanUtils.copyProperties(userRepository.save(userEntity), response);
        response.setPassword("********");
        return response;
    }

    @Override
    public UserDto removeById(Long id) {
        var found = userRepository.getReferenceById(id);

        userRepository.deleteById(id);

        UserDtoFull dto = new UserDtoFull();
        try {
            found.getId(); // Will throw the exception if there is no entity found
            BeanUtils.copyProperties(found, dto);
            dto.setUserRole(found.getUserRole().getRole());
            dto.setPassword("********");
        } catch (EntityNotFoundException e) {
            LOGGER.error("User not found by id: {}", id);
            throw new EntityNotFoundException("User not found by id: " + id);
        }

        return dto;
    }

    // TODO with pagination! Also in getUsers
    @Override
    public List<UserDto> getUsersByFilter(UserDto userFilter) {
        return null;
    }
}
