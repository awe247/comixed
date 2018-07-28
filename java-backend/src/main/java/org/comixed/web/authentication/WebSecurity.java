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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
public class ComixEdWebSecurity extends WebSecurityConfigurerAdapter
{
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ComixEdUserDetailsService userDetailsService;

    @Autowired
    private ComixEdAuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
	private ComixEdAuthenticationFailureHandler authenticationFailureHandler;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception
    {
        auth.userDetailsService(this.userDetailsService); // .passwordEncoder(this.bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        // @formatter:off
        http
            .authorizeRequests()
                .antMatchers("/", "/comics/count", "/login").permitAll()
                .antMatchers("/comics/**", "/pages/**").hasRole("READER")
                .antMatchers("/files/**").hasRole("ADMIN")
            .and()
            .formLogin()
                .successHandler(authenticationSuccessHandler)
				.failureHandler(authenticationFailureHandler)
                .loginPage("/login")
                .permitAll()
            .and()
            .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/")
                .permitAll()
            .and()
            .csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            .and()
            .addFilter(new ComixEdAuthenticationFilter(this.authenticationManager(), getApplicationContext()))
            .addFilter(new ComixEdAuthorizationFilter(this.authenticationManager())).sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // @formatter:on
        http.exceptionHandling().accessDeniedPage("/403");
    }
}
