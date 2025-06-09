package MJ.animal_Hospital_Service.service.hospital;

import MJ.animal_Hospital_Service.domain.Hospital;
import MJ.animal_Hospital_Service.dto.HospitalDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface HospitalMapper {


  @Mapping(source = "placeId", target = "placeId")
  @Mapping(source = "formattedAddress", target = "formattedAddress")
  @Mapping(source = "lat", target = "lat")
  @Mapping(source = "lng", target = "lng")
  @Mapping(source = "nationalPhoneNumber", target = "nationalPhoneNumber")
  @Mapping(source = "name", target = "name")
  HospitalDto toDto(Hospital hospital);

  @Mapping(source = "placeId", target = "placeId")
  @Mapping(source = "formattedAddress", target = "formattedAddress")
  @Mapping(source = "lat", target = "lat")
  @Mapping(source = "lng", target = "lng")
  @Mapping(source = "nationalPhoneNumber", target = "nationalPhoneNumber")
  @Mapping(source = "name", target = "name")
  Hospital toEntity(HospitalDto dto);
}
