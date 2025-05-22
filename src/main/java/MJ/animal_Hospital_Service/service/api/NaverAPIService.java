package MJ.animal_Hospital_Service.service.api;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class NaverAPIService {

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

}
