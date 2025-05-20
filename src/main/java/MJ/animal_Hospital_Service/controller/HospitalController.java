package MJ.animal_Hospital_Service.controller;


import MJ.animal_Hospital_Service.dto.HospitalDto;
import MJ.animal_Hospital_Service.dto.HospitalTagUpdateRequest;
import MJ.animal_Hospital_Service.service.hospital.HospitalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HospitalController {
  private final HospitalService hospitalService;

  @PatchMapping("/addTag")
  public ResponseEntity<HospitalDto> addTag(@RequestBody HospitalTagUpdateRequest updateRequest){
   return ResponseEntity.ok()
       .body(hospitalService.addTag(updateRequest.getPlaceId(), updateRequest.getTag()));
  }
}
//https://apis.map.kakao.com/web/guide/#loadlibrary