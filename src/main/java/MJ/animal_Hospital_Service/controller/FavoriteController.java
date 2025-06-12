package MJ.animal_Hospital_Service.controller;

import MJ.animal_Hospital_Service.dto.FavoriteDto;
import MJ.animal_Hospital_Service.dto.HospitalDto;
import MJ.animal_Hospital_Service.service.favorite.FavoriteService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/favorite")
public class FavoriteController {
  private final FavoriteService favoriteService;

  @GetMapping
  public ResponseEntity<List<HospitalDto>> getList(){
    return ResponseEntity.ok(favoriteService.getList());
  }

  @PutMapping("/{hospitalId}")
  public ResponseEntity<Void> updateFavorite(@PathVariable String hospitalId){
    favoriteService.saveOrDelete(hospitalId);
    return ResponseEntity.noContent().build();
  }

}
