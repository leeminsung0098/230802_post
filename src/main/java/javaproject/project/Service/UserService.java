package javaproject.project.Service;

import com.fasterxml.jackson.databind.JsonNode;
import javaproject.project.Entity.User_T;
import javaproject.project.Repository.UserRepository;
import javaproject.project.Utill.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    //api로그인용
    private final Environment env;
    private final RestTemplate restTemplate=new RestTemplate();

    public User_T create(String loginId, String email, String password ,String nickname) {
        User_T user = new User_T();
        user.setLoginId(loginId);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setNickname(nickname);
        this.userRepository.save(user);
        return user;
    }

//    중복확인 시작
    public boolean checkLoginIdDuplicate(String loginId) {
        return userRepository.existsByLoginId(loginId);
    }

//    중복확인 끝
//구글 이메일 로그인 api 사용용도 시작
     public void socialLogin(String code, String registrationId) {
        System.out.println("code = " + code);
        System.out.println("registrationId = " + registrationId);

    }
    private String getAccessToken(String authorizationCode, String registrationId) {
        String clientId =  env.getProperty("oauth2." + registrationId + ".client-id");
        String clientSecret = env.getProperty("oauth2." + registrationId + ".client-secret");
        String redirectUri = env.getProperty("oauth2." + registrationId + ".redirect-uri");
        String tokenUri = env.getProperty("oauth2." + registrationId + ".token-uri");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", authorizationCode);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity entity = new HttpEntity(params, headers);

        ResponseEntity<JsonNode> responseNode = restTemplate.exchange(tokenUri, HttpMethod.POST, entity, JsonNode.class);
        JsonNode accessTokenNode = responseNode.getBody();
        return accessTokenNode.get("access_token").asText();
    }
    //구글 이메일 로그인 api 사용용도 끝

//    유저정보꺼내기용도
    public User_T getUser(String loginId) {
        Optional<User_T> user = this.userRepository.findByLoginId(loginId);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new DataNotFoundException("user_T not found");
        }
    }
    //임시비밀번호로 유저정보에 저장.
    public User_T SetTempPassword(String loginId , String authnum){
        Optional<User_T> user = this.userRepository.findByLoginId(loginId);
        if (user.isPresent()) {
            user.get().setPassword(passwordEncoder.encode(authnum));
            userRepository.save(user.get());
            return user.get();
        } else {
            throw new DataNotFoundException("user_T not found");
        }
    }

//    유저정보꺼내기용도


}
