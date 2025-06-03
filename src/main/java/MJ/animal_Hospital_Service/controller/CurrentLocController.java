package MJ.animal_Hospital_Service.controller;

import MJ.animal_Hospital_Service.dto.CurrentLoc;
import MJ.animal_Hospital_Service.dto.HospitalDto;
import MJ.animal_Hospital_Service.service.location.LocationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/loc")
public class CurrentLocController {
  private final LocationService locationService;

  @GetMapping
  public ResponseEntity<List<HospitalDto>> getHospitalsNearCurrentLocation(HttpServletRequest request) {
    try {
      String clientIp = locationService.extractClientIp(request);
      CurrentLoc loc = locationService.getCurrentLocation(clientIp);
      List<HospitalDto> hospitals = locationService.findCloseHospitals(loc);
      return ResponseEntity.ok(hospitals);
    } catch (Exception e) {
      return ResponseEntity.internalServerError().build();
    }
  }

  @GetMapping("/curr")
  public ResponseEntity<CurrentLoc> getCurrentLocation(HttpServletRequest request) {
    try {
      String clientIp = locationService.extractClientIp(request);
      CurrentLoc loc = locationService.getCurrentLocation(clientIp);
      return ResponseEntity.ok(loc);
    } catch (Exception e) {
      return ResponseEntity.internalServerError().build();
    }
  }
}
