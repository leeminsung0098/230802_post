package javaproject.project.CreateForm;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//https://chb2005.tistory.com/173
//JoinRequest 과같음 DTO
public class UserCreateForm {
    @Size(min = 3, max = 25)
    @NotBlank(message = "로그인 아이디가 비어있습니다.")
    @NotEmpty(message = "사용자 ID는 필수항목입니다.")
    private String loginId;

    @Size(min = 3, max = 25)
    @NotBlank(message = "사용자닉네임이 비어있습니다.")
    @NotEmpty(message = "사용자 닉네임은 필수항목입니다.")
    private String nickname;

    @Size(min = 3, max = 25)
    @NotEmpty(message = "비밀번호는 필수항목입니다.")
    @NotBlank(message = "로그인 비밀번호가 비어있습니다.")
    private String password1;

    @NotEmpty(message = "비밀번호 확인은 필수항목입니다.")
    @NotBlank(message = "로그인 비밀번호가 비어있습니다.")
    private String password2;

    @NotEmpty(message = "이메일은 필수항목입니다.")
    @NotBlank(message = "로그인 이메일은이 비어있습니다.")
    @Email
    private String email;
}
