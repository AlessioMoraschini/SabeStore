package com.am.design.development.sabestore.impl;

import com.am.design.development.sabestore.data.entity.UserEntity;
import com.am.design.development.sabestore.data.repository.UserRepository;
import com.am.design.development.sabestore.dto.UserDto;
import com.am.design.development.sabestore.facade.UserFacade;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserFacadeImpl implements UserFacade {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<UserDto> getUsers() {
        List<UserDto> userDtos = new ArrayList<>();

        for (UserEntity ent : userRepository.findAll()) {
            UserDto dto = new UserDto();
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

        BeanUtils.copyProperties(userRepository.save(userEntity), userDto);
        return userDto;
    }

    @Override
    public UserDto removeById(Long id) {
        var found = userRepository.getReferenceById(id);

        userRepository.deleteById(id);

        UserDto dto = new UserDto();
        BeanUtils.copyProperties(found, dto);
        return dto;
    }

    @Override
    public List<UserDto> getUsersByFilter(UserDto userFilter) {
        return null;
    }
}
