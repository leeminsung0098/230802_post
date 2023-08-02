package javaproject.project.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.Set;


@Getter
@Setter
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Integer id;
    @Column(columnDefinition = "TEXT")
    private  String content;
    @CreatedDate
    private LocalDateTime createDate;
    @ManyToOne
    private Board board;//
    @ManyToOne
    private User_T author; //
    private LocalDateTime modifyDate;

    @ManyToMany
    Set<User_T> voter;
}
