package MJ.animal_Hospital_Service.repository;

import MJ.animal_Hospital_Service.domain.Hospital;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface HospitalRepository extends JpaRepository<Hospital, Long> {
  @Query("SELECT h.placeId FROM Hospital h")
  Set<String> findAllLocationIds();

  @Query(
      value = "SELECT * FROM hospital WHERE ST_Distance_Sphere(loc, ST_GeomFromText(CONCAT('POINT(', ?1, ' ', ?2, ')'), 4326 )) > ?3 * 5000 AND ST_Distance_Sphere(loc, ST_GeomFromText(CONCAT('POINT(', ?1, ' ', ?2, ')'), 4326 )) <= (?3 + 1) * 5000",
      nativeQuery = true
  )
  List<Hospital> findAllByLoc(@Param("x")double x, @Param("y")double y, @Param("step") int step);

  Optional<Hospital> findByPlaceId(String placeId);

  void deleteHospitalByPlaceId(String placeId);
}
