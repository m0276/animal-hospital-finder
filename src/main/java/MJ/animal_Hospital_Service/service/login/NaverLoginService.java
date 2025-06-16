package MJ.animal_Hospital_Service.service.login;


import MJ.animal_Hospital_Service.dto.SocialLoginUserInfo;
import MJ.animal_Hospital_Service.service.user.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class NaverLoginService {
  private final UserService userService;
  private final CommonLoginService loginService;
  private final RestTemplate restTemplate = new RestTemplate();


  @Value("${naver_request_url}")
  String startUrl;

  @Value("${client_id}")
  String clientId;

  @Value("${client_secret}")
  String clientSecret;

  @Value("${naver_redirect}")
  String endUrl;

  @Value("${naver_request_token_url}")
  String token;

  public String makeLoginURL(String state){
    return startUrl
        + "?response_type=code"
        + "&client_id=" + clientId
        + "&redirect_uri=" + endUrl
        + "&state=" + state;
  }

  public String getToken(String code, String state){

    return token
        + "?grant_type=authorization_code"
        + "&client_id=" + clientId
        + "&client_secret=" + clientSecret
        + "&code=" + code
        + "&state=" + state;
  }


  public String getNaverApiKey(HttpSession session, String state, String code){
    String savedState = (String) session.getAttribute("naver_oauth_state");

    if (savedState == null || !savedState.equals(state)) {
      throw new AuthorizationDeniedException("no equals state");
    }

    session.removeAttribute("naver_oauth_state");

    return restTemplate.getForEntity(
        getToken(code, state),
        String.class).getBody();

  }

  public void doNaverLogin(HttpSession session, String token) {
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode tokenJson = objectMapper.readTree(token);
      String accessToken = tokenJson.get("access_token").asText();

      SocialLoginUserInfo socialLoginUserInfo = userService.getNaverUserInfo(accessToken);
      if (socialLoginUserInfo == null)
        throw new NoSuchElementException("토큰 파싱 실패");

      MJ.animal_Hospital_Service.domain.User user = userService.saveOrUpdateNaverUser(
          socialLoginUserInfo);

      loginService.login(session,user);


    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
