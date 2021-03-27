package com.example.demo.service;

import com.example.demo.dao.DataContentMapper;
import com.example.demo.dao.DataInfoMapper;
import com.example.demo.dao.DataMapper;
import com.example.demo.model.DataAsset;
import com.example.demo.model.DataContent;
import com.example.demo.model.DataInfo;
import com.example.demo.model.FileDealerCode;
import com.example.demo.utils.DataUtils;
import com.example.demo.utils.RSAUtil;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;
import java.util.Date;

public class FileSaveService {
    SqlSession session;
    Session webSession;
    MultipartFile file;
    byte[] fileBytes;
    byte[] scratchBytes;
    String dataId;
    KeyPair keyPair;

    public FileSaveService(Session webSession) {
        this.webSession = webSession;
    }

    RSAUtil rsaUtil = new RSAUtil();
    public void init(MultipartFile file) throws IOException, NoSuchAlgorithmException {
        String resource = "SqlMapConfig.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream);
        this.session = factory.openSession();
        this.file = file;
        this.fileBytes = file.getBytes();
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(1024);
        keyPair = generator.generateKeyPair();
    }
    public FileDealerCode saveToLocal(){
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

            //文件本地保存
            fos.write(fileBytes);
            //保存至datainfotable
            return FileDealerCode.FILE_SAVE_SUCCESS;
        }catch (Exception e){
            e.printStackTrace();
            return FileDealerCode.FILE_SAVE_FAILED;
        }
    }

    public FileDealerCode saveDataAsset(String v){
        dataId = DataUtils.generateShortUuid();
        String ownerId = (String) webSession.getAttribute("userId");
        Date date = new Date();
        String createdDate = date.toString();
        String scratch = Arrays.toString(Arrays.copyOfRange(fileBytes, 0, Math.min(255,fileBytes.length)));
        DataAsset dataAsset = new DataAsset(dataId,ownerId, ownerId,dataId, v,createdDate,scratch);
        //        DataMapper dataMapper = session.getMapper(DataMapper.class);
        DataMapper dataMapper = session.getMapper(DataMapper.class);
        try{
            dataMapper.insertData(dataAsset);
            //session.commit();
            //session.close();
            return FileDealerCode.DATA_ASSET_SAVE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return FileDealerCode.DATA_ASSET_SAVE_FAILED;
        }
    }

    public FileDealerCode saveDataInfo(String dataname, String size,String description){

        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        DataInfo dataInfo = new DataInfo(dataId,dataname,size,description,privateKey.toString());
        DataInfoMapper dataInfoMapper = session.getMapper(DataInfoMapper.class);
        //datainfotable(dataid,dataname,size,description,key)
        try{
            dataInfoMapper.insertDataInfo(dataInfo);
            //session.commit();
            //session.close();
            return FileDealerCode.DATA_INFO_SAVE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return FileDealerCode.DATA_INFO_SAVE_FAILED;
        }
    }
    public FileDealerCode saveDataContent(){
        DataContentMapper dataContentMapper = session.getMapper(DataContentMapper.class);
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

        try {
            DataContent dataContent = new DataContent(dataId,rsaUtil.encrypt(publicKey,fileBytes));
            dataContentMapper.insertDataContent(dataContent);
            session.commit();
            session.close();
            return FileDealerCode.DATA_CONTENT_SAVE_SUCCESS;
        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
            return FileDealerCode.DATA_CONTENT_SAVE_FAILED;
        }

    }
}
