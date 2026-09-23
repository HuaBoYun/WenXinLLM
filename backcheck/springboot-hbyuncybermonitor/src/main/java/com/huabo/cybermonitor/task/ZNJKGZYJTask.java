package com.huabo.cybermonitor.task;

import com.huabo.cybermonitor.entity.MonitorSolution;
import com.huabo.cybermonitor.entity.Staff;
import com.huabo.cybermonitor.service.IMonitorSolutionService;
import com.huabo.cybermonitor.service.IMonitorSolutionresultService;
import com.huabo.cybermonitor.task.base.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ZNJKGZYJTask implements Task {

	private List<MonitorSolution> monitorSolutions;
//	@Autowired
//	private HibernateExecuteService hibernateExecuteService;
	@Autowired
	private IMonitorSolutionresultService iMonitorSolutionresultService;
	@Autowired
	private IMonitorSolutionService iMonitorSolutionService;
	private Staff staff;
//	@Autowired
//	private TblacctbookService tblacctbookService;
//	@Autowired
//	private TblMonitorPrewarningService tblMonitorPrewarningService;
//	private Set<TblMonitorSolutionRule>  solutionRules;
	private String label;
	private Integer source;
	
//	@Resource
//	private TblAccBookService tblAccBookService;

//	@Autowired
//	public void run() {
//		if(null!=monitorSolutions&&monitorSolutions.size()>0){
//			for (TblMonitorSolution monitorSolution : monitorSolutions) {
//				boolean isCreate = true;
//				Serializable serializable = null;
//				if (null != monitorSolution) {
//					//Set<TblMonitorSolutionRule> solutionRules = monitorSolution.getTblMonitorSolutionRules();
//					if(monitorSolution.getTblMonitorSolutionRules().size()>0){
//						monitorSolution.setRunstatus(1);
//					}
//					tblMonitorSolutionService.merge(monitorSolution);
//					for (TblMonitorSolutionRule tblMonitorSolutionRule : monitorSolution.getTblMonitorSolutionRules()) {
//						Long time = System.currentTimeMillis();
//						TblMonitorRule rule = tblMonitorSolutionRule.getTblMonitorRule();
//
//						if (rule != null) {
//							//TblAcctbook acctbook = this.tblacctbookService.findAllById(new BigDecimal(rule.getConnectionstrings()));
//							TblAccBook acctbook=tblAccBookService.findByBookIdOne(rule.getConnectionstrings());
//							String password = FxglConfig.FXGL_ORACEL_BOOK_PWD;
//
//							if (rule.getRulesql() == null || rule.getRulesql().isEmpty() || rule.getConnectionstrings() == null
//									|| rule.getConnectionstrings().isEmpty()){
//								monitorSolution.setRunstatus(3);
//								return;
//							}
//							HibernatePartner instance = new HibernatePartner();
//							try {
//								Class.forName("oracle.jdbc.driver.OracleDriver");
//								instance.setConnection(FxglConfig.FXGL_ORACLE_CONN_STR, acctbook.getAcctid(), password);
//								String sql=rule.getRulesql().replace("@", acctbook.getBookyear().toString());
//								List<Map<String, Object>> list = instance.getData(sql);
//								if(list.size()>0){
//									if(isCreate){
//										TblMonitorSolutionResult monitorSolutionResult = new TblMonitorSolutionResult();
//										monitorSolutionResult.setMonitorSolution(monitorSolution);
//										monitorSolutionResult.setSaveTime(new Date());
//										monitorSolutionResult.setStaff(Staff);
//										monitorSolutionResult.setSource(getSource());
//										serializable = this.tblMonitorSolutionResultService.save(monitorSolutionResult);
//										isCreate =false;
//									}
//									hibernateExecuteService.insertData("ZNJK_GZ_" + rule.getRuleid().toString(),acctbook.getAcctid(), list,
//											Staff.getStaffid(), getLabel()+"_"+time);
//									TblMonitorPrewarning prewarning = new TblMonitorPrewarning();
//									prewarning.setTblMonitorRule(rule);
//									prewarning.setSaveDateTime(new Date());
//									prewarning.setStaff(Staff);
//									prewarning.setSignId(getLabel()+"_"+time.toString());
//									prewarning.setMonitorSolutionResult(this.tblMonitorSolutionResultService.get(serializable));
//									this.tblMonitorPrewarningService.add(prewarning);
//								}
//								instance.free();
//								monitorSolution.setRunstatus(2);
//							} catch (Exception e) {
//								e.printStackTrace();
//								monitorSolution.setRunstatus(3);
//								//tblMonitorSolutionService.merge(monitorSolution);
//								return;
//							} finally {
//								instance.free();
//							}
//						}
//
//					}
//	//				TblMonitorSolutionResult monitorSolutionResult = new TblMonitorSolutionResult();
//	//				monitorSolutionResult.setMonitorSolution(monitorSolution);
//	//				monitorSolutionResult.setSaveTime(new Date());
//	//				monitorSolutionResult.setStaff(Staff);
//	//				this.tblMonitorSolutionResultService.save(monitorSolutionResult);
//
//					tblMonitorSolutionService.merge(monitorSolution);
//				}
//			}
//		}
//
//	}

	public Staff getStaff() {
		return staff;
	}

	public void setStaff(Staff staff) {
		this.staff = staff;
	}


//	public Set<TblMonitorSolutionRule> getSolutionRules() {
//		return solutionRules;
//	}
//
//	public void setSolutionRules(Set<TblMonitorSolutionRule> solutionRules) {
//		this.solutionRules = solutionRules;
//	}
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public List<MonitorSolution> getMonitorSolutions() {
		return monitorSolutions;
	}

	public void setMonitorSolutions(List<MonitorSolution> monitorSolutions) {
		this.monitorSolutions = monitorSolutions;
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
