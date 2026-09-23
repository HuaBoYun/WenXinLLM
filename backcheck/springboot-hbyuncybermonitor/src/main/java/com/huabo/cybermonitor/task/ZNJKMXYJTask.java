package com.huabo.cybermonitor.task;


import com.hbfk.entity.TblStaffUtil;
import com.huabo.cybermonitor.entity.MonitorSolution;
import com.huabo.cybermonitor.entity.Staff;
import com.huabo.cybermonitor.service.IMonitorSolutionService;
import com.huabo.cybermonitor.service.IMonitorSolutionresultService;
import com.huabo.cybermonitor.task.base.Task;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ZNJKMXYJTask implements Task {

//	@Autowired
//	private TblacctbookService tblacctbookService;
//	@Autowired
//	private HibernateExecuteService hibernateExecuteService;
//	@Autowired
//	private MonitorModelResultService iMonitorModelResultService;
	@Autowired
	private IMonitorSolutionresultService iMonitorSolutionresultService;
	@Autowired
	private IMonitorSolutionService iMonitorSolutionService;
	private List<MonitorSolution> monitorSolutions;
	//private Set<TblMonitorModel> models;
	private String lable;
	private Staff tblStaff;
	private Integer source;
	//@Resource
	//private TblAccBookService tblAccBookService;

//
//	@Override
//	public void run() {
//		//Set<TblMonitorModel> models = monitorSolution.getTblMonitorSolutionModels();
//		for (TblMonitorSolution monitorSolution : monitorSolutions) {
//			Serializable serializable = null;
//			boolean isCreate = true;
//			for (TblMonitorModel tblMonitorModel : monitorSolution.getTblMonitorSolutionModels()) {
//				try {
//					//TblAcctbook acctbook = this.tblacctbookService.findAllById(new BigDecimal(tblMonitorModel.getConnectionstrings()));
//					TblAccBook acctbook=tblAccBookService.findByBookIdOne(tblMonitorModel.getConnectionstrings());
//					HibernatePartner hibernatePartner = HibernatePartner.getHibernatePartnerInit(acctbook.getAcctid());
//					Long date = System.currentTimeMillis();
//					String time = getLable()+"_"+date.toString();
//						// 第一步
//						if (StringUtils.isNotBlank(tblMonitorModel.getModelstep1())) {
//							String tableName = "ZNJK_MX_" + tblMonitorModel.getModelid().toString()+"_1";
//							boolean isNot = this.hibernateExecuteService.isNotExistsTable(tableName,acctbook.getAcctid());
//							String sqlall=tblMonitorModel.getModelstep1().replace("@", acctbook.getBookyear().toString());
//							if (!isNot) {
//								List<String> solumns = hibernatePartner.getTbable(sqlall);
//								this.hibernateExecuteService.createTable(tableName,acctbook.getAcctid(), solumns);
//							}
//							List<Map<String, Object>> list = hibernatePartner.getData(sqlall);
//							if(list.size()>0){
//								if(isCreate){
//									TblMonitorSolutionResult monitorSolutionResult = new TblMonitorSolutionResult();
//									monitorSolutionResult.setMonitorSolution(monitorSolution);
//									monitorSolutionResult.setSaveTime(new Date());
//									monitorSolutionResult.setTblStaff(tblStaff);
//									monitorSolutionResult.setSource(getSource());
//									serializable = this.tblMonitorSolutionResultService.save(monitorSolutionResult);
//									isCreate = false;
//								}
//								this.hibernateExecuteService.insertData(tableName,acctbook.getAcctid(), list, tblStaff.getStaffid(), time.toString());
//							}
//							// 第二步
//							if (StringUtils.isNotBlank(tblMonitorModel.getModelstep2())) {
//								String tableName1 = "ZNJK_MX_" + tblMonitorModel.getModelid().toString() + "_2";
//								boolean isNot1 = this.hibernateExecuteService.isNotExistsTable(tableName1,acctbook.getAcctid());
//								String sql = tblMonitorModel.getModelstep2().replace("$", tableName);
//								if (!isNot1) {
//									List<String> solumns = hibernatePartner.getTbable(sql);
//									this.hibernateExecuteService.createTable(tableName1,acctbook.getAcctid(), solumns);
//								}
//								List<Map<String, Object>> list2 = hibernatePartner.getData(sql);
//								if(list2.size()>0){
//									this.hibernateExecuteService.insertData(tableName1,acctbook.getAcctid(), list2, tblStaff.getStaffid(), time.toString());
//								}
//								// 第三步
//								if (StringUtils.isNotBlank(tblMonitorModel.getModelstep3())) {
//									String tableName2 = "ZNJK_MX_" + tblMonitorModel.getModelid().toString() + "_3";
//									boolean isNot2 = this.hibernateExecuteService.isNotExistsTable(tableName2,acctbook.getAcctid());
//									String sql1 = tblMonitorModel.getModelstep3().replace("$", tableName1);
//									if (!isNot2) {
//										List<String> solumns = hibernatePartner.getTbable(sql1);
//										this.hibernateExecuteService.createTable(tableName2,acctbook.getAcctid(), solumns);
//									}
//									List<Map<String, Object>> list3 = hibernatePartner.getData(sql1);
//									if(list3.size()>0){
//										this.hibernateExecuteService.insertData(tableName2,acctbook.getAcctid(), list3, tblStaff.getStaffid(), time.toString());
//									}
//									// 第四步
//									if (StringUtils.isNotBlank(tblMonitorModel.getModelstep4())) {
//										String tableName3 = "ZNJK_MX_" + tblMonitorModel.getModelid().toString() + "_4";
//										boolean isNot3 = this.hibernateExecuteService.isNotExistsTable(tableName3,acctbook.getAcctid());
//										String sql2 = tblMonitorModel.getModelstep4().replace("$", tableName2);
//										if (!isNot3) {
//											List<String> solumns = hibernatePartner.getTbable(sql2);
//											this.hibernateExecuteService.createTable(tableName3,acctbook.getAcctid(), solumns);
//										}
//										List<Map<String, Object>> list4 = hibernatePartner.getData(sql2);
//										if(list4.size()>0){
//											this.hibernateExecuteService.insertData(tableName3,acctbook.getAcctid(), list4, tblStaff.getStaffid(), time.toString());
//										}
//										// 第五步
//										if (StringUtils.isNotBlank(tblMonitorModel.getModelstep5())) {
//											String tableName4 = "ZNJK_MX_" + tblMonitorModel.getModelid().toString() + "_5";
//											boolean isNot4 = this.hibernateExecuteService.isNotExistsTable(tableName4,acctbook.getAcctid());
//											String sql3 = tblMonitorModel.getModelstep4().replace("$", tableName3);
//											if (!isNot4) {
//												List<String> solumns = hibernatePartner.getTbable(sql3);
//												this.hibernateExecuteService.createTable(tableName4,acctbook.getAcctid(), solumns);
//											}
//											List<Map<String, Object>> list5 = hibernatePartner
//													.getData(sql3);
//											if(list5.size()>0){
//												this.hibernateExecuteService.insertData(tableName4,acctbook.getAcctid(), list5, tblStaff.getStaffid(), time.toString());
//											}
//										}
//									}
//								}
//							}
//						}
//
//						hibernatePartner.free();
//						if(null!=serializable){
//							TblMonitorModelResult modelResult = new TblMonitorModelResult();
//							modelResult.setMonitorModel(tblMonitorModel);
//							modelResult.setSaveTime(new Date());
//							modelResult.setTblStaff(tblStaff);
//							modelResult.setSignId(time.toString());
//							modelResult.setMonitorSolutionResult(this.tblMonitorSolutionResultService.get(serializable));
//							this.tblMonitorModelResultService.save(modelResult);
//						}
//						monitorSolution.setRunstatus(2);
//					} catch (Exception e) {
//						monitorSolution.setRunstatus(3);
//						e.printStackTrace();
//					}
//			}
//			this.monitorSolutionService.merge(monitorSolution);
//		}
//
//	}

	public Staff getTblStaff() {
		return tblStaff;
	}

	public void setTblStaff(Staff Staff) {
		this.tblStaff = Staff;
	}

	

	public List<MonitorSolution> getMonitorSolutions() {
		return monitorSolutions;
	}

	public void setMonitorSolutions(List<MonitorSolution> monitorSolutions) {
		this.monitorSolutions = monitorSolutions;
	}

	public String getLable() {
		return lable;
	}

	public void setLable(String lable) {
		this.lable = lable;
	}

	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}


	@Override
	public void run() {

	}
}
