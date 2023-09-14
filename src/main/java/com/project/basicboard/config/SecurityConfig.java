package com.project.basicboard.config;

import com.project.basicboard.dto.UserAccountDto;
import com.project.basicboard.dto.security.BoardPrincipal;
import com.project.basicboard.repository.UserAccountRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebSecurity
//@EnableWebMvc
@Configuration
public class SecurityConfig {

    /*
    * [Spring Security 6.0 변경] 참고!
    * build.gradle 에서
    * implementation 'org.thymeleaf.extras:thymeleaf-extras-springsecurity5' 가 아닌
    * implementation 'org.thymeleaf.extras:thymeleaf-extras-springsecurity6' 를 적용하면
    *  mvcMatchers(), antMatchers()  가 적용되지 않음.
    * springsecurity6은 spring security6 및 스프링부트3.0 이후 버전에 대응함
    * 때문에 mvcMatchers(), antMatchers() 는 전부 requestMatchers()로 바꿔서 사용해야 한다.
    * */

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        //.anyRequest().permitAll()
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .mvcMatchers("/api/**").permitAll()
                        .mvcMatchers(
                                HttpMethod.GET,
                                "/",
                                "/articles",
                                "/articles/search-hashtag"
                        ).permitAll()  // 위와 매칭되는 요청이오면 허용한다.
                        .anyRequest().authenticated()   // 나머지 요청은 인증이 필요하다.
                )
/*                .formLogin(login -> login	// form 방식 로그인 사용
                        .defaultSuccessUrl("/view/dashboard", true)	// 성공 시 dashboard로
                        .permitAll())*/
                .formLogin().and()
                .logout()
                .logoutSuccessUrl("/")
                .and()
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService(UserAccountRepository userAccountRepository) {
        return username -> userAccountRepository
                .findById(username)
                //username에 있는 것들을 dto로 변환
                .map(UserAccountDto::from)
                .map(BoardPrincipal::from)
                //인증된 사용자를 못찾은 경우
                .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다 - username: " + username));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}