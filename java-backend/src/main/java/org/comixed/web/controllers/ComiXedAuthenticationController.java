/*
 * ComiXed - A digital comic book library management application.
 * Copyright (C) 2018, The ComiXed Project
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

package org.comixed.web.controllers;

import org.comixed.library.model.ComiXedUser;
import org.comixed.repositories.ComiXedUserRepository;
import org.comixed.web.authentication.AuthToken;
import org.comixed.web.authentication.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/token")
public class ComiXedAuthenticationController
{
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private ComiXedUserRepository userRepository;

    @RequestMapping(value = "/generate-token",
                    method = RequestMethod.POST)
    public ResponseEntity<AuthToken> register(@RequestParam("email") String email,
                                              @RequestParam("password") String password) throws AuthenticationException
    {
        logger.debug("Attemping to authenticate user: {}", email);
        final Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email,
                                                                                                                         password));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        final ComiXedUser user = userRepository.findByEmail(email);
        final String token = jwtTokenUtil.generateToken(user);
        return ResponseEntity.ok(new AuthToken(token, user.getEmail()));
    }
}
