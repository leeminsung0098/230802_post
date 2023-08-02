package javaproject.project.Test;

import jakarta.persistence.criteria.*;
import javaproject.project.Entity.Board;
import javaproject.project.Entity.User_T;
import javaproject.project.Repository.BoardRepository;
import javaproject.project.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class testService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final BoardRepository boardRepository;


    public Page<Board> getList(int page, String kw , Integer boardMap) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 20, Sort.by(sorts));
        Specification<Board> spec = board_Search(kw,boardMap);
        return this.boardRepository.findAll(spec,pageable);
//        아래는 쿼리문을 사용할경우의 반환문 위는 일반 자바 코드
//        return this.questionRepository.findAllByKeyword(kw, pageable);
    }
    public Page<Board> getList(int page, String kw, Integer boardMap, String attribute, boolean sortAscending) {
        List<Sort.Order> sorts = new ArrayList<>();
//        int a = paging.getContent().get(0).getVoter().toArray().length;
        Pageable pageable = PageRequest.of(page, 20);
        if (sortAscending) {
            return this.boardRepository.findByOrderByVoterDesc(pageable);
        } else {
            return this.boardRepository.findByOrderByVoterAsc(pageable);
        }
//        System.out.println("정상작동");

//        Specification<Board> spec = board_Search(kw, boardMap);

    }
    private Specification<Board> board_Search(String kw ,Integer boardMap) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;
            @Override
            //root는 기본 보드, 쿼리문없을때도 있음이란 표시,
            public Predicate toPredicate(Root<Board> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Board bt= new Board();
                User_T tt= new User_T();


                query.distinct(true);  // 중복을 제거
                Join<Board, User_T> u1 = q.join("author", JoinType.LEFT);//테이블생성 (left조인)
                return cb.and(cb.equal(q.get("boardMap"),boardMap),
                        cb.or(cb.like(q.get("title"), "%" + kw + "%"), // 제목
                                cb.like(q.get("content"), "%" + kw + "%"),      // 내용
                                cb.like(u1.get("nickname"), "%" + kw + "%")));// 질문 작성자
                //보드맵
            }
        };
    }
}
