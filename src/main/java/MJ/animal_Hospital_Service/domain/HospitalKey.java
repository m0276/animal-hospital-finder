package MJ.animal_Hospital_Service.domain;

import java.util.Objects;
import org.springframework.stereotype.Component;

@Component
public class HospitalKey {
  private final String name;
  private final double lat;
  private final double lng;

  public HospitalKey(String name, double lat, double lng) {
    this.name = normalize(name); // 공백 제거, 소문자화 등 유사도 보정
    this.lat = lat;
    this.lng = lng;
  }

  private String normalize(String input) {
    return input == null ? "" : input.trim().toLowerCase();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof HospitalKey)) return false;
    HospitalKey that = (HospitalKey) o;
    return Math.abs(that.lat - lat) < 0.0003 &&  // 약 30m 오차 허용
        Math.abs(that.lng - lng) < 0.0003 &&
        Objects.equals(name, that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, (int)(lat * 10000), (int)(lng * 10000)); // hash 오차 줄임
  }
}

