package com.am.design.development.sabestore.facade;

import com.am.design.development.sabestore.dto.UserDto;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;

public interface UserFacade {

    List<UserDto>  getUsers();
    UserDto  addUser(UserDto userDto);
    UserDto  removeById(Long id) throws AccountNotFoundException;
    List<UserDto>  getUsersByFilter(UserDto userFilter);
}
