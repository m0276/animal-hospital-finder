package MJ.animal_Hospital_Service.repository;

import MJ.animal_Hospital_Service.domain.Favorite;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

  List<Favorite> findByUserId(Long userId);
  void deleteByHospitalIdAndUserId(String hospitalId,Long userId);
}
