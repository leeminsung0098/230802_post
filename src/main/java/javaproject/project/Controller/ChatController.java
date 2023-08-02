package javaproject.project.Controller;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Log4j2
public class ChatController {

    @GetMapping("/chat")//이부분 수정해야함
    public String chatGET(){
        log.info("@ChatController, chat GET()");
        return "chat";
    }
}