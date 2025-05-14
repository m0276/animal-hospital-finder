package MJ.animal_Hospital_Service.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.Instant;
import lombok.Getter;

@Entity
@Getter
public class Favorite {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  @Column(nullable = false, name = "user_id")
  Long userId;

  @Column(nullable = false, name = "hospital_location")
      // location은 위도,경도로 하면 된다 "위도,경도" 이렇게 저장해서 ,으로 나누어 쓰는 구조로 하는게 나을지도
  String hospitalLocation;
}
