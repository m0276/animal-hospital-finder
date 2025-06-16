package MJ.animal_Hospital_Service.service.user;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import MJ.animal_Hospital_Service.config.Role;
import MJ.animal_Hospital_Service.config.SecurityConfig;
import MJ.animal_Hospital_Service.domain.User;
import MJ.animal_Hospital_Service.dto.UserCreateRequest;
import MJ.animal_Hospital_Service.dto.UserDto;
import MJ.animal_Hospital_Service.dto.UserUpdateRequest;
import MJ.animal_Hospital_Service.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith( MockitoExtension.class)
class UserServiceTest {

  @InjectMocks
  UserService userService;

  @Mock
  UserRepository userRepository;

  @Spy
  private BCryptPasswordEncoder encoder;

  @Spy
  UserMapper userMapper = Mappers.getMapper(UserMapper.class);

  Authentication authentication = mock(Authentication.class);
  SecurityContext securityContext = mock(SecurityContext.class);
  org.springframework.security.core.userdetails.User userDetails;

  @BeforeEach
  void setAuth() {
    userDetails =
        new org.springframework.security.core.userdetails.User("testUser",
            "password", Set.of());
    SecurityContextHolder.setContext(securityContext);
  }


  @Test
  @DisplayName("개인 유저 저장")
  void saveUserAuth() {
    UserCreateRequest request = new UserCreateRequest();
    request.setUsername("test User");
    request.setPassword("12345");

    UserDto user = userService.saveUserAuth(request);

    assertEquals(user.username(), request.getUsername());
    assertEquals(user.roles(), Set.of(Role.ROLE_USER));
  }

  @Test
  @DisplayName("병원 유저 저장")
  void saveHospitalAuth() {
    UserCreateRequest request = new UserCreateRequest();
    request.setUsername("test User");
    request.setPassword("12345");

    UserDto user = userService.saveHospitalAuth(request);

    assertEquals(user.username(), request.getUsername());
    assertEquals(user.roles(), Set.of(Role.ROLE_HOSPITAL));
  }

  @Test
  @DisplayName("유저 비밀번호 변경")
  void update() {
    User resultUser = User.builder()
        .username("testUser")
        .password(encoder.encode("password"))
        .roles(Set.of(Role.ROLE_USER))
        .build();

    UserUpdateRequest request = new UserUpdateRequest();

    request.setUsername("testUser");
    request.setNewPassword("newPassword");
    given(authentication.getPrincipal()).willReturn(userDetails);
    given(securityContext.getAuthentication()).willReturn(authentication);

    given(userRepository.findByUsername("testUser")).willReturn(Optional.of(resultUser));

    userService.update(request);

    assertTrue(encoder.matches("newPassword", resultUser.getPassword()));
  }

  @Test
  @DisplayName("유저 삭제")
  void delete() {
    doNothing().when(userRepository).delete(any(User.class));
    given(userRepository.findByUsername(any(String.class))).willReturn(
        Optional.of(User.builder().username("testUser").build()));

    given(authentication.getPrincipal()).willReturn(userDetails);
    given(securityContext.getAuthentication()).willReturn(authentication);

    userService.delete("testUser");

    verify(userRepository, times(1)).delete(any(User.class));
  }

  @Test
  void get() {
    User user = User.builder()
        .id(1L)
        .username("testUser")
        .build();

    given(authentication.getPrincipal()).willReturn(userDetails);
    given(securityContext.getAuthentication()).willReturn(authentication);

    given(userRepository.findByUsername("testUser")).willReturn(Optional.of(user));

    UserDto userDto = userService.get("testUser");

    assertEquals(userDto.id(), user.getId());
  }

  @Test
  void findByUserNameReturnId() {
    User user = User.builder()
        .id(1L)
        .username("testUser")
        .build();

    given(authentication.getPrincipal()).willReturn(userDetails);
    given(securityContext.getAuthentication()).willReturn(authentication);

    given(userRepository.findByUsername("testUser")).willReturn(Optional.of(user));

    Long result = userService.findByUserNameReturnId("testUser");

    assertEquals(user.getId(), result);
  }
}