package org.karthick.dietplanner.security;

import lombok.AllArgsConstructor;
import org.karthick.dietplanner.security.filter.AuthenticationFilter;
import org.karthick.dietplanner.security.filter.ExceptionHandlerFilter;
import org.karthick.dietplanner.security.filter.JWTAuthenticationFilter;
import org.karthick.dietplanner.security.manager.CustomAuthenticationManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {
  private final CustomAuthenticationManager customAuthenticationManager;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    AuthenticationFilter authenticationFilter =
        new AuthenticationFilter(customAuthenticationManager);
    authenticationFilter.setFilterProcessesUrl("/authenticate");
    http.csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
            authorizeRequests ->
                authorizeRequests
                    .requestMatchers(HttpMethod.POST, SecurityConstants.REGISTER_PATH)
                    .permitAll())
        .authorizeHttpRequests(authorizeRequests -> authorizeRequests.anyRequest().authenticated())
        .addFilterBefore(new ExceptionHandlerFilter(), AuthenticationFilter.class)
        .addFilter(authenticationFilter)
        .addFilterAfter(new JWTAuthenticationFilter(), AuthenticationFilter.class)
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    return http.build();
  }
}
