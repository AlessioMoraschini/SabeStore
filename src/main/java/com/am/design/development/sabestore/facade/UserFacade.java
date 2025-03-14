package com.am.design.development.sabestore.facade;

import com.am.design.development.sabestore.dto.UserDto;
import jakarta.mail.MessagingException;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;

public interface UserFacade {

    List<UserDto>  getUsers();
    UserDto  getCurrentUserDetails(String mail);
    UserDto  addUser(UserDto userDto, boolean isSuperUser) throws MessagingException;

    @Transactional(transactionManager = "userDbTransactionManager")
    UserDto resendVerificationMail(String mail) throws MessagingException;

    void  verifyUser(Long userId, String userRandomIdentifier);
    UserDto  updateUser(UserDto userDto, boolean isSuperUser);
    UserDto  removeById(Long id) throws AccountNotFoundException;
    List<UserDto>  getUsersByFilter(UserDto userFilter);
}
