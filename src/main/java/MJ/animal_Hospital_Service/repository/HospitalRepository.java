package MJ.animal_Hospital_Service.repository;

import MJ.animal_Hospital_Service.domain.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HospitalRepository extends JpaRepository<Hospital, String> {

}
