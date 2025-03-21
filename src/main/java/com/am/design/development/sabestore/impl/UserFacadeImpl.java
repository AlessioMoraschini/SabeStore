package com.am.design.development.sabestore.impl;

import com.am.design.development.data.userdb.entity.RoleEntity;
import com.am.design.development.data.userdb.entity.UserEntity;
import com.am.design.development.data.userdb.repository.RoleRepository;
import com.am.design.development.data.userdb.repository.UserRepository;
import com.am.design.development.dto.UserRole;
import com.am.design.development.dto.UserVerificationStatus;
import com.am.design.development.sabestore.dto.UserDto;
import com.am.design.development.sabestore.dto.UserDtoFull;
import com.am.design.development.sabestore.facade.UserFacade;
import com.am.design.development.utilities.impl.EmailService;
import com.am.design.development.utilities.utils.WebAppUtils;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Autowired
    private EmailService emailService;
    @Autowired
    private WebAppUtils webAppUtils;

    @Override
    @Transactional(transactionManager = "userDbTransactionManager", readOnly = true)
    public Page<UserDto> getUsers(Pageable pageable) {
        List<UserDto> userDtos = new ArrayList<>();

        var dbResult = userRepository.findAll(pageable);
        return dbResult.map(user -> {
            UserDtoFull dto = new UserDtoFull();
            copyUserFromEntityMaskingPsw(user, dto);
            return dto;
        });
    }

    @Override
    @Transactional(transactionManager = "userDbTransactionManager", readOnly = true)
    public UserDto getCurrentUserDetails(String mail) {
        UserEntity ent = userRepository.findByMail(mail);
        UserDtoFull dto = new UserDtoFull();
        copyUserFromEntityMaskingPsw(ent, dto);
        return dto;
    }

    @Override
    @Transactional(transactionManager = "userDbTransactionManager")
    public UserDto addUser(UserDto userDto, boolean isSuperUser) throws MessagingException {
        var userFromDb = userRepository.findByMail(userDto.getMail());
        if (userFromDb != null) {
            throw new RuntimeException("At least one User already found in DB with same mail");
        }

        var roleEntities = userDto.getRoles().stream()
                .filter(role -> isSuperUser || role.equals(UserRole.USER))
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
                .verificationStatus(isSuperUser?
                        UserVerificationStatus.VERIFIED : UserVerificationStatus.PENDING)
                .randomIdentifier(RandomStringUtils.secureStrong().randomAlphabetic(128))
                .build();

        UserDtoFull response = new UserDtoFull();
        copyUserFromEntityMaskingPsw(userRepository.save(userEntity), response);

        if(!isSuperUser) {
            sendVerificationEmail(response);
        }

        return response;
    }

    @Override
    @Transactional(transactionManager = "userDbTransactionManager")
    public UserDto resendVerificationMail(String mail) throws MessagingException {
        var userFromDb = userRepository.findByMail(mail);
        if (userFromDb == null) {
            throw new RuntimeException("User not found in DB");
        }

        var userDto = new UserDtoFull();
        copyUserFromEntityMaskingPsw(userFromDb, userDto);
        sendVerificationEmail(userDto);

        return userDto;
    }

    private void sendVerificationEmail(UserDtoFull user) throws MessagingException {
        String subject = "Verifica il tuo account";
        String verificationUrl = webAppUtils.getRootUrl() + "/user/verifyUser?userId=" + user.getId()
                + "&userRandomIdentifier=" + user.getRandomIdentifier();
        String htmlText = "<p>Click on the link to verify your account:</p>" +
                "<a href=\"" + verificationUrl + "\">" + verificationUrl + "</a>";
        emailService.sendEmail(user.getMail(), subject, htmlText, true);
    }

    @Override
    public void verifyUser(Long userId, String userRandomIdentifier) {
        var userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Invalid User: not found in our database."));

        userEntity.setVerificationStatus(UserVerificationStatus.VERIFIED);
        userRepository.save(userEntity);
    }

    @Override
    @Transactional(transactionManager = "userDbTransactionManager")
    public UserDto updateUser(UserDto userDto, boolean isSuperUser) {
        var userFromDb = userRepository.findByMail(userDto.getMail());
        if (userFromDb == null) {
            throw new RuntimeException("User not found in DB");
        }

        // Reset roles before to update, can do this only if superuser to avoid one escalating it's grants!
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
        copyUserFromEntityMaskingPsw(userRepository.save(userFromDb), response);
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
            copyUserFromEntityMaskingPsw(found, dto);
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

    private void copyUserFromEntityMaskingPsw(UserEntity userEntity, UserDto userDto){
        BeanUtils.copyProperties(userRepository.save(userEntity), userDto);
        userDto.setRoles(userEntity.getRoles().stream()
                .map(RoleEntity::getRole)
                .collect(Collectors.toSet()));
        userDto.setPassword("********");
    }
}
