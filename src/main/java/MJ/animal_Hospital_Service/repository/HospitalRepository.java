package MJ.animal_Hospital_Service.repository;

import MJ.animal_Hospital_Service.domain.Hospital;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface HospitalRepository extends JpaRepository<Hospital, String> {
  @Query("SELECT h.locationId FROM Hospital h")
  Set<String> findAllLocationIds();

  @Query(
      value = "SELECT * FROM hospital WHERE ST_Distance_Sphere(loc, POINT(?1, ?2)) <= 5000",
      nativeQuery = true
  )
  List<Hospital> findAllByLoc(double x, double y);
}
