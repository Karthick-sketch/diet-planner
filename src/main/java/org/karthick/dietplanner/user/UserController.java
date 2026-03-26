package org.karthick.dietplanner.user;

import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.AllArgsConstructor;
import org.karthick.dietplanner.security.JWTTokenService;
import org.karthick.dietplanner.security.SecurityConstants;
import org.karthick.dietplanner.user.dto.UserDTO;
import org.karthick.dietplanner.user.entity.User;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

  private final UserService userService;
  private final JWTTokenService jwtTokenService;

  @GetMapping("/{username}")
  public UserDTO getUser(@PathVariable String username) {
    return userService.findUserDTOByUsername(username);
  }

  @PostMapping("/register")
  public UserDTO register(@RequestBody User user) {
    return userService.registerUser(user);
  }

  @PostMapping("/refresh")
  public void accessToken(
    HttpServletRequest request,
    HttpServletResponse response
  ) throws IOException {
    String cookieHeader = request.getHeader("Cookie");
    String refreshToken = cookieHeader.replace(
      SecurityConstants.REFRESH_TOKEN_HEADER + "=",
      ""
    );
    if (refreshToken != null) {
      try {
        String username = jwtTokenService.validateToken(refreshToken);
        String newAccessToken = jwtTokenService.generateToken(
          username,
          SecurityConstants.ACCESS_TOKEN_EXPIRATION
        );
        response.setHeader(
          SecurityConstants.AUTHORIZATION,
          SecurityConstants.BEARER + newAccessToken
        );
      } catch (JWTVerificationException e) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("Invalid or expired refresh token");
        response.getWriter().flush();
      }
    } else {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      response.getWriter().write("Refresh token missing");
      response.getWriter().flush();
    }
  }

  @PostMapping("/logout")
  public void logout(HttpServletResponse response) {
    Cookie cookie = new Cookie(SecurityConstants.REFRESH_TOKEN_HEADER, "");
    cookie.setPath("/");
    cookie.setMaxAge(0);
    response.addCookie(cookie);
  }
}
