package MJ.animal_Hospital_Service.controller;


import MJ.animal_Hospital_Service.dto.HospitalDto;
import MJ.animal_Hospital_Service.dto.HospitalTagUpdateRequest;
import MJ.animal_Hospital_Service.service.hospital.HospitalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hospital")
public class HospitalController {
  private final HospitalService hospitalService;

  @PatchMapping("/tag")
  public ResponseEntity<HospitalDto> addTag(@RequestBody HospitalTagUpdateRequest updateRequest){
   return ResponseEntity.ok()
       .body(hospitalService.addTag(updateRequest.getPlaceId(), updateRequest.getTag()));
  }

  @PatchMapping("/tag/{place}/{tagNumber}")
  public ResponseEntity<HospitalDto> removeTag(@PathVariable String place,
      @PathVariable Integer tagNumber){
    return ResponseEntity.ok().body(hospitalService.deleteTag(place,tagNumber));
  }

  @PatchMapping("/tag/{place}/{tagNum}/{newTag}")
  public ResponseEntity<HospitalDto> changeTag(@PathVariable String place,
      @PathVariable Integer tagNum, @PathVariable String newTag){
    return ResponseEntity.ok().body(hospitalService.updateTag(place,newTag,tagNum));
  }
}