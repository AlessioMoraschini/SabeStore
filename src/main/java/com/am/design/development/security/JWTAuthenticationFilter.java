package com.am.design.development.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static long EXPIRATION_TIME = 600L;

    private static final String SECRET = System.getenv("JWT_SECRET");

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        // in case of OPTIONS request it is a preflight request done by the browser to check CORS. This should not be analyzed by AuthFilter
        if ("OPTIONS".equalsIgnoreCase(((HttpServletRequest)request).getMethod())) {
            ((HttpServletResponse)response).setStatus(HttpServletResponse.SC_OK);
            return;
        }

        // Il resto della logica di autenticazione
        super.doFilter(request, response, chain);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException {
        try {
            AuthRequest creds = new ObjectMapper()
                    .readValue(req.getInputStream(), AuthRequest.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getUsername(),
                            creds.getPassword()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication auth) throws IOException, ServletException {
        String username = ((User) auth.getPrincipal()).getUsername();
        Collection<? extends GrantedAuthority> roles = auth.getAuthorities();

        String token = Jwts.builder()
                .setSubject(username)
                .claim("roles", roles.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .setExpiration(Date.from(Instant.now().plus(EXPIRATION_TIME, ChronoUnit.SECONDS)))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
        res.addHeader("Authorization", "Bearer " + token);
        res.addHeader("Roles", roles.stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(",")));
    }
}
