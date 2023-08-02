package javaproject.project.Repository;


import jakarta.persistence.criteria.CriteriaBuilder;
import javaproject.project.Entity.Board;
import javaproject.project.Entity.User_T;
import javaproject.project.Test.BoardVote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository  extends JpaRepository<Board, Integer>,
        JpaSpecificationExecutor<Board>
{
    //    Book findByAuthor(String author);
    Page<Board> findAll(Pageable pageable);
    Page<Board> findAll(Specification<Board> spec, Pageable pageable);
//    Page<Board> findBy VoterAsc(Pageable pageable);
    Page<Board> findByOrderByVoterDesc(Pageable pageable);
    Page<Board>findByOrderByVoterAsc(Pageable pageable);

    Page<Board>findAllByOrderByVoterDesc(Pageable pageable);
    Page<Board>findAllByOrderByVoterAsc(Pageable pageable);
    Page<Board>findAllByBoardMap(Specification<Board> spec, Pageable pageable,Integer boardMap);
    long countBoardVotersById(Integer boardId);

//    @Query("select "
//            + "distinct b "
//            + "from Board b "
//            + "left outer join User_T u1 on b.author=u1 "
//            + "left outer join Comment c on c.question=q "
//            + "left outer join User_T u2 on c.author=u2 "
//            + "where "
//            + "   b.subject like %:kw% "
//            + "   or b.content like %:kw% "
//            + "   or u1.username like %:kw% "
//            + "   or c.content like %:kw% "
//            + "   or u2.username like %:kw% ")
//    Page<Board> findAllByKeyword(@Param("kw") String kw, Pageable pageable);

}
