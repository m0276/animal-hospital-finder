package MJ.animal_Hospital_Service.service.favorite;

import MJ.animal_Hospital_Service.domain.Favorite;
import MJ.animal_Hospital_Service.dto.FavoriteDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FavoriteMapper {
  FavoriteDto toFavoriteDto(Favorite favorite);
}
