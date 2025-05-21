package MJ.animal_Hospital_Service.controller;


import MJ.animal_Hospital_Service.dto.UserCreateRequest;
import MJ.animal_Hospital_Service.dto.UserDto;
import MJ.animal_Hospital_Service.dto.UserUpdateRequest;
import MJ.animal_Hospital_Service.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

  private final UserService userService;

  @GetMapping("/me")
  public ResponseEntity<UserDto> userInfo(){
    return ResponseEntity.status(HttpStatus.OK).body(userService.
        get(SecurityContextHolder.getContext().getAuthentication().getName()));
  }

  @PostMapping
  public ResponseEntity<UserDto> joinUser(@RequestBody UserCreateRequest request){
    return ResponseEntity.status(HttpStatus.CREATED).body(userService.saveUserAuth(request));
  }


  @PostMapping("/hospital")
  public ResponseEntity<UserDto> joinHospital(@RequestBody UserCreateRequest request){
    return ResponseEntity.status(HttpStatus.CREATED).body(userService.saveHospitalAuth(request));
  }

  @DeleteMapping("/me")
  public ResponseEntity<Void> delete(){
    userService.delete(SecurityContextHolder.getContext().getAuthentication().getName());
    return ResponseEntity.ok().build();
  }

  @PatchMapping("/me")
  public ResponseEntity<UserDto> updatePassword(@RequestBody UserUpdateRequest request){
    return ResponseEntity.status(HttpStatus.OK).body(userService.update(request));
  }
}
