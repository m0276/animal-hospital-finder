package MJ.animal_Hospital_Service.service.location;

import MJ.animal_Hospital_Service.dto.CurrentLoc;
import MJ.animal_Hospital_Service.dto.HospitalDto;
import MJ.animal_Hospital_Service.service.data.DataPatchService;
import MJ.animal_Hospital_Service.service.hospital.HospitalService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class LocationService {
  private final HospitalService hospitalService;
  private final RestTemplate restTemplate;

  @Value("${api_access_key}")
  private String access;

  @Value("${geo_api_secret_key}")
  private String secret;

  public List<HospitalDto> findCloseHospitals(CurrentLoc currentLoc) {
    return hospitalService.findInRangeHospital(currentLoc.getLat(), currentLoc.get_long());
  }

  public CurrentLoc getCurrentLocation(String ip) {
    try {
      String requestMethod = "GET";
      String hostName = "https://geolocation.apigw.ntruss.com";
      String requestUrl = "/geolocation/v2/geoLocation";

      Map<String, List<String>> requestParameters = Map.of(
          "ip", List.of(ip),
          "ext", List.of("t"),
          "responseFormatType", List.of("json")
      );

      SortedMap<String, SortedSet<String>> parameters = convertToSortedMap(requestParameters);
      String timestamp = Long.toString(System.currentTimeMillis());
      String baseString = requestUrl + "?" + getRequestQueryString(parameters);
      String signature = makeSignature(requestMethod, baseString, timestamp, access, secret);

      String fullUrl = hostName + baseString;
      HttpHeaders headers = new HttpHeaders();
      headers.set("x-ncp-apigw-timestamp", timestamp);
      headers.set("x-ncp-iam-access-key", access);
      headers.set("x-ncp-apigw-signature-v2", signature);
      headers.setLocation(new URI(fullUrl));

      String responseBody = sendRequest(fullUrl, headers);
      ObjectMapper mapper = new ObjectMapper();
      JsonNode root = mapper.readTree(responseBody);
      JsonNode geo = root.path("geoLocation");

      CurrentLoc loc = new CurrentLoc();
      loc.setLat(geo.path("lat").asText());
      loc.set_long(geo.path("long").asText());

      return loc;

    } catch (Exception e) {
      throw new RuntimeException("Failed to retrieve current location", e);
    }
  }

  private String sendRequest(String url, HttpHeaders headers) {
    HttpEntity<Void> entity = new HttpEntity<>(headers);
    ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    return response.getBody();
  }

  private SortedMap<String, SortedSet<String>> convertToSortedMap(Map<String, List<String>> params) {
    SortedMap<String, SortedSet<String>> sortedMap = new TreeMap<>();
    params.forEach((key, values) -> {
      SortedSet<String> sortedValues = sortedMap.computeIfAbsent(key, k -> new TreeSet<>());
      sortedValues.addAll(Optional.ofNullable(values).orElse(List.of("")));
    });
    return sortedMap;
  }

  private String getRequestQueryString(SortedMap<String, SortedSet<String>> params) {
    StringBuilder sb = new StringBuilder();
    Iterator<Map.Entry<String, SortedSet<String>>> it = params.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry<String, SortedSet<String>> entry = it.next();
      Iterator<String> vit = entry.getValue().iterator();
      while (vit.hasNext()) {
        sb.append(entry.getKey()).append("=").append(vit.next());
        if (it.hasNext() || vit.hasNext()) sb.append("&");
      }
    }
    return sb.toString();
  }

  public String makeSignature(String method, String baseString, String timestamp, String accessKey, String secretKey) {
    try {
      String message = method + " " + baseString + "\n" + timestamp + "\n" + accessKey;
      Mac mac = Mac.getInstance("HmacSHA256");
      mac.init(new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
      return Base64.getEncoder().encodeToString(mac.doFinal(message.getBytes(StandardCharsets.UTF_8)));
    } catch (NoSuchAlgorithmException | InvalidKeyException e) {
      throw new RuntimeException("Failed to create HMAC signature", e);
    }
  }

  public String extractClientIp(HttpServletRequest request) {
    String ip = request.getHeader("X-Forwarded-For");
    if (ip == null || ip.isEmpty()) {
      ip = request.getRemoteAddr();
    }
    if ("0:0:0:0:0:0:0:1".equals(ip) || "::1".equals(ip)) {
      ip = "39.118.137.94";
    }
    return ip;
  }

  private final DataPatchService dataPatchService;

  public void currLocSet(String lat, String lng){
    dataPatchService.currLoc(lat,lng);
  }
}
