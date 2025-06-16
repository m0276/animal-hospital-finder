package MJ.animal_Hospital_Service.repository;

import MJ.animal_Hospital_Service.domain.Issue;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {

  List<Issue> findByUserId(Long userId);
}
