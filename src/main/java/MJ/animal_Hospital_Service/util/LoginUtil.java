package MJ.animal_Hospital_Service.util;


import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class LoginUtil {

  public static boolean isLogin(){
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return authentication != null &&
        authentication.isAuthenticated() &&
        !(authentication instanceof AnonymousAuthenticationToken);
  }

  public static String getCurrentUser() {
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    if (principal instanceof org.springframework.security.core.userdetails.User userDetails) {
      return userDetails.getUsername();
    }

    if (principal instanceof String username) {
      if (!username.equalsIgnoreCase("anonymousUser")) {
        return username;
      }
    }


    throw new IllegalStateException("현재 로그인된 사용자가 없습니다.");
  }
}