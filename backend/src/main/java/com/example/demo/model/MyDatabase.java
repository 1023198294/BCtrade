package com.example.demo.model;


import java.sql.*;

public class MyDatabase {

    public final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    public final String DB_URL = "jdbc:mysql://47.100.138.100:3306/final?autoReconnect=true&useSSL=false&characterEncoding=utf-8";
    public final String USER = "root";
    public final String PASS = "Jj971124.";
    public Properties SQLType;
    public String _sql = null;
    public Connection conn = null;
    public Statement stmt = null;



    public String getUSER() {
        return USER;
    }

    public  String getJdbcDriver() {
        return JDBC_DRIVER;
    }

    public  String getDbUrl() {
        return DB_URL;
    }

    public String getPASS() {
        return PASS;
    }

    public Properties getSQLType() {
        return SQLType;
    }

    public void setSQLType(Properties SQLType) {
        this.SQLType = SQLType;
    }

    public String get_sql() {
        return _sql;
    }

    public void set_sql(String _sql) {
        this._sql = _sql;
    }

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public Statement getStmt() {
        return stmt;
    }

    public void setStmt(Statement stmt) {
        this.stmt = stmt;
    }
}
