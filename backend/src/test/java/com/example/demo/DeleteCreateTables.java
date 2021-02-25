package com.example.demo;

import com.example.demo.model.MyDatabase;
import com.example.demo.service.MyDatabaseService;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.sql.SQLException;


public class DeleteCreateTables {
    @Test
    public void deleteTest() throws SQLException {
        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        MyDatabase dMyDatabase = (MyDatabase) context.getBean("deleteTable");
        MyDatabaseService myDatabaseService = new MyDatabaseService(dMyDatabase);
        myDatabaseService.connect();
        myDatabaseService.execute();
        myDatabaseService.close();
    }
    @Test
    public void createTest() throws SQLException {
        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        MyDatabase cMyDatabase = (MyDatabase) context.getBean("createTable");
        MyDatabaseService myDatabaseService = new MyDatabaseService(cMyDatabase);
        myDatabaseService.connect();
        myDatabaseService.execute();
        myDatabaseService.close();
    }


}
