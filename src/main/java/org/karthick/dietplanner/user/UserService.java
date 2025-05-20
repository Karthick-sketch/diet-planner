package org.karthick.dietplanner.user;

import lombok.AllArgsConstructor;
import org.karthick.dietplanner.exception.EntityNotFoundException;
import org.karthick.dietplanner.shared.Mapper;
import org.karthick.dietplanner.user.dto.UserDTO;
import org.karthick.dietplanner.user.entity.User;
import org.karthick.dietplanner.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final Mapper mapper;

  public User findUserByUsername(String username) {
    Optional<User> user = userRepository.findByUsername(username);
    if (user.isEmpty()) {
      throw new EntityNotFoundException("Username not found");
    }
    return user.get();
  }

  public UserDTO findUserDTOByUsername(String username) {
    return toUserDTO(findUserByUsername(username));
  }

  public UserDTO createUser(User user) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    return toUserDTO(userRepository.save(user));
  }

  private UserDTO toUserDTO(User user) {
    return mapper.convertToDto(user, UserDTO.class);
  }
}
