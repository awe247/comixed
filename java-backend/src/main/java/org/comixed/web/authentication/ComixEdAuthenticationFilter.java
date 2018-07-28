/*
 * ComixEd - A digital comic book library management application.
 * Copyright (C) 2017, Darryl L. Pierce
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.package
 * org.comixed;
 */

package org.comixed.web.authentication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.comixed.library.model.ComixEdUser;
import org.comixed.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class ComixEdAuthenticationFilter extends UsernamePasswordAuthenticationFilter implements
                                         AuthenticationConstants
{
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    private AuthenticationManager authenticationManager;

    private Utils utils;

    public ComixEdAuthenticationFilter(AuthenticationManager authenticationManager,
                                       ApplicationContext applicationContext)
    {
        this.authenticationManager = authenticationManager;
        this.utils = (Utils )applicationContext.getBean("utils");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException
    {
        this.logger.debug("Attempting authentication");
        try
        {
            ComixEdUser user = new ObjectMapper().readValue(request.getInputStream(), ComixEdUser.class);

            return this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(),
                                                                                                   this.utils.createHash(user.getPassword()
                                                                                                                             .getBytes()),
                                                                                                   new ArrayList<>()));
        }
        catch (IOException error)
        {
            this.logger.error("failed to authenticate user", error);
            throw new ComixEdAuthenticationException("failed to authenticate user", error);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException
    {
        User user = (User )authResult.getPrincipal();
        logger.debug("Successfully authenticated: username={}", user.getUsername());
        String token = Jwts.builder().setSubject(user.getUsername())
                           .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                           .signWith(SignatureAlgorithm.HS512, SECRET.getBytes()).compact();
        logger.debug("Creating authentication header");
        response.addHeader(HEADER_STRING_AUTH_TOKEN, TOKEN_PREFIX + token);
    }
}
