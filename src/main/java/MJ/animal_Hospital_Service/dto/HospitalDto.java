package MJ.animal_Hospital_Service.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HospitalDto {

  private String name;
  @JsonProperty("formattedAddress")
  private String formattedAddress;
  private double lat;
  private double lng;
  @JsonProperty("id")
  private String placeId;
  private String weekdayDescriptions;
  private boolean openNow;
  private String tag;
  private String tag2;
  private String tag3;
  private String websiteUri;
  private String nationalPhoneNumber;

  @JsonProperty("location")
  private void unpackGeometry(Map<String, Object> location) {
    this.lat = (Double) location.get("latitude");
    this.lng = (Double) location.get("longitude");
  }

  @JsonProperty("opening_hours")
  private void unpackOpeningHours(Map<String, Object> openingHours) {
    if (openingHours != null && openingHours.get("open_now") != null) {
      this.openNow = (Boolean) openingHours.get("open_now");
    }
  }

  @JsonProperty("displayName")
  private void getName(Map<String, Object> displayName){
    this.name = (String) displayName.get("text");
  }

}
