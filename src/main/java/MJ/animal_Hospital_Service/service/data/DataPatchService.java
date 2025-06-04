package MJ.animal_Hospital_Service.service.data;

import MJ.animal_Hospital_Service.domain.Hospital;
import MJ.animal_Hospital_Service.domain.HospitalKey;
import MJ.animal_Hospital_Service.dto.HospitalDto;
import MJ.animal_Hospital_Service.repository.HospitalRepository;
import MJ.animal_Hospital_Service.service.api.ApiService;

import MJ.animal_Hospital_Service.service.hospital.HospitalMapper;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
  private final HospitalMapper hospitalMapper;
  private final HospitalRepository hospitalRepository;
  private String lat;
  private String lng;

  public void updateHospitals() {
    List<Set<HospitalDto>> setList = apiService.searchHospitals(lat,lng);
    @NotNull Set<HospitalDto> unique = setList.getFirst();
    @NotNull Set<HospitalDto> allDay =  setList.get(1);
    @NotNull Set<HospitalDto> small =  setList.getLast();

    System.out.println("uni:" + unique);
    Map<HospitalKey, Hospital> hospitalMap = new HashMap<>();

    for (HospitalDto dto : unique) {
      HospitalKey key = new HospitalKey(dto.getName(), dto.getLat(), dto.getLng());
      Hospital hospital = hospitalMap.getOrDefault(key, hospitalMapper.toEntity(dto));
      hospital.addTag("특수동물");
      hospitalMap.put(key, hospital);
    }

    for (HospitalDto dto : allDay) {
      HospitalKey key = new HospitalKey(dto.getName(), dto.getLat(), dto.getLng());
      Hospital hospital = hospitalMap.getOrDefault(key, hospitalMapper.toEntity(dto));
      hospital.addTag("24시간");
      hospitalMap.put(key, hospital);
    }

    for (HospitalDto dto : small) {
      HospitalKey key = new HospitalKey(dto.getName(), dto.getLat(), dto.getLng());
      Hospital hospital = hospitalMap.getOrDefault(key, hospitalMapper.toEntity(dto));
      hospital.addTag("소동물");
      hospitalMap.put(key, hospital);
    }

    List<Hospital> hospitalList = hospitalMap.values().stream().toList();
    hospitalRepository.saveAll(hospitalList);
    deleteHospitals(hospitalList);
  }

  private void deleteHospitals(List<Hospital> list){
     Set<String> dbIds = hospitalRepository.findAllLocationIds();

     Set<String> newIds = list.stream().map(Hospital::getLocationId)
         .collect(Collectors.toSet());

     dbIds.removeAll(newIds);
     if(!dbIds.isEmpty()) hospitalRepository.deleteAllById(dbIds.stream().toList());
  }

  public void currLoc(String lat, String lng){
   this.lat = lat;
   this.lng = lng;
  }
}
