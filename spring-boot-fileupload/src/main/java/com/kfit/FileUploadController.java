package com.kfit;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

/**
 * @Description
 * @Author lujichao
 * @Date 2019/2/13
 */
@Controller
public class FileUploadController {
    /**
     * 访问路径:http://127.0.0.1:8080/file
     *
     * @return
     */
    @RequestMapping("/file")
    public String file() {
        return "/file";
    }

    /**
     * 文件上传的具体实现方法
     *
     * @param file
     * @return
     */
    @RequestMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(new File(file.getOriginalFilename())));
                outputStream.write(file.getBytes());
                outputStream.flush();
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
                return "上传失败" + e.getMessage();
            }
            return "上传成功";
        } else {
            return "上传失败,因为文件是空的";
        }
    }

    @RequestMapping("/multifile")
    public String multifile() {
        return "/multifile";
    }

    /**
     * 多文件具体上传时间，主要是使用了MultipartHttpServletRequest和MultipartFile
     *
     * @param request
     * @return
     */
    @RequestMapping("/batch/upload")
    public String handleFileUpload(HttpServletRequest request) {
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
        MultipartFile file = null;
        BufferedOutputStream stream = null;
        for (int i = 0; i < files.size(); ++i) {
            file = files.get(i);
            if (!file.isEmpty()) {
                try {
                    byte[] bytes = file.getBytes();
                    stream = new BufferedOutputStream(new FileOutputStream(new File(file.getOriginalFilename())));
                    stream.write(bytes);
                    stream.close();

                } catch (Exception e) {
                    stream = null;
                    return "You failed to upload" + i + "=>" + e.getMessage();
                }
            } else {
                return "You failed to upload" + i + "because the file was empty.";
            }
        }
        return "upload successful";
    }
}

