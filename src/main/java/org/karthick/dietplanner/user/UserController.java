package org.karthick.dietplanner.user;

import lombok.AllArgsConstructor;
import org.karthick.dietplanner.user.dto.UserDTO;
import org.karthick.dietplanner.user.entity.User;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {
  private final UserService userService;

  @GetMapping("{username}")
  public UserDTO getUser(@PathVariable String username) {
    return userService.findUserDTOByUsername(username);
  }

  @PostMapping("/register")
  public UserDTO register(@RequestBody User user) {
    return userService.createUser(user);
  }
}
