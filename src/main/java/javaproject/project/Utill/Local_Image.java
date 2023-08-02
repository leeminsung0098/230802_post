package javaproject.project.Utill;


import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@RestController
public class Local_Image {
    @GetMapping(value="/image/view", produces= MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody byte[] getImage(
            @RequestParam("file_name") String file_name) // A
            throws IOException {
        FileInputStream fis = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        파일 저장용 경로코드
//                String folder = request.getServletContext().getRealPath("/");
//                btn_google.png
//                http://localhost:8081/image/view?file_name=btn_google_click
//                http://localhost:8081/image/view?file_name=btn_google_hover
//                http://localhost:8081/image/view?file_name=btn_google_nomal
//                C:\project\src\main\resources\static\image\btn_google_click.png
//        String fileDir = "C:\\project\\src\\main\\resources\\static\\image\\" + file_name + ".png"; // 파일경로
        String fileDir = "src\\main\\resources\\static\\image\\" + file_name; // 파일경로
        try{
            fis = new FileInputStream(fileDir);
        } catch(FileNotFoundException e){
            e.printStackTrace();
        }

        int readCount = 0;
        byte[] buffer = new byte[1024];
        byte[] fileArray = null;

        try{
            while((readCount = fis.read(buffer)) != -1){
                baos.write(buffer, 0, readCount);
            }
            fileArray = baos.toByteArray();
            fis.close();
            baos.close();
        } catch(IOException e){
            throw new RuntimeException("File Error");
        }
        return fileArray;
    }
}