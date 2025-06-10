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
  @Mapping(source = "openTime", target = "weekdayDescriptions")
  @Mapping(source = "placeUrl" , target = "websiteUri")
  HospitalDto toDto(Hospital hospital);

  @Mapping(source = "placeId", target = "placeId")
  @Mapping(source = "formattedAddress", target = "formattedAddress")
  @Mapping(source = "lat", target = "lat")
  @Mapping(source = "lng", target = "lng")
  @Mapping(source = "weekdayDescriptions", target = "openTime")
  @Mapping(source = "websiteUri", target = "placeUrl")
  @Mapping(source = "nationalPhoneNumber", target = "nationalPhoneNumber")
  @Mapping(source = "name", target = "name")
  Hospital toEntity(HospitalDto dto);
}
