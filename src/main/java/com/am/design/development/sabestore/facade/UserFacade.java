package com.am.design.development.sabestore.facade;

import com.am.design.development.sabestore.dto.UserDto;

import java.util.List;

public interface UserFacade {

    List<UserDto>  getUsers();
    List<UserDto>  getUsersByFilter(UserDto userFilter);
}
