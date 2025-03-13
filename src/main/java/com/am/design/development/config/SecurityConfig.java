package com.am.design.development.config;

import com.am.design.development.data.userdb.repository.UserRepository;
import com.am.design.development.security.CustomUserDetailService;
import com.am.design.development.security.JWTAuthenticationFilter;
import com.am.design.development.security.JWTAuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@ConditionalOnProperty(prefix = "security", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableMethodSecurity // needed to setup authorization in controllers
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private SecurityProperties securityProperties;

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JWTAuthenticationFilter jwtAuthenticationFilter,
            JWTAuthorizationFilter jwtAuthorizationFilter) throws Exception
    {

        http.csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> authorizationManagerRequestMatcherRegistry
                    .requestMatchers(securityProperties.getUnauthenticatedEndpoints()).permitAll()
                    .anyRequest().authenticated())
                .headers(headersConfigurer -> headersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilter(jwtAuthenticationFilter);
        http.addFilter(jwtAuthorizationFilter);
        return http.build();
    }

    @Bean
    public CustomUserDetailService customUserDetailService(UserRepository userRepository) {
        return new CustomUserDetailService();
    }

    @Bean
    @Primary
    public UserDetailsService userDetailsService(CustomUserDetailService customUserDetailService) {
        return customUserDetailService;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(authenticationProvider);
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JWTAuthenticationFilter jwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        JWTAuthenticationFilter filter = new JWTAuthenticationFilter(authenticationManager);
        filter.setFilterProcessesUrl("/login");
        return filter;
    }

    @Bean
    public JWTAuthorizationFilter jwtAuthorizationFilter(AuthenticationManager authenticationManager) {
        return new JWTAuthorizationFilter(authenticationManager);
    }

}

