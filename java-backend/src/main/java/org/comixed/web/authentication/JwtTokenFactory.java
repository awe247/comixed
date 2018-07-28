
package org.comixed.web.authentication;

import java.util.Date;
import java.util.stream.Collectors;

import org.comixed.library.model.ComixEdUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenFactory implements
                             AuthenticationConstants
{
    private final JwtSettings settings;

    @Autowired
    public JwtTokenFactory(JwtSettings settings)
    {
        this.settings = settings;
    }

    /**
     * Factory method for issuing new JWT Tokens.
     * 
     * @param username
     * @param roles
     * @return
     */
    public AccessJwtToken createAccessJwtToken(ComixEdUser comixEdUser)
    {
        // if (StringUtils.isBlank(comixEdUser.getEmail())) throw new
        // IllegalArgumentException("Cannot create JWT Token without username");

        if (comixEdUser.getRoles() == null
            || comixEdUser.getRoles().isEmpty()) throw new IllegalArgumentException("User doesn't have any privileges");

        Claims claims = Jwts.claims().setSubject(comixEdUser.getEmail());
        claims.put("scopes", comixEdUser.getRoles().stream().map(s -> s.toString()).collect(Collectors.toList()));

        String token = Jwts.builder().setClaims(claims).setIssuer(settings.getTokenIssuer())
                           .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                           .signWith(SignatureAlgorithm.HS512, settings.getTokenSigningKey()).compact();

        return new AccessJwtToken(token, claims);
    }

    public JwtToken createRefreshToken(ComixEdUser comixEdUser)
    {
        // if (StringUtils.isBlank(comixEdUser.getEmail())) { throw new
        // IllegalArgumentException("Cannot create JWT Token without username");
        // }

        Claims claims = Jwts.claims().setSubject(comixEdUser.getEmail());
        claims.put("scopes", comixEdUser.getRoles());

        String token = Jwts.builder().setClaims(claims).setIssuer(settings.getTokenIssuer())
                           .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                           .signWith(SignatureAlgorithm.HS512, settings.getTokenSigningKey()).compact();

        return new AccessJwtToken(token, claims);
    }
}