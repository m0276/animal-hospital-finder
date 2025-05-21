package MJ.animal_Hospital_Service.service.api;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class HospitalBatchService {

  private final ApiService apiService;
  private final DataPatchService dataPatchService;

  @Scheduled(cron = "0 0 0 * * *")
  public void batch(){
    dataPatchService.updateHospitals(apiService.getApi());
  }

}
