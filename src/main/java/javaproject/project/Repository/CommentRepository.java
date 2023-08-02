package javaproject.project.Repository;

import javaproject.project.Entity.Board;
import javaproject.project.Entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CommentRepository extends JpaRepository<Comment, Integer>,
        JpaSpecificationExecutor<Comment> {
       Page<Comment> findAllByBoard(Board board , Pageable pageable);


}
