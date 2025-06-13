package MJ.animal_Hospital_Service.config;

import MJ.animal_Hospital_Service.service.login.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder(){
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http  .csrf(csrf -> csrf
            .csrfTokenRepository(cookieCsrfTokenRepository())
            .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
            .ignoringRequestMatchers( new AntPathRequestMatcher("/api/user/**", "POST"))
        )
        .sessionManagement(session -> session
            .sessionFixation().migrateSession()
        )
        .httpBasic(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests((authorize) -> authorize
            .requestMatchers("/api/favorite/**").authenticated()
            .requestMatchers("/api/hospital/**").authenticated()
            .requestMatchers("/api/hospital/tag/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE,"/api/user/me").authenticated()
            .requestMatchers(HttpMethod.GET,"/api/user/me").authenticated()
            .requestMatchers(HttpMethod.PATCH,"/api/user/me").authenticated()
            .requestMatchers("/api/issue/**").authenticated()
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

  private final CustomUserDetailsService userDetailsService;

  @Bean
  public AuthenticationManager authManager(HttpSecurity http) throws Exception {
    AuthenticationManagerBuilder authManagerBuilder =
        http.getSharedObject(AuthenticationManagerBuilder.class);

    authManagerBuilder
        .userDetailsService(userDetailsService)
        .passwordEncoder(bCryptPasswordEncoder());

    return authManagerBuilder.build();
  }

  @Bean
  public CookieCsrfTokenRepository cookieCsrfTokenRepository() {
    CookieCsrfTokenRepository repo = CookieCsrfTokenRepository.withHttpOnlyFalse();
    repo.setCookiePath("/");
    repo.setSecure(false);
    return repo;
  }

}
