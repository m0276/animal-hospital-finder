package MJ.animal_Hospital_Service.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Hospital {
  @Id
  @Column(name = "location_id")
  String locationId;
  @Column(nullable = false, name = "place_name")
  String placeName;
  @Column
  String phone;
  @Column(name = "address_name")
  String addressName;
  @Column(name = "road_address_name")
  String roadAddressName;
  @Column(name = "place_url")
  String placeUrl;
  @Column(nullable = false)
  String x;
  @Column(nullable = false)
  String y;
  @Column
  String tag;
  @Column
  String tag2;
  @Column
  String tag3;

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public void setTag(String tag) {
    this.tag = tag;
  }

  public void setPlace_url(String place_url) {
    this.placeUrl = place_url;
  }

  public void setTag2(String tag2) {
    this.tag2 = tag2;
  }

  public void setTag3(String tag3) {
    this.tag3 = tag3;
  }

}
