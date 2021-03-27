package com.example.demo.controller;

import com.example.demo.model.FileDealerCode;
import com.example.demo.service.FileSaveService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
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
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("admin/")
@CrossOrigin
public class FileDealController {
    @RequestMapping(value = "upload")
    public String upload(MultipartFile file, String dataname, String size, String value, String description) throws SocketException, IOException {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession(false);
        FileSaveService fileSaveService = new FileSaveService(session);
        try {
            fileSaveService.init(file);

        } catch (Exception e) {
            e.printStackTrace();
            return "初始化失败";
        }
        try {
            FileDealerCode code = fileSaveService.saveToLocal();
            if(code==FileDealerCode.DATA_ASSET_SAVE_FAILED){
                throw new Exception();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "保存本地失败";
        }

        try {
            FileDealerCode code = fileSaveService.saveDataAsset(value);
            if(code==FileDealerCode.DATA_ASSET_SAVE_FAILED){
                throw  new  Exception();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "保存数据资产失败";
        }

        try {
            FileDealerCode code = fileSaveService.saveDataInfo(dataname,size,description);
            if(code==FileDealerCode.DATA_INFO_SAVE_FAILED){
                throw new Exception();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "保存数据信息失败";
        }

        try{
            FileDealerCode code = fileSaveService.saveDataContent();
            if(code==FileDealerCode.DATA_CONTENT_SAVE_FAILED){
                throw new Exception();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "保存数据内容失败";
        }
        return "上传成功";
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
