package javaproject.project.Service;

import javaproject.project.Entity.User_T;
import javaproject.project.Repository.UserRepository;
import javaproject.project.CreateForm.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserSecurityService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
            //여기 들어가는 파인드바이닉네임 안에 유저네임은 꼭 유저네임으로 들어가야함 양식이 그럼 닉네임 했다가 안됬었음
        Optional<User_T> _user = this.userRepository.findByLoginId(loginId);

        if (_user.isEmpty()) {

            throw new UsernameNotFoundException("사용자를 찾을수 없습니다.");
        }
        User_T user = _user.get();
        List<GrantedAuthority> authorities = new ArrayList<>();
        if ("admin".equals(loginId)) {
            authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
        } else {
            authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));
        }
        //로그인이 성공했는지 확인여부를 위한 출력문 개발자를 위한 코드라 출력문은 지워도 상관 없음
//        System.out.println("================성공");
        return new User(user.getLoginId(), user.getPassword(), authorities);
    }
}