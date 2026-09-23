package com.hbfk.exception;


import java.sql.Connection;
import java.sql.SQLException;

/**
 * 数据库异常类
 *
 * @author 第三方开发组 YanYu
 * @version V3.1.0
 * @copyright 第三方流程组件
 * @date 2021/3/16 10:10
 */
public class DataException extends Exception {

    public DataException(){
        super();
    }

    public DataException(String message) {
        super(message);
    }

    public static DataException errorLink(String warning) {
        return null;
    }

    /**
     * mysql表重复
     * @param error 错误信息
     */
    public static DataException tableExists(String error,Connection rollbackConn){
        executeRollback(rollbackConn);
        //Mysql英文报错，临时解决方案
        error = error.replace("Table","表").replace("already exists","已经存在。");
        return new DataException(error);
    }

    public static SQLException rollbackDataException(SQLException e, Connection rollbackConn) {
        executeRollback(rollbackConn);
        return e;
    }

    private static void executeRollback(Connection conn){
        try {
            conn.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
