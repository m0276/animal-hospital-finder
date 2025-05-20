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

@Configuration
public class SecurityConfig {

  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder(){
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http .csrf(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests((authorize) -> authorize
            .requestMatchers("/api/hospital/**").hasRole("HOSPITAL")
            .requestMatchers(HttpMethod.DELETE,"/api/user/me").authenticated()
            .requestMatchers(HttpMethod.GET,"/api/user/me").authenticated()
            .requestMatchers(HttpMethod.PATCH,"/api/user/me").authenticated()
            .requestMatchers("/").permitAll()
            .anyRequest().authenticated())
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
