package MJ.animal_Hospital_Service.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HospitalDto {
//  String location_id;
//  String place_name;
//  String phone;
//  String address_name;
//  String road_address_name;
//  String x;
//  String y;
//  String place_url;
  private String name;

  @JsonProperty("formatted_address")
  private String formattedAddress;
  private double lat;
  private double lng;
  @JsonProperty("place_id")
  private String placeId;
  private double rating;
  private boolean openNow;
  private String tag;
  private String tag2;
  private String tag3;

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
