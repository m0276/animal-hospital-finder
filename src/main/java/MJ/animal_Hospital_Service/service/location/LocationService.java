package MJ.animal_Hospital_Service.service.location;

import MJ.animal_Hospital_Service.dto.CurrentLoc;
import MJ.animal_Hospital_Service.dto.HospitalDto;
import MJ.animal_Hospital_Service.service.hospital.HospitalService;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
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
  @Value("{api_access_key}")
  String access;

  @Value("geo_api_secret_key")
  String secrete;

  @Value("{geo_loc_request_url}")
  String url;


  public List<HospitalDto> findCloseHospitals(CurrentLoc currentLoc){
    String x = currentLoc.getLat();
    String y = currentLoc.get_long();
    return hospitalService.findInRangeHospital(x,y);
  }

  private String makeSignature(String method, String url, String timestamp, String accessKey, String secretKey) throws Exception {
    String space = " ";
    String newLine = "\n";

    String message = new StringBuilder()
        .append(method)
        .append(space)
        .append(url)
        .append(newLine)
        .append(timestamp)
        .append(newLine)
        .append(accessKey)
        .toString();

    SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
    Mac mac = Mac.getInstance("HmacSHA256");
    mac.init(signingKey);

    byte[] rawHmac = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));

    return Base64.getEncoder().encodeToString(rawHmac);
  }

  public HttpHeaders getLocation(){
    try {
      HttpHeaders httpHeaders = new HttpHeaders();
      httpHeaders.add("x-ncp-apigw-timestamp",
          String.valueOf(System.currentTimeMillis()));
      httpHeaders.add("x-ncp-iam-access-key",access);
      httpHeaders.add("x-ncp-apigw-signature-v2",
          makeSignature(HttpMethod.GET.toString(),url,Instant.now().toString(),access,secrete));

      return httpHeaders;


    } catch (Exception e) {
      throw new RuntimeException("현재 위치를 불러오던 도중 오류가 발생했습니다.");
    }
  }


}
