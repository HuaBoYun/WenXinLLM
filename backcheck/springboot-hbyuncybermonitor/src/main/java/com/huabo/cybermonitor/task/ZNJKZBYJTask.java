package com.huabo.cybermonitor.task;


import com.huabo.cybermonitor.entity.MonitorSolution;
import com.huabo.cybermonitor.entity.Staff;
import com.huabo.cybermonitor.service.IMonitorSolutionService;
import com.huabo.cybermonitor.task.base.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ZNJKZBYJTask implements Task {

	private List<MonitorSolution> monitorSolutions;
//	@Autowired
//	private TblacctbookService tblacctbookService;
//	@Autowired
//	private TblDataAnalysisService tblDataAnalysisService;
//	@Autowired
//	private TblmonitorIndicatorResultService tblmonitorIndicatorResultService;
	@Autowired
	private IMonitorSolutionService iMonitorSolutionService;
	private Staff tblStaff;
	private String label;
	private Integer source;
	//private Set<TblIndicator> indicators;
//	@Resource
//	private TblAccBookService tblAccBookService;

//	@Autowired
//	public void run() {
//		if (null != monitorSolutions&&monitorSolutions.size()>0) {
//			//Set<TblIndicator> indicators = getIndicators();
//			String executeId = System.currentTimeMillis()+"_";
//		for (TblMonitorSolution monitorSolution : monitorSolutions) {
//			try {
//				for (TblIndicator tblIndicator : monitorSolution.getTblMonitorSolutionIndicators()) {
//					String gongsi = tblIndicator.getFormula();
//					List<String> listSplit = splitStr(gongsi);
//					 String zhongwen  =	tblIndicator.getForlumachs();//中文公式
//					 List<String> zhongwenSplit = splitStr(zhongwen);
//					//TblAcctbook acctbook = this.tblacctbookService.findAllById(new BigDecimal(tblIndicator.getConnectionstrings()));
//					TblAccBook acctbook=tblAccBookService.findByBookIdOne(tblIndicator.getConnectionstrings());
//					HibernatePartner hibernatePartner = HibernatePartner.getHibernatePartnerInit(acctbook.getAcctid());
//					Double result = 0d;
//					StringBuffer sb = new StringBuffer();
//					for (int i = 0; i < listSplit.size(); i++) {
//						if (isNumeric(listSplit.get(i))) {
//							if(isNumeric(zhongwenSplit.get(i))&&zhongwenSplit.get(i).indexOf("%")!=-1){
//								continue ;
//							}
//							TblBiDatasource biDatasource = tblDataAnalysisService.findById(listSplit.get(i));
//
//							if(biDatasource!=null){
//								String sql = biDatasource.getSqlstring();
//								//int index=sql.indexOf("-$");
//								//获取上一年的账套
//								/*if(index>=0){
//									Integer year= acctbook.getAccyear()-1;
//									TblAcctbook oldacctbook = this.tblacctbookService.findByYear(year.toString(), acctbook.getOrgid().toString());
//									sql=sql.replace("-$", oldacctbook.getAcctid());
//								}else{
//									//当前账套
//									sql = sql.replace("$", acctbook.getAcctid());
//								}*/
//								if(sql.indexOf("-@")>0){
//									sql=sql.replace("-@", acctbook.getBookyear().toString());
//								}else{
//									sql=sql.replace("@", acctbook.getBookyear().toString());
//								}
//								sql = sql.replace("$", acctbook.getAcctid());
//
//								Object object = hibernatePartner.execSql(sql);
//								String zhi;
//								if(object==null || object==""){
//									if(tblIndicator.getForlumachs().indexOf("/")>0){
//										zhi="1";
//									}else{
//										zhi="0";
//									}
//
//								}else{
//									zhi =hibernatePartner.execSql(sql).toString();
//								}
//								sb.append(zhi);
//							}else{
//								sb.append(listSplit.get(i));
//							}
//						} else {
//							sb.append(listSplit.get(i));
//						}
//					}
//					hibernatePartner.free();
//					ScriptEngine jse = new ScriptEngineManager().getEngineByName("JavaScript");
//					result = new   BigDecimal(jse.eval(sb.toString()).toString()).setScale(2,  BigDecimal.ROUND_HALF_UP).doubleValue();
//					System.out.println(result);
//					TblmonitorIndicatorResult indicatorResult = new TblmonitorIndicatorResult();
//					//指标
//					indicatorResult.setIndicator(tblIndicator);
//					//模型id
//					indicatorResult.setSolutIonresulId(monitorSolution.getSolutionid());
//					indicatorResult.setExecuteId(executeId);
//					indicatorResult.setSource(getSource());
//					indicatorResult.setSign(getLabel());
//					indicatorResult.setSaveTime(new Date());
//					indicatorResult.setScore(result);
//					indicatorResult.setStaff(tblStaff);
//					indicatorResult.setTolerance(this.tblmonitorIndicatorResultService.yuzhiResult(tblIndicator.getIndicatorid(), result));
//					this.tblmonitorIndicatorResultService.save(indicatorResult);
//					 monitorSolution.setRunstatus(2);
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//				 monitorSolution.setRunstatus(3);
//			}
//			 this.tblMonitorSolutionService.modify(monitorSolution);
//		}
//
//	}
//}

	private List<String> splitStr(String string) throws Exception {
		List<String> listSplit = new ArrayList<String>();
		Matcher matcher = Pattern.compile("\\d+(\\.\\d+)?|[+*\\-/()]|([\u4e00-\u9fa5]+)").matcher(string);
		// 用正则拆分成每个元素
		while (matcher.find()) {
			System.out.println(matcher.group(0));
			listSplit.add(matcher.group(0));
		}
		return listSplit;
	}
	  private static boolean isNumeric(String str){  
		  for (int i = str.length();--i>=0;){    
		   if (!Character.isDigit(str.charAt(i))){  
		    return false;  
		   }  
		  }  
		  return true;  
		}  
	public Staff getTblStaff() {
		return tblStaff;
	}

	public void setTblStaff(Staff tblStaff) {
		this.tblStaff = tblStaff;
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

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}


	@Override
	public void run() {

	}
}
