package MJ.animal_Hospital_Service.service.hospital;

import MJ.animal_Hospital_Service.domain.Hospital;
import MJ.animal_Hospital_Service.dto.HospitalDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface HospitalMapper {

  @Mapping(target = "x", source = "x", qualifiedByName = "doubleToString")
  @Mapping(target = "y", source = "y", qualifiedByName = "doubleToString")
  HospitalDto toDto(Hospital hospital);

  @Mapping(target = "x", source = "x", qualifiedByName = "stringToDouble")
  @Mapping(target = "y", source = "y", qualifiedByName = "stringToDouble")
  Hospital toEntity(HospitalDto dto);

  @Named("doubleToString")
  static String doubleToString(Double value) {
    return value != null ? value.toString() : null;
  }

  @Named("stringToDouble")
  static Double stringToDouble(String value) {
    return value != null ? Double.parseDouble(value) : null;
  }
}
