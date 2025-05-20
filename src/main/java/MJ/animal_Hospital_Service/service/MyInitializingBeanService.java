package MJ.animal_Hospital_Service.service;

import MJ.animal_Hospital_Service.domain.Hospital;
import MJ.animal_Hospital_Service.dto.HospitalDto;
import MJ.animal_Hospital_Service.repository.HospitalRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Transactional
@RequiredArgsConstructor
public class MyInitializingBeanService implements InitializingBean {
  private final HospitalRepository hospitalRepository;
  @Value("${restApiKey}")
  private String restApiKey;

  @Value("${googleApiKey}")
  private String googleKey;


  @Override
  public void afterPropertiesSet() {
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

      ObjectMapper objectMapper = new ObjectMapper();

      JsonNode documents = objectMapper.readTree(response.getBody()).path("documents");
      for (JsonNode doc : documents) {
        HospitalDto dto = objectMapper.treeToValue(doc, HospitalDto.class);
        saveHospital(dto);
      }

    }
    catch (JsonProcessingException e){
      e.printStackTrace();
    }
  }

  private List<HospitalDto> searchHospitals(String keyword) {
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

  @Scheduled(cron = "0 0 0 * * *")
  public void batch(){
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

      ObjectMapper objectMapper = new ObjectMapper();

      JsonNode documents = objectMapper.readTree(response.getBody()).path("documents");
      for (JsonNode doc : documents) {
        HospitalDto dto = objectMapper.treeToValue(doc, HospitalDto.class);
        if(hospitalRepository.findById(dto.getLocation_id()).isPresent()) continue;
        saveHospital(dto);
      }
    }
    catch (JsonProcessingException e){
      e.printStackTrace();
    }

  }

  private void saveHospital(HospitalDto dto) {
    Hospital hospital = Hospital.builder()
        .locationId(dto.getLocation_id())
        .addressName(dto.getAddress_name())
        .placeName(dto.getPlace_name())
        .roadAddressName(dto.getRoad_address_name())
        .x(dto.getX())
        .y(dto.getY())
        .build();


    if(dto.getPhone() != null) hospital.setPhone(dto.getPhone());
    if(dto.getPlace_url() != null) hospital.setPlace_url(dto.getPlace_url());

    if(searchHospitals("특수동물").stream().map(HospitalDto::getRoad_address_name).collect(Collectors.toSet())
        .contains(dto.getRoad_address_name())) hospital.setTag("특수동물");

    if(searchHospitals("24시간 진료").stream().map(HospitalDto::getRoad_address_name).collect(Collectors.toSet())
        .contains(dto.getRoad_address_name())){
      if(hospital.getTag() != null) hospital.setTag2("24시간");
      else hospital.setTag("24시간");
    }

    if(searchHospitals("소동물").stream().map(HospitalDto::getRoad_address_name).collect(Collectors.toSet())
        .contains(dto.getRoad_address_name())){
      if(hospital.getTag() == null) hospital.setTag("소동물");
      else if(hospital.getTag2() == null) hospital.setTag2("소동물");
      else hospital.setTag3("소동물");
    }

    hospitalRepository.save(hospital);
  }
}
