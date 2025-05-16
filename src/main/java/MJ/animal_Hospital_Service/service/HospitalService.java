package MJ.animal_Hospital_Service.service;


import MJ.animal_Hospital_Service.domain.Hospital;
import MJ.animal_Hospital_Service.repository.HospitalRepository;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class HospitalService {

  private final HospitalRepository hospitalRepository;

  Hospital findHospitalById(String id){
    return hospitalRepository.findById(id).orElseThrow(NoSuchElementException::new);
  }



}
