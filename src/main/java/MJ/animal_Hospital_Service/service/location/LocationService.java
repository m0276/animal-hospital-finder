package MJ.animal_Hospital_Service.service.location;

import MJ.animal_Hospital_Service.dto.CurrentLoc;
import MJ.animal_Hospital_Service.dto.HospitalDto;
import MJ.animal_Hospital_Service.service.hospital.HospitalService;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class LocationService {
  private final HospitalService hospitalService;

  @Value("${api_access_key}")
  private String access;

  @Value("${geo_api_secret_key}")
  private String secret;

  @Value("${geo_loc_request_url}")
  private String baseUrl;

  private final RestTemplate restTemplate = new RestTemplate();

  public List<HospitalDto> findCloseHospitals(CurrentLoc currentLoc) {
    String x = currentLoc.getLat();
    String y = currentLoc.get_long();
    return hospitalService.findInRangeHospital(x, y);
  }

  public CurrentLoc getCurrentLocation(String clientIp) {
    try {
      String apiPath = "/geolocation/v2/geoLocation";
      String fullUrl = baseUrl + apiPath + "?ip=" + clientIp + "&ext=t&responseFormat=json";
      String timestamp = String.valueOf(System.currentTimeMillis());

      HttpHeaders headers = new HttpHeaders();
      headers.add("x-ncp-apigw-timestamp", timestamp);
      headers.add("x-ncp-iam-access-key", access);
      headers.add("x-ncp-apigw-signature-v2",
          makeSignature(apiPath, timestamp, access, secret));

      HttpEntity<Void> httpEntity = new HttpEntity<>(headers);
      ResponseEntity<Map> response = restTemplate.exchange(fullUrl, HttpMethod.GET, httpEntity, Map.class);

      Map<String, Object> location = (Map<String, Object>) response.getBody().get("location");
      String lat = location.get("lat").toString();
      String lon = location.get("lng").toString();
      CurrentLoc loc = new CurrentLoc();
      loc.setLat(lat);
      loc.set_long(lon);
      return loc;

    } catch (Exception e) {
      throw new RuntimeException("위치 정보를 가져오는 중 오류 발생: " + e.getMessage());
    }
  }

  private String makeSignature(String url, String timestamp, String accessKey, String secretKey) throws Exception {
    String message = "GET" + " " + url + "\n" + timestamp + "\n" + accessKey;

    SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
    Mac mac = Mac.getInstance("HmacSHA256");
    mac.init(signingKey);

    byte[] rawHmac = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));
    return Base64.getEncoder().encodeToString(rawHmac);
  }
}
