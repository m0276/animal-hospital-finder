package MJ.animal_Hospital_Service.service.hospital;

import MJ.animal_Hospital_Service.domain.Hospital;
import MJ.animal_Hospital_Service.dto.HospitalDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HospitalMapper {

  @Mapping(target = "x", source = "x", expression = "java(doubleToString)")
  @Mapping(target = "y", source = "y", expression = "java(doubleToString)")
  HospitalDto toDto(Hospital hospital);

  private String doubleToString(Double x){
    return Double.toString(x);
  }


}
