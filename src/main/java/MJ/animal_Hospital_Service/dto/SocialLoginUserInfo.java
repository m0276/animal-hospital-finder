package MJ.animal_Hospital_Service.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SocialLoginUserInfo {
  private String id;
  private String name;
}