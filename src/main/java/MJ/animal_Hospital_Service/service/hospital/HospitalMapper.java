package MJ.animal_Hospital_Service.service.hospital;

import MJ.animal_Hospital_Service.domain.Hospital;
import MJ.animal_Hospital_Service.dto.HospitalDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface HospitalMapper {

  @Mapping(target = "lat", source = "x")
  @Mapping(target = "lng", source = "y")
  HospitalDto toDto(Hospital hospital);

  @Mapping(target = "x", source = "lat")
  @Mapping(target = "y", source = "lng")
  Hospital toEntity(HospitalDto dto);
//
//  @Named("doubleToString")
//  static String doubleToString(Double value) {
//    return value != null ? value.toString() : null;
//  }
//
//  @Named("stringToDouble")
//  static Double stringToDouble(String value) {
//    return value != null ? Double.parseDouble(value) : null;
//  }
}
