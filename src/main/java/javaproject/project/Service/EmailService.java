package javaproject.project.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import javaproject.project.Entity.EmailMessage;
import javaproject.project.Entity.User_T;
import javaproject.project.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;
    private final UserRepository userRepository;
    private final UserService userService;

//  ===  비밀번호 찿기 시작


    public String sendMail_Password(EmailMessage emailMessage, String type, String loginId) {
        String authNum =createCode();//임시비밀번호생성

        MimeMessage mimeMessage = javaMailSender.createMimeMessage(); //메일 객체 생성
        //이메일과 아이디를 사용해 비교처리
        Optional<User_T> _user1 = this.userRepository.findByEmail(emailMessage.getTo());
        if (_user1.isEmpty()) {
            throw new UsernameNotFoundException("사용자를 찾을수 없습니다.");
        }
        Optional<User_T> _user2 = this.userRepository.findByLoginId(loginId);
        if (_user2.isEmpty()) {
            throw new UsernameNotFoundException("사용자를 찾을수 없습니다.");
        }


        User_T u1 =_user1.get();
        User_T u2 =_user2.get();
//      비교하여도 찾을수없을경우 에러반환
        if(!u1.getLoginId().equals(u2.getLoginId())){
            throw new UsernameNotFoundException("사용자를 찾을수 없습니다.");
        }

        //비밀번호를 임시비밀번호로 셋팅하는장소 set부분은 setPassword로 서비스에서 생성해야함
//   if (type.equals("password")) userService.SetTempPassword(emailMessage.getTo(), authNum);
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(emailMessage.getTo()); // 메일 수신자
            mimeMessageHelper.setSubject(emailMessage.getSubject()); // 메일 제목
            mimeMessageHelper.setText(setContext_Password(authNum, type), true); // 메일 본문 내용, HTML 여부
            javaMailSender.send(mimeMessage);
            userService.SetTempPassword(loginId ,authNum);



            return authNum;

        } catch (MessagingException e) {
            System.out.println("fail");
            throw new RuntimeException(e);
        }
    }

    // 인증번호 및 임시 비밀번호 생성 메서드
    public String createCode() {
        Random random = new Random();
        StringBuffer key = new StringBuffer();

        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(4);

            switch (index) {
                case 0: key.append((char) ((int) random.nextInt(26) + 97)); break;
                case 1: key.append((char) ((int) random.nextInt(26) + 65)); break;
                default: key.append(random.nextInt(9));
            }
        }
        return key.toString();
    }
    // thymeleaf를 통한 html 적용
    public String setContext_Password(String code, String type) {
        Context context = new Context();
        context.setVariable("code", code);
        return templateEngine.process(type, context);
    }
// ===   비밀번호 찿기 끝


//    === 아이디 찾기 시작
    public String sendMail_Id(EmailMessage emailMessage, String type) {
//        emailMessage.getTo() 는 안에 이메일 반환
       System.out.println("getTo : "+emailMessage.getTo());
        Optional<User_T> _user = this.userRepository.findByEmail(emailMessage.getTo());
        if (_user.isEmpty()) {
            throw new UsernameNotFoundException("사용자를 찾을수 없습니다.");
        }
        User_T user =_user.get();


            System.out.println("확인 정보 : " + user.getLoginId());
            if(!(user.getProviderId()==null))// 구글연결아이디일경우 처리
                user.setLoginId("구글 연결 아이디 발견");

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(emailMessage.getTo()); // 메일 수신자
            mimeMessageHelper.setSubject(emailMessage.getSubject()); // 메일 제목
            mimeMessageHelper.setText(setContext_Id( type,user), true); // 메일 본문 내용, HTML 여부
            javaMailSender.send(mimeMessage);

            return user.getLoginId();
        } catch (MessagingException e) {
            System.out.println("fail");
            throw new RuntimeException(e);
        }
    }
//    보낼 html에 실제 데이터를담는 함수
    public String setContext_Id(String type,User_T user) {
        Context context = new Context();
        context.setVariable("user",user);
        return templateEngine.process(type, context);
    }
//      === 아이디 찾기 끝
}
