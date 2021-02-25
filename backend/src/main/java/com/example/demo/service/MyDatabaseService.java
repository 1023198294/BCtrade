package com.example.demo.service;
import com.example.demo.model.MyDatabase;
import com.example.demo.model.Properties;
import org.springframework.lang.Nullable;

import javax.xml.transform.Result;
import java.sql.*;

public class MyDatabaseService {
    private MyDatabase myDatabase;

    public MyDatabaseService(MyDatabase myDatabase) {
        this.myDatabase = myDatabase;
    }

    public void connect(){
        try {
            Class.forName(myDatabase.JDBC_DRIVER);
            myDatabase.conn = DriverManager.getConnection(myDatabase.DB_URL,myDatabase.USER,myDatabase.PASS);
            myDatabase.stmt = myDatabase.conn.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
    @Nullable
    public ResultSet execute() throws SQLException{
        boolean hasExecute;
        ResultSet rs = null;
        try {
            if (myDatabase.SQLType== Properties.EXECUTE_TYPE){
                hasExecute = myDatabase.stmt.execute(myDatabase._sql);
                System.out.println("Execute OK");
                System.out.println(myDatabase.stmt.getUpdateCount()+" rows affected");
            }
            else if(myDatabase.SQLType==Properties.QUERY_TYPE){
                rs = myDatabase.stmt.executeQuery(myDatabase._sql);
                hasExecute = rs != null;
                if(hasExecute){
                    System.out.println("ExecuteQuery OK");
                    System.out.println(myDatabase.stmt.getUpdateCount()+" rows affected");
                }
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return rs;
    }

    public void close() throws SQLException {
        try {
            if(myDatabase.stmt!=null)
                myDatabase.stmt.close();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        try {
            if(myDatabase.conn!=null)
                myDatabase.conn.close();

        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }
}
