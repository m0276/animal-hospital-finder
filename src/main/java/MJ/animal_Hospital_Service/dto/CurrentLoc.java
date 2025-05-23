package MJ.animal_Hospital_Service.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CurrentLoc {
  private String lat;

  @JsonProperty("long")
  private String _long;

}
