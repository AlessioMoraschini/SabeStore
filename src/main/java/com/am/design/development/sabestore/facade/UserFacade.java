package com.am.design.development.sabestore.facade;

import com.am.design.development.sabestore.dto.UserDto;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;

public interface UserFacade {

    List<UserDto>  getUsers();
    UserDto  getCurrentUserDetails(String mail);
    UserDto  addUser(UserDto userDto);
    UserDto  updateUser(UserDto userDto, boolean isSuperUser);
    UserDto  removeById(Long id) throws AccountNotFoundException;
    List<UserDto>  getUsersByFilter(UserDto userFilter);
}
