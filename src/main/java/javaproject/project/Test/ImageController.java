package javaproject.project.Test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
public class ImageController {

    @PostMapping("/ajax/saveimage")
    public String saveImage(@RequestParam("file") MultipartFile file) {
        String name = randomString();
        String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.'));
        String filename = name + ext;
        String destination = "/home/xxx/public_html/images/" + filename;
        try {
            File destinationFile = new File(destination);
            file.transferTo(destinationFile);
            return "http://xxx.com/images/" + filename;
        } catch (IOException e) {
            e.printStackTrace();
            // 파일 업로드 실패 처리
            return "Error during file upload";
        }
    }

    private String randomString() {
        return UUID.randomUUID().toString();
    }
}