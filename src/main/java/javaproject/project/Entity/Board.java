package javaproject.project.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

//게시판에 들어가야할 내용
// 제목   title
// 보드쪽내용 content
// 저자에 대한 내용 ManyToOne
// 댓글에 대한 내용 OneToMany
//
@Getter
@Setter
@Entity
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 150)
    private String title;
    @Column(columnDefinition = "TEXT")
    private String content;

    //보드맵은 게시판마다 분류하기위한 용도
    //1_1= 유저게시판의리뷰         2_1 사업자 부분공사
    //1_2 = 유저게시판의 질문       2_2 사업자 전체공사
    //1_3 = 유저게시판의 팁
    //1_4 = 유저게시판의 자유게시판
    @Column()
    private Integer boardMap ;

    @Column()
    private Integer views;


    @CreatedDate
    private LocalDateTime createDate;

//    댓글추가
    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private List<Comment> commentList;

    @ManyToOne
    private User_T author; //
    private LocalDateTime modifyDate;

    @ManyToMany
    Set<User_T> voter;


}
