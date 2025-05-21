package MJ.animal_Hospital_Service.service.api;

import MJ.animal_Hospital_Service.domain.Hospital;
import MJ.animal_Hospital_Service.dto.HospitalDto;
import MJ.animal_Hospital_Service.repository.HospitalRepository;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataPatchService {
  private final ApiService apiService;
  private final HospitalRepository hospitalRepository;

   void updateHospitals(List<Hospital> hospitals) {
    Set<String> unique = apiService.searchHospitals("특수동물").stream().map(HospitalDto::getRoad_address_name).collect(
        Collectors.toSet());
    Set<String> allDay = apiService.searchHospitals("24시간 진료").stream().map(HospitalDto::getRoad_address_name).collect(Collectors.toSet());
    Set<String> small = apiService.searchHospitals("소동물").stream().map(HospitalDto::getRoad_address_name).collect(Collectors.toSet());


    for(Hospital hospital : hospitals){
      if(unique.contains(hospital.getRoadAddressName())) hospital.setTag("특수동물");

      if(allDay.contains(hospital.getRoadAddressName())){
        if(hospital.getTag() != null) hospital.setTag2("24시간");
        else hospital.setTag("24시간");
      }

      if(small.contains(hospital.getRoadAddressName())){
        if(hospital.getTag() == null) hospital.setTag("소동물");
        else if(hospital.getTag2() == null) hospital.setTag2("소동물");
        else hospital.setTag3("소동물");
      }
    }


    hospitalRepository.saveAll(hospitals);
    deleteHospitals();
  }

  private void deleteHospitals(){
     Set<String> dbIds = hospitalRepository.findAllLocationIds();

     Set<String> newIds = apiService.getApi().stream()
         .map(Hospital::getLocationId).collect(Collectors.toSet());

     dbIds.removeAll(newIds);
     if(!dbIds.isEmpty()) hospitalRepository.deleteAllById(dbIds.stream().toList());
  }

}
