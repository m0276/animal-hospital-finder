package MJ.animal_Hospital_Service.service.login;


import MJ.animal_Hospital_Service.dto.SocialLoginUserInfo;
import MJ.animal_Hospital_Service.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommonLoginService {

  public void login(HttpSession session, MJ.animal_Hospital_Service.domain.User user){

    session.setAttribute("LOGIN_USER", user);

    UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
        .username(user.getUsername())
        .password(user.getPassword())
        .authorities("ROLE_USER")
        .build();

    Authentication authentication = new UsernamePasswordAuthenticationToken(
        userDetails,
        null,
        userDetails.getAuthorities()
    );

    SecurityContextHolder.getContext().setAuthentication(authentication);

    session.setAttribute(
        HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
        SecurityContextHolder.getContext()
    );
  }

}
