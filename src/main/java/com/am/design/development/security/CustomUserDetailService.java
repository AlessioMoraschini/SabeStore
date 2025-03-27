package com.am.design.development.security;

import com.am.design.development.data.userdb.entity.UserEntity;
import com.am.design.development.data.userdb.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
        UserEntity sabeUser = userRepository.findByMail(mail);
        if (sabeUser == null) {
            throw new UsernameNotFoundException("User not found");
        }

        UserBuilder builder = User.withUsername(sabeUser.getMail());
        builder.password(sabeUser.getPassword());
        builder.roles(sabeUser.getRoles().stream()
                .map(role -> role.getRole().name())
                .toArray(String[]::new));

        return builder.build();
    }
}

