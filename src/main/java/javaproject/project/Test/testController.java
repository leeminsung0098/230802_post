package javaproject.project.Test;

import jakarta.servlet.http.HttpSession;
import javaproject.project.Entity.Board;
import javaproject.project.Entity.User_T;
import javaproject.project.Service.BoardService;
import javaproject.project.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RequestMapping("/test")
@RequiredArgsConstructor
@Controller
public class testController {

    private final UserService userService;
    private final BoardService boardService;
    private final testService testService;

    @GetMapping("/t1")
    public String userBoardMain(){
        return "UserBoard/Main";
    }
    
    @GetMapping ("/t2")
    public String t2(Model model,
                     HttpSession session,
                     @RequestParam(value = "attribute",defaultValue = "createDate") String attribute){
//        getBoardsWithVoterPaging();
        boolean sortAscending = true;
        Boolean previousSortAscending = (Boolean) session.getAttribute("sortAscending");

        if (previousSortAscending != null) {
            sortAscending = !previousSortAscending; // 이전 정렬 방식을 토글합니다.
        }
        Page<Board> paging = this.testService.getList(0, "", 14, attribute, sortAscending);
        session.setAttribute("sortAscending", sortAscending); // 세션에 현재 정렬 방식을 저장합니다.
        System.out.println("현제페이지겟수 : " +paging.get().toArray().length);
        model.addAttribute("paging",paging);
        List<Board> bs = paging.getContent();
        int i =0;
        BoardVote boardVote;
        for(Board b : paging)
            i++;
            System.out.println("추천수 : " +i);

        return "test/t2";
    }
    @GetMapping("/t3")
    public String userT3(){
        return "test/t3";
    }
    @RequestMapping(value ="/t4",method = RequestMethod.POST)
    @ResponseBody
    public String userT4(@RequestParam("file") MultipartFile multipartFile){
        System.out.println(multipartFile);
        return "test/t3";
    }

    @GetMapping ("/t5")
    public String userT5(){
        return "test/t5";
    }


//    public void getBoardsWithVoterPaging(    ) {
//        int page=0;
//        int size = 10;
//        Integer boardId = 0;
//        // 페이지 번호와 페이지 크기를 기반으로 Pageable 객체 생성
//        PageRequest pageable = PageRequest.of(page, size);
//
//        // BoardService의 메서드를 호출하여 페이징된 Board 엔티티 반환
//        Page<Board> paging= this.testService.getBoardsWithVoterPaging(boardId, pageable);
//        for(Board b : paging){
//           System.out.println(b.getViews() );
//
//        }
//    }

    
}
