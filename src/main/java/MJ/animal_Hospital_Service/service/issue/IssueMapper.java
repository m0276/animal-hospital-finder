package MJ.animal_Hospital_Service.service.issue;

import MJ.animal_Hospital_Service.domain.Issue;
import MJ.animal_Hospital_Service.dto.IssueDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IssueMapper {
  IssueDto toDto(Issue issue);

  Issue toEntity(IssueDto dto);
}
