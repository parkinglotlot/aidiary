//package com.aidiary.user.util;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//public class SecurityConfig {
//
// @Bean
//  public PasswordEncoder passwordEncoder() {
//   return new BCryptPasswordEncoder();
// }
//
// @Bean
// public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
//  //CSRF(Cross_Site Request Forgery) 교차 사이트 요청 위조
////  http.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.ignoringRequestMatchers("/login")).authorizeHttpRequests((requests) -> requests.anyRequest().permitAll());
//  //http.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.ignoringRequestMatchers("/valMembership")).authorizeHttpRequests((requests) -> requests.anyRequest().permitAll());
//http.csrf(AbstractHttpConfigurer::disable)
//    .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
//    .sessionManagement(session -> session
//        .sessionFixation().none()
//        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)) //세션이 필요하면 생성하도록 설정
//    .headers(headers ->  headers.frameOptions(f -> f.disable()));
//
//  return http.build();
// }
//
//}
