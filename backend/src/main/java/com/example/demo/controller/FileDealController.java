package com.example.demo.controller;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
@RestController
@RequestMapping("admin/")
@CrossOrigin
public class FileDealController {
    @RequestMapping(value = "upload")
    public String upload(MultipartFile file) throws SocketException, IOException {
        String fileName = file.getOriginalFilename();
        String pathName = "./file/";
        File dir = new File(pathName);
        if(!dir.exists()){
            dir.mkdirs();
        }
        File checkFile = new File(pathName+fileName);
        //FileWriter writer = null;
        try(FileOutputStream fos = new FileOutputStream(checkFile)){
            if(!checkFile.exists()){
                checkFile.createNewFile();
            }
            //writer = new FileWriter(checkFile,true);
            //writer.write(file.getBytes());
            fos.write(file.getBytes());
            return "上传成功";
        }catch (Exception e){
            e.printStackTrace();
            return "上传失败";
        }
    }

    @RequestMapping(value = "testDownload")
    public String  download(HttpServletRequest request, HttpServletResponse response){
        String fileName = request.getParameter("fileName");
        String pathName = "./file/";
        System.out.println(fileName);
        //fileName = "STAR.txt";
        File file = new File(pathName+fileName);

        try(FileInputStream is = new FileInputStream(file)){
            //os = response.getOutputStream();
            //response.reset();
            response.setContentType("application/x-download;charset=GBK");
            //response.setHeader("Content-Disposition", "attachment;filename="+ new String(fileName.getBytes("utf-8"), "iso-8859-1"));
            IOUtils.copy(is,response.getOutputStream());
            response.getOutputStream().flush();
        } catch (IOException e) {
            e.printStackTrace();
            return "failed! file not exist";
        }
        return  null;
        //return "failed";
    }

}
