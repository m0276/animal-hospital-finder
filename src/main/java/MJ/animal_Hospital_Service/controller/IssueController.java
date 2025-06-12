package MJ.animal_Hospital_Service.controller;

import MJ.animal_Hospital_Service.dto.IssueDto;
import MJ.animal_Hospital_Service.service.issue.IssueService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/issue")
public class IssueController {
  private final IssueService issueService;

  @PostMapping
  public ResponseEntity<IssueDto> createIssue(@RequestBody IssueDto issueDto){
    return ResponseEntity.status(HttpStatus.CREATED).body(issueService.createIssue(issueDto));
  }

  @GetMapping
  public ResponseEntity<List<IssueDto>> getPostIssues(){
    return ResponseEntity.ok(issueService.getIssues());
  }

  @PatchMapping("/{id}")
  public ResponseEntity<IssueDto> patchIssue(@PathVariable Long id, @RequestBody IssueDto issueDto){
    return ResponseEntity.ok(issueService.patchIssue(id,issueDto));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteIssue(@PathVariable Long id){
    issueService.deleteIssue(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/{id}")
  public ResponseEntity<IssueDto> getIssue(@PathVariable Long id){
    return ResponseEntity.ok(issueService.getIssue(id));
  }

}
