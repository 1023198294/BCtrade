package com.example.demo.controller;

import com.example.demo.service.WalletService;
import com.example.demo.service.blockchain.MyBlockChainService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("admin/")
public class TradeController {

    @RequestMapping("remain")
    public String remain(){
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession(false);
        String userId = (String) session.getAttribute("userId");
        if(userId!=null){
            try{
                MyBlockChainService myBlockChainService = new MyBlockChainService();
                String remain = myBlockChainService.queryWalletById(userId);
                if(remain!=null){
                    return remain;
                }else{
                    return "余额查询错误";
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "余额查询错误";
            }
        }
        return "0.0";
    }
}
