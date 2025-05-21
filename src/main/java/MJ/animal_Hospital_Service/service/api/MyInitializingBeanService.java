package MJ.animal_Hospital_Service.service.api;

import MJ.animal_Hospital_Service.domain.Hospital;
import MJ.animal_Hospital_Service.service.user.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MyInitializingBeanService implements InitializingBean {
  private final ApiService apiService;
  private final DataPatchService dataPatchService;
  private final UserService userService;


  @Override
  public void afterPropertiesSet() {
    userService.findAdmin(); // 관리자 계정 생성
    dataPatchService.updateHospitals(apiService.getApi());
  }

}
