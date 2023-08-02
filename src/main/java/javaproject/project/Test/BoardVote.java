package javaproject.project.Test;

import jakarta.persistence.*;
import javaproject.project.Entity.Comment;
import javaproject.project.Entity.User_T;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class BoardVote {
    private Integer id;
    private String title;
    private String content;
    private Integer boardMap ;
    private Integer views;
    private LocalDateTime createDate;
    private List<Comment> commentList;
    private User_T author; //
    private LocalDateTime modifyDate;
    private Integer voter;
}
