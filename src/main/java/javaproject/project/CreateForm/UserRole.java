package javaproject.project.CreateForm;
import lombok.Getter;


@Getter
public enum UserRole {
//    로그인 할때 아이디 가 DB에 일치하면 어드민이이랑 유저 구분해서 로그인하는거임 덕환코드
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    UserRole(String value) {
        this.value = value;
    }

    private String value;
}
