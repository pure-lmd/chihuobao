package com.example.controller;

import cn.hutool.core.io.FileUtil;
import com.example.common.Result;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;

@RestController
@RequestMapping("/files")
public class FileController {

    private static final String filePath = System.getProperty("user.dir") + "/files/";

    @PostMapping("/upload")
    public Result upload(MultipartFile file) {
        String fileName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
        String realFilePath = filePath + fileName;
        try {
            if (!FileUtil.isDirectory(filePath)) {
                FileUtil.mkdir(filePath);
            }
            FileUtil.writeBytes(file.getBytes(), realFilePath);
        } catch (IOException e) {
            System.out.println("File upload error");
        }
        String url = "/files/download/" + fileName;
        return Result.success(url);
    }

    @GetMapping("/download/{fileName}")
    public void download(@PathVariable String fileName, HttpServletResponse response) {
        try {
            String realFilePath = filePath + fileName;
            byte[] bytes = FileUtil.readBytes(realFilePath);
            // 设置Content-Type为图片类型，让浏览器直接显示
            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
            String contentType = "image/" + (suffix.equals("jpg") ? "jpeg" : suffix);
            response.setContentType(contentType);
            ServletOutputStream os = response.getOutputStream();
            os.write(bytes);
            os.flush();
            os.close();
        } catch (IOException e) {
            System.out.println("File download error");
        }
    }
}
