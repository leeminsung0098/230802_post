package javaproject.project.Controller;


import jakarta.validation.Valid;
import javaproject.project.CreateForm.Email.EmailPostDto;
import javaproject.project.CreateForm.Email.LoginIdAndEmailPostDto;
import javaproject.project.Entity.EmailMessage;
import javaproject.project.Entity.User_T;
import javaproject.project.Repository.UserRepository;
import javaproject.project.Service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RequestMapping("/send-mail")
@Controller
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;
    private final UserRepository userRepository;


    // 임시 비밀번호 발급
    @PostMapping("/password")
    public String  sendPasswordMail(@Valid LoginIdAndEmailPostDto loginIdAndEmailPostDto, BindingResult bindingResult) {
// 순서 먼저 아이디와 이메일을 받아와야함
        if (bindingResult.hasErrors()) {
            System.out.println("받기오류");
        return "Login/Password_Find";
        }
        System.out.println("이메일 : "+loginIdAndEmailPostDto.getEmail());
        System.out.println("아이디 : "+loginIdAndEmailPostDto.getLoginId());

//        아이디랑 이메일로 계정조회후 있으면 이메일 전송
        EmailMessage emailMessage = EmailMessage.builder()
                .to(loginIdAndEmailPostDto.getEmail())
                .subject("[SAVIEW] 임시 비밀번호 발급")
                .build();
        emailService.sendMail_Password(emailMessage, "email/send_Mail_Password",loginIdAndEmailPostDto.getLoginId());
        return "redirect:/UserBoard/main";
    }
    @PostMapping("/id")
    public String sendIddMail(@Valid EmailPostDto emailPostDto, BindingResult bindingResult) {
        EmailMessage emailMessage = EmailMessage.builder()
                .to(emailPostDto.getEmail())
                .subject("[방구석 갤러리] 방게 아이디 조회")
                .build();
        System.out.println("수신자 이메일확인 : ");
        System.out.println(emailPostDto.getEmail());

        emailService.sendMail_Id(emailMessage, "email/send_Mail_Id");

        return "redirect:/UserBoard/main";
    }




    // 회원가입 이메일 인증 - 요청 시 body로 인증번호 반환하도록 작성하였음


//    //아래는 가입가능한 이메일을 보내는 메서드 사용할지 의문
//    @PostMapping("/email")
//    public ResponseEntity sendJoinMail(@RequestBody EmailPostDto emailPostDto) {
//        EmailMessage emailMessage = EmailMessage.builder()
//                .to(emailPostDto.getEmail())
//                .subject("[SAVIEW] 이메일 인증을 위한 인증 코드 발송")
//                .build();
//
//        String code = emailService.sendMail(emailMessage, "email");
//
//        EmailResponseDto emailResponseDto = new EmailResponseDto();
//        emailResponseDto.setCode(code);
//
//        return ResponseEntity.ok(emailResponseDto);
//    }
}