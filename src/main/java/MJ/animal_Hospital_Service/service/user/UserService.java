package MJ.animal_Hospital_Service.service.user;


import MJ.animal_Hospital_Service.domain.User;
import MJ.animal_Hospital_Service.dto.UserCreateRequest;
import MJ.animal_Hospital_Service.dto.UserDto;
import MJ.animal_Hospital_Service.dto.UserUpdateRequest;
import MJ.animal_Hospital_Service.repository.UserRepository;
import MJ.animal_Hospital_Service.util.LoginUtil;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.NotAcceptableStatusException;

@RequiredArgsConstructor
@Service
@Transactional
public class UserService {

  private UserMapper userMapper;
  private UserRepository userRepository;

  public UserDto save(UserCreateRequest request){
    User user = User.builder().username(request.getUsername())
        .password(request.getPassword()).build();
    userRepository.save(user);

    return userMapper.toUserDto(user);
  }


  public UserDto update(UserUpdateRequest request){
    if(!loginCheck()) throw new NotAcceptableStatusException("로그인을 진행해주세요");

    User user = userRepository.findByUsername(LoginUtil.getCurrentUser());
    user.setPassword(request.getNewPassword());

    return userMapper.toUserDto(user);
  }

  public void delete(String username){
    if(!loginCheck()) throw new NotAcceptableStatusException("로그인을 진행해주세요");
    User user = userRepository.findByUsername(LoginUtil.getCurrentUser());
    if(user.getUsername().equals(username)) userRepository.delete(user);
  }

  public UserDto get(String username){
    if(!loginCheck()) throw new NotAcceptableStatusException("로그인을 진행해주세요");
    User user = userRepository.findByUsername(LoginUtil.getCurrentUser());
    if(user.getUsername().equals(username)) return userMapper.toUserDto(user);

    throw new NoSuchElementException("해당 유저가 없습니다.");
  }

  private boolean loginCheck(){
    return LoginUtil.isLogin();
  }
}
