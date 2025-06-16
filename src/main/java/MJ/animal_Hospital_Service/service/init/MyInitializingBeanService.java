package MJ.animal_Hospital_Service.service.init;

import MJ.animal_Hospital_Service.repository.HospitalRepository;
import MJ.animal_Hospital_Service.service.api.ApiService;
import MJ.animal_Hospital_Service.service.data.DataPatchService;
import MJ.animal_Hospital_Service.service.user.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MyInitializingBeanService{
  private final DataPatchService dataPatchService;
  private final UserService userService;
  private final HospitalRepository hospitalRepository;

  @PostConstruct
  public void init() {
    if(hospitalRepository.findAll().isEmpty()){
      userService.findAdmin(); // 관리자 계정 생성
      dataPatchService.updateHospitals();
    }
  }

}
