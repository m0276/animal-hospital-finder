package MJ.animal_Hospital_Service.service.favorite;

import MJ.animal_Hospital_Service.repository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FavoriteService {
  private FavoriteRepository favoriteRepository;
}
