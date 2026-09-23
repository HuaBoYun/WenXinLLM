package com.huabo.monitor.service;

import java.math.BigDecimal;
import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.huabo.monitor.entity.CatVo;
import com.huabo.monitor.entity.TblAttachment;
import com.huabo.monitor.entity.TblTestplan;
import com.huabo.monitor.entity.TblTesttask;
import com.huabo.monitor.entity.TblTesttaskProblemFind;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author yhr
 * @since 2022-09-08
 */
public interface ITblTesttaskProblemFindService extends IService<TblTesttaskProblemFind> {
	void saveTesttsak(TblTesttaskProblemFind task, String attids) throws Exception;

	void updateTesttsak(TblTesttaskProblemFind task, String attids) throws Exception;

	void delTesttsak(BigDecimal testtaskid) throws Exception;

	List<TblTesttaskProblemFind> getByTesttaskid(BigDecimal testtaskid) throws Exception;

	TblTesttaskProblemFind getById(BigDecimal findid) throws Exception;

	IPage<TblTesttaskProblemFind> findAllRwToOrg(Integer pageNumber, String userid, String orgid);

	IPage<TblTesttaskProblemFind> findALLProblemLedgerList(Integer pageNumber, Integer pageSize, String userid,
			String orgid, TblTesttaskProblemFind tblTesttaskProblemFind, Integer authorityType);

	PageInfo<TblTesttaskProblemFind> findALLProblemLedgerListNew(Integer pageNumber, Integer pageSize, String userid,
			String orgid, TblTesttaskProblemFind tblTesttaskProblemFind, Integer authorityType, TblStaffUtil user)
					throws Exception;

	String saveProblem(String token, TblTesttask task, String findid) throws Exception;

	void delFjByTypeAndId(String type, String attid) throws Exception;

	public List<TblAttachment> getRepAttById(String id) throws Exception;

	List<TblTesttaskProblemFind> getExportProblemLedgerList(String staffid, String orgid,
			TblTesttaskProblemFind tblTesttaskProblemFind, String ids) throws Exception;

	void updateProfind(TblTesttaskProblemFind profind) throws Exception;

	void sendreform(BigDecimal findid) throws Exception;

	JsonBean forwardPersonnel(String token, String ids, BigDecimal staffId);

	JsonBean getProblemTransferList(String token,BigDecimal id);
}
