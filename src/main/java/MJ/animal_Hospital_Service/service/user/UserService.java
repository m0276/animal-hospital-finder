package MJ.animal_Hospital_Service.service.user;


import MJ.animal_Hospital_Service.config.Role;
import MJ.animal_Hospital_Service.domain.User;
import MJ.animal_Hospital_Service.dto.NaverUserInfo;
import MJ.animal_Hospital_Service.dto.UserCreateRequest;
import MJ.animal_Hospital_Service.dto.UserDto;
import MJ.animal_Hospital_Service.dto.UserUpdateRequest;
import MJ.animal_Hospital_Service.repository.UserRepository;
import MJ.animal_Hospital_Service.util.LoginUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

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
    User user = userRepository.findByUsername(LoginUtil.getCurrentUser())
        .orElseThrow(() -> new NoSuchElementException("해당 유저가 없습니다."));
    user.setPassword(request.getNewPassword());

    return userMapper.toUserDto(user);
  }

  public void delete(String username){
    User user = userRepository.findByUsername(LoginUtil.getCurrentUser())
        .orElseThrow(() -> new NoSuchElementException("해당 유저가 없습니다."));
    if(user.getUsername().equals(username)) userRepository.delete(user);
    else throw new AuthorizationDeniedException("권한이 없습니다");
  }

  public UserDto get(String username){
    User user = userRepository.findByUsername(LoginUtil.getCurrentUser())
        .orElseThrow(() -> new NoSuchElementException("해당 유저가 없습니다."));

    if(user.getUsername().equals(username)) return userMapper.toUserDto(user);
    else throw new AuthorizationDeniedException("권한이 없습니다");
  }

  public Long findByUserNameReturnId(String username){
     return userRepository.findByUsername(LoginUtil.getCurrentUser())
        .orElseThrow(() -> new NoSuchElementException("해당 유저가 없습니다.")).getId();
  }

  public void findAdmin(){
    if(userRepository.findByUsername("admin").isEmpty()){
      User user = User.builder()
          .roles(Set.of(Role.ROLE_ADMIN))
          .username("admin")
          .password("admin1234")
          .build();

      userRepository.save(user);
    }
  }

  private final RestTemplate restTemplate = new RestTemplate();
  private final ObjectMapper objectMapper = new ObjectMapper();

  public NaverUserInfo getUserInfo(String accessToken) {
    String url = "https://openapi.naver.com/v1/nid/me";

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(accessToken);
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    HttpEntity<String> entity = new HttpEntity<>("", headers);

    ResponseEntity<String> response = restTemplate.exchange(
        url,
        HttpMethod.GET,
        entity,
        String.class
    );

    try {
      JsonNode jsonNode = objectMapper.readTree(response.getBody());
      JsonNode responseNode = jsonNode.get("response");

      return NaverUserInfo.builder()
          .id(responseNode.get("id").asText())
          .name(responseNode.get("name").asText(null))
          .build();

    } catch (Exception e) {
      throw new NoSuchElementException("Failed to parse user info from Naver", e);
    }
  }

  public User saveOrUpdateNaverUser(NaverUserInfo userInfo) {
    Optional<User> existing = userRepository.findByTypeAndTypeId("naver",userInfo.getId());

    return existing.orElseGet(() -> existing
        .orElseGet(() -> {
          User newUser = User.builder()
              .username(userInfo.getName())
              .password(encoder.encode("naverUser" + UUID.randomUUID()))
              .type("naver")
              .typeId(userInfo.getId())
              .build();
          return userRepository.save(newUser);
        }));


  }
}
