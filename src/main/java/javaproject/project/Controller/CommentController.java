package javaproject.project.Controller;

import jakarta.validation.Valid;
import javaproject.project.CreateForm.CommentForm;
import javaproject.project.Entity.Board;
import javaproject.project.Entity.Comment;
import javaproject.project.Entity.User_T;
import javaproject.project.Service.BoardService;
import javaproject.project.Service.CommentService;
import javaproject.project.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RequestMapping("/Comment")
@RequiredArgsConstructor
@Controller
public class CommentController {
    private final BoardService boardService;
    private final CommentService commentService;
    private final UserService userService;


    //    댓글 DB 생성에 관한 내용    시작
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}")
    public String createComment(Model model,
                               @PathVariable("id") Integer id,
                               @Valid CommentForm commentForm,
                               BindingResult bindingResult,
                               @RequestParam(value="page", defaultValue="0") int page,
                               Principal principal) {

        Board board = this.boardService.getBoard(id);
        User_T user = this.userService.getUser(principal.getName());// 계정가져옴
//        댓글을 만들기위해서 필요한건  Comment 자체 하나
        Page<Comment> paging_Comment=this.commentService.getList(page,board);

        if (bindingResult.hasErrors()) {//실패 할경우 다시 패이징을 반환하여 처리
            model.addAttribute("board", board);
            model.addAttribute("paging_Comment",paging_Comment);
            return "UserBoard/free_detail";
        }

        Comment comment = this.commentService.create(board,
                commentForm.getContent(), user);
        return String.format("redirect:/UserBoard/free_Board_Detail/%s#comment_%s",
                comment.getBoard().getId(),
                comment.getId());
    }

//    댓글DB 생성에 관한 내용    끝

    //    댓글DB 삭제에 관한 내용    시작
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String answerDelete(Principal principal, @PathVariable("id") Integer id) {

        Comment comment= this.commentService.getComment(id);
        if (!comment.getAuthor().getLoginId().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.commentService.delete(comment);
        return String.format("redirect:/UserBoard/free_Board_Detail/%s#scrollbottom", comment.getBoard().getId());
    }
    //   댓글DB 삭제에 관한 내용    끝


//    //    AnswerDB 수정에 관한 내용    시작
//    @PreAuthorize("isAuthenticated()")
//    @GetMapping("/modify/{id}")
//    public String answerModify(AnswerForm answerForm, @PathVariable("id") Integer id, Principal principal) {
//        Answer answer = this.answerService.getAnswer(id);
//        if (!answer.getAuthor().getUsername().equals(principal.getName())) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
//        }
//        answerForm.setContent(answer.getContent());
//        return "answer/answer_form";
//    }
//    @PreAuthorize("isAuthenticated()")
//    @PostMapping("/modify/{id}")
//    public String answerModify(@Valid AnswerForm answerForm, BindingResult bindingResult,
//                               @PathVariable("id") Integer id, Principal principal)
//    {
//        if (bindingResult.hasErrors()) {
//            return "answer/answer_form";
//        }
//        Answer answer = this.answerService.getAnswer(id);
//        if (!answer.getAuthor().getUsername().equals(principal.getName())) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
//        }
//        this.answerService.modify(answer, answerForm.getContent());
//        return String.format("redirect:/question/detail/%s#answer_%s",
//                answer.getQuestion().getId(), answer.getId());
//    }
//    //    AnswerDB 수정에 관한 내용 끝


//
//    @PreAuthorize("isAuthenticated()")
//    @GetMapping("/vote/{id}")
//    public String answerVote(Principal principal, @PathVariable("id") Integer id) {
//        Answer answer = this.answerService.getAnswer(id);
//        SiteUser siteUser = this.userService.getUser(principal.getName());
//        this.answerService.vote(answer, siteUser);
//        return String.format("redirect:/question/detail/%s#answer_%s",
//                answer.getQuestion().getId(), answer.getId());
//    }

}