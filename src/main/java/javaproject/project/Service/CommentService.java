package javaproject.project.Service;


import jakarta.persistence.criteria.*;
import javaproject.project.Entity.Board;
import javaproject.project.Entity.Comment;
import javaproject.project.Entity.User_T;
import javaproject.project.Repository.CommentRepository;
import javaproject.project.Utill.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;

    private Specification<Comment> comment_search(Board board ) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;
            @Override
            //root는 기본 보드, 쿼리문없을때도 있음이란 표시,
            public Predicate toPredicate(Root<Comment> q, CriteriaQuery<?> query, CriteriaBuilder cb) {

                query.distinct(true);  // 중복을 제거
                Join<Board, User_T> u1 = q.join("author", JoinType.LEFT);//테이블생성 (left조인)
                return cb.equal(q.get("board"),board);
            }
        };
    }
    public Page<Comment> getList(int page, Board board){
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 5, Sort.by(sorts));
        Specification<Comment> spec = comment_search(board);
        return this.commentRepository.findAll(spec,pageable);
    }
    public Comment getComment(int id){
        Optional<Comment> comment = this.commentRepository.findById(id);
        if (comment.isPresent()) {
            return comment.get();
        } else {
            throw new DataNotFoundException("board not found");
        }
    }

    public Comment create(Board board,
                          String content,
                          User_T author) {
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setCreateDate(LocalDateTime.now());
        comment.setBoard(board);
        comment.setAuthor(author);
        this.commentRepository.save(comment);
        return comment;
    }
    public void modify(Comment comment, String content) {
        comment.setContent(content);
        comment.setModifyDate(LocalDateTime.now());
        this.commentRepository.save(comment);
    }


    public void delete(Comment comment) {
        this.commentRepository.delete(comment);
    }
    public void vote(Comment comment, User_T user) {
        comment.getVoter().add(user);
        this.commentRepository.save(comment);
    }

}

