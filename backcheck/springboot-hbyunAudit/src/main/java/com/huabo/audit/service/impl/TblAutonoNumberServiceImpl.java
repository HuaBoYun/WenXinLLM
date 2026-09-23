package com.huabo.audit.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.TblOrgNoId;
import com.huabo.audit.oracle.mapper.TblAutonoNumberMapper;
import com.huabo.audit.service.TblAutonoNumberService;

@Service
public class TblAutonoNumberServiceImpl implements TblAutonoNumberService {
	
	@Resource
	public TblAutonoNumberMapper tblAutonoNumberMapper;
	
	@Resource
    private UserProvider userProvider;
	

	@Override
	public JsonBean findFlowNextId(String tblName, String column, String orgCol, BigDecimal noId,
			String chChoiceCol, String choiceVal, String bjf,String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		BigDecimal orgid = loginStaff.getCurrentOrg().getOrgid();
		
		
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		
		
		Integer isUse = this.getIsUseAutoNoInfo(orgid);
		if(isUse == 0){
			resultMap.put("data", "-1");//该组织没有使用自定义编码
	    	return ResponseFormat.retParam(1,200,resultMap);
		}else{
			TblOrgNoId orgNo = this.getCodeRule(orgid,noId);
			if(orgNo == null){
				resultMap.put("data", "-1");//该组织没有使用自定义编码
		    	return ResponseFormat.retParam(1,200,resultMap);
			}else{
				String noSql = null;
				String result = null;
				String jgf = orgNo.getNocode();
				if(jgf.indexOf("_") != -1){
					jgf=jgf.replace("_","/_");
				}
				
				//判断插入的是一级编号和同级编号是
					noSql = "SELECT MAX("+column+") FROM "+tblName+" WHERE ";
					
//					if("MySql".equals(SysConfig.get("databaseType"))){
//						Object res = this.tblAutonoNumberDao.excuteFunReturnUnique("select getChildrenDeptList("+orgid+")");
//						Object ult = this.tblAutonoNumberDao.excuteFunReturnUnique("select getHyOrgRootList(-1)");
//						noSql += "(FIND_IN_SET("+orgCol+",'"+res+"') OR "+orgCol+" = "+orgid+" OR FIND_IN_SET("+orgCol+",'"+ult+"'))";
//					}else{
						noSql += " TO_CHAR("+orgCol+") IN ( SELECT TO_CHAR(ORGID) FROM TBL_ORGANIZATION WHERE ORGTYPE = 0 START WITH FATHERORGID = "+orgid+" CONNECT BY PRIOR FATHERORGID = ORGID UNION ALL SELECT TO_CHAR(ORGID) FROM TBL_ORGANIZATION WHERE ORGID = "+orgid+" UNION ALL SELECT TO_CHAR(ORGID) FROM TBL_ORGANIZATION WHERE ORGTYPE >=100 START WITH FATHERORGID = -1 CONNECT BY PRIOR ORGID = FATHERORGID ) ";
//					}
					//判断用户有没有使用分隔符
					if(orgNo.getNoSepartor()!=null){
//						if("Oracle".equals(SysConfig.get("databaseType"))){
							if(jgf.indexOf("_") != -1){
								if("_".equals(orgNo.getNoSepartor())){
									jgf += "/"+orgNo.getNoSepartor();
								}else{
									jgf += orgNo.getNoSepartor();
								}
								jgf += "%' escape '/";
							}else{
								if("_".equals(orgNo.getNoSepartor())){
									jgf += "/"+orgNo.getNoSepartor() + "%' escape '/";
								}else{
									jgf += orgNo.getNoSepartor()+"%";
								}
							}
//						}else{
//							jgf += "%";
//						}
						noSql += " AND "+column+" LIKE '"+jgf+"' AND (LENGTH(REPLACE("+column+",'"+orgNo.getNocode()+"','')) - LENGTH(REPLACE(REPLACE("+column+",'"+orgNo.getNocode()+"',''),'"+orgNo.getNoSepartor()+"',''))) = "+1;
					}else{
//						if("Oracle".equals(SysConfig.get("databaseType"))){
							if(jgf.indexOf("_") != -1){
								jgf += "%' escape '/";
							}else{
								jgf += "%";
							}
//						}else{
//							jgf += "%";
//						}
						noSql += " AND "+column+" LIKE '"+jgf+"' ";
//								+ "AND (LENGTH(REPLACE("+column+",'"+orgNo.getNocode()+"','')) - LENGTH(REPLACE(REPLACE("+column+",'"+orgNo.getNocode()+"',''),'-',''))) = 0 AND (LENGTH(REPLACE("+column+",'"+orgNo.getNocode()+"','')) - LENGTH(REPLACE(REPLACE("+column+",'"+orgNo.getNocode()+"',''),'_','')))  = 0"
//								+" AND (LENGTH(REPLACE("+column+",'"+orgNo.getNocode()+"','')) - LENGTH(REPLACE(REPLACE("+column+",'"+orgNo.getNocode()+"',''),',','')))  = 0 AND (LENGTH(REPLACE("+column+",'"+orgNo.getNocode()+"','')) - LENGTH(REPLACE(REPLACE("+column+",'"+orgNo.getNocode()+"',''),'.','')))  = 0"
//								+" AND (LENGTH(REPLACE("+column+",'"+orgNo.getNocode()+"','')) - LENGTH(REPLACE(REPLACE("+column+",'"+orgNo.getNocode()+"',''),'`','')))  = 0 AND (LENGTH(REPLACE("+column+",'"+orgNo.getNocode()+"','')) - LENGTH(REPLACE(REPLACE("+column+",'"+orgNo.getNocode()+"',''),'~','')))  = 0";
					}
					if(chChoiceCol != null){
						if("包含".equals(bjf)){
							bjf = "LIKE";
							choiceVal = "'%"+choiceVal+"%'";
						}else if("等于".equals(bjf)){
							bjf="=";
							choiceVal = "'"+choiceVal+"'";
						}
						noSql += " AND "+chChoiceCol+" "+bjf+" "+choiceVal;
					}
					//查询是否存在规则编号 ， 没有就新增 有就在此基础上加1
					result = tblAutonoNumberMapper.selectUniqueColumn(noSql);
					String res = getNewCode(result,orgNo);
					
					resultMap.put("data", res);//该组织没有使用自定义编码
			    	return ResponseFormat.retParam(1,200,resultMap);
			}
		}
	}
	
	/**
	 * 获取组织是否使用自定义编号
	 * @param orgid
	 * @return
	 * @throws Exception
	 */
	public Integer getIsUseAutoNoInfo(BigDecimal orgid) throws Exception {
		String isUse = tblAutonoNumberMapper.getisUse(orgid);
		return Integer.parseInt(isUse);
	}
	
	/**
	 * 获取组织的编号设置信息
	 * @param orgid
	 * @param noId
	 * @return
	 * @throws Exception
	 */
	private TblOrgNoId getCodeRule(BigDecimal orgid,BigDecimal noId) throws Exception{
		List<Object> objList = tblAutonoNumberMapper.getcodeRule(orgid,noId);
		
		TblOrgNoId orgNo = null;
		Object[] objs = null;
		
		for (Object object : objList) {
			orgNo = new TblOrgNoId();
//			objs = (Object[])object;
//			orgNo.setNocode(objs[0].toString());
//			if(objs[1]!=null){
//				orgNo.setNoSepartor(objs[1].toString());
//			}
//			orgNo.setNoNumber(Integer.parseInt(objs[2].toString()));
			
			//==
			orgNo.setNocode((String)object);
		}
		return orgNo;
	}
	
	/**
	 * 产生新的编号不保持上下级关系的编号
	 * @param result
	 * @param orgNo
	 * @return
	 */
	private String getNewCode(String result,TblOrgNoId orgNo ){
		String code;
		if(result == null||"".equals(result)){
			code = orgNo.getNocode();
			if(orgNo.getNoSepartor() != null){
				code += orgNo.getNoSepartor();
			}
			
			if(null != orgNo.getNoNumber()) {
				if(orgNo.getNoNumber()==-1){
					code += "01";
				}else if(orgNo.getNoNumber()==1){
					code+="00001";
				}else if(orgNo.getNoNumber()==2){
					code+="001";
				}
			}
			
		}else{
			if(orgNo.getNoSepartor() == null){
				String num = result.replace(orgNo.getNocode(),"");
				Integer no = Integer.parseInt(num);
				no++;
				int nolength = num.length()-no.toString().length();
				code = orgNo.getNocode();
				for (int i = 0; i < nolength; i++) {
					code += "0";
				}
				code += no;
			}else{
				String num = result.replace(orgNo.getNocode()+orgNo.getNoSepartor(),"");
				Integer no = Integer.parseInt(num);
				no++;
				int nolength = num.length()-no.toString().length();
				code = orgNo.getNocode()+orgNo.getNoSepartor();
				for (int i = 0; i < nolength; i++) {
					code += "0";
				}
				code += no;
			}
		}
		return code;
	}

