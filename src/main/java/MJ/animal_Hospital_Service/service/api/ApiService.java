package MJ.animal_Hospital_Service.service.api;


import MJ.animal_Hospital_Service.domain.Hospital;
import MJ.animal_Hospital_Service.dto.HospitalDto;
import MJ.animal_Hospital_Service.service.hospital.HospitalMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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

  @Value("${restApiKey}")
  private String restApiKey;

  @Value("${googleApiKey}")
  private String googleKey;

  public List<Hospital> getApi() {
    try{
      HttpHeaders headers = new HttpHeaders();
      headers.set("Authorization", "KakaoAK " + restApiKey);

      HttpEntity<String> entity = new HttpEntity<>(headers);

      String kakaoUrl = UriComponentsBuilder
          .fromHttpUrl("https://dapi.kakao.com/v2/local/search/keyword.json")
          .queryParam("query", "동물병원")
          .queryParam("category_group_code", "HP8")
          .toUriString();

      RestTemplate restTemplate = new RestTemplate();

      ResponseEntity<String> response = restTemplate.exchange(
          kakaoUrl,
          HttpMethod.GET,
          entity,
          String.class
      );

      List<Hospital> list = new ArrayList<>();

      JsonNode documents = objectMapper.readTree(response.getBody()).path("documents");
      for (JsonNode doc : documents) {
        list.add(hospitalMapper.toEntity(objectMapper.treeToValue(doc, HospitalDto.class)));
      }

      return list;
    }
    catch (JsonProcessingException e){
      e.printStackTrace();
    }

    return Collections.emptyList();
  }

   protected List<HospitalDto> searchHospitals(String keyword) {
    String url = UriComponentsBuilder
        .fromHttpUrl("https://maps.googleapis.com/maps/api/place/textsearch/json")
        .queryParam("query", keyword)
        .queryParam("region", "KR")
        .queryParam("key", googleKey)
        .toUriString();

    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

    List<HospitalDto> results = new ArrayList<>();
    if (response.getStatusCode() == HttpStatus.OK) {
      List<Map<String, Object>> places = (List<Map<String, Object>>) response.getBody().get("results");

      for (Map<String, Object> place : places) {
        Map<String, Object> geometry = (Map<String, Object>) place.get("geometry");
        Map<String, Object> location = (Map<String, Object>) geometry.get("location");

        HospitalDto loc = new HospitalDto();
        loc.setPlace_name((String) place.get("username"));
        loc.setRoad_address_name((String) place.get("formatted_address"));
        loc.setX((String) location.get("lat"));
        loc.setY((String) location.get("lng"));
        loc.setLocation_id((String) place.get("place_name"));

        results.add(loc);
      }
    }
    return results;
  }

}
