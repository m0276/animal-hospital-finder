package MJ.animal_Hospital_Service.exception;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
  private int errorCode;
  private String message;
}