	@Override
	public String findRootNumberByParentId(String chilNumberCol, String chilTblName, String chilParentCol,
			String parentIdCol, String parentTblName, String parnetOrgCol, String token, BigDecimal noId,
			String middleTblname, String middleChilCol, String middleParentCol, String type) throws Exception {
		
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return null;
		}
		BigDecimal orgid = loginStaff.getCurrentOrg().getOrgid();
		
		Integer isUse = this.getIsUseAutoNoInfo(orgid);
		if(isUse == 0){
			return "-1";//该组织没有使用自定义编码
		}else{
			TblOrgNoId orgNo = this.getCodeRule(orgid,noId);
			String noSql = null;
			String result = null;
			String jgf = orgNo.getNocode();
			
			Object chids = 0;
			Object hyids = 0;
			
//			if("MySql".equals(SysConfig.get("databaseType"))){
//				chids = this.tblAutonoNumberDao.excuteFunReturnUnique("select getChildrenDeptList("+orgid+")");
//				hyids = this.tblAutonoNumberDao.excuteFunReturnUnique("select getHyOrgRootList(-1)");
//			}
			
			if(jgf.indexOf("_") != -1){
				jgf=jgf.replace("_","/_");
			}
			noSql = "SELECT MAX("+chilNumberCol+") FROM "+chilTblName+" WHERE "+chilParentCol+" IN (";
			if(middleTblname == null){
				noSql += "SELECT "+parentIdCol+" FROM "+parentTblName+" WHERE ";
				
//				if("MySql".equals(SysConfig.get("databaseType"))){
//					if(type!=null && type.equals("hy")) {
//						noSql += "( FIND_IN_SET("+parnetOrgCol+",'"+chids+"') OR "+parnetOrgCol+" ="+orgid+" OR FIND_IN_SET("+parnetOrgCol+",'"+hyids+"') )";
//					}else{
//						noSql += "( FIND_IN_SET("+parnetOrgCol+",'"+chids+"') OR "+parnetOrgCol+" ="+orgid+" )";
//					}
//				}else{
					noSql += parnetOrgCol+" IN	(SELECT ORGID FROM TBL_ORGANIZATION WHERE ORGTYPE = 0 START WITH FATHERORGID = "+orgid+" CONNECT BY PRIOR FATHERORGID = ORGID UNION ALL SELECT ORGID FROM TBL_ORGANIZATION WHERE ORGID = "+orgid;
					if(type!=null && type.equals("hy")) {
						noSql+=" UNION ALL SELECT TO_CHAR(ORGID) FROM TBL_ORGANIZATION " + 
							"WHERE ORGTYPE >=100 START WITH FATHERORGID = -1 CONNECT BY PRIOR ORGID = FATHERORGID ";
					}
					noSql+=" ) ";
//				}
			}else{
				noSql += "SELECT "+middleChilCol+" FROM "+middleTblname+" WHERE "+middleParentCol+" IN (SELECT "+parentIdCol+" FROM "+parentTblName+" WHERE ";
//				if("MySql".equals(SysConfig.get("databaseType"))){
//					if(type!=null && type.equals("hy")) {
//						noSql += "( FIND_IN_SET("+parnetOrgCol+",'"+chids+"') OR "+parnetOrgCol+" ="+orgid+" OR FIND_IN_SET("+parnetOrgCol+",'"+hyids+"') )";
//					}else{
//						noSql += "( FIND_IN_SET("+parnetOrgCol+",'"+chids+"') OR "+parnetOrgCol+" ="+orgid+" )";
//					}
//				}else{		
					noSql +=parnetOrgCol+" IN (SELECT TO_CHAR(ORGID) FROM TBL_ORGANIZATION WHERE ORGTYPE = 0 START WITH FATHERORGID = "+orgid+" CONNECT BY PRIOR FATHERORGID = ORGID"
							+" UNION ALL SELECT TO_CHAR(ORGID) FROM TBL_ORGANIZATION WHERE ORGID = "+orgid;
					if(type!=null && type.equals("hy")) {
						noSql+=" UNION ALL SELECT TO_CHAR(ORGID) FROM TBL_ORGANIZATION " + 
							"WHERE ORGTYPE >=100 START WITH FATHERORGID = -1 CONNECT BY PRIOR ORGID = FATHERORGID";
					}
					noSql+=" )";
//				}
				noSql+= ")";
			}
			noSql += ")";
			if(orgNo.getNoSepartor()!=null){
				if("Oracle".equals(SysConfig.get("databaseType"))){
					if(jgf.indexOf("_") != -1){
						if("_".equals(orgNo.getNoSepartor())){
							jgf += "/"+orgNo.getNoSepartor();
						}else{
							jgf += orgNo.getNoSepartor();
						}
						jgf += "%' escape '/";
					}else{
						if("_".equals(orgNo.getNoSepartor())){
							jgf += "/"+orgNo.getNoSepartor() + "%' escape '/";
						}else{
							jgf += orgNo.getNoSepartor()+"%";
						}
					}
				}else{
					jgf += orgNo.getNoSepartor()+"%";
				}
				noSql += " AND "+chilNumberCol+" LIKE '"+jgf+"' AND (LENGTH(REPLACE("+chilNumberCol+",'"+orgNo.getNocode()+"','')) - LENGTH(REPLACE(REPLACE("+chilNumberCol+",'"+orgNo.getNocode()+"',''),'"+orgNo.getNoSepartor()+"',''))) = 1";
			}else{
//				if("Oracle".equals(SysConfig.get("databaseType"))){
					if(jgf.indexOf("_") != -1){
						jgf += "%' escape '/";
					}else{
						jgf += "%";
					}
//				}else{
//					jgf += "%";
//				}
				noSql += " AND "+chilNumberCol+" LIKE '"+jgf+"'";
//						+ " AND (LENGTH(REPLACE("+chilNumberCol+",'"+orgNo.getNocode()+"','')) - LENGTH(REPLACE(REPLACE("+chilNumberCol+",'"+orgNo.getNocode()+"',''),'-',''))) = 0"
//						+" AND (LENGTH(REPLACE("+chilNumberCol+",'"+orgNo.getNocode()+"','')) - LENGTH(REPLACE(REPLACE("+chilNumberCol+",'"+orgNo.getNocode()+"',''),'_',''))) = 0 AND (LENGTH(REPLACE("+chilNumberCol+",'"+orgNo.getNocode()+"','')) - LENGTH(REPLACE(REPLACE("+chilNumberCol+",'"+orgNo.getNocode()+"',''),',',''))) = 0"
//						+" AND (LENGTH(REPLACE("+chilNumberCol+",'"+orgNo.getNocode()+"','')) - LENGTH(REPLACE(REPLACE("+chilNumberCol+",'"+orgNo.getNocode()+"',''),'`',''))) = 0 AND (LENGTH(REPLACE("+chilNumberCol+",'"+orgNo.getNocode()+"','')) - LENGTH(REPLACE(REPLACE("+chilNumberCol+",'"+orgNo.getNocode()+"',''),'.',''))) = 0"
//						+" AND (LENGTH(REPLACE("+chilNumberCol+",'"+orgNo.getNocode()+"','')) - LENGTH(REPLACE(REPLACE("+chilNumberCol+",'"+orgNo.getNocode()+"',''),'~',''))) = 0";
			}
//			result = tblAutonoNumberDao.selectUniqueColumn(noSql);
			result = tblAutonoNumberMapper.selectUniqueColumn(noSql);
			return getNewCode(result,orgNo);
		}
	}

}
