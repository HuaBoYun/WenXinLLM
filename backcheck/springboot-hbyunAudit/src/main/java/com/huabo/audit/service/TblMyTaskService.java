package com.huabo.audit.service;

import java.util.List;
import java.util.Map;

import com.huabo.audit.oracle.entity.TblMyTask;

 

public interface TblMyTaskService {
    void updateSetting(TblMyTask task,String attids) throws Exception;

    List<TblMyTask> getByFromid(String toString);

    List<TblMyTask> findByLendid(String lendid);

    public TblMyTask findOndbyFrom(String fromid);

    void insertMyTaskSetting(TblMyTask task) throws Exception;

	List<TblMyTask> findByObj(String string, String string2, String string3, String string4);
	
//	Map<String, Object> delAprAttById(Integer attid);
	
    void updateSetting(TblMyTask task) throws Exception;

//    void updateMySqlSetting(TblMyTaskMySql task) throws Exception;
//
//    List<TblMyTaskMySql> getByMySqlFromid(String toString);
//
//    List<TblMyTaskMySql> findByMySqlLendid(String lendid);
//
//    TblMyTaskMySql findOndbyMySqlFrom(String fromid);
//
//    void insertMySqlMyTaskSetting(TblMyTaskMySql task) throws Exception;
	
}
