package com.am.design.development.sabestore.impl;

import com.am.design.development.data.userdb.entity.UserEntity;
import com.am.design.development.data.userdb.repository.UserRepository;
import com.am.design.development.sabestore.dto.UserDto;
import com.am.design.development.sabestore.dto.UserDtoFull;
import com.am.design.development.sabestore.facade.UserFacade;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserFacadeImpl implements UserFacade {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserFacadeImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<UserDto> getUsers() {
        List<UserDto> userDtos = new ArrayList<>();

        for (UserEntity ent : userRepository.findAll()) {
            UserDtoFull dto = new UserDtoFull();
            BeanUtils.copyProperties(ent, dto);
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

        var userEntity = UserEntity.builder()
                .age(userDto.getAge())
                .surname(userDto.getSurname())
                .name(userDto.getName())
                .build();

        UserDtoFull response = new UserDtoFull();
        BeanUtils.copyProperties(userRepository.save(userEntity), response);
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
        } catch (EntityNotFoundException e) {
            LOGGER.error("User not found by id: {}", id);
            throw new EntityNotFoundException("User not found by id: " + id);
        }

        return dto;
    }

    @Override
    public List<UserDto> getUsersByFilter(UserDto userFilter) {
        return null;
    }
}
