package MJ.animal_Hospital_Service.repository;

import MJ.animal_Hospital_Service.domain.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

}
