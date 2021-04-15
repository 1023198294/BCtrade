package com.example.demo.controller;

import com.example.demo.dao.DataContentMapper;
import com.example.demo.dao.DataInfoMapper;
import com.example.demo.dao.DataMapper;
import com.example.demo.model.DataAsset;
import com.example.demo.model.DataContent;
import com.example.demo.model.DataInfo;
import com.example.demo.model.FileDealerCode;
import com.example.demo.service.FileSaveService;
import com.example.demo.utils.AESUtil;
import com.example.demo.utils.DataJSON;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
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
import javax.xml.crypto.Data;
import java.io.*;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@RequestMapping("admin/")
@CrossOrigin
public class FileDealController {
    @RequestMapping(value = "upload")
    public String upload(MultipartFile file, String dataname,String dataRealName, String size, String value, String description) throws SocketException, IOException {
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
            String uid = (String) session.getAttribute("userId");
            if(!fileSaveService.checkClient(uid))
                return "该用户已被封禁，无法上传数据";
        } catch (Exception e) {
            e.printStackTrace();
            return "查询用户合法性失败";
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
            FileDealerCode code = fileSaveService.saveDataInfo(dataname,size,description,value);
            if(code==FileDealerCode.DATA_INFO_SAVE_FAILED){
                throw new Exception();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "保存数据信息失败";
        }

        try{
            FileDealerCode code = fileSaveService.saveDataContent(dataRealName);
            if(code==FileDealerCode.DATA_CONTENT_SAVE_FAILED){
                throw new Exception();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "保存数据内容失败";
        }
        return "上传成功";
    }

    @RequestMapping(value = "upload2")
    public String upload2(MultipartFile file, String dataname,String dataRealName, String size, String value, String description,String rate,String creatorId,String originalId) throws SocketException, IOException {
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
            String uid = (String) session.getAttribute("userId");
            if(!fileSaveService.checkClient(uid))
                return "该用户已被封禁，无法上传数据";
        } catch (Exception e) {
            e.printStackTrace();
            return "查询用户合法性失败";
        }

        try {
            FileDealerCode code = fileSaveService.saveDataAsset2(value,creatorId,originalId,rate);
            if(code==FileDealerCode.DATA_ASSET_SAVE_FAILED){
                throw  new  Exception();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "保存数据资产失败";
        }

        try {
            FileDealerCode code = fileSaveService.saveDataInfo(dataname,size,description,value);
            if(code==FileDealerCode.DATA_INFO_SAVE_FAILED){
                throw new Exception();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "保存数据信息失败";
        }

        try{
            FileDealerCode code = fileSaveService.saveDataContent(dataRealName);
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
    @RequestMapping(value = "getRealName")
    public String getRealName(String dataid) throws IOException {
        String resource = "SqlMapConfig.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession session = factory.openSession();
        DataContentMapper dataContentMapper = session.getMapper(DataContentMapper.class);
        try {
            String dataRealName = dataContentMapper.getDataRealNameById(dataid);
            System.out.println(dataRealName);
            return dataRealName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @RequestMapping(value = "Download")
    public String downloadById(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Subject subject = SecurityUtils.getSubject();
        Session webSession = subject.getSession(false);
        String userId = (String) webSession.getAttribute("userId");
        if(userId==null){
            //非法请求
            return null;
        }
        String resource = "SqlMapConfig.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession session = factory.openSession();
        String dataId = request.getParameter("dataId");
        System.out.println("downloadById#request DataId:"+dataId);
        System.out.println("downloadById#from user"+userId);
        DataContentMapper dataContentMapper = session.getMapper(DataContentMapper.class);
        DataContent dataContent= dataContentMapper.getDataContentById(dataId);
        //System.out.println("downloadById#request DataId:"+dataContent);
        DataInfoMapper dataInfoMapper = session.getMapper(DataInfoMapper.class);
        DataMapper dataMapper = session.getMapper(DataMapper.class);
        DataInfo dataInfo = dataInfoMapper.getDataInfoById(dataId);
        byte[] key = dataInfo.getKey();
        //byte[] content = dataContent.getContentByte();
        String fileName = dataContent.getName();
            if(!subject.hasRole("admin")){
            try {
                DataAsset dataAsset = dataMapper.getDataByOriginDataIdAndOwnerId(dataId,userId);
                System.out.println("downloadById#data's ownerId:"+dataAsset.getOwnerId());
                //
                if(dataAsset==null){
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        try {
            response.setContentType("application/x-download;charset=GBK");
            byte[] flow = AESUtil.decrypt(dataContent.getContent(),key);
            //IOUtils.copy(flow,response.getOutputStream());
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(flow);
            response.getOutputStream().flush();
            response.getOutputStream().close();
            response.setStatus(200);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(501);
        }
        return null;
    }
    @RequestMapping("search")
    public String search(String dname,String page,String pageSize) throws IOException {
        String resource = "SqlMapConfig.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession session = factory.openSession();
        //UserMapper userMapper = session.getMapper(UserMapper.class);
        DataInfoMapper dataInfoMapper = session.getMapper(DataInfoMapper.class);
        try {
            int p = 0;

            if(!page.equals("-1")){
                p = Integer.parseInt(page);
            }
            PageHelper.startPage(p,Integer.parseInt(pageSize));
            List<DataInfo> res = dataInfoMapper.getDataInfoByLikeName(dname);
            long total = PageHelper.count(new ISelect() {
                @Override
                public void doSelect() {
                    dataInfoMapper.getDataInfoByLikeName(dname);
                }
            });
            return DataJSON.dataInfo2js(res,p,total).toString();
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "失败";
        }
    }

    @RequestMapping("getOwnData")
    public String getOwnData(String page,String pageSize) throws IOException {
        String resource = "SqlMapConfig.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession session = factory.openSession();
        //UserMapper userMapper = session.getMapper(UserMapper.class);
        DataMapper dataMapper = session.getMapper(DataMapper.class);
        try {
            Subject subject = SecurityUtils.getSubject();
            Session webSession = subject.getSession(false);
            String userId = (String) webSession.getAttribute("userId");
            int p = 0;

            if(!page.equals("-1")){
                p = Integer.parseInt(page);
            }
            PageHelper.startPage(p,Integer.parseInt(pageSize));
            List<DataAsset> res = dataMapper.getDataById(userId);
            long total = PageHelper.count(new ISelect() {
                @Override
                public void doSelect() {
                    dataMapper.getDataById(userId);
                }
            });
            return DataJSON.dataAsset2js(res,p,total).toString();
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "失败";
        }
    }

    @RequestMapping("purchase")
    public String purchase(String source) throws IOException {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession(false);
        FileSaveService fileSaveService = new FileSaveService(session);
        try {
            return fileSaveService.purchase(source);
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    @RequestMapping("queryInfo")
    public String queryDataInfo(String dataId){
        FileSaveService fileSaveService = new FileSaveService();
        try {
            return fileSaveService.getDataInfoById(dataId);
        } catch (IOException e) {
            e.printStackTrace();
            return "outer failed";
        }
    }

    //    public String addTradeInfo(final Context ctx,String fromId,String toId,String dataId,String originalDataId,String creatorId,double rate,double value){
}
