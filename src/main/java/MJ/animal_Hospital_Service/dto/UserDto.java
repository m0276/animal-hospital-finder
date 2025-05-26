package MJ.animal_Hospital_Service.dto;

import MJ.animal_Hospital_Service.config.Role;
import java.util.Set;

public record UserDto(
    Long id,
    String username,
    Set<Role> roles
) {
}
