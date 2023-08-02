package javaproject.project.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/Store")
@RequiredArgsConstructor
@Controller
public class StoreController {
    @GetMapping("/")
    public String store_Main(){
        return "Store/store";
    }
}
