package com.pitang.desafio.tcepe.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pitang.desafio.tcepe.exception.expections.ErrorMessage;
import com.pitang.desafio.tcepe.model.User;
import com.pitang.desafio.tcepe.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    private final ITokenService ITokenService;
    private final IUserRepository userRepository;

    @Autowired
    public SecurityFilter(final ITokenService ITokenService, final IUserRepository userRepository) {
        this.ITokenService = ITokenService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {

        final String token = this.recoverToken(request);

        if (Objects.nonNull(token)) {
            try {
                final String login = ITokenService.validateToken(token);
                final Optional<User> user = userRepository.findByLogin(login);

                user.ifPresent(userFound -> SecurityContextHolder
                        .getContext()
                        .setAuthentication(new UsernamePasswordAuthenticationToken(
                                userFound, null, userFound.getAuthorities())));

            } catch (JWTVerificationException | IllegalArgumentException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write(new ObjectMapper().writeValueAsString(getError()));
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private String recoverToken(final HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");

        return Objects
                .isNull(authHeader)
                ? null :
                authHeader
                        .replace("Bearer ", "");
    }

    private ErrorMessage getError() {
        return new ErrorMessage("Unauthorized - Invalid session", 1778);
    }
}
