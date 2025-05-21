package MJ.animal_Hospital_Service.service.user;


import MJ.animal_Hospital_Service.config.Role;
import MJ.animal_Hospital_Service.domain.User;
import MJ.animal_Hospital_Service.dto.UserCreateRequest;
import MJ.animal_Hospital_Service.dto.UserDto;
import MJ.animal_Hospital_Service.dto.UserUpdateRequest;
import MJ.animal_Hospital_Service.repository.UserRepository;
import MJ.animal_Hospital_Service.util.LoginUtil;
import java.util.NoSuchElementException;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class UserService {

  private final UserMapper userMapper;
  private final UserRepository userRepository;
  private final BCryptPasswordEncoder encoder;

  public UserDto saveUserAuth(UserCreateRequest request){
    User user = User.builder().username(request.getUsername())
        .password(encoder.encode(request.getPassword()))
        .roles(Set.of(Role.ROLE_USER)).build();
    userRepository.save(user);

    return userMapper.toUserDto(user);
  }

  public UserDto saveHospitalAuth(UserCreateRequest request){
    User user = User.builder().username(request.getUsername())
        .password(encoder.encode(request.getPassword()))
        .roles(Set.of(Role.ROLE_HOSPITAL)).build();
    userRepository.save(user);

    return userMapper.toUserDto(user);
  }


  public UserDto update(UserUpdateRequest request){
    User user = userRepository.findByUsername(LoginUtil.getCurrentUser());
    user.setPassword(request.getNewPassword());

    return userMapper.toUserDto(user);
  }

  public void delete(String username){
    User user = userRepository.findByUsername(LoginUtil.getCurrentUser());
    if(user.getUsername().equals(username)) userRepository.delete(user);
  }

  public UserDto get(String username){
    User user = userRepository.findByUsername(LoginUtil.getCurrentUser());
    if(user.getUsername().equals(username)) return userMapper.toUserDto(user);

    throw new NoSuchElementException("해당 유저가 없습니다.");
  }

  public Long findByUserNameReturnId(String username){
    if(userRepository.findByUsername(username) == null) throw new NoSuchElementException();
    return userRepository.findByUsername(username).getId();
  }

  public void findAdmin(){
    if(userRepository.findByUsername("admin") == null){
      User user = User.builder()
          .roles(Set.of(Role.ROLE_ADMIN))
          .username("admin")
          .password("admin1234")
          .build();

      userRepository.save(user);
    }
  }
}
