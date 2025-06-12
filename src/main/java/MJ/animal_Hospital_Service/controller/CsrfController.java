package MJ.animal_Hospital_Service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CsrfController {

  @GetMapping("/csrf")
  public ResponseEntity<CsrfToken> csrf(CsrfToken token) {
    return ResponseEntity.ok(token);
  }
}