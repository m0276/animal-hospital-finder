package MJ.animal_Hospital_Service.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
public class SecurityConfig {

  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder(){
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http  .csrf(csrf -> csrf
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) // 클라이언트에서도 접근 가능
        )
        .httpBasic(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests((authorize) -> authorize
            .requestMatchers("/api/hospital/**").hasAnyRole("HOSPITAL","ADMIN")
            .requestMatchers(HttpMethod.DELETE,"/api/user/me").authenticated()
            .requestMatchers(HttpMethod.GET,"/api/user/me").authenticated()
            .requestMatchers(HttpMethod.PATCH,"/api/user/me").authenticated()

            .anyRequest().permitAll())
        .formLogin(login -> login.defaultSuccessUrl("/"))
        .logout(logout -> logout.logoutSuccessUrl("/")
            .invalidateHttpSession(true)
            .deleteCookies("JSESSIONID"))
        .securityContext(securityContext ->
            securityContext.securityContextRepository(new HttpSessionSecurityContextRepository()));
    return http.build();
  }

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer(){
    return web -> web.ignoring()
        .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
  }

}
