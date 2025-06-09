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
  @JsonProperty("formatted_address")
  private String formattedAddress;
  private double lat;
  private double lng;
  @JsonProperty("id")
  private String placeId;
  private double rating;
  private boolean openNow;
  private String tag;
  private String tag2;
  private String tag3;

  private String nationalPhoneNumber;

  @JsonProperty("geometry")
  private void unpackGeometry(Map<String, Object> geometry) {
    Map<String, Object> location = (Map<String, Object>) geometry.get("location");
    this.lat = (Double) location.get("lat");
    this.lng = (Double) location.get("lng");
  }

  @JsonProperty("opening_hours")
  private void unpackOpeningHours(Map<String, Object> openingHours) {
    if (openingHours != null && openingHours.get("open_now") != null) {
      this.openNow = (Boolean) openingHours.get("open_now");
    }
  }

}
