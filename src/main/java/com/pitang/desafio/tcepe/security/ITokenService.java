package com.pitang.desafio.tcepe.security;

import com.pitang.desafio.tcepe.model.User;

public interface ITokenService {

    String generateToken(final User user);

    String validateToken(final String token);
}
