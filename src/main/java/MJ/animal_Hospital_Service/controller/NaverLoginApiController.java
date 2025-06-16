package MJ.animal_Hospital_Service.controller;

import MJ.animal_Hospital_Service.dto.SocialLoginUserInfo;
import MJ.animal_Hospital_Service.service.login.NaverLoginService;
import MJ.animal_Hospital_Service.service.user.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import java.net.URI;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth/naver")
public class NaverLoginApiController {
  private final NaverLoginService naverLoginService;

  @GetMapping
  public ResponseEntity<Void> redirectToNaverLogin(HttpSession session) {
    String state = UUID.randomUUID().toString();
    session.setAttribute("naver_oauth_state", state);

    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(URI.create(naverLoginService.makeLoginURL(state)));
    return new ResponseEntity<>(headers, HttpStatus.FOUND);
  }


  @GetMapping("/callback")
  public ResponseEntity<String> handleNaverCallback(
      @RequestParam("code") String code,
      @RequestParam("state") String state,
      HttpSession session
  ) {
    String token = naverLoginService.getNaverApiKey(session,state, code);
    naverLoginService.doNaverLogin(session, token);

    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(URI.create("/"));
    return new ResponseEntity<>(headers, HttpStatus.FOUND);


  }


}
