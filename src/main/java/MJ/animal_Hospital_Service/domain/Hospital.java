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
  String location_id;
  @Column(nullable = false)
  String place_name;
  @Column(nullable = false)
  String category_name;
  @Column(nullable = false)
  String category_group_code;
  @Column(nullable = false)
  String category_group_name;
  @Column
  String phone;
  @Column(nullable = false)
  String address_name;
  @Column(nullable = false)
  String road_address_name;
  @Column
  String place_url;
  @Column
  String x;
  @Column
  String y;
  @Column
  String tag;
  @Column
  String tag2;

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public void setTag(String tag) {
    this.tag = tag;
  }

  public void setPlace_url(String place_url) {
    this.place_url = place_url;
  }

  public void setTag2(String tag2) {
    this.tag2 = tag2;
  }
}
