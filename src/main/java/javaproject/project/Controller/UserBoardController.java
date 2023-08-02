package javaproject.project.Controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import javaproject.project.CreateForm.BoardCreateForm;
import javaproject.project.CreateForm.CommentForm;
import javaproject.project.Entity.Comment;
import javaproject.project.Entity.User_T;
import javaproject.project.Service.BoardService;
import javaproject.project.Entity.Board;
import javaproject.project.Service.CommentService;
import javaproject.project.Service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

//보드맵은 게시판마다 분류하기위한 용도
//11= 유저게시판의리뷰         21 사업자 부분공사
//12 = 유저게시판의 질문       22 사업자 전체공사
//13 = 유저게시판의 팁
//14 = 유저게시판의 자유게시판
@RequestMapping("/UserBoard")
@RequiredArgsConstructor
@Controller
@Log4j2
public class UserBoardController {
    private final BoardService boardService;
    private final CommentService commentService;
    private final UserService userService;

    @GetMapping("/main")
    public String userBoardMain() {

        return "UserBoard/Main";
    }


    // ===자유 게시판 시작===
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create_Board_Free")
//    주의; BoardCreateForm를 받는쪽에서 생성하지않으면 html에서 BoardCreateForm는 없는 객체 처리가됨 주의
    public String showCreateBoardForm(BoardCreateForm boardCreateForm) {
//        System.out.println("접속은성공?");
        return "Create/User/Create_Board_free";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create_Board_Free")
    public String boardCreateForm(@Valid BoardCreateForm boardCreateForm,
                                  BindingResult bindingResult,
                                  Principal principal) {
        if (bindingResult.hasErrors()) {
            return "Create/user/create_Board_Free";
        }
        System.out.println("아이디 : " + principal.getName());
        User_T user = this.userService.getUser(principal.getName());
        this.boardService.create(
                boardCreateForm.getTitle(),
                boardCreateForm.getContent(),
                user, 14);
        return "redirect:/UserBoard/free_Board";
    }

    //    session은 모델과 달리 변수가 정적으로 페이지에 존재함으로 재사용이 가능
    @GetMapping("/free_Board")//자유게시판부터작업
    public String userBoardFreeBoard(Model model,
                                     HttpSession session,
                                     @RequestParam(value = "page", defaultValue = "0") int page,
                                     @RequestParam(value = "kw", defaultValue = "") String kw,
                                     @RequestParam(value = "attribute", defaultValue = "createDate") String attribute) {

        boolean sortAscending = true;
        Boolean previousSortAscending = (Boolean) session.getAttribute("sortAscending");

        if (previousSortAscending != null) {
            sortAscending = !previousSortAscending; // 이전 정렬 방식을 토글합니다.
        }

        Page<Board> paging = this.boardService.getList(page, kw, 14, attribute, sortAscending);

        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);
        model.addAttribute("attribute", attribute);
        model.addAttribute("sortAscending", sortAscending);
        session.setAttribute("sortAscending", sortAscending); // 세션에 현재 정렬 방식을 저장합니다.

        return "UserBoard/free";
    }

    //commentForm 댓글을 작성하기위한 폼
    @GetMapping("/free_Board_Detail/{id}")
    public String userBoardFreeBoardDetail(
            Model model,
            CommentForm commentForm,
            @PathVariable("id") Integer id,
            @RequestParam(value = "page", defaultValue = "0") int page) {

        Board board = this.boardService.getBoard(id);
        Page<Comment> paging_Comment = this.commentService.getList(page, board);
//        조회수 늘리기위한 함수추가
        this.boardService.boardViewsCount(board);

        model.addAttribute("board", board);
        model.addAttribute("paging_Comment", paging_Comment);
        //댓글관련 후에 추가해야함
        return "UserBoard/free_detail";
    }

