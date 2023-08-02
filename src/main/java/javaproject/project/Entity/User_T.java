package javaproject.project.Entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Entity
@Component
public class User_T {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TEXT")
    private String nickname;    //로그인아이디와유사

    @Column(columnDefinition = "TEXT")
    private String password;

    @Column(columnDefinition = "TEXT" ,unique = true)
    private String loginId;


    @Column(columnDefinition = "TEXT",unique = true )
    private String email;
//    유저 구글 등록을위한 엔티티
    private String provider;
//    sub
    private String providerId;


    @Column(columnDefinition = "TEXT")
    private String businessman_num;

    @Column(columnDefinition = "TEXT")
    private String businessman_site;




}
