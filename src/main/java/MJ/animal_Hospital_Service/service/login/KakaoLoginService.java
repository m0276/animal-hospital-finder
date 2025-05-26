package MJ.animal_Hospital_Service.service.login;

import MJ.animal_Hospital_Service.dto.SocialLoginUserInfo;
import MJ.animal_Hospital_Service.service.user.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Transactional
public class KakaoLoginService {
  private final UserService userService;
  private final CommonLoginService loginService;
  @Value("${restApiKey}")
  private String kakaoApiKey;

  @Value("${kakao_redirect}")
  private String kakaoRedirectUri;

  private final RestTemplate restTemplate = new RestTemplate();

  public String getkakaoAccessToken(HttpSession session, String code, String state){
    String savedState = (String) session.getAttribute("kakao_state");

    if (savedState == null || !savedState.equals(state)) {
      throw new AuthorizationDeniedException("no equal state");
    }

    session.removeAttribute("kakao_state");

    String url = "https://kauth.kakao.com/oauth/token";

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    headers.set("Accept-Charset", "utf-8");

    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("grant_type", "authorization_code");
    params.add("client_id", kakaoApiKey);
    params.add("redirect_uri", kakaoRedirectUri);
    params.add("code", code);

    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

    return restTemplate.postForEntity(url, request, String.class).getBody();
  }

  public void doKakaoLogin(HttpSession session, String token){
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode tokenJson = objectMapper.readTree(token);
      String accessToken = tokenJson.get("access_token").asText();

      SocialLoginUserInfo socialLoginUserInfo = getKakaoUser(accessToken);
      if (socialLoginUserInfo == null) throw new NoSuchElementException("토큰 파싱 실패");


      MJ.animal_Hospital_Service.domain.User user = userService.saveOrUpdateKakaoUser(
          socialLoginUserInfo);

      loginService.login(session,user);

    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException();
    }
  }

  public SocialLoginUserInfo getKakaoUser(String accessToken){
    return userService.getKakaoUserInfo(accessToken);
  }
}