    //자유게시판 수정기능
    // ===자유 게시판 끝===
    // ===질문 게시판===
    //질문게시판 목록
    @GetMapping("/question")
    public String user_Board_Question(Model model,
                                      HttpSession session,
                                      @RequestParam(value = "page", defaultValue = "0") int page,
                                      @RequestParam(value = "kw", defaultValue = "") String kw,
                                      @RequestParam(value = "attribute", defaultValue = "createDate") String attribute) {
        boolean sortAscending = true;
        Boolean previousSortAscending = (Boolean) session.getAttribute("sortAscending");

        if (previousSortAscending != null) {
            sortAscending = !previousSortAscending; // 이전 정렬 방식을 토글합니다.
        }

        Page<Board> paging = this.boardService.getList(page, kw, 12, attribute, sortAscending);


        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);
        model.addAttribute("attribute", attribute);
        model.addAttribute("sortAscending", sortAscending);
        session.setAttribute("sortAscending", sortAscending); // 세션에 현재 정렬 방식을 저장합니다.
        return "UserBoard/question";
    }

    // 질문게시판 디테일
    @GetMapping("/question_Detail/{id}")
    public String userBoardQuestionDetail(Model model,
                                          CommentForm commentForm,
                                          @PathVariable("id") Integer id,
                                          @RequestParam(value = "page", defaultValue = "0") int page) {
        Board board = this.boardService.getBoard(id);
        Page<Comment> paging_Comment = this.commentService.getList(page, board);
//        조회수 늘리기위한 함수추가
        this.boardService.boardViewsCount(board);

        model.addAttribute("board", board);
        model.addAttribute("paging_Comment", paging_Comment);
        return "UserBoard/question_detail";
    }

    //질문 게시판 게시물작성 기능
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create_board_question")
    public String boardCreateFormQuestion(BoardCreateForm boardCreateForm) {
        return "Create/User/Create_Board_Question";
    }

    // 질문게시판 게시물 작성
    @PostMapping("/create_board_question")
    public String boardCreateFormQuestion(@Valid BoardCreateForm boardCreateForm,
                                          BindingResult bindingResult,
                                          Principal principal) {

        if (bindingResult.hasErrors()) {
            return "create_Board_Question";
        }
        User_T user = this.userService.getUser(principal.getName());
        this.boardService.create(
                boardCreateForm.getTitle(),
                boardCreateForm.getContent(),
                user, 12);
        return "redirect:/UserBoard/question";
    }
    // ===질문 게시판 끝===


    // ===리뷰 게시판 시작===
    //리뷰게시판 목록
    @GetMapping("/review")
    public String userBoardReview(Model model,
                                  HttpSession session,
                                  @RequestParam(value = "page", defaultValue = "0") int page,
                                  @RequestParam(value = "kw", defaultValue = "") String kw,
                                  @RequestParam(value = "attribute", defaultValue = "createDate") String attribute) {
        boolean sortAscending = true;
        Boolean previousSortAscending = (Boolean) session.getAttribute("sortAscending");

        if (previousSortAscending != null) {
            sortAscending = !previousSortAscending; // 이전 정렬 방식을 토글합니다.
        }

        Page<Board> paging = this.boardService.getList(page, kw, 11, attribute, sortAscending);

        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);
        model.addAttribute("attribute", attribute);
        model.addAttribute("sortAscending", sortAscending);
        session.setAttribute("sortAscending", sortAscending); // 세션에 현재 정렬 방식을 저장합니다.
        return "UserBoard/review";
    }

    // 리뷰게시판 디테일
    @GetMapping("/review_Detail/{id}")
    public String userBoardReviewDetail(Model model,
                                        CommentForm commentForm,
                                        @PathVariable("id") Integer id,
                                        @RequestParam(value = "page", defaultValue = "0") int page) {
        Board board = this.boardService.getBoard(id);
        Page<Comment> paging_Comment = this.commentService.getList(page, board);
//        조회수 늘리기위한 함수추가
        this.boardService.boardViewsCount(board);

        model.addAttribute("board", board);
        model.addAttribute("paging_Comment", paging_Comment);
        return "UserBoard/review_detail";
    }


    //리뷰게시판 게시물 작성기능
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create_board_review")
    public String boardCreateFormReview(BoardCreateForm boardCreateForm) {
        return "Create/User/Create_Board_Review";
    }

    // 리뷰게시판 게시물 작성
    @PostMapping("/create_board_review")
    public String boardCreateForm_review(@Valid BoardCreateForm boardCreateForm,
                                         BindingResult bindingResult,
                                         Principal principal) {

        if (bindingResult.hasErrors()) {
            return "create_Board_Review";
        }
        User_T user = this.userService.getUser(principal.getName());
        this.boardService.create(
                boardCreateForm.getTitle(),
                boardCreateForm.getContent(),
                user, 11);
        return "redirect:/UserBoard/review";
    }
    // ===리뷰 게시판 끝===

    //    ===성향태스트 시작====
    @GetMapping("/tendency")
    public String userBoardTendency() {
        return "UserBoard/tendency";
    }

    @GetMapping("/tendency_Detail")
    public String userBoardTendencyDetail() {
        return "UserBoard/tendency_detail";
    }
