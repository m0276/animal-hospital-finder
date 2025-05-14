package MJ.animal_Hospital_Service.repository;

import MJ.animal_Hospital_Service.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  User findByUsername(String username);
}
