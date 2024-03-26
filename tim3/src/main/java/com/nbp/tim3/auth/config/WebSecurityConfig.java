package com.nbp.tim3.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig{

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authProvider;
    private final LogoutHandler logoutHandler;

    public WebSecurityConfig(JwtAuthenticationFilter jwtAuthFilter, AuthenticationProvider authProvider, LogoutHandler logoutHandler) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.authProvider = authProvider;
        this.logoutHandler = logoutHandler;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers("/**","/auth/login","/auth/register","/auth/refresh-token","/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and().exceptionHandling()
                // setting custom access denied handler for not authorized request
                .accessDeniedHandler(new CustomAccessDeniedHandler())
                // setting custom entry point for unauthenticated request
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout()
                .logoutUrl("/auth/logout")
                .addLogoutHandler(logoutHandler)
                .logoutSuccessHandler(((request, response, authentication) -> {
                    SecurityContextHolder.clearContext();
                }));


        return http.build();
    }
}
