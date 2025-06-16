package MJ.animal_Hospital_Service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IssueDto {
  Long id;
  String type;
  String issue;
  String hospitalId;
  String hospitalName;
}
