package MJ.animal_Hospital_Service.service.issue;

import MJ.animal_Hospital_Service.domain.Issue;
import MJ.animal_Hospital_Service.dto.IssueDto;
import MJ.animal_Hospital_Service.repository.IssueRepository;
import MJ.animal_Hospital_Service.service.hospital.HospitalService;
import MJ.animal_Hospital_Service.service.user.UserService;
import MJ.animal_Hospital_Service.util.LoginUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class IssueService {
  private final IssueRepository issueRepository;
  private final UserService userService;
  private final IssueMapper issueMapper;
  private final HospitalService hospitalService;

  public IssueDto createIssue(IssueDto dto){
    String type = dto.getType();
    String issue = dto.getIssue();
    Long userId = userService.findByUserNameReturnId(LoginUtil.getCurrentUser());

    Issue is = new Issue();

    is.setType(type);
    is.setIssue(issue);
    is.setUserId(userId);
    is.setHospitalId(dto.getHospitalId());
    is.setHospitalName(hospitalService.findHospitalInfo(dto.getHospitalId()).getName());

    issueRepository.save(is);

    return issueMapper.toDto(is);
  }

  public List<IssueDto> getIssues(){
    Long userId = userService.findByUserNameReturnId(LoginUtil.getCurrentUser());

    List<Issue> issues = issueRepository.findByUserId(userId);

    List<IssueDto> issueDtos = new ArrayList<>();

    for(Issue issue : issues){
      issueDtos.add(issueMapper.toDto(issue));
    }

    return issueDtos;
  }

  public IssueDto patchIssue(Long id, IssueDto issueDto){
   Issue issue =  issueRepository.findById(id).orElseThrow(NoSuchElementException::new);
   issue.setIssue(issueDto.getIssue());

   return issueMapper.toDto(issue);
  }

  public void deleteIssue(Long id){
    issueRepository.deleteById(id);
  }

  public IssueDto getIssue(Long id){
    return issueMapper.toDto(issueRepository.findById(id).orElseThrow(NoSuchElementException::new));
  }

}
