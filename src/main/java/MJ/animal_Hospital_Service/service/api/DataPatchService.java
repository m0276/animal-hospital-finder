package MJ.animal_Hospital_Service.service.api;

import MJ.animal_Hospital_Service.domain.Hospital;
import MJ.animal_Hospital_Service.domain.HospitalKey;
import MJ.animal_Hospital_Service.dto.HospitalDto;
import MJ.animal_Hospital_Service.repository.HospitalRepository;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataPatchService {

  private final ApiService apiService;
  private final HospitalRepository hospitalRepository;

  void updateHospitals(List<Hospital> hospitals) {
    @NotNull Set<HospitalKey> unique = apiService.searchHospitals("특수동물").stream().map(
            dto -> new HospitalKey(dto.getPlace_name(), Double.parseDouble(dto.getX()),
                Double.parseDouble(dto.getY())))
        .collect(Collectors.toSet());
    @NotNull Set<HospitalKey> allDay = apiService.searchHospitals("24시간 진료").stream().map(
            dto -> new HospitalKey(dto.getPlace_name(), Double.parseDouble(dto.getX()),
                Double.parseDouble(dto.getY())))
        .collect(Collectors.toSet());
    @NotNull Set<HospitalKey> small = apiService.searchHospitals("소동물").stream().map(
            dto -> new HospitalKey(dto.getPlace_name(), Double.parseDouble(dto.getX()),
                Double.parseDouble(dto.getY())))
        .collect(Collectors.toSet());

    for (Hospital hospital : hospitals) {
      HospitalKey key = new HospitalKey(hospital.getPlaceName(), hospital.getX(), hospital.getY());

      if (unique.contains(key)) {
        hospital.setTag("특수동물");
      }

      if (allDay.contains(key)) {
        if (hospital.getTag() != null)
          hospital.setTag2("24시간");
        else
          hospital.setTag("24시간");
      }

      if (small.contains(key)) {
        if (hospital.getTag() == null)
          hospital.setTag("소동물");
        else if (hospital.getTag2() == null)
          hospital.setTag2("소동물");
        else
          hospital.setTag3("소동물");
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
