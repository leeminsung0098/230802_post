package javaproject.project;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {

    @GetMapping("/h")
    @ResponseBody
    public String index() {
        return "redirect:/h2-console";
    }
//    @GetMapping("/")
//    public String root() {  //루트로 들어왓을때 아래경로로 redirect 해버림
//        return "redirect:/question/list";
//    }

    @GetMapping("/")
    public String root() {  //루트로 들어왓을때 아래경로로 redirect 해버림
        return "redirect:/UserBoard/main";
    }
    @GetMapping("/main")
    public String test(){
        return "redirect:/UserBoard/main";}

}