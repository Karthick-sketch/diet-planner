package org.karthick.dietplanner.user;

import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.AllArgsConstructor;
import org.karthick.dietplanner.security.JWTTokenService;
import org.karthick.dietplanner.security.SecurityConstants;
import org.karthick.dietplanner.user.dto.AccessTokenDTO;
import org.karthick.dietplanner.user.dto.UserDTO;
import org.karthick.dietplanner.user.entity.User;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
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
  public ResponseEntity<?> requestAccessToken(
    @CookieValue(
      name = SecurityConstants.REFRESH_TOKEN_HEADER,
      required = false
    ) String refreshToken
  ) {
    if (refreshToken == null) {
      return ResponseEntity.badRequest().body("Refresh token missing");
    }
    try {
      String username = jwtTokenService.validateToken(refreshToken);
      String newAccessToken = jwtTokenService.generateToken(
        username,
        SecurityConstants.ACCESS_TOKEN_EXPIRATION
      );
      return ResponseEntity.ok().body(new AccessTokenDTO(newAccessToken));
    } catch (JWTVerificationException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
        "Invalid or expired refresh token"
      );
    }
  }

  @PostMapping("/logout")
  public ResponseEntity<?> logout() {
    ResponseCookie cookie = ResponseCookie.from(
      SecurityConstants.REFRESH_TOKEN_HEADER,
      ""
    )
      .httpOnly(true)
      .sameSite("Lax")
      .secure(false)
      .path("/")
      .maxAge(0)
      .build();
    return ResponseEntity.ok()
      .header(HttpHeaders.SET_COOKIE, cookie.toString())
      .build();
  }
}
