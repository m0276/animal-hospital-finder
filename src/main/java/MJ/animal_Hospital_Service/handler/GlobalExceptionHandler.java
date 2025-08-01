package MJ.animal_Hospital_Service.handler;

import MJ.animal_Hospital_Service.exception.ErrorResponse;
import java.util.NoSuchElementException;
import javax.security.sasl.AuthenticationException;
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

  @ExceptionHandler(NullPointerException.class)
  public ResponseEntity<ErrorResponse> NullPointException(NullPointerException e){
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(
        HttpStatus.BAD_REQUEST.value(),
        e.getMessage()));
  }

  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<ErrorResponse> AuthenticationException(AuthorizationDeniedException e){
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(
        HttpStatus.UNAUTHORIZED.value(),
        e.getMessage()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> exception(Exception e){
//    e.printStackTrace();
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(
        HttpStatus.INTERNAL_SERVER_ERROR.value(),
        e.getMessage()));
  }

}
