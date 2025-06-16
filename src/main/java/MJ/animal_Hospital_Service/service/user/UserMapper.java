package MJ.animal_Hospital_Service.service.user;

import MJ.animal_Hospital_Service.domain.User;
import MJ.animal_Hospital_Service.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

  @Mapping(source = "roles", target = "roles")
  UserDto toUserDto(User user);
}
