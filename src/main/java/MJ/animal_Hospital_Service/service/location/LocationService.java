package MJ.animal_Hospital_Service.service.location;

import MJ.animal_Hospital_Service.dto.CurrentLoc;
import MJ.animal_Hospital_Service.dto.HospitalDto;
import MJ.animal_Hospital_Service.service.hospital.HospitalService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
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

  private final RestTemplate restTemplate;

  public List<HospitalDto> findCloseHospitals(CurrentLoc currentLoc) {
    String x = currentLoc.getLat();
    String y = currentLoc.get_long();
    return hospitalService.findInRangeHospital(x, y);
  }
//
//  public CurrentLoc  getCurrentLocation(String clientIp) {
//    try {
//      String apiPath = "/geolocation/v2/geoLocation";
//      String fullUrl = baseUrl + apiPath + "?ip=" + clientIp + "&ext=t&responseFormat=json";
//      String timestamp = String.valueOf(System.currentTimeMillis());
//
//      HttpHeaders headers = new HttpHeaders();
//      headers.add("x-ncp-apigw-timestamp", timestamp);
//      headers.add("x-ncp-iam-access-key", access);
//      headers.add("x-ncp-apigw-signature-v2",
//          makeSignature(fullUrl, timestamp, access, secret));
//
//      HttpEntity<Void> httpEntity = new HttpEntity<>(headers);
//      ResponseEntity<Map> response = restTemplate.exchange(fullUrl, HttpMethod.GET, httpEntity, Map.class);
//      Map<String, Object> location = (Map<String, Object>) response.getBody().get("geoLocation");
//      String lat = location.get("lat").toString();
//      String lon = location.get("lng").toString();
//      CurrentLoc loc = new CurrentLoc();
//      loc.setLat(lat);
//      loc.set_long(lon);
//      return loc;
//
//    } catch (Exception e) {
//      throw new RuntimeException("위치 정보를 가져오는 중 오류 발생: " + e.getMessage());
//    }
//  }
//
//  private String makeSignature(String url, String timestamp, String accessKey, String secretKey) throws Exception {
//    String message = "GET" + " " + url + "\n" + timestamp + "\n" + accessKey;
//    SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
//    Mac mac = Mac.getInstance("HmacSHA256");
//    mac.init(signingKey);
//
//    byte[] rawHmac = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));
//    return Base64.getEncoder().encodeToString(rawHmac);
//  }


  public CurrentLoc getCurrentLocation(String ip){
    String requestMethod = "GET";
    String hostName = "https://geolocation.apigw.ntruss.com";
    String requestUrl = "/geolocation/v2/geoLocation";

    Map<String, List<String>> requestParameters = new HashMap<>();
    requestParameters.put("ip", List.of(ip));
    requestParameters.put("ext", List.of("t"));
    requestParameters.put("responseFormatType", List.of("json"));

    SortedMap<String, SortedSet<String>> parameters = convertTypeToSortedMap(requestParameters);

    String timestamp = Long.toString(System.currentTimeMillis());

    String baseString = requestUrl + "?" + getRequestQueryString(parameters);

    String signature = makeSignature(requestMethod, baseString, timestamp, access, secret);

    final String requestFullUrl = hostName + baseString;
    final HttpHeaders request = new HttpHeaders();
    try{
      request.setLocation(new URI(requestFullUrl));
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }

    request.add("x-ncp-apigw-timestamp",timestamp);
    request.add("x-ncp-iam-access-key",access);
    request.add("x-ncp-apigw-signature-v2",signature);

    String body = getResponseFromApi(requestFullUrl,request);
    String lat,lon;

    try{
      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode root = objectMapper.readTree(body);
      JsonNode geo = root.path("geoLocation");

       lat = geo.path("lat").asText();
       lon = geo.path("long").asText();
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }

    CurrentLoc loc = new CurrentLoc();
    loc.setLat(lat);
    loc.set_long(lon);
    return loc;
  }

  private String getResponseFromApi(String url, HttpHeaders headers) {
    HttpEntity<Void> httpEntity = new HttpEntity<>(headers);
    ResponseEntity<String> response = restTemplate.exchange(
        url,
        HttpMethod.GET,
        httpEntity,
        String.class
    );
    return response.getBody();
  }


  private static SortedMap<String, SortedSet<String>> convertTypeToSortedMap(final Map<String, List<String>> requestParameters) {
    final SortedMap<String, SortedSet<String>> significateParameters = new TreeMap<>();
    for (String parameterName : requestParameters.keySet()) {
      List<String> parameterValues = requestParameters.get(parameterName);
      if (parameterValues == null) {
        parameterValues = new ArrayList<>();
      }

      for (String parameterValue : parameterValues) {
        if (parameterValue == null) {
          parameterValue = "";
        }

        SortedSet<String> significantValues = significateParameters.computeIfAbsent(parameterName,
            k -> new TreeSet<>());
        significantValues.add(parameterValue);
      }

    }
    return significateParameters;
  }

  private static String getRequestQueryString(final SortedMap<String, SortedSet<String>> significantParameters) {
    final StringBuilder queryString = new StringBuilder();
    final Iterator<Entry<String, SortedSet<String>>> paramIt = significantParameters.entrySet().iterator();
    while (paramIt.hasNext()) {
      final Map.Entry<String, SortedSet<String>> sortedParameter = paramIt.next();
      final Iterator<String> valueIt = sortedParameter.getValue().iterator();
      while (valueIt.hasNext()) {
        final String parameterValue = valueIt.next();

        queryString.append(sortedParameter.getKey()).append('=').append(parameterValue);

        if (paramIt.hasNext() || valueIt.hasNext()) {
          queryString.append('&');
        }
      }
    }
    return queryString.toString();
  }


  public String makeSignature(String method, String baseString,
      String timestamp, String accessKey, String secretKey){
    String space = " ";
    String newLine = "\n";

    String message = new StringBuilder()
        .append(method)
        .append(space)
        .append(baseString)
        .append(newLine)
        .append(timestamp)
        .append(newLine)
        .append(accessKey)
        .toString();

    try{
      SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
      Mac mac = Mac.getInstance("HmacSHA256");
      mac.init(signingKey);
      byte[] rawHmac = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));
      return Base64.getEncoder().encodeToString(rawHmac);
    } catch (InvalidKeyException | NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  }

  public String extractClientIp(HttpServletRequest request) {
    String clientIp = request.getHeader("X-Forwarded-For");
    if (clientIp == null || clientIp.isEmpty()) {
      clientIp = request.getRemoteAddr();
    }
    if ("0:0:0:0:0:0:0:1".equals(clientIp) || "::1".equals(clientIp)) {
      clientIp = "39.118.137.94";
    }
    return clientIp;
  }


}
