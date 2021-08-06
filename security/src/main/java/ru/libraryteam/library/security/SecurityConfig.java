package ru.libraryteam.library.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.libraryteam.library.db.repository.UserRepository;
import ru.libraryteam.library.security.handler.LibraryAccessDeniedHandler;
import ru.libraryteam.library.security.handler.LibraryFailureHandler;
import ru.libraryteam.library.security.handler.LibrarySuccessHandler;
import ru.libraryteam.library.security.impl.UserDetailsServiceImpl;
import ru.libraryteam.library.service.security.ProfileMapper;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final UserRepository userRepository;
  private final ProfileMapper profileMapper;

  @Autowired
  public SecurityConfig(
    UserRepository userRepository,
    ProfileMapper profileMapper
  ) {
    this.userRepository = userRepository;
    this.profileMapper = profileMapper;
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsServiceBean());
    var daoAuthenticationProvider = new DaoAuthenticationProvider();
    daoAuthenticationProvider.setUserDetailsService(userDetailsService());
    daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

    auth.authenticationProvider(daoAuthenticationProvider);

  }
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  public static void main(String[] args) {
    System.out.println(new BCryptPasswordEncoder().encode("admin"));
  }

  @Bean
  @Override
  public UserDetailsService userDetailsServiceBean() throws Exception {
    return new UserDetailsServiceImpl(userRepository);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
      .cors().disable()
      .csrf().disable()
      .formLogin()
      .loginProcessingUrl("/auth/login")
      .successHandler(new LibrarySuccessHandler(userRepository, profileMapper))
      .failureHandler(new LibraryFailureHandler())

      .and()
      .authorizeRequests()
      .anyRequest().not().fullyAuthenticated();
      //.mvcMatchers(HttpMethod.GET, "/auth/login").not().authenticated()
      //.anyRequest().fullyAuthenticated();
  }


}
