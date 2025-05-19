package org.karthick.dietplanner.user;

import lombok.AllArgsConstructor;
import org.karthick.dietplanner.config.ModelMapperConfig;
import org.karthick.dietplanner.user.dto.UserDTO;
import org.karthick.dietplanner.user.entity.User;
import org.karthick.dietplanner.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class UserService {
  private UserRepository userRepository;
  private ModelMapperConfig mapper;

  public User findUserByUsername(String username) {
    Optional<User> user = userRepository.findByUsername(username);
    if (user.isEmpty()) {
      throw new UsernameNotFoundException("Username not found");
    }
    return user.get();
  }

  public UserDTO createUser(User user) {
    return mapper.convertToDto(userRepository.save(user), UserDTO.class);
  }
}