//   ====성향테스트 끝====


    // ===팁 게시판 시작===
    //팁게시판 목록
    @GetMapping("/tip")
    public String user_Board_Tip(Model model,
                                 HttpSession session,
                                 @RequestParam(value = "page", defaultValue = "0") int page,
                                 @RequestParam(value = "kw", defaultValue = "") String kw,
                                 @RequestParam(value = "attribute", defaultValue = "createDate") String attribute) {
        boolean sortAscending = true;
        Boolean previousSortAscending = (Boolean) session.getAttribute("sortAscending");

        if (previousSortAscending != null) {
            sortAscending = !previousSortAscending; // 이전 정렬 방식을 토글합니다.
        }

        Page<Board> paging = this.boardService.getList(page, kw, 13, attribute, sortAscending);

        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);
        model.addAttribute("attribute", attribute);
        model.addAttribute("sortAscending", sortAscending);
        session.setAttribute("sortAscending", sortAscending); // 세션에 현재 정렬 방식을 저장합니다.
        return "UserBoard/tip";
    }

    // 팁 게시판 디테일
    @GetMapping("/tip_Detail/{id}")
    public String user_Board_Tip_Detail(Model model,
                                        CommentForm commentForm,
                                        @PathVariable("id") Integer id,
                                        @RequestParam(value = "page", defaultValue = "0") int page) {
        Board board = this.boardService.getBoard(id);
        Page<Comment> paging_Comment = this.commentService.getList(page, board);
//        조회수 늘리기위한 함수추가
        this.boardService.boardViewsCount(board);

        model.addAttribute("board", board);
        model.addAttribute("paging_Comment", paging_Comment);
        return "UserBoard/tip_detail";
    }

    //팁게시판 게시물 작성기능
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create_board_tip")
    public String boardCreateForm_Tip(BoardCreateForm boardCreateForm) {
        return "Create/User/Create_Board_Tip";
    }

    @PostMapping("/create_board_tip")
    public String boardCreateForm_Tip(@Valid BoardCreateForm boardCreateForm,
                                      BindingResult bindingResult,
                                      Principal principal) {

        if (bindingResult.hasErrors()) {
            return "create_Board_Tip";
        }
        User_T user = this.userService.getUser(principal.getName());
        this.boardService.create(
                boardCreateForm.getTitle(),
                boardCreateForm.getContent(),
                user, 13);
        return "redirect:/UserBoard/tip";
    }

    // ===팁 게시판 끝==
    //    추천 맵핑 추가시작
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String BoardVote(Principal principal, @PathVariable("id") Integer id) {
        Board board = this.boardService.getBoard(id);
        User_T user = this.userService.getUser(principal.getName());
        String path = "";
        switch (board.getBoardMap()) {
            case 11:
                path = "review_Detail";
                break;
            case 12:
                path = "question_Detail";
                break;
            case 13:
                path = "tip_Detail";
                break;
            case 14:
                path = "free_Board_Detail";
                break;
        }
        this.boardService.vote(board, user);

        return String.format("redirect:/UserBoard/%s/%s", path, id);
//        return String.format("redirect:/UserBoard/free_Board_Detail/%s", id);

    }
//    추천맵핑추가 끝

    //  === 유저보드데이터 삭제 시작
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String BoardDelete(Principal principal, @PathVariable("id") Integer id) {
        Board board = this.boardService.getBoard(id);
        String path = "";
        if (!board.getAuthor().getLoginId().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        switch (board.getBoardMap()) {
            case 11:
                path = "review";
                break;
            case 12:
                path = "question";
                break;
            case 13:
                path = "tip";
                break;
            case 14:
                path = "free_Board";
                break;
        }
        this.boardService.delete(board);
        return String.format("redirect:/UserBoard/%s", path);
    }
// === 유저보드데이터 삭제 끝

// === 유저보드데이터 수정 시작

    @PreAuthorize("isAuthenticated()")
    @GetMapping("Board_Modify/{id}")
    public String modifyBoard(Model model, BoardCreateForm boardCreateForm, @PathVariable("id") Integer id, Principal principal) {
        Board board = this.boardService.getBoard(id);
        String path = "";
        // 수정권한확인 닉네임으로
        if (!board.getAuthor().getLoginId().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        switch (board.getBoardMap()) {
            case 11:
                path = "modify_Board_Review";
                break;
            case 12:
                path = "modify_Board_Question";
                break;
            case 13:
                path = "modify_Board_Tip";
                break;
            case 14:
                path = "modify_Board_Free";
                break;
        }
        // 수정된 내용을 board 객체에 반영
        boardCreateForm.setTitle(board.getTitle());
        boardCreateForm.setContent(board.getContent());

        return String.format("Modify/User/%s", path);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("Board_Modify/{id}")
    public String modifyBoard(@Valid BoardCreateForm boardCreateForm, BindingResult bindingResult,
                              Principal principal, @PathVariable("id") Integer id) {
        Board board = this.boardService.getBoard(id);
        String path = "";
        if (!board.getAuthor().getLoginId().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        switch (board.getBoardMap()) {
            case 11:
                path = "review_Detail";
                break;
            case 12:
                path = "question_Detail";
                break;
            case 13:
                path = "tip_Detail";
                break;
            case 14:
                path = "free_Board_Detail";
                break;
        }
        this.boardService.modify(board, boardCreateForm.getTitle(), boardCreateForm.getContent());

        return String.format("redirect:/UserBoard/%s/%s", path, id);
    }
// === 유저보드 데이터 수정 끝


}
