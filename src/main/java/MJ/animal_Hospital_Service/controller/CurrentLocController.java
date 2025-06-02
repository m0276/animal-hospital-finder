package MJ.animal_Hospital_Service.controller;

import MJ.animal_Hospital_Service.dto.CurrentLoc;
import MJ.animal_Hospital_Service.dto.HospitalDto;
import MJ.animal_Hospital_Service.service.location.LocationService;
import jakarta.servlet.http.HttpServletRequest;
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

  @GetMapping
  public ResponseEntity<List<HospitalDto>> getCurrLocNearHospitals(HttpServletRequest request){
    String clientIp = locationService.extractClientIp(request);
    CurrentLoc loc = locationService.getCurrentLocation(clientIp);
    if (loc == null) {
      return ResponseEntity.internalServerError().build();
    }

    List<HospitalDto> hospitals = locationService.findCloseHospitals(loc);

    return ResponseEntity.ok(hospitals);
  }

  @GetMapping("/curr")
  public ResponseEntity<CurrentLoc> getLocation(HttpServletRequest request) {
    String clientIp = locationService.extractClientIp(request);
    if (clientIp == null || clientIp.isEmpty()) {
      clientIp = request.getRemoteAddr();
    }
    if ("0:0:0:0:0:0:0:1".equals(clientIp) || "::1".equals(clientIp)) {
      clientIp = "39.118.137.94";
    }
    CurrentLoc loc = locationService.getCurrentLocation(clientIp);
    return ResponseEntity.ok(loc);
  }
}
