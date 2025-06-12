package MJ.animal_Hospital_Service.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "issue")
public class Issue {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  @Column
  String type;

  @Column
  String issue;

  @Column(name = "user_id")
  Long userId;

  @Column(name = "hospital_id")
  Long hospitalId;
}
