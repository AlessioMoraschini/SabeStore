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

        for(UserEntity ent : userRepository.findAll()){
            UserDto dto = new UserDto();
            BeanUtils.copyProperties(ent, dto);
            userDtos.add(dto);
        }
        return userDtos;
    }

    @Override
    public List<UserDto> getUsersByFilter(UserDto userFilter) {
        return null;
    }
}
