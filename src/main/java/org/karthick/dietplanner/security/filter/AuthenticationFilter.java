package org.karthick.dietplanner.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.AllArgsConstructor;
import org.karthick.dietplanner.security.JWTTokenService;
import org.karthick.dietplanner.security.SecurityConstants;
import org.karthick.dietplanner.user.entity.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@AllArgsConstructor
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private AuthenticationManager authenticationManager;
  private final JWTTokenService jwtTokenService;

  @Override
  public Authentication attemptAuthentication(
    HttpServletRequest request,
    HttpServletResponse response
  ) throws AuthenticationException {
    try {
      User user = new ObjectMapper().readValue(
        request.getInputStream(),
        User.class
      );
      Authentication authentication = new UsernamePasswordAuthenticationToken(
        user.getUsername(),
        user.getPassword()
      );
      return authenticationManager.authenticate(authentication);
    } catch (IOException exception) {
      throw new AuthenticationServiceException(
        "Failed to parse authentication request body",
        exception
      );
    }
  }

  @Override
  protected void successfulAuthentication(
    HttpServletRequest request,
    HttpServletResponse response,
    FilterChain chain,
    Authentication auth
  ) {
    String accessToken = jwtTokenService.generateToken(
      auth.getName(),
      SecurityConstants.ACCESS_TOKEN_EXPIRATION
    );
    String refreshToken = jwtTokenService.generateToken(
      auth.getName(),
      SecurityConstants.REFRESH_TOKEN_EXPIRATION
    );

    response.setHeader(
      SecurityConstants.AUTHORIZATION,
      SecurityConstants.BEARER + accessToken
    );
    response.setHeader(
      SecurityConstants.REFRESH_TOKEN_HEADER,
      SecurityConstants.BEARER + refreshToken
    );
  }

  @Override
  protected void unsuccessfulAuthentication(
    HttpServletRequest request,
    HttpServletResponse response,
    AuthenticationException failed
  ) throws IOException {
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.getWriter().write(failed.getMessage());
    response.getWriter().flush();
  }
}
