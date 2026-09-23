package com.huabo.cybermonitor.task;

import com.huabo.cybermonitor.entity.MonitorRule;
import com.huabo.cybermonitor.entity.Staff;
import com.huabo.cybermonitor.task.base.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ZNJKGZTask implements Task {

	private List<MonitorRule> rules;
//	@Autowired
//	private HibernateExecuteService hibernateExecuteService;
//	@Autowired
//	private TblMonitorRuleService tblMonitorRuleService;
//	@Autowired
//	private TblMonitorPrewarningService tblMonitorPrewarningService;
	@Autowired
	private Staff staff;
//	@Autowired
//	private TblacctbookService tblacctbookService;
	private String label;
//	@Resource
//	private TblAccBookService tblAccBookService;


//	@Autowired
//	public void run() {
//		//if (StringUtils.isNotBlank(id)) {
//			//TblMonitorRule rule = this.tblMonitorRuleService.findOne(id);
//		if (rules != null) {
//			for (TblMonitorRule rule : rules) {
//				//	String userName = ContextHolder.getContext();
//					//TblAcctbook acctbook = this.tblacctbookService.findAllById(new BigDecimal(rule.getConnectionstrings()));
//					TblAccBook acctbook=tblAccBookService.findByBookIdOne(rule.getConnectionstrings());
//					Long time =  System.currentTimeMillis();
//					if (rule.getRulesql() == null || rule.getRulesql().isEmpty() || rule.getConnectionstrings() == null
//							|| rule.getConnectionstrings().isEmpty()){
//						rule.setRunstatus(3);
//						return;
//					}
//					 HibernatePartner instance =null;
//					try {
//					    instance =  HibernatePartner.getHibernatePartnerInit(acctbook.getAcctid());
//						//Class.forName("oracle.jdbc.driver.OracleDriver");
//						//instance.setConnection(FxglConfig.FXGL_ORACLE_CONN_STR, acctbook.getAcctid(), password);
//					    String sql=rule.getRulesql().replace("@", acctbook.getBookyear()).replace("$", acctbook.getAcctid());
//						List<Map<String, Object>> list = instance.getData(sql);
//						int resultCount = 0;
//						if(list.size()>0){
//							hibernateExecuteService.insertData("ZNJK_GZ_" + rule.getRuleid().toString(),acctbook.getAcctid(), list,
//									Staff.getStaffid(), getLabel()+"_"+time);
//							resultCount = list.size();
//						}
//						TblMonitorPrewarning prewarning = new TblMonitorPrewarning();
//						prewarning.setTblMonitorRule(rule);
//						prewarning.setSaveDateTime(new Date());
//						prewarning.setStaff(Staff);
//						prewarning.setResultCount(resultCount);
//						prewarning.setSignId(getLabel()+"_"+time.toString());
//						tblMonitorPrewarningService.add(prewarning);
//						rule.setRunstatus(2);
//					}  catch (Exception e) {
//						e.printStackTrace();
//						rule.setRunstatus(3);
//					}finally {
//						if(null!=instance){
//							instance.free();
//						}
//					}
//					this.tblMonitorRuleService.merge(rule);
//				}
//		}
//
//	}
	//}


	public String getLabel() {
		return label;
	}



	public void setLabel(String label) {
		this.label = label;
	}





	public Staff getStaff() {
		return staff;
	}

	public void setStaff(Staff Staff) {
		this.staff = Staff;
	}

	@Override
	public void run() {

	}



	public List<MonitorRule> getRules() {
		return rules;
	}



	public void setRules(List<MonitorRule> rules) {
		this.rules = rules;
	}

}
