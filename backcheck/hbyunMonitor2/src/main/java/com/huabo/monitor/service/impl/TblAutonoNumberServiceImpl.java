package com.huabo.monitor.service.impl;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.monitor.config.DateBaseConfig;
import com.huabo.monitor.entity.TblOrgNoId;
import com.huabo.monitor.mapper.TblAutonoNumberMapper;
import com.huabo.monitor.service.TblAutonoNumberService;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TblAutonoNumberServiceImpl implements TblAutonoNumberService {

    @Resource
    public TblAutonoNumberMapper tblAutonoNumberMapper;

    @Resource
    private UserProvider userProvider;

    @Override
    public JsonBean findFlowNextId(String tblName, String column, String orgCol, Integer noId,
                                   String chChoiceCol, String choiceVal, String bjf, String token) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        BigDecimal orgid = loginStaff.getCurrentOrg().getOrgid();


        Map<String, Object> resultMap = new HashMap<String, Object>(0);


        Integer isUse = this.getIsUseAutoNoInfo(orgid);
        if (isUse == 0) {
            resultMap.put("data", "-1");//该组织没有使用自定义编码
            return ResponseFormat.retParam(1, 200, resultMap);
        } else {
            TblOrgNoId orgNo = this.getCodeRule(orgid, noId);
            if (orgNo == null) {
                resultMap.put("data", "-1");//该组织没有使用自定义编码
                return ResponseFormat.retParam(1, 200, resultMap);
            } else {
                String noSql = null;
                String result = null;
                String jgf = orgNo.getNocode();
                if (jgf.indexOf("_") != -1) {
                    jgf = jgf.replace("_", "/_");
                }

                //判断插入的是一级编号和同级编号是
                noSql = "SELECT MAX(" + column + ") FROM " + tblName + " WHERE ";

//					if("MySql".equals(SysConfig.get("databaseType"))){
//						Object res = this.tblAutonoNumberDao.excuteFunReturnUnique("select getChildrenDeptList("+orgid+")");
//						Object ult = this.tblAutonoNumberDao.excuteFunReturnUnique("select getHyOrgRootList(-1)");
//						noSql += "(FIND_IN_SET("+orgCol+",'"+res+"') OR "+orgCol+" = "+orgid+" OR FIND_IN_SET("+orgCol+",'"+ult+"'))";
//					}else{
                noSql += " TO_CHAR(" + orgCol + ") IN ( SELECT TO_CHAR(ORGID) FROM TBL_ORGANIZATION WHERE ORGTYPE = 0 START WITH FATHERORGID = " + orgid + " CONNECT BY PRIOR FATHERORGID = ORGID UNION ALL SELECT TO_CHAR(ORGID) FROM TBL_ORGANIZATION WHERE ORGID = " + orgid + " UNION ALL SELECT TO_CHAR(ORGID) FROM TBL_ORGANIZATION WHERE ORGTYPE >=100 START WITH FATHERORGID = -1 CONNECT BY PRIOR ORGID = FATHERORGID ) ";
//					}
                //判断用户有没有使用分隔符
                if (orgNo.getNoSepartor() != null) {
//						if("Oracle".equals(SysConfig.get("databaseType"))){
                    if (jgf.indexOf("_") != -1) {
                        if ("_".equals(orgNo.getNoSepartor())) {
                            jgf += "/" + orgNo.getNoSepartor();
                        } else {
                            jgf += orgNo.getNoSepartor();
                        }
                        jgf += "%' escape '/";
                    } else {
                        if ("_".equals(orgNo.getNoSepartor())) {
                            jgf += "/" + orgNo.getNoSepartor() + "%' escape '/";
                        } else {
                            jgf += orgNo.getNoSepartor() + "%";
                        }
                    }
//						}else{
//							jgf += "%";
//						}
                    noSql += " AND " + column + " LIKE '" + jgf + "' AND (LENGTH(REPLACE(" + column + ",'" + orgNo.getNocode() + "','')) - LENGTH(REPLACE(REPLACE(" + column + ",'" + orgNo.getNocode() + "',''),'" + orgNo.getNoSepartor() + "',''))) = " + 1;
                } else {
//						if("Oracle".equals(SysConfig.get("databaseType"))){
                    if (jgf.indexOf("_") != -1) {
                        jgf += "%' escape '/";
                    } else {
                        jgf += "%";
                    }
//						}else{
//							jgf += "%";
//						}
                    noSql += " AND " + column + " LIKE '" + jgf + "' AND (LENGTH(REPLACE(" + column + ",'" + orgNo.getNocode() + "','')) - LENGTH(REPLACE(REPLACE(" + column + ",'" + orgNo.getNocode() + "',''),'-',''))) = 0 AND (LENGTH(REPLACE(" + column + ",'" + orgNo.getNocode() + "','')) - LENGTH(REPLACE(REPLACE(" + column + ",'" + orgNo.getNocode() + "',''),'_','')))  = 0"
                            + " AND (LENGTH(REPLACE(" + column + ",'" + orgNo.getNocode() + "','')) - LENGTH(REPLACE(REPLACE(" + column + ",'" + orgNo.getNocode() + "',''),',','')))  = 0 AND (LENGTH(REPLACE(" + column + ",'" + orgNo.getNocode() + "','')) - LENGTH(REPLACE(REPLACE(" + column + ",'" + orgNo.getNocode() + "',''),'.','')))  = 0"
                            + " AND (LENGTH(REPLACE(" + column + ",'" + orgNo.getNocode() + "','')) - LENGTH(REPLACE(REPLACE(" + column + ",'" + orgNo.getNocode() + "',''),'`','')))  = 0 AND (LENGTH(REPLACE(" + column + ",'" + orgNo.getNocode() + "','')) - LENGTH(REPLACE(REPLACE(" + column + ",'" + orgNo.getNocode() + "',''),'~','')))  = 0";
                }
                if (chChoiceCol != null) {
                    if ("包含".equals(bjf)) {
                        bjf = "LIKE";
                        choiceVal = "'%" + choiceVal + "%'";
                    } else if ("等于".equals(bjf)) {
                        bjf = "=";
                        choiceVal = "'" + choiceVal + "'";
                    }
                    noSql += " AND " + chChoiceCol + " " + bjf + " " + choiceVal;
                }
                //查询是否存在规则编号 ， 没有就新增 有就在此基础上加1
                result = tblAutonoNumberMapper.selectUniqueColumn(noSql);
                String res = getNewCode(result, orgNo);

                resultMap.put("data", res);//该组织没有使用自定义编码
                return ResponseFormat.retParam(1, 200, resultMap);
            }
        }
    }

    public String findNumberLevelNexidByParent(Integer noId, String parentNumberCol, String parentTblName, String parentIdCol, String parentId,
                                               String chilNumberCol, String chilTblName, String chilOrgCol, BigDecimal orgid, Map<String, String> choiceMap) throws Exception {
        Integer isUse = this.getIsUseAutoNoInfo(orgid);
        if (isUse == 0) {
            return "-1";//该组织没有使用自定义编码
        } else {
            TblOrgNoId orgNo = this.getCodeRule(orgid, noId);
            if (orgNo == null) {
                return "-1";
            } else {
                Object childIds = 0;
                Object hyIds = 0;
                if(DateBaseConfig.DATABASETYPE.equals("MySql")) {
                    // todo: sql
//					childIds = this.tblAutonoNumberMapper.excuteFunReturnUnique("select getChildrenDeptList("+orgid+")");
                    childIds = this.tblAutonoNumberMapper.selectUniqueColumn("select getChildrenDeptList(" + orgid + ")");
                    hyIds = this.tblAutonoNumberMapper.selectUniqueColumn("select getHyOrgRootList(-1)");
                }

                String parentSql = "SELECT " + parentNumberCol + " FROM " + parentTblName + " WHERE " + parentIdCol + " = " + parentId;
                String parentNumber = tblAutonoNumberMapper.selectUniqueColumn(parentSql);

                String jgf = parentNumber;
                String noSql = null;
                //Oracle数据库特殊字符处理
                jgf = parentNumber.replace("_", "/_");
                if (orgNo.getNoSepartor() != null) {
                        if(DateBaseConfig.DATABASETYPE.equals("Oracle")) {
                        if (parentNumber.indexOf("_") != -1) {
                            if ("_".equals(orgNo.getNoSepartor())) {
                                jgf += "/" + orgNo.getNoSepartor();
                            } else {
                                jgf += orgNo.getNoSepartor();
                            }
                            jgf += "%' escape '/";
                        } else {
                            if ("_".equals(orgNo.getNoSepartor())) {
                                jgf += "/" + orgNo.getNoSepartor() + "%' escape '/";
                            } else {
                                jgf += orgNo.getNoSepartor() + "%";
                            }
                        }
                    } else {
                        jgf += "%";
                    }
                    noSql = "SELECT MAX(" + chilNumberCol + ") FROM " + chilTblName + " WHERE " + chilNumberCol + " LIKE '" + jgf + "' AND ";
                    if(DateBaseConfig.DATABASETYPE.equals("MySql")) {
                        noSql += "(FIND_IN_SET(" + chilOrgCol + ",'" + childIds + "') OR " + chilOrgCol + " = " + orgid + " OR FIND_IN_SET(" + chilOrgCol + ",'" + hyIds + "'))";
                    } else {
                        noSql += chilOrgCol + " IN (SELECT TO_CHAR(ORGID) FROM TBL_ORGANIZATION WHERE ORGTYPE = 0 START WITH FATHERORGID = " + orgid + " CONNECT BY PRIOR FATHERORGID = ORGID UNION ALL SELECT TO_CHAR(ORGID) FROM TBL_ORGANIZATION WHERE ORGID = " + orgid + " UNION ALL SELECT TO_CHAR(ORGID) FROM TBL_ORGANIZATION WHERE ORGTYPE >=100 START WITH FATHERORGID = -1 CONNECT BY PRIOR ORGID = FATHERORGID 	UNION ALL SELECT TO_CHAR(ORGID) FROM TBL_ORGANIZATION WHERE ORGTYPE >=100 START WITH FATHERORGID IN (SELECT ORGID FROM TBL_ORGANIZATION WHERE ORGTYPE >=100 AND FATHERORGID = -1) CONNECT BY FATHERORGID = PRIOR ORGID 	UNION ALL SELECT TO_CHAR(ORGID) FROM TBL_ORGANIZATION WHERE ORGTYPE >=100 START WITH FATHERORGID IN (SELECT ORGID FROM TBL_ORGANIZATION WHERE ORGTYPE >=100  AND FATHERORGID = -1) CONNECT BY FATHERORGID = PRIOR ORGID )";
                    }

                    noSql += " AND (LENGTH(REPLACE(" + chilNumberCol + ",'" + parentNumber + "','')) - LENGTH(REPLACE(REPLACE(" + chilNumberCol + ",'" + parentNumber + "',''),'" + orgNo.getNoSepartor() + "','')))=1"
                            + " AND LENGTH(REPLACE(" + chilNumberCol + ",'" + parentNumber + "','')) = (SELECT MIN(LENGTH(REPLACE(" + chilNumberCol + ",'" + parentNumber + "',''))) FROM " + chilTblName + " WHERE " + chilNumberCol + " LIKE '" + jgf + "' AND LENGTH(REPLACE(" + chilNumberCol + ",'" + parentNumber + "','')) != 0 AND ";

                    if(DateBaseConfig.DATABASETYPE.equals("MySql")) {
                        noSql += "(FIND_IN_SET(" + chilOrgCol + ",'" + childIds + "') OR " + chilOrgCol + " =" + orgid + " OR FIND_IN_SET(" + chilOrgCol + ",'" + hyIds + "'))";
                    } else {
                        noSql += chilOrgCol + " IN ( SELECT TO_CHAR(ORGID) FROM TBL_ORGANIZATION WHERE ORGTYPE = 0 START WITH FATHERORGID = " + orgid + " CONNECT BY PRIOR FATHERORGID = ORGID UNION ALL SELECT TO_CHAR(ORGID) FROM TBL_ORGANIZATION WHERE ORGID = " + orgid + " UNION ALL SELECT TO_CHAR(ORGID) FROM TBL_ORGANIZATION WHERE ORGTYPE >=100 START WITH FATHERORGID = -1 CONNECT BY PRIOR FATHERORGID = ORGID UNION ALL SELECT TO_CHAR(ORGID) FROM TBL_ORGANIZATION WHERE ORGTYPE >=100 START WITH FATHERORGID IN (SELECT ORGID FROM TBL_ORGANIZATION WHERE ORGTYPE >=100  AND FATHERORGID = -1) CONNECT BY FATHERORGID = PRIOR ORGID )";
                    }

                    String choiceSql = "";
                    if (choiceMap != null && choiceMap.size() != 0) {
                        for (Map.Entry<String, String> entry : choiceMap.entrySet()) {
                            choiceSql += " AND " + entry.getKey() + " = '" + entry.getValue() + "'";
                        }
                    }
                    noSql += choiceSql + " )" + choiceSql;
                } else {
                        if(DateBaseConfig.DATABASETYPE.equals("Oracle")) {
                        if (parentNumber.indexOf("_") != -1) {
                            jgf += "%' escape '/";
                        } else {
                            jgf += "%";
                        }
                    } else {
                        jgf += "%";
                    }
                    noSql = "SELECT MAX(" + chilNumberCol + ") FROM " + chilTblName + " WHERE " + chilNumberCol + " LIKE '" + jgf + "' AND ";
                    if(DateBaseConfig.DATABASETYPE.equals("MySql")) {
                        noSql += "(FIND_IN_SET(" + chilOrgCol + ",'" + childIds + "') OR " + chilOrgCol + " =" + orgid + " OR FIND_IN_SET(" + chilOrgCol + ",'" + hyIds + "'))";
                    } else {
                        noSql += chilOrgCol + " IN ( SELECT TO_CHAR(ORGID) FROM TBL_ORGANIZATION WHERE ORGTYPE = 0 START WITH FATHERORGID = " + orgid + " CONNECT BY PRIOR FATHERORGID = ORGID UNION ALL SELECT TO_CHAR(ORGID) FROM TBL_ORGANIZATION WHERE ORGID = " + orgid + " UNION ALL SELECT TO_CHAR(ORGID) FROM TBL_ORGANIZATION WHERE ORGTYPE >=100 START WITH FATHERORGID = -1 CONNECT BY PRIOR ORGID = FATHERORGID UNION ALL SELECT TO_CHAR(ORGID) FROM TBL_ORGANIZATION WHERE ORGTYPE >=100 START WITH FATHERORGID IN (SELECT ORGID FROM TBL_ORGANIZATION WHERE ORGTYPE >=100  AND FATHERORGID = -1) CONNECT BY FATHERORGID = PRIOR ORGID UNION ALL SELECT TO_CHAR(ORGID) FROM TBL_ORGANIZATION WHERE ORGTYPE >=100 START WITH FATHERORGID IN (SELECT ORGID FROM TBL_ORGANIZATION WHERE ORGTYPE >=100  AND FATHERORGID = -1) CONNECT BY FATHERORGID = PRIOR ORGID) ";
                    }

                    noSql += " AND LENGTH(REPLACE(" + chilNumberCol + ",'" + parentNumber + "','')) = (SELECT MIN(LENGTH(REPLACE(" + chilNumberCol + ",'" + parentNumber + "',''))) FROM " + chilTblName + " WHERE " + chilNumberCol + " LIKE '" + jgf + "' AND LENGTH(REPLACE(" + chilNumberCol + ",'" + parentNumber + "','')) != 0 AND ";


                    if(DateBaseConfig.DATABASETYPE.equals("MySql")) {
                        noSql += "(FIND_IN_SET(" + chilOrgCol + ",'" + childIds + "') OR " + chilOrgCol + " =" + orgid + " OR FIND_IN_SET(" + chilOrgCol + ",'" + hyIds + "'))";
                    } else {
                        noSql += chilOrgCol + " IN ( SELECT TO_CHAR(ORGID) FROM TBL_ORGANIZATION WHERE ORGTYPE = 0 START WITH FATHERORGID = " + orgid + " CONNECT BY PRIOR FATHERORGID = ORGID UNION ALL SELECT TO_CHAR(ORGID) FROM TBL_ORGANIZATION WHERE ORGID = " + orgid + " UNION ALL SELECT TO_CHAR(ORGID) FROM TBL_ORGANIZATION WHERE ORGTYPE >=100 START WITH FATHERORGID = -1 CONNECT BY PRIOR FATHERORGID = ORGID UNION ALL SELECT TO_CHAR(ORGID) FROM TBL_ORGANIZATION WHERE ORGTYPE >=100 START WITH FATHERORGID IN (SELECT ORGID FROM TBL_ORGANIZATION WHERE ORGTYPE >=100  AND FATHERORGID = -1) CONNECT BY FATHERORGID = PRIOR ORGID )";
                    }

                    String choiceSql = "";
                    if (choiceMap != null && choiceMap.size() != 0) {
                        for (Map.Entry<String, String> entry : choiceMap.entrySet()) {
                            choiceSql += " AND " + entry.getKey() + " = '" + entry.getValue() + "'";
                        }
                    }
                    noSql += choiceSql + " )" + choiceSql;
                    noSql += " AND (LENGTH(REPLACE(" + chilNumberCol + ",'" + parentNumber + "','')) - LENGTH(REPLACE(REPLACE(" + chilNumberCol + ",'" + parentNumber + "',''),'-',''))) = 0"
                            + " AND (LENGTH(REPLACE(" + chilNumberCol + ",'" + parentNumber + "','')) - LENGTH(REPLACE(REPLACE(" + chilNumberCol + ",'" + parentNumber + "',''),'_',''))) = 0"
                            + " AND (LENGTH(REPLACE(" + chilNumberCol + ",'" + parentNumber + "','')) - LENGTH(REPLACE(REPLACE(" + chilNumberCol + ",'" + parentNumber + "',''),',',''))) = 0"
                            + " AND (LENGTH(REPLACE(" + chilNumberCol + ",'" + parentNumber + "','')) - LENGTH(REPLACE(REPLACE(" + chilNumberCol + ",'" + parentNumber + "',''),'.',''))) = 0"
                            + " AND (LENGTH(REPLACE(" + chilNumberCol + ",'" + parentNumber + "','')) - LENGTH(REPLACE(REPLACE(" + chilNumberCol + ",'" + parentNumber + "',''),'~',''))) = 0"
                            + " AND (LENGTH(REPLACE(" + chilNumberCol + ",'" + parentNumber + "','')) - LENGTH(REPLACE(REPLACE(" + chilNumberCol + ",'" + parentNumber + "',''),'`',''))) = 0";
                }
                String code = tblAutonoNumberMapper.selectUniqueColumn(noSql);
                return getNewCodeByParentNumber(code, orgNo, parentNumber);
            }
        }
    }

    /**
     * 产生新的编号保持上下级关系的编号
     *
     * @param result
     * @param orgNo
     * @return
     */
    private String getNewCodeByParentNumber(String result, TblOrgNoId orgNo, String parentNumber) {
        String code;
        if (result == null || "".equals(result)) {
            code = parentNumber;
            if (orgNo.getNoSepartor() != null) {
                code += orgNo.getNoSepartor();
            }
            if (orgNo.getNoNumber() == -1) {
                code += "01";
            } else if (orgNo.getNoNumber() == 1) {
                code += "00001";
            } else if (orgNo.getNoNumber() == 2) {
                code += "001";
            }
        } else {
            String num = result.replace(parentNumber, "");
            if (orgNo.getNoSepartor() != null) {
                parentNumber += orgNo.getNoSepartor();
                num = num.substring(1, num.length());
            }
            Integer no = Integer.parseInt(num);
            no++;
            int nolength = num.length() - no.toString().length();
            for (int i = 0; i < nolength; i++) {
                parentNumber += "0";
            }
            code = parentNumber + no;
        }

        return code;
    }

    @Override
    public String getNewCodeByHyZsk(BigDecimal orgid, Integer noId, String parentId, String parentTblName, String parentIdCol,
                                    String parentFatherCol, String parentNumberCol, String ancestorsNo, String chilTblName, String chilNumberCol,
                                    String chilOrgCol, String chChoiceCol, String choiceVal, String bjf, String token) throws Exception {
        Integer isUse = this.getIsUseAutoNoInfo(orgid);
        if (isUse == 0) {
            return "-1";//该组织没有使用自定义编码
        } else {
            TblOrgNoId orgNo = this.getCodeRule(orgid, noId);
            if (orgNo == null) {
                return "-1";
            } else {
                String fatherSql = "SELECT " + parentFatherCol + " FROM " + parentTblName + " WHERE " + parentIdCol + " = " + parentId;
                String fatherId = tblAutonoNumberMapper.selectUniqueColumn(fatherSql);

                //如果是根级则产生新的编号，否则产生下级编号
                String code;
                if (ancestorsNo.equals(fatherId)) {
                    code = this.findFlowNextId(chilTblName, chilNumberCol, chilOrgCol, noId, chChoiceCol, choiceVal, bjf, token).getData().toString();
                } else {
                    Map<String, String> choiceMap = null;
                    if (choiceVal != null) {
                        choiceMap = new HashMap<String, String>(0);
                        choiceMap.put(chChoiceCol, choiceVal);
                    }
                    code = this.findNumberLevelNexidByParent(noId, parentNumberCol, parentTblName, parentIdCol, parentId, chilNumberCol, chilTblName, chilOrgCol, orgid, choiceMap);
                }
                return code;
            }
        }
    }

    /**
     * 获取组织是否使用自定义编号
     *
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
     *
     * @param orgid
     * @param noId
     * @return
     * @throws Exception
     */
    private TblOrgNoId getCodeRule(BigDecimal orgid, Integer noId) throws Exception {
        List<JSONObject> objList = tblAutonoNumberMapper.getcodeRule(orgid, noId);
        TblOrgNoId orgNo = null;
        for (Object object : objList) {
        	JSONObject ob=(JSONObject)object;
            orgNo = new TblOrgNoId();
 			orgNo.setNoSepartor(ob.getString("SEPARTOR"));
 			orgNo.setNoNumber(ob.getString("NUMBER")!=null?Integer.parseInt(ob.getString("NUMBER")):0);
            orgNo.setNocode(ob.getString("CODE"));
        }
        return orgNo;
    }

    /**
     * 产生新的编号不保持上下级关系的编号
     *
     * @param result
     * @param orgNo
     * @return
     */
    private String getNewCode(String result, TblOrgNoId orgNo) {
        String code;
        if (result == null || "".equals(result)) {
            code = orgNo.getNocode();
            if (orgNo.getNoSepartor() != null) {
                code += orgNo.getNoSepartor();
            }

            if (null != orgNo.getNoNumber()) {
                if (orgNo.getNoNumber() == -1) {
                    code += "01";
                } else if (orgNo.getNoNumber() == 1) {
                    code += "00001";
                } else if (orgNo.getNoNumber() == 2) {
                    code += "001";
                }
            }

        } else {
            if (orgNo.getNoSepartor() == null) {
                String num = result.replace(orgNo.getNocode(), "");
                Integer no = Integer.parseInt(num);
                no++;
                int nolength = num.length() - no.toString().length();
                code = orgNo.getNocode();
                for (int i = 0; i < nolength; i++) {
                    code += "0";
                }
                code += no;
            } else {
                String num = result.replace(orgNo.getNocode() + orgNo.getNoSepartor(), "");
                Integer no = Integer.parseInt(num);
                no++;
                int nolength = num.length() - no.toString().length();
                code = orgNo.getNocode() + orgNo.getNoSepartor();
                for (int i = 0; i < nolength; i++) {
                    code += "0";
                }
                code += no;
            }
        }
        return code;
    }
    
    

	/**
	 * 此方法 适用于 一节点所在的表存在组织Id 并且 在该一级节点下的二级、三级节点等等所在的表，都存在一级节点的主键列，
	 * 满足上述两个条件，节点都存在于一张表也可使用此方法
	 * 
	 * 
	 * 传入参数说明 
	 * chTblName ---- 插入子节点所在的表 
	 * chNumberCol --- 插入子节点所在的表 编号的列名 
	 * chirldIdCol  ----  子节点表中一级节点的父列名
	 * chOrgCol --- 编号所在的组织的列名  此处无用
	 * orgid -----  组织ID
	 * noId  ------ TBL_AUTONO_INFO的主键标识 列入 流程编号就是3 
	 * parentTblName --- 父节点所在的表
	 * parentIdCol   ---- 父节点主键ID的列名
	 * parentId ----- 父节点的主键值
	 * parentNumberCol --- 父节点编号的 列名
	 * grandFatherTblName ---- 一级节点所在的表名
	 * grandFatherIdCol  ----- 一级节点主键ID  的列名
	 * grandFatherOrgCol  ----- 一级节点所在的组织 
	 */
    @Override
    public String findNumberLevelNextId(String chTblName, String chNumberCol,
			String chOrgCol, BigDecimal orgid, Integer noId,
			String parentTblName, String parentIdCol, String parentId,
			String grandFatherTblName,String grandFatherIdCol,String grandFatherOrgCol,String chirldIdCol,String parentNumberCol,String token) throws Exception{
    	   TblStaffUtil loginStaff = userProvider.get();
           if (loginStaff == null) {
        	   return "-1";
           }
    	Integer isUse = this.getIsUseAutoNoInfo(orgid);
		if(isUse == 0){
			return "-1";//该组织没有使用自定义编码
		}else{
			TblOrgNoId orgNo = this.getCodeRule(orgid,noId);
			if(orgNo == null){
				return "-1";
			}else{
				String parentSql = "SELECT "+parentNumberCol+" FROM "+parentTblName+" WHERE "+parentIdCol+" = "+parentId;
				String parentNumber = tblAutonoNumberMapper.selectUniqueColumn(parentSql);
				String jgf = parentNumber;
				String noSql = null;
				//Oracle数据库特殊字符处理
				jgf = parentNumber.replace("_","/_");
				Object obj = 0;
                    if(DateBaseConfig.DATABASETYPE.equals("MySql")) {
					obj = this.tblAutonoNumberMapper.selectUniqueColumn("select getChildrenDeptList("+orgid+")");
				}
				
				if(orgNo.getNoSepartor()!=null){
	               if(DateBaseConfig.DATABASETYPE.equals("Oracle")) {
						if(parentNumber.indexOf("_") != -1){
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
						jgf += "%";
					}
					
					noSql = "SELECT MAX("+chNumberCol+") FROM "+chTblName+" WHERE "+chNumberCol+" LIKE '"+jgf+"' AND LENGTH(REPLACE("+chNumberCol+",'"+parentNumber+"','')) != 0  AND LENGTH(REPLACE("
							+chNumberCol+",'"+parentNumber+"','')) = ( SELECT MIN(LENGTH(REPLACE("
							+chNumberCol+",'"+parentNumber+"',''))) FROM "+chTblName+" WHERE "+chNumberCol+" LIKE '"+jgf+"' "
							+" AND LENGTH(REPLACE("+chNumberCol+",'"+parentNumber+"','')) != 0"
							+" AND  "+chirldIdCol+" IN ( SELECT "+grandFatherIdCol+" FROM "+grandFatherTblName+" WHERE ";
							
					        if(DateBaseConfig.DATABASETYPE.equals("MySql")) {
								noSql += "(FIND_IN_SET("+grandFatherOrgCol+",'"+obj+"') OR "+grandFatherOrgCol+" = "+orgid+")";
							}else{
								noSql += grandFatherOrgCol+" IN ( SELECT ORGID FROM TBL_ORGANIZATION WHERE ORGTYPE = 0 START WITH FATHERORGID = "+orgid +" CONNECT BY PRIOR FATHERORGID = ORGID	UNION ALL SELECT ORGID FROM TBL_ORGANIZATION WHERE ORGID = "+orgid+" ) ";
							}
							noSql += ") ) AND  "+chirldIdCol+" IN ( SELECT "+grandFatherIdCol+" FROM "+grandFatherTblName+" WHERE ";
					        if(DateBaseConfig.DATABASETYPE.equals("MySql")) {
								noSql += "(FIND_IN_SET("+grandFatherOrgCol+",'"+obj+"') OR "+grandFatherOrgCol+" = "+orgid+")";
							}else{
								noSql+= grandFatherOrgCol+" IN ( SELECT ORGID FROM TBL_ORGANIZATION WHERE ORGTYPE = 0 START WITH FATHERORGID = "+orgid+" CONNECT BY PRIOR FATHERORGID = ORGID UNION ALL SELECT ORGID FROM TBL_ORGANIZATION WHERE ORGID = "+orgid+" )";
							}		
							noSql += " ) AND (LENGTH(REPLACE("+chNumberCol+",'"+parentNumber+"','')) - LENGTH(REPLACE(REPLACE("+chNumberCol+",'"+parentNumber+"',''),'"+orgNo.getNoSepartor()+"',''))) = 1";
				}else{
			        if(DateBaseConfig.DATABASETYPE.equals("Oracle")) {
						if(parentNumber.indexOf("_") != -1){
							jgf += "%' escape '/";
						}else{
							jgf += "%";
						}
					}else{
						jgf += "%";
					}
					noSql = "SELECT MAX("+chNumberCol+") FROM "+chTblName+" WHERE "+chNumberCol+" LIKE '"+jgf+"' AND LENGTH(REPLACE("+chNumberCol+",'"+parentNumber+"','')) != 0  AND LENGTH(REPLACE("
							+chNumberCol+",'"+parentNumber+"','')) = ( SELECT MIN(LENGTH(REPLACE("
							+chNumberCol+",'"+parentNumber+"',''))) FROM "+chTblName+" WHERE "+chNumberCol+" LIKE '"+jgf+"'"
							+" AND LENGTH(REPLACE("+chNumberCol+",'"+parentNumber+"','')) != 0"
							+" AND  "+chirldIdCol+" IN ( SELECT "+grandFatherIdCol+" FROM "+grandFatherTblName+" WHERE ";
					
			        if(DateBaseConfig.DATABASETYPE.equals("MySql")) {
						noSql += "(FIND_IN_SET("+grandFatherOrgCol+",'"+obj+"') OR "+grandFatherOrgCol+" = "+orgid+")";
					}else{
						noSql += grandFatherOrgCol+ " IN ( SELECT ORGID FROM TBL_ORGANIZATION WHERE ORGTYPE = 0 START WITH FATHERORGID = "+orgid+" CONNECT BY PRIOR FATHERORGID = ORGID UNION ALL SELECT ORGID FROM TBL_ORGANIZATION WHERE ORGID = "+orgid+" ) ";
					}				
					
					noSql += " )) AND  "+chirldIdCol+" IN ( SELECT "+grandFatherIdCol+" FROM "+grandFatherTblName+" WHERE ";
					
			        if(DateBaseConfig.DATABASETYPE.equals("MySql")) {
						noSql += "(FIND_IN_SET("+grandFatherOrgCol+",'"+obj+"') OR "+grandFatherOrgCol+" = "+orgid+")";
					}else{
						noSql += grandFatherOrgCol +" IN ( SELECT ORGID FROM TBL_ORGANIZATION WHERE ORGTYPE = 0 START WITH FATHERORGID = "+orgid+" CONNECT BY PRIOR FATHERORGID = ORGID UNION ALL SELECT ORGID FROM TBL_ORGANIZATION WHERE ORGID = "+orgid+" ) ";
					}
					
					noSql += " ) AND (LENGTH(REPLACE("+chNumberCol+",'"+parentNumber+"','')) - LENGTH(REPLACE(REPLACE("+chNumberCol+",'"+parentNumber+"',''),'-',''))) = 0"
							+" AND (LENGTH(REPLACE("+chNumberCol+",'"+parentNumber+"','')) - LENGTH(REPLACE(REPLACE("+chNumberCol+",'"+parentNumber+"',''),'_',''))) = 0"
							+" AND (LENGTH(REPLACE("+chNumberCol+",'"+parentNumber+"','')) - LENGTH(REPLACE(REPLACE("+chNumberCol+",'"+parentNumber+"',''),',',''))) = 0"
							+" AND (LENGTH(REPLACE("+chNumberCol+",'"+parentNumber+"','')) - LENGTH(REPLACE(REPLACE("+chNumberCol+",'"+parentNumber+"',''),'.',''))) = 0"
							+" AND (LENGTH(REPLACE("+chNumberCol+",'"+parentNumber+"','')) - LENGTH(REPLACE(REPLACE("+chNumberCol+",'"+parentNumber+"',''),'~',''))) = 0"
							+" AND (LENGTH(REPLACE("+chNumberCol+",'"+parentNumber+"','')) - LENGTH(REPLACE(REPLACE("+chNumberCol+",'"+parentNumber+"',''),'`',''))) = 0";
				}
				
				String code = tblAutonoNumberMapper.selectUniqueColumn(noSql);
				return getNewCodeByParentNumber(code,orgNo,parentNumber);
			}
		}
	}
	
    
    /**
	 * 此方法 根据父级的组织ID，查询该组织下的所有父级ID,在根据父级ID 查询出需要插入信息的最大编号
	 * 适用于，插入的子级信息无法依靠组织划分需要依靠父级划分。子级的编号独立，与父级编号产生上下级的关系，
	 * 只要上级编号存在组织信息，就可查询下级编号。
	 * 
	 * chidNumberCol ---- 子表中编号的列名
	 * chilTblName   ---- 子表的表名
	 * chilParentCol ---- 子表中父级列的列名
	 * parentIdCol   ---- 父表主键ID的列名
	 * parentTblName ---- 父表的表名
	 * parnetOrgCol  ---- 父表中组织的列名
	 * orgid         ---- 组织ID
	 * noId          ---- 编号信息表对应的Id
	 * middleTblname ---- 中间表表名
	 * middleChilCol ---- 中间表子列的列名
	 * middleParentCol ---- 中间表父列的列名
	 */
	@Override
	public String findRootNumberByParentIdLevel(String chilNumberCol,String chilTblName, String chilParentCol, String parentIdCol,
			String parentTblName, String parnetOrgCol,String parentNumberCol,String parentId, BigDecimal orgid,Integer noId, String middleTblname, String middleChilCol,
			String middleParentCol,String middleIdCol, String middleNumberCol,String token) throws Exception{
 	   TblStaffUtil loginStaff = userProvider.get();
       if (loginStaff == null) {
    	   return "-1";
       }
		Integer isUse = this.getIsUseAutoNoInfo(orgid);
		if(isUse == 0){
			return "-1";//该组织没有使用自定义编码
		}else{
			TblOrgNoId orgNo = this.getCodeRule(orgid,noId);
			if(orgNo == null){
				return "-1";
			}else{
				Object chids = 0;
			        if(DateBaseConfig.DATABASETYPE.equals("MySql")) {
					chids = this.tblAutonoNumberMapper.selectUniqueColumn("select getChildrenDeptList("+orgid+")");
				}
				
				String parentSql = "SELECT "+parentNumberCol+" FROM "+parentTblName+" WHERE "+parentIdCol+" = "+parentId;
				if(middleIdCol != null){
					parentSql = "SELECT "+middleNumberCol+" FROM "+middleTblname+" WHERE "+middleIdCol+" = "+parentId;
				}
				String parentNumber = tblAutonoNumberMapper.selectUniqueColumn(parentSql);
				String jgf = parentNumber;
				String noSql = null;
				//Oracle数据库特殊字符处理
				if(jgf.indexOf("_") != -1){
					jgf=jgf.replace("_","/_");
				}
				
				noSql = "SELECT MAX("+chilNumberCol+") FROM "+chilTblName+" WHERE "+chilParentCol+" IN (";
				if(middleTblname == null){
					noSql += "SELECT "+parentIdCol+" FROM "+parentTblName+" WHERE ";
					if(DateBaseConfig.DATABASETYPE.equals("MySql")) {
						noSql += "( FIND_IN_SET("+parnetOrgCol+",'"+chids+"') OR "+parnetOrgCol+" ="+orgid+" )";
					}else{
						noSql += parnetOrgCol+" IN	(SELECT ORGID FROM TBL_ORGANIZATION WHERE ORGTYPE = 0 START WITH FATHERORGID = "+orgid+" CONNECT BY PRIOR FATHERORGID = ORGID UNION ALL SELECT ORGID FROM TBL_ORGANIZATION WHERE ORGID = "+orgid+")";
					}
				}else{
					noSql += "SELECT "+middleChilCol+" FROM "+middleTblname+" WHERE ";
					if(DateBaseConfig.DATABASETYPE.equals("MySql")) {
						noSql += middleParentCol+" IN (SELECT "+parentIdCol+" FROM "+parentTblName+" WHERE ( FIND_IN_SET("+parnetOrgCol+",getChildrenDeptList("+orgid+")) OR "+parnetOrgCol+" ="+orgid+"))";
					}else{
						noSql += middleParentCol+" IN (SELECT "+parentIdCol+" FROM "+parentTblName+" WHERE "+parnetOrgCol+" IN (SELECT TO_CHAR(ORGID) FROM TBL_ORGANIZATION WHERE ORGTYPE = 0 START WITH FATHERORGID = "+orgid+" CONNECT BY PRIOR FATHERORGID = ORGID"
								+" UNION ALL SELECT TO_CHAR(ORGID) FROM TBL_ORGANIZATION WHERE ORGID = "+orgid+" ))";
					}
				}
				noSql += ")";
				if(orgNo.getNoSepartor()!=null){
						if(DateBaseConfig.DATABASETYPE.equals("Oracle")) {
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
					noSql += " AND "+chilNumberCol+" LIKE '"+jgf+"' AND (LENGTH(REPLACE("+chilNumberCol+",'"+parentNumber+"','')) - LENGTH(REPLACE(REPLACE("+chilNumberCol+",'"+parentNumber+"',''),'"+orgNo.getNoSepartor()+"',''))) = 1";
				}else{
					if(DateBaseConfig.DATABASETYPE.equals("Oracle")) {
						if(jgf.indexOf("_") != -1){
							jgf += "%' escape '/";
						}else{
							jgf += orgNo.getNoSepartor()+"%";
						}
					}else{
						jgf += "%";
					}
					noSql += " AND "+chilNumberCol+" LIKE '"+jgf+"' AND (LENGTH(REPLACE("+chilNumberCol+",'"+parentNumber+"','')) - LENGTH(REPLACE(REPLACE("+chilNumberCol+",'"+parentNumber+"',''),'-',''))) = 0"
							+" AND (LENGTH(REPLACE("+chilNumberCol+",'"+parentNumber+"','')) - LENGTH(REPLACE(REPLACE("+chilNumberCol+",'"+parentNumber+"',''),'_',''))) = 0 AND (LENGTH(REPLACE("+chilNumberCol+",'"+parentNumber+"','')) - LENGTH(REPLACE(REPLACE("+chilNumberCol+",'"+parentNumber+"',''),',',''))) = 0"
							+" AND (LENGTH(REPLACE("+chilNumberCol+",'"+parentNumber+"','')) - LENGTH(REPLACE(REPLACE("+chilNumberCol+",'"+parentNumber+"',''),'`',''))) = 0 AND (LENGTH(REPLACE("+chilNumberCol+",'"+parentNumber+"','')) - LENGTH(REPLACE(REPLACE("+chilNumberCol+",'"+parentNumber+"',''),'.',''))) = 0"
							+" AND (LENGTH(REPLACE("+chilNumberCol+",'"+parentNumber+"','')) - LENGTH(REPLACE(REPLACE("+chilNumberCol+",'"+parentNumber+"',''),'~',''))) = 0";
				}
				
				
				String code = tblAutonoNumberMapper.selectUniqueColumn(noSql);
				return getNewCodeByParentNumber(code,orgNo,parentNumber);
			}
		}
	}

}
