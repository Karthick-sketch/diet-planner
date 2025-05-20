package org.karthick.dietplanner.security.manager;

import lombok.AllArgsConstructor;
import org.karthick.dietplanner.user.UserService;
import org.karthick.dietplanner.user.entity.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CustomAuthenticationManager implements AuthenticationManager {
  private final UserService userService;
  private final PasswordEncoder passwordEncoder;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    User user = userService.findUserByUsername(authentication.getName());
    if (!passwordEncoder.matches(authentication.getCredentials().toString(), user.getPassword())) {
      throw new BadCredentialsException("Bad credentials");
    }
    return new UsernamePasswordAuthenticationToken(
        authentication.getName(), authentication.getCredentials());
  }
}
