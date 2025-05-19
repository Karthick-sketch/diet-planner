package org.karthick.dietplanner.security.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.karthick.dietplanner.security.SecurityConstants;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

public class JWTAuthenticationFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {
    String header = request.getHeader(SecurityConstants.AUTHORIZATION);
    if (header != null && header.startsWith(SecurityConstants.BEARER)) {
      String token = header.replace(SecurityConstants.BEARER, "").trim();
      String username =
          JWT.require(Algorithm.HMAC512(SecurityConstants.SECRET_KEY))
              .build()
              .verify(token)
              .getSubject();
      Authentication authentication =
          new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    filterChain.doFilter(request, response);
  }
}
