package javaproject.project.Service;

import javaproject.project.Entity.User_T;
import javaproject.project.Repository.UserRepository;
import javaproject.project.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

//SecurityConfig의 후처리를 돕기위한 서비스 객체
@RequiredArgsConstructor
@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;
//    private final UserService userService;
//아래는 구글로부터 받은 userRequest 함수
    @Override       //실질적으로 후처리가 되는 자동으로 만들어지는 함수 장소 오버라이딩으로 약간의 수정
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("userRequest :" +userRequest);
        System.out.println("getAccessToken :" +userRequest.getAccessToken().getTokenValue());
        System.out.println("getClientRegistration :" +userRequest.getClientRegistration());
        System.out.println("loadUser :" +super.loadUser(userRequest).getAttributes());
        //현제 상황 정리 구글로그인버튼 클릭-구글 로그인창- 로그인완료
        //로직 code를 리턴 (oauth-client라이브러리 ->accessToken요청)
        //userRequest정보 ->loardUser함수 호출 -> 구글로부터 회원프로필을 받아준다.
    if (!userRepository.existsByLoginId(super.loadUser(userRequest).getName())){
        Map map=super.loadUser(userRequest).getAttributes();
        User_T user = new User_T();
        user.setLoginId((String)map.get("sub"));
        user.setNickname((String)map.get("name"));
        user.setProviderId((String)map.get("sub"));
        user.setEmail((String)map.get("email"));
        user.setProvider("google.com");
        userRepository.save(user);
    }


//loadUser :{sub=114164763232688933253,
// name=백성호,
// given_name=성호,
// family_name=백,
// picture=https://lh3.googleusercontent.com/a/AAcHTtdCsWYwnR-25jIntEV3eLc0rtcm286gkpm_RhBrLc5S=s96-c,
// email=tjdgh9023@gmail.com,
// email_verified=true, locale=ko}


        return super.loadUser(userRequest);
    }
}
//    public User_T create(String nickname, String email, String password) {
//        User_T user = new User_T();
//        user.setNickname(nickname);
//        user.setEmail(email);
//        user.setPassword(passwordEncoder.encode(password));
//        this.userRepository.save(user);
//        return user;
//    }
//     userService.create(userCreateForm.getNickname(),
//             userCreateForm.getEmail(), userCreateForm.getPassword1());
