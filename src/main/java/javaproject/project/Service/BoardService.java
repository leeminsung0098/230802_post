package javaproject.project.Service;

import jakarta.persistence.criteria.*;
import javaproject.project.Utill.DataNotFoundException;
import javaproject.project.Entity.Board;
import javaproject.project.Entity.User_T;
import javaproject.project.Repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BoardService {
    private  final BoardRepository boardRepository;

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
    private Specification<Board> board_Search(String kw ) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;
            @Override
            //root는 기본 보드, 쿼리문없을때도 있음이란 표시,
            public Predicate toPredicate(Root<Board> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Board bt= new Board();
                User_T tt= new User_T();


                query.distinct(true);  // 중복을 제거
                Join<Board, User_T> u1 = q.join("author", JoinType.LEFT);//테이블생성 (left조인)
                return cb.or(cb.like(q.get("title"), "%" + kw + "%"), // 제목
                        cb.like(q.get("content"), "%" + kw + "%"),      // 내용
                        cb.like(u1.get("nickname"), "%" + kw + "%"));   // 질문 작성자
            }
        };
    }
    //조회수 증감 함수시작
    public void boardViewsCount(Board board){
        board.setViews(board.getViews()+1);
        this.boardRepository.save(board);
    }
    //조회수 증감 함수 끝
    public Page<Board> getList(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 20, Sort.by(sorts));
        Specification<Board> spec = board_Search(kw);
        return this.boardRepository.findAll(spec,pageable);
//        아래는 쿼리문을 사용할경우의 반환문 위는 일반 자바 코드
//        return this.questionRepository.findAllByKeyword(kw, pageable);
    }
    public Page<Board> getList(int page, String kw ,Integer boardMap) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 20, Sort.by(sorts));
        Specification<Board> spec = board_Search(kw,boardMap);
        return this.boardRepository.findAll(spec,pageable);
//        아래는 쿼리문을 사용할경우의 반환문 위는 일반 자바 코드
//        return this.questionRepository.findAllByKeyword(kw, pageable);
    }
    // === 유저보드 정렬 구현 시작
    public Page<Board> getList(int page, String kw, Integer boardMap, String attribute, boolean sortAscending) {
        List<Sort.Order> sorts = new ArrayList<>();

        Specification<Board> spec = board_Search(kw, boardMap);
        if (sortAscending) {
            sorts.add(Sort.Order.asc(attribute)); // 오름차순 정렬
        } else {
            sorts.add(Sort.Order.desc(attribute)); // 내림차순 정렬
        }


        Pageable pageable = PageRequest.of(page, 20, Sort.by(sorts));
        //이부분은 사실상 기능안함 현제로서는
        if (attribute.equals("voter")){//추천수를 할경우 솔트를 제거해야함  order에서 처리하기때문
            pageable = PageRequest.of(page, 20);
            if (sortAscending) {
                return this.boardRepository.findAllByOrderByVoterAsc(pageable);//오름차
            } else {
                return this.boardRepository.findAllByOrderByVoterDesc(pageable);//내림차
            }
        }
        return this.boardRepository.findAll(spec, pageable);
    }

    // === 유저보드 정렬 구현 끝
    public Board create(String title,
                        String content,
                        User_T author,
                        Integer boardMap) {

        Board board = new Board();
        board.setContent(content);
        board.setTitle(title);
        board.setCreateDate(LocalDateTime.now());
        board.setAuthor(author);
        board.setBoardMap(boardMap);
        board.setViews(0);
        this.boardRepository.save(board);
        return board;
    }
    //보드정보를 가지고오기위한 함수
    public Board getBoard(Integer id) {
        Optional<Board> board = this.boardRepository.findById(id);
        if (board.isPresent()) {
            return board.get();
        } else {
            throw new DataNotFoundException("board not found");
        }
    }

    //수정
    public void modify(Board board, String title, String content) {
        board.setTitle(title);
        board.setContent(content);
        board.setModifyDate(LocalDateTime.now());
        this.boardRepository.save(board);
    }
    //삭제
    public void delete(Board board) {
        this.boardRepository.delete(board);
    }
//    추천수 카운트
    public void vote(Board board, User_T user) {
        board.getVoter().add(user);
        this.boardRepository.save(board);
    }


}
