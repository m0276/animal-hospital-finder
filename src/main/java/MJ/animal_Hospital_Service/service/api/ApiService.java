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
import org.springframework.http.MediaType;
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

public List<Set<HospitalDto>> searchHospitals() {
  List<String> keywords = List.of("특수동물", "24시간동물", "소동물");
  List<Set<HospitalDto>> resultsPerKeyword = new ArrayList<>();

  for (String keyword : keywords) {
    Set<HospitalDto> keywordResults = new HashSet<>();

      try {
        String url = "https://places.googleapis.com/v1/places:searchText";
        Map<String, Object> requestBody = Map.of(
            "textQuery", keyword + "병원,한국"
        );

        String requestBodyJson = objectMapper.writeValueAsString(requestBody);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Goog-Api-Key", googleKey);
        headers.set("X-Goog-FieldMask", "*");

        HttpEntity<String> http = new HttpEntity<>(requestBodyJson, headers);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, http, String.class);

        JsonNode root = objectMapper.readTree(responseEntity.getBody());
        JsonNode resultsNode = root.path("places");

        if(resultsNode.isArray()){
          for(JsonNode node:resultsNode){
            HospitalDto dto = objectMapper.treeToValue(node, HospitalDto.class);
            keywordResults.add(dto);
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
        break;
      }


    resultsPerKeyword.add(keywordResults);
  }

  return resultsPerKeyword;
}

}
