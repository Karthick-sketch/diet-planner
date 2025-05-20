package org.karthick.dietplanner.security;

import lombok.AllArgsConstructor;
import org.karthick.dietplanner.user.UserService;
import org.karthick.dietplanner.user.dto.UserDTO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserSession {
  private final UserService userService;

  public String getAuthenticatedUserId() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth.isAuthenticated()) {
      UserDTO userDTO = userService.findUserDTOByUsername(auth.getName());
      return userDTO.getId();
    }
    return null;
  }
}
