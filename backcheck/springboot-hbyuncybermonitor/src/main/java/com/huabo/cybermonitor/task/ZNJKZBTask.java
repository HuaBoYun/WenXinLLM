package com.huabo.cybermonitor.task;


import com.huabo.cybermonitor.entity.Indicator;
import com.huabo.cybermonitor.entity.Staff;
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
public class ZNJKZBTask  implements Task {

//	@Autowired
//	private TblacctbookService tblacctbookService;
//	@Autowired
//	private TblmonitorIndicatorResultService tblmonitorIndicatorResultService;
//	@Autowired
//	private IindicatorService tblIndicatorService;
	private List<Indicator> indicators;
	private Staff tblStaff;
	private Integer source;
	private String executeId;
//	@Resource
//	private TblAccBookService tblAccBookService;


//	@Override
//	public void run() {
//		for (TblIndicator indicator : indicators) {
//			HibernatePartner hibernatePartner= null;
//			try {
//				String gongsi = indicator.getFormula();
//				List<String> listSplit = splitStr(gongsi);
//				String zhongwen  =	indicator.getForlumachs();//中文公式
//				List<String> zhongwenSplit = splitStr(zhongwen);
//				TblAccBook acctbook=tblAccBookService.findByBookIdOne(indicator.getConnectionstrings());
//				//TblAcctbook acctbook = this.tblacctbookService.findAllById(new BigDecimal(indicator.getConnectionstrings()));
//				hibernatePartner=	HibernatePartner.getHibernatePartnerInit(acctbook.getAcctid());
//				Double result = 0d;
//				StringBuffer sb = new StringBuffer();
//				for (int i = 0; i < listSplit.size(); i++) {
//					if(isNumeric(listSplit.get(i))){
//						//过滤公式中存在的  数字 和 %
//						if(isNumeric(zhongwenSplit.get(i))){
//							sb.append(listSplit.get(i));
//							continue ;
//						}
//						TblDataAnalysisService tblDataAnalysisService = (TblDataAnalysisService) SpringContextHolder.getBean("TblDataAnalysisService");
//						TblBiDatasource biDatasource =	tblDataAnalysisService.getData(new BigDecimal(listSplit.get(i)));
//						String sql = biDatasource.getSqlstring();
//
//						/*int index=sql.indexOf("-$");
//						//获取上一年的账套
//						if(index>=0){
//							Integer year= acctbook.getAccyear()-1;
//							TblAcctbook oldacctbook = this.tblacctbookService.findByYear(year.toString(), acctbook.getOrgid().toString());
//							sql=sql.replace("-$", oldacctbook.getAcctid());
//						}else{
//							//当前账套
//							sql = sql.replace("$", acctbook.getAcctid());
//						}*/
//						sql = sql.replace("$", acctbook.getAcctid());
//						sql=sql.replace("@", acctbook.getBookyear().toString());
//						System.out.println(sql);
//						Object object = hibernatePartner.execSql(sql);
//						String zhi;
//						if(object==null || object==""){
//							if(indicator.getForlumachs().indexOf("/")>0){
//								zhi="1";
//							}else{
//								zhi="0";
//							}
//
//						}else{
//							zhi =hibernatePartner.execSql(sql).toString();
//						}
//						sb.append(zhi);
//					}else{
//						sb.append(listSplit.get(i));
//					}
//				}
//				ScriptEngine jse = new ScriptEngineManager().getEngineByName("JavaScript");
//				result = new   BigDecimal(jse.eval(sb.toString()).toString()).setScale(2,  BigDecimal.ROUND_HALF_UP).doubleValue();
//				System.out.println(result);
//				TblmonitorIndicatorResult indicatorResult = new TblmonitorIndicatorResult();
//				indicatorResult.setIndicator(indicator);
//				indicatorResult.setSaveTime(new Date());
//				indicatorResult.setScore(result);
//				indicatorResult.setStaff(tblStaff);
//				indicatorResult.setSource(getSource());
//				indicatorResult.setExecuteId(getExecuteId());
//				String tolerance =  this.tblmonitorIndicatorResultService.yuzhiResult(indicator.getIndicatorid(), result);
//				indicatorResult.setTolerance(tolerance);
//				this.tblmonitorIndicatorResultService.save(indicatorResult);
//				indicator.setRunstatus(new BigDecimal("2"));
//				this.tblIndicatorService.modify(indicator);
//			} catch (Exception e) {
//				indicator.setRunstatus(new BigDecimal("3"));
//				this.tblIndicatorService.modify(indicator);
//				e.printStackTrace();
//			}finally {
//				if(null!=hibernatePartner){
//					hibernatePartner.free();
//				}
//			}
//		}
//
//	}
	
	
	
	
	
	  private  List<String> splitStr(String string) throws Exception {
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
		public String getExecuteId() {
			return executeId;
		}
		public void setExecuteId(String executeId) {
			this.executeId = executeId;
		}

	public Staff getTblStaff() {
		return tblStaff;
	}
	public void setTblStaff(Staff tblStaff) {
		this.tblStaff = tblStaff;
	}
	public Integer getSource() {
		return source;
	}
	public void setSource(Integer source) {
		this.source = source;
	}





	public List<Indicator> getIndicators() {
		return indicators;
	}





	public void setIndicators(List<Indicator> indicators) {
		this.indicators = indicators;
	}

	@Override
	public void run() {

	}
}
