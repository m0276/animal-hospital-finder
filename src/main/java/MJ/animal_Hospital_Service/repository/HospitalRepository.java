package MJ.animal_Hospital_Service.repository;

import MJ.animal_Hospital_Service.domain.Hospital;
import java.util.Collection;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface HospitalRepository extends JpaRepository<Hospital, String> {
  @Query("SELECT h.locationId FROM Hospital h")
  Set<String> findAllLocationIds();
}
