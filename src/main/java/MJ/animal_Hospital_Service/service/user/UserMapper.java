package MJ.animal_Hospital_Service.service.user;

import MJ.animal_Hospital_Service.domain.User;
import MJ.animal_Hospital_Service.dto.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
  UserDto toUserDto(User user);
}
