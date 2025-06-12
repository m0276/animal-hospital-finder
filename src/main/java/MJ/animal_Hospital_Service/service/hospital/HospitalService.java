package MJ.animal_Hospital_Service.service.hospital;


import MJ.animal_Hospital_Service.domain.Hospital;
import MJ.animal_Hospital_Service.dto.HospitalDto;
import MJ.animal_Hospital_Service.repository.HospitalRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class HospitalService {

  private final HospitalRepository hospitalRepository;
  private final HospitalMapper hospitalMapper;

  // 태그 변경
  public HospitalDto updateTag(String placeId, String tag, int tagNumber){
    Hospital hospital = hospitalRepository.findById(placeId)
        .orElseThrow(NoSuchElementException::new);

    if(tagNumber == 1) hospital.setTag(tag);
    else if(tagNumber == 2) hospital.setTag2(tag);
    else hospital.setTag3(tag);

    return hospitalMapper.toDto(hospital);
  }

  // 태그 추가
  public HospitalDto addTag(String placeId, String tag){
    Hospital hospital = hospitalRepository.findById(placeId)
        .orElseThrow(NoSuchElementException::new);

    if(hospital.getTag() == null) hospital.setTag(tag);
    else if(hospital.getTag2() == null) hospital.setTag2(tag);
    else hospital.setTag3(tag);

    return hospitalMapper.toDto(hospital);
  }

  // 태그 삭제
  public HospitalDto deleteTag(String placeId, int tagNumber){
    Hospital hospital = hospitalRepository.findById(placeId)
        .orElseThrow(NoSuchElementException::new);

    if(tagNumber == 1) hospital.setTag(null);
    else if(tagNumber == 2) hospital.setTag2(null);
    else hospital.setTag3(null);

    return hospitalMapper.toDto(hospital);
  }

  public List<HospitalDto> findInRangeHospital(String x, String y){
    List<Hospital> list = hospitalRepository.findAllByLoc(Double.parseDouble(x),Double.parseDouble(y));

    List<HospitalDto> result = new ArrayList<>();
    for(Hospital h : list){
      result.add(hospitalMapper.toDto(h));
    }

    return result;
  }

  public HospitalDto findHospitalInfo(String hospitalId){
    Hospital hos = hospitalRepository.findByPlaceId(hospitalId).orElseThrow(NoSuchElementException::new);

    return hospitalMapper.toDto(hos);
  }

}
