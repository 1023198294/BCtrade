package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

@RequestMapping("/file")
@RestController
public class FileDealController {

    @RequestMapping(value = "upload")
    public String upload(MultipartFile file) throws SocketException, IOException {
        String fileName = file.getOriginalFilename();
        String pathName = "./file/"+fileName;
        // 在file文件夹中创建名为fileName的文件
        //OutputStreamWriter op = new OutputStreamWriter(new FileOutputStream("./file/" + fileName), StandardCharsets.UTF_8);
        // 获取文件输入流
        //InputStreamReader inputSt
        // reamReader = new InputStreamReader(file.getInputStream());
        //char[] bytes = new char[12];
        // 如果这里的bytes不是数组，则每次只会读取一个字节，例如test会变成 t   e     s    t
        //while (inputStreamReader.read(bytes) != -1){
        //    op.write(bytes);
        //}
        // 关闭输出流
        //op.close();
        // 关闭输入流
        //inputStreamReader.close();
        FileOutputStream fos = null;
        try{
            fos = new FileOutputStream(pathName);
            fos.write(file.getBytes());
            return "上传成功";
        }catch (Exception e){
            e.printStackTrace();
            return "上传失败";
        }finally {
            try{fos.close();}catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
