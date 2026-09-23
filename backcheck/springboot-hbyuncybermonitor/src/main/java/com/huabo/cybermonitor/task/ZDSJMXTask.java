package com.huabo.cybermonitor.task;


import com.huabo.cybermonitor.task.base.ZDSJTask;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ZDSJMXTask implements ZDSJTask {

//	@Autowired
//	private TblacctbookService tblacctbookService;
//	@Autowired
//	private HibernateExecuteService hibernateExecuteService;
//	@Autowired
//	private TblNbsjAuditModelResultServer auditModelResultServer;
//	@Override
//	public void run(TblStaff tblStaff,String accid, String executeId,List<TblNbsjAuditModel> tblNbsjAuditModels) {
//		for (TblNbsjAuditModel tblNbsjAuditModel : tblNbsjAuditModels) {
//			if(tblNbsjAuditModel!=null){
//				HibernatePartner hibernatePartner = null;
//			try {
//				TblAcctbook acctbook = this.tblacctbookService.findAllById(new BigDecimal(accid));
//				TblNbsjAuditModelResult modelResults = auditModelResultServer.findOneByAccidAndModelid(accid, tblNbsjAuditModel.getModelId().toString());
//				if(modelResults!=null){
//					hibernateExecuteService.deleteTable("ZDSJ_MX_" + tblNbsjAuditModel.getModelId().toString()+"_"+modelResults.getIndexSql(), acctbook.getAcctid());
//					auditModelResultServer.delete(modelResults);
//				}
//				int indexSql = 0;
//				int isData = 0;
//				 hibernatePartner = HibernatePartner.getHibernatePartnerInit(acctbook.getAcctid());
//				Long time = System.currentTimeMillis();
//					// 第一步
//				boolean res = false;
//					if (StringUtils.isNotBlank(tblNbsjAuditModel.getModelstep1())) {
//						indexSql = 1;
//						String tableName = "ZDSJ_MX_" + tblNbsjAuditModel.getModelId().toString()+"_1";
//						boolean isNot = this.hibernateExecuteService.isNotExistsTable(tableName,acctbook.getAcctid());
//						if (!isNot) {
//							List<String> solumns = hibernatePartner.getTbable(tblNbsjAuditModel.getModelstep1());
//							if(solumns.size()>0){
//								res = true;
//							}
//							this.hibernateExecuteService.createTable(tableName,acctbook.getAcctid(), solumns);
//						}else{
//							res = true;
//						}
//						List<Map<String, Object>> list = hibernatePartner.getData(tblNbsjAuditModel.getModelstep1());
//						if(list.size()>0){
//							isData = 1;
//						}
//						this.hibernateExecuteService.insertData(tableName,acctbook.getAcctid(), list, tblStaff.getStaffid(), executeId+time.toString());
//						// 第二步
//						if (StringUtils.isNotBlank(tblNbsjAuditModel.getModelstep2())) {
//							indexSql = 2;
//							String tableName1 = "ZDSJ_MX_" + tblNbsjAuditModel.getModelId().toString() + "_2";
//							boolean isNot1 = this.hibernateExecuteService.isNotExistsTable(tableName1,acctbook.getAcctid());
//							String sql = tblNbsjAuditModel.getModelstep2().replace("$", tableName);
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
//							this.hibernateExecuteService.insertData(tableName1,acctbook.getAcctid(), list2, tblStaff.getStaffid(), executeId+time.toString());
//							// 第三步
//							if (StringUtils.isNotBlank(tblNbsjAuditModel.getModelstep3())) {
//								indexSql = 3;
//								String tableName2 = "ZDSJ_MX_" + tblNbsjAuditModel.getModelId().toString() + "_3";
//								boolean isNot2 = this.hibernateExecuteService.isNotExistsTable(tableName2,acctbook.getAcctid());
//								String sql1 = tblNbsjAuditModel.getModelstep3().replace("$", tableName1);
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
//								this.hibernateExecuteService.insertData(tableName2,acctbook.getAcctid(), list3, tblStaff.getStaffid(), executeId+time.toString());
//								// 第四步
//								if (StringUtils.isNotBlank(tblNbsjAuditModel.getModelstep4())) {
//									indexSql = 4;
//									String tableName3 = "ZDSJ_MX_" + tblNbsjAuditModel.getModelId().toString() + "_4";
//									boolean isNot3 = this.hibernateExecuteService.isNotExistsTable(tableName3,acctbook.getAcctid());
//									String sql2 = tblNbsjAuditModel.getModelstep4().replace("$", tableName2);
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
//									this.hibernateExecuteService.insertData(tableName3,acctbook.getAcctid(), list4, tblStaff.getStaffid(),executeId+ time.toString());
//									// 第五步
//									if (StringUtils.isNotBlank(tblNbsjAuditModel.getModelstep5())) {
//										indexSql =5;
//										String tableName4 = "ZDSJ_MX_" + tblNbsjAuditModel.getModelId().toString() + "_5";
//										boolean isNot4 = this.hibernateExecuteService.isNotExistsTable(tableName4,acctbook.getAcctid());
//										String sql3 = tblNbsjAuditModel.getModelstep5().replace("$", tableName3);
//										if (!isNot4) {
//											List<String> solumns = hibernatePartner.getTbable(sql3);
//											if(solumns.size()>0){
//												this.hibernateExecuteService.createTable(tableName4,acctbook.getAcctid(), solumns);
//											}
//										}
//										List<Map<String, Object>> list5 = hibernatePartner.getData(sql3);
//										if(list5.size()>0){
//											isData = 5;
//										}
//										this.hibernateExecuteService.insertData(tableName4,acctbook.getAcctid(), list5, tblStaff.getStaffid(), executeId+time.toString());
//									}
//								}
//							}
//						}
//					}
//					if(isData!=0 && isData>1){
//						for (int i = 1; i <isData; i++) {
//							hibernateExecuteService.deleteTable("ZDSJ_MX_" + tblNbsjAuditModel.getModelId().toString()+"_"+i, acctbook.getAcctid());
//						}
//
//					}
//					if(res){
//						TblNbsjAuditModelResult modelResult = new TblNbsjAuditModelResult();
//						modelResult.setMonitorModel(tblNbsjAuditModel);
//						modelResult.setSaveTime(new Date());
//						modelResult.setTblStaff(tblStaff);
//						modelResult.setIndexSql(indexSql);
//						modelResult.setIsData(isData);
//						modelResult.setSolutionResultid(Integer.parseInt(accid));
//						modelResult.setSignId(executeId+time.toString());
//						this.auditModelResultServer.save(modelResult);
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}finally{
//					hibernatePartner.free();
//				}
//		}
//		}
//	}



}
