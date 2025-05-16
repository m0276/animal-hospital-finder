package MJ.animal_Hospital_Service.service.login;

import MJ.animal_Hospital_Service.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SocialLoginService {
  private final UserService userService;

}
