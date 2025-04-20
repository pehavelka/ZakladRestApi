package cz.zakladresapi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import cz.zakladresapi.authapi.user.UserController;
import cz.zakladresapi.authapi.user.UserService;
import cz.zakladresapi.authapi.user.domain.UserDto;

@ExtendWith(MockitoExtension.class)
class UserTest {

  @Mock
  private UserService userService;
  
  
  @InjectMocks
  private UserController userController;

  @Test
  void seznam() {
    
    UserDto user1 = UserDto.builder()
      .id(1)
      .email("user1@test.cz")
      .fullName("User 1")
      .platnost(true)
      .zmenaCas(LocalDateTime.now())
      .zmenaUzivatel("postgres")
      .build();
    
    UserDto user2 = UserDto.builder()
    .id(1)
    .email("user1@test.cz")
    .fullName("User 1")
    .platnost(true)
    .zmenaCas(LocalDateTime.now())
    .zmenaUzivatel("postgres")
    .build();
    
    when(userService.seznam(null)).thenReturn(Arrays.asList(user1, user2));
    
    List<UserDto> result = userController.getSeznam(null).getBody();
    assertThat(result).hasSize(2);
  }
  
  @Test
  void detail() {
    UserDto user1 = UserDto.builder()
      .id(1)
      .email("user1@test.cz")
      .fullName("User 1")
      .platnost(true)
      .zmenaCas(LocalDateTime.now())
      .zmenaUzivatel("postgres")
      .build();
    
    UserDto user2 = UserDto.builder()
    .id(1)
    .email("user1@test.cz")
    .fullName("User 2")
    .platnost(true)
    .zmenaCas(LocalDateTime.now())
    .zmenaUzivatel("postgres")
    .build();
    
    when(userService.detail(1)).thenReturn(Optional.of(user1));
    when(userService.detail(2)).thenReturn(Optional.of(user2));
    
    Optional<UserDto> u1 = (Optional<UserDto>) userController.detail(1).getBody();
    Optional<UserDto> u2 = (Optional<UserDto>) userController.detail(2).getBody();
    
    assertThat(u1.map(UserDto::getFullName)).hasValue("User 1");
    assertThat(u2.map(UserDto::getFullName)).hasValue("User 2");
    
    
  }

}
