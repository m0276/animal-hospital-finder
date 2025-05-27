package MJ.animal_Hospital_Service.service.data;

import MJ.animal_Hospital_Service.domain.Hospital;
import MJ.animal_Hospital_Service.domain.HospitalKey;
import MJ.animal_Hospital_Service.repository.HospitalRepository;
import MJ.animal_Hospital_Service.service.api.ApiService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.text.similarity.JaroWinklerSimilarity;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataPatchService {

  private final ApiService apiService;
  private final HospitalRepository hospitalRepository;

  public void updateHospitals(List<Hospital> hospitals) {

    class HospitalMatcher {

      final double nameDiff = 0.9;
      final double locDiff =  0.0005;

      private final JaroWinklerSimilarity similarity = new JaroWinklerSimilarity();

      boolean matches(Set<HospitalKey> candidates, HospitalKey target) {
//        return candidates.stream().anyMatch(candidate -> {
//          double score = similarity.apply(candidate.getName(), target.getName());
//          boolean close = Math.abs(candidate.getLat() - target.getLat()) < locDiff &&
//              Math.abs(candidate.getLng() - target.getLng()) < locDiff;
//          return score >= nameDiff && close;
//        });
        return candidates.stream()
            .filter(candidate -> Math.abs(candidate.getLat() - target.getLat()) < locDiff &&
                Math.abs(candidate.getLng() - target.getLng()) < locDiff)
            .anyMatch(candidate -> similarity.apply(candidate.getName(), target.getName()) >= nameDiff);
      }
    }

    HospitalMatcher matcher = new HospitalMatcher();

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

      if (matcher.matches(unique,key)) {
        hospital.setTag("특수동물");
      }

      if (matcher.matches(allDay,key)) {
        if (hospital.getTag() != null)
          hospital.setTag2("24시간");
        else
          hospital.setTag("24시간");
      }

      if (matcher.matches(small,key)) {
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
