package MJ.animal_Hospital_Service.dto;


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
  private String formattedAddress;
  private double lat;
  private double lng;
  private String placeId;
  private double rating;
  private boolean openNow;
  private String tag;
  private String tag2;
  private String tag3;
}
