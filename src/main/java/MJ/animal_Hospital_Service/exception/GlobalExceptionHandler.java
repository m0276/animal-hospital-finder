package MJ.animal_Hospital_Service.exception;

import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(NoSuchElementException.class)
  public ResponseEntity<ErrorResponse> NoSuchException(NoSuchElementException exception){
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(
        HttpStatus.BAD_REQUEST.value(),
        exception.getMessage()));
  }

  @ExceptionHandler(AuthorizationDeniedException.class)
  public ResponseEntity<ErrorResponse> AuthException(AuthorizationDeniedException e){
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(
        HttpStatus.UNAUTHORIZED.value(),
        e.getMessage()));
  }
}
