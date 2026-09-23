package com.huabo.cybermonitor.task;


import com.hbfk.entity.TblStaffUtil;
import com.huabo.cybermonitor.entity.MonitorModel;
import com.huabo.cybermonitor.entity.Staff;
import com.huabo.cybermonitor.service.IMonitorModelService;
import com.huabo.cybermonitor.service.IMonitorSolutionresultService;
import com.huabo.cybermonitor.task.base.Task;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ZNJKMXTask implements Task {

//	@Autowired
//	private TblacctbookService tblacctbookService;
	@Autowired
	private IMonitorModelService iMonitorModelService;
//	@Autowired
//	private HibernateExecuteService hibernateExecuteService;
	@Autowired
	private IMonitorSolutionresultService iMonitorSolutionresultService;
	private List<MonitorModel> tblMonitorModels;
	private TblStaffUtil Staff;
	private String executeId;
//	@Resource
//	private TblAccBookService tblAccBookService;

//
//	@Override
//	public void run() {
//		for (MonitorModel tblMonitorModel : tblMonitorModels) {
//			//HibernatePartner hibernatePartner = null;
//			try {
//				int indexSql = 0;
//				int isData = 0;
//				TblAccBook acctbook=tblAccBookService.findByBookIdOne(tblMonitorModel.getConnectionstrings());
//				//TblAcctbook acctbook = this.tblacctbookService.findAllById(new BigDecimal(tblMonitorModel.getConnectionstrings()));
//				 hibernatePartner = HibernatePartner.getHibernatePartnerInit(acctbook.getAcctid());
//				Long time = System.currentTimeMillis();
//					// 第一步
//				boolean res = false;
//					if (StringUtils.isNotBlank(tblMonitorModel.getModelstep1())) {
//						indexSql = 1;
//						String tableName = "ZNJK_MX_" + tblMonitorModel.getModelid().toString()+"_1";
//						boolean isNot = this.hibernateExecuteService.isNotExistsTable(tableName,acctbook.getAcctid());
//						String sqlall=tblMonitorModel.getModelstep1().replace("@", acctbook.getBookyear().toString());
//						if (!isNot) {
//							List<String> solumns = hibernatePartner.getTbable(sqlall);
//							if(solumns.size()>0){
//								res = true;
//							}
//							this.hibernateExecuteService.createTable(tableName,acctbook.getAcctid(), solumns);
//						}else{
//							res = true;
//						}
//						List<Map<String, Object>> list = hibernatePartner.getData(sqlall);
//						if(list.size()>0){
//							isData = 1;
//						}
//						this.hibernateExecuteService.insertData(tableName,acctbook.getAcctid(), list, Staff.getStaffid(), getExecuteId()+time.toString());
//						// 第二步
//						if (StringUtils.isNotBlank(tblMonitorModel.getModelstep2())) {
//							indexSql = 2;
//							String tableName1 = "ZNJK_MX_" + tblMonitorModel.getModelid().toString() + "_2";
//							boolean isNot1 = this.hibernateExecuteService.isNotExistsTable(tableName1,acctbook.getAcctid());
//							String sql = tblMonitorModel.getModelstep2().replace("$", tableName);
//							if (!isNot1) {
//								List<String> solumns = hibernatePartner.getTbable(sql);
//								if(solumns.size()>0){
//									this.hibernateExecuteService.createTable(tableName1,acctbook.getAcctid(), solumns);
//								}
//							}
//							List<Map<String, Object>> list2 = hibernatePartner.getData(sql);
//							if(list2.size()>0){
//								isData = 2;
//							}
//							this.hibernateExecuteService.insertData(tableName1,acctbook.getAcctid(), list2, Staff.getStaffid(), getExecuteId()+time.toString());
//							// 第三步
//							if (StringUtils.isNotBlank(tblMonitorModel.getModelstep3())) {
//								indexSql = 3;
//								String tableName2 = "ZNJK_MX_" + tblMonitorModel.getModelid().toString() + "_3";
//								boolean isNot2 = this.hibernateExecuteService.isNotExistsTable(tableName2,acctbook.getAcctid());
//								String sql1 = tblMonitorModel.getModelstep3().replace("$", tableName1);
//								if (!isNot2) {
//									List<String> solumns = hibernatePartner.getTbable(sql1);
//									if(solumns.size()>0){
//										this.hibernateExecuteService.createTable(tableName2,acctbook.getAcctid(), solumns);
//									}
//								}
//								List<Map<String, Object>> list3 = hibernatePartner.getData(sql1);
//								if(list3.size()>0){
//									isData = 3;
//								}
//								this.hibernateExecuteService.insertData(tableName2,acctbook.getAcctid(), list3, Staff.getStaffid(), getExecuteId()+time.toString());
//								// 第四步
//								if (StringUtils.isNotBlank(tblMonitorModel.getModelstep4())) {
//									indexSql = 4;
//									String tableName3 = "ZNJK_MX_" + tblMonitorModel.getModelid().toString() + "_4";
//									boolean isNot3 = this.hibernateExecuteService.isNotExistsTable(tableName3,acctbook.getAcctid());
//									String sql2 = tblMonitorModel.getModelstep4().replace("$", tableName2);
//									if (!isNot3) {
//										List<String> solumns = hibernatePartner.getTbable(sql2);
//										if(solumns.size()>0){
//											this.hibernateExecuteService.createTable(tableName3,acctbook.getAcctid(), solumns);
//										}
//									}
//									List<Map<String, Object>> list4 = hibernatePartner.getData(sql2);
//									if(list4.size()>0){
//										isData = 4;
//									}
//									this.hibernateExecuteService.insertData(tableName3,acctbook.getAcctid(), list4, Staff.getStaffid(),getExecuteId()+ time.toString());
//									// 第五步
//									if (StringUtils.isNotBlank(tblMonitorModel.getModelstep5())) {
//										indexSql =5;
//										String tableName4 = "ZNJK_MX_" + tblMonitorModel.getModelid().toString() + "_5";
//										boolean isNot4 = this.hibernateExecuteService.isNotExistsTable(tableName4,acctbook.getAcctid());
//										String sql3 = tblMonitorModel.getModelstep5().replace("$", tableName3);
//										if (!isNot4) {
//											List<String> solumns = hibernatePartner.getTbable(sql3);
//											if(solumns.size()>0){
//												this.hibernateExecuteService.createTable(tableName4,acctbook.getAcctid(), solumns);
//											}
//										}
//										List<Map<String, Object>> list5 = hibernatePartner
//												.getData(sql3);
//										if(list5.size()>0){
//											isData = 5;
//										}
//										this.hibernateExecuteService.insertData(tableName4,acctbook.getAcctid(), list5, Staff.getStaffid(), getExecuteId()+time.toString());
//									}
//								}
//							}
//						}
//					}
//
//					if(res){
//						TblMonitorModelResult modelResult = new TblMonitorModelResult();
//						modelResult.setMonitorModel(tblMonitorModel);
//						modelResult.setSaveTime(new Date());
//						modelResult.setStaff(Staff);
//						modelResult.setIndexSql(indexSql);
//						modelResult.setIsData(isData);
//						modelResult.setSignId(getExecuteId()+time.toString());
//						this.tblMonitorModelResultService.save(modelResult);
//					}
//					tblMonitorModel.setRunstatus(2);
//				} catch (Exception e) {
//					tblMonitorModel.setRunstatus(3);
//					e.printStackTrace();
//				}finally{
//					hibernatePartner.free();
//				}
//				this.tblMonitorModelService.modify(tblMonitorModel);
//		}
//
//	}

	public TblStaffUtil getStaff() {
		return Staff;
	}

	public void setStaff(TblStaffUtil Staff) {
		this.Staff = Staff;
	}

	public List<MonitorModel> getTblMonitorModels() {
		return tblMonitorModels;
	}

	public void setTblMonitorModels(List<MonitorModel> tblMonitorModels) {
		this.tblMonitorModels = tblMonitorModels;
	}


	public String getExecuteId() {
		if(null!=executeId){
			return executeId+"_";
		}else{
			return "";
		}
	}

	public void setExecuteId(String executeId) {
		this.executeId = executeId;
	}


	@Override
	public void run() {

	}
}
