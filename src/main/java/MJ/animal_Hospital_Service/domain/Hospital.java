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
  @Id //(x+y)
  String hospitalLocation;

  @Column(nullable = false)
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


}
