package MJ.animal_Hospital_Service.service.api;


import MJ.animal_Hospital_Service.controller.CurrentLocController;
import MJ.animal_Hospital_Service.domain.Hospital;
import MJ.animal_Hospital_Service.dto.CurrentLoc;
import MJ.animal_Hospital_Service.dto.HospitalDto;
import MJ.animal_Hospital_Service.service.hospital.HospitalMapper;
import MJ.animal_Hospital_Service.service.location.LocationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class ApiService {
  private final ObjectMapper objectMapper;
  private final HospitalMapper hospitalMapper;
  private final RestTemplate restTemplate;

  @Value("${restApiKey}")
  private String restApiKey;

  @Value("${googleApiKey}")
  private String googleKey;

//  public List<Hospital> getApi() {
//    try{
//      HttpHeaders headers = new HttpHeaders();
//      headers.set("Authorization", "KakaoAK " + restApiKey);
//
//      HttpEntity<String> entity = new HttpEntity<>(headers);
//
//      String query = URLEncoder.encode("동물병원", StandardCharsets.UTF_8);
//
//      String kakaoUrl = UriComponentsBuilder
//          .fromHttpUrl("https://dapi.kakao.com/v2/local/search/keyword.json")
//          .queryParam("query", query)
//          .build(true)
//          .toUriString();
//// TO DO : 전국 동물 병원은 받아오기 어려움 (limit 존재) > 현재 위치 기반 20KM 가능..
//
//      ResponseEntity<String> response = restTemplate.exchange(
//          kakaoUrl,
//          HttpMethod.GET,
//          entity,
//          String.class
//      );
//
//      //System.out.println(response.getBody());
//
//      if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
//        return Collections.emptyList();
//      }
//
//      List<Hospital> list = new ArrayList<>();
//
//      JsonNode documents = objectMapper.readTree(response.getBody()).path("documents");
//      for (JsonNode doc : documents) {
//        list.add(hospitalMapper.toEntity(objectMapper.treeToValue(doc, HospitalDto.class)));
//      }
//      //System.out.println(list);
//      return list;
//    }
//    catch (JsonProcessingException e){
//      e.printStackTrace();
//    }
//
//    return Collections.emptyList();
//  }

public List<Set<HospitalDto>> searchHospitals(String lat, String lng) {
  List<String> keywords = List.of("특수동물", "24시간", "소동물");
  List<Set<HospitalDto>> resultsPerKeyword = new ArrayList<>();

  for (String keyword : keywords) {
    Set<HospitalDto> keywordResults = new HashSet<>();
    String pageToken = null;

    do {
      try {
        UriComponentsBuilder builder = UriComponentsBuilder
            .fromHttpUrl("https://maps.googleapis.com/maps/api/place/textsearch/json")
            .queryParam("query", keyword)
            .queryParam("location",lat+","+lng)
            .queryParam("radius","50000")
            .queryParam("region", "KR")
            .queryParam("key", googleKey);

        if (pageToken != null) {
          builder.queryParam("pagetoken", pageToken);
          Thread.sleep(2500);
        }

        String url = builder.toUriString();
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        System.out.println(response.toString());
        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) break;

        JsonNode root = objectMapper.readTree(response.getBody());
        JsonNode resultsNode = root.path("results");

        if (resultsNode.isArray()) {
          for (JsonNode placeNode : resultsNode) {
            HospitalDto dto = objectMapper.treeToValue(placeNode, HospitalDto.class);
            keywordResults.add(dto);
          }
        }

        pageToken = root.has("next_page_token") ? root.get("next_page_token").asText() : null;

      } catch (Exception e) {
        e.printStackTrace();
        break;
      }

    } while (pageToken != null);

    resultsPerKeyword.add(keywordResults);
  }

  return resultsPerKeyword;
}

}
