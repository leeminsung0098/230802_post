//로그인관련 나중에 추가할 부분
package javaproject.project;

import javaproject.project.Service.PrincipalOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig   {
    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                //구글api추가사항 시작
//                .requestMatchers("/security-login/info").authenticated()
//                .requestMatchers("/security-login/admin/**").hasAuthority(UserRole.ADMIN.name()).anyRequest().permitAll()
                //구글api추가사항 끝
                .requestMatchers(new AntPathRequestMatcher("/**")).permitAll()
                .and()
                //구글api추가사항 시작
//                .formLogin((formLogin)->formLogin
//                        .usernameParameter("lo oauth2ginId")
//                        .passwordParameter("password")
//                        .loginPage("/security-login/login")
//                        .defaultSuccessUrl("/security-login")
//                        .failureUrl("/security-login/login"))
////and
//                .logout((logout)->logout
//                        .logoutUrl("/security-login/logout")
//                        .invalidateHttpSession(true)
//                        .deleteCookies("JSESSIONID"))
//and           //구글api추가사항 끝
                .csrf((csrf)->csrf.disable())
//and
                .headers(headers->headers
                        .addHeaderWriter(new XFrameOptionsHeaderWriter(
                                XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)))
//and
                .formLogin((formLogin)->formLogin
                        .loginPage("/user/login")
                        .defaultSuccessUrl("/"))
//and
                .logout((logout)->logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true))
//                and
//                1.코드받기 2.엑세스 토큰 3. 사용자프로필정보가져오기 4. 그정보를 토대로 회원가입을 진행
//                4-2 (받은정보객체 이메일, 전화번호ㅡ 이름 등)만약  구글에서 받은객체보다 추가사항이 필요하다면 정보를 가져와서 처리해야함
                .oauth2Login((oauth2Login)->oauth2Login
                        .loginPage("/user/login")
                        .defaultSuccessUrl("/")
//                        유저 엑셋스토큰과 사용자프로필정보를 후처리(엔드포인트)하기위한정보
                        .userInfoEndpoint((userInfoEndpoint) ->userInfoEndpoint
                                        .userService(principalOauth2UserService)
                                )
//                  코드받기작업
                )

        ;


        return http.build();
    }

    @Bean//를 Spring에 등록해놓고 비밀번호 암호화, 비밀번호 체크할 때 사용용도
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    //    로그인시 필요한 Bean 코드
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}