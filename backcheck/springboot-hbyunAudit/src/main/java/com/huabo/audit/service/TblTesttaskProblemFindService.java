package com.huabo.audit.service;

import java.util.List;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblTesttaskProblemFind;
import com.huabo.audit.oracle.vo.TblTesttaskProblemFindVo;;

/**
* 描述: Service
* @author: ziyao
* @date: 2022-04-13
*/
public interface TblTesttaskProblemFindService {
 
	JsonBean  findALLProblemLedgerList(Integer pageNumber,Integer pageSize, String userid, String orgid,TblTesttaskProblemFind tblTesttaskProblemFind);

	JsonBean  findALLProblemLedgerList(Integer pageNumber,Integer pageSize, String token,TblTesttaskProblemFindVo vo) throws Exception;

	List<TblTesttaskProblemFind> getExportProblemLedgerList(String staffid, String orgid) throws Exception;

	
}
