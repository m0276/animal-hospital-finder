package MJ.animal_Hospital_Service.service.batch;

import MJ.animal_Hospital_Service.service.api.ApiService;
import MJ.animal_Hospital_Service.service.data.DataPatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class HospitalBatchService {

  private final ApiService apiService;
  private final DataPatchService dataPatchService;

  @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
  public void batch(){
    dataPatchService.updateHospitals(apiService.getApi());
  }

}
