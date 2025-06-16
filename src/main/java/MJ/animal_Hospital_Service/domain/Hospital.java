package MJ.animal_Hospital_Service.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.locationtech.jts.geom.Point;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name =  "hospital")
public class Hospital {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  @Column(name = "location_id", nullable = false)
  String placeId;
  @Column(nullable = false, name = "place_name")
  String name;
  @Column(name = "phone")
  String nationalPhoneNumber;
  @Column(name = "address_name")
  String formattedAddress;
  @Column(name = "place_url")
  String placeUrl;
  @Column(nullable = false, name = "x")
  Double lng;
  @Column(nullable = false, name = "y")
  Double lat;
  @Column
  String tag;
  @Column
  String tag2;
  @Column
  String tag3;
  @Column(columnDefinition = "geometry(Point,4326)", nullable = false)
  Point loc;
  @Column(name = "open_time")
  String openTime;


  public void setPhone(String phone) {
    this.nationalPhoneNumber = phone;
  }
  public void setPlace_url(String place_url) {
    this.placeUrl = place_url;
  }

  public void addTag(String newTag) {
    if (newTag == null) return;

    if (newTag.equals(tag) || newTag.equals(tag2) || newTag.equals(tag3)) {
      return;
    }

    if (tag == null) tag = newTag;
    else if (tag2 == null) tag2 = newTag;
    else if (tag3 == null) tag3 = newTag;
  }

}
