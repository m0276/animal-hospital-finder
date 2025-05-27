package MJ.animal_Hospital_Service.controller;

import MJ.animal_Hospital_Service.dto.CurrentLoc;
import MJ.animal_Hospital_Service.dto.HospitalDto;
import MJ.animal_Hospital_Service.service.location.LocationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/loc")
public class CurrentLocController {
  private final LocationService locationService;
  private final RestTemplate restTemplate = new RestTemplate();

  @Value("{geo_loc_request_url}")
  String url;

  @GetMapping
  public ResponseEntity<List<HospitalDto>> getCurrLocNearHospitals(){
    CurrentLoc loc = restTemplate.exchange(url, HttpMethod.GET,
        new HttpEntity<>(locationService.getLocation()), CurrentLoc.class).getBody();

    if(loc == null) return ResponseEntity.internalServerError().build();

    return ResponseEntity.ok().body(locationService.findCloseHospitals(loc));
  }
}
