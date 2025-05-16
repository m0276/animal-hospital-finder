package MJ.animal_Hospital_Service.service.hospital;

import MJ.animal_Hospital_Service.domain.Hospital;
import MJ.animal_Hospital_Service.dto.HospitalDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HospitalMapper {
  HospitalDto toDto(Hospital hospital);
}
