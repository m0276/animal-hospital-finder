package MJ.animal_Hospital_Service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequest {
  String username;
  String newPassword;
}
