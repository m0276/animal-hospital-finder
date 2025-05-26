package MJ.animal_Hospital_Service.controller;

import MJ.animal_Hospital_Service.service.login.KakaoLoginService;
import jakarta.servlet.http.HttpSession;
import java.net.URI;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth/kakao")
public class KakaoLoginApiController {

  private final KakaoLoginService kakaoLoginService;

  @Value("${restApiKey}")
  private String kakaoApiKey;

  @Value("${kakao_redirect}")
  private String kakaoRedirectUri;

  @GetMapping
  public ResponseEntity<Void> kakaoLogin(HttpSession session) {
    String state = UUID.randomUUID().toString();
    session.setAttribute("kakao_state", state);

    HttpHeaders headers = new HttpHeaders();
    String uri = "https://kauth.kakao.com/oauth/authorize?"
        + "response_type=code&client_id="
        +kakaoApiKey +"&redirect_uri=" + kakaoRedirectUri;
    headers.setLocation(URI.create(uri));

    return new ResponseEntity<>(headers, HttpStatus.FOUND);
  }


  @GetMapping("/callback")
  public ResponseEntity<String> kakaoAccessToken(
      @RequestParam("code") String code,
      @RequestParam("state") String state,
      HttpSession session
  ) {
    String token = kakaoLoginService.getkakaoAccessToken(session,code, state);
    kakaoLoginService.doKakaoLogin(session, token);

    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(URI.create("/"));
    return new ResponseEntity<>(headers, HttpStatus.FOUND);
  }

}
