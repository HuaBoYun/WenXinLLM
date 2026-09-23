package com.huabo.cybermonitor.service.impl;

import com.huabo.cybermonitor.mapper.OrganizationMapper;
import com.huabo.cybermonitor.mapper.TblOrgNoId;
import com.huabo.cybermonitor.mapper.YhrPageMapper;
import com.huabo.cybermonitor.service.IAutoId;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Map;

/**
 * @author kangjx
 * @createTime 2022/9/16
 */
@Service
public class AutoIdService implements IAutoId {

    @Resource
    OrganizationMapper organizationServiceMapper;

    @Resource
    YhrPageMapper yhrPageMapper;

    /**
     * 传入参数说明
     * tabName ----  插入编号所在的表 列入 tbl_flow
     * column --- 编号的列名 例如 tbl_flow 表中的 FLOWNUMBER
     * orgCol --- 编号所在的组织的列名  列入 tbl_flow 表中 COMPANY
     * orgid -----  组织ID
     * noId  ------ TBL_AUTONO_INFO的主键标识 列入 流程编号就是3
     * chChoiceCol  -------    插入编号所在的表添加额外条件的列名，例如风险分类里的 MODULETYPE
     * choiceVal   ---------   插入编号所在的表添加额外条件的值，例如风险分类里的 MODULETYPE 的值FXSJK
     */
    @Override
    public String findFlowNextId(String tblName, String column, String orgCol, BigDecimal orgid, Integer NoId, String chChoiceCol, String choiceVal, String bjf) throws Exception {

        Integer isUse = organizationServiceMapper.selectUniqueColumn(orgid);
        if (isUse == 0) {
            return "-1";//该组织没有使用自定义编码
        } else {
            String codeRuleSql = "SELECT CASE WHEN TON.ISUSEDEFAULT = 0 THEN TAI.NODEFAULTCODE	ELSE TON.NOCODE	END AS \"CODE\" ," +
                    "CASE WHEN TON.ISUSEDEFAULT = 0 THEN TAI.NODEFAULTSEPARTOR ELSE TON.NOSEPARTOR END AS  SEP,	CASE WHEN TON.ISUSEDEFAULT = 0 THEN TAI.NODEFAULTNUMBER ELSE TON.NONUMBER END AS \"NUMBER\"" +
                    " FROM TBL_AUTONO_INFO TAI LEFT JOIN TBL_ORG_NO TON ON TAI.NOID = TON.NOID WHERE TON.ORGID = " + orgid + " AND TON.NOID = " + NoId;
            Map<String, Object> orgNo = yhrPageMapper.queryBySql(codeRuleSql);


            if (orgNo == null) {
                return "-1";
            } else {
                String noSql = null;
                String result = null;
                String jgf = String.valueOf(orgNo.get("CODE"));
                String noCode = String.valueOf(orgNo.get("CODE"));
                String sep = String.valueOf(orgNo.get("SEP"));

                TblOrgNoId ton = new TblOrgNoId();
                ton.setNocode(noCode);
                ton.setNoNumber(Integer.valueOf(String.valueOf(orgNo.get("NUMBER"))));
                if (sep != null) {
                    ton.setNoSepartor(sep);
                }

                if (jgf.indexOf("_") != -1) {
                    jgf = jgf.replace("_", "/_");
                }

                //判断插入的是一级编号和同级编号是
                noSql = "SELECT MAX(" + column + ") FROM " + tblName + " WHERE ";

                noSql += " TO_CHAR(" + orgCol + ") IN ( SELECT TO_CHAR(ORGID) FROM TBL_ORGANIZATION WHERE ORGTYPE = 0 START WITH FATHERORGID = " + orgid + " CONNECT BY PRIOR FATHERORGID = ORGID UNION ALL SELECT TO_CHAR(ORGID) FROM TBL_ORGANIZATION WHERE ORGID = " + orgid + " UNION ALL SELECT TO_CHAR(ORGID) FROM TBL_ORGANIZATION WHERE ORGTYPE >=100 START WITH FATHERORGID = -1 CONNECT BY PRIOR ORGID = FATHERORGID ) ";

                //判断用户有没有使用分隔符
                if (sep != null) {

                    if (jgf.indexOf("_") != -1) {
                        if ("_".equals(sep)) {
                            jgf += "/" + sep;
                        } else {
                            jgf += sep;
                        }
                        jgf += "%' escape '/";
                    } else {
                        if ("_".equals(sep)) {
                            jgf += "/" + sep + "%' escape '/";
                        } else {
                            jgf += sep + "%";
                        }
                    }

                    noSql += " AND " + column + " LIKE '" + jgf + "' AND (LENGTH(REPLACE(" + column + ",'" + noCode + "','')) - LENGTH(REPLACE(REPLACE(" + column + ",'" + noCode + "',''),'" + sep + "',''))) = " + 1;
                } else {

                    if (jgf.indexOf("_") != -1) {
                        jgf += "%' escape '/";
                    } else {
                        jgf += "%";
                    }

                    noSql += " AND " + column + " LIKE '" + jgf + "' AND (LENGTH(REPLACE(" + column + ",'" + noCode + "','')) - LENGTH(REPLACE(REPLACE(" + column + ",'" + noCode + "',''),'-',''))) = 0 AND (LENGTH(REPLACE(" + column + ",'" + noCode + "','')) - LENGTH(REPLACE(REPLACE(" + column + ",'" + noCode + "',''),'_','')))  = 0"
                            + " AND (LENGTH(REPLACE(" + column + ",'" + noCode + "','')) - LENGTH(REPLACE(REPLACE(" + column + ",'" + noCode + "',''),',','')))  = 0 AND (LENGTH(REPLACE(" + column + ",'" + noCode + "','')) - LENGTH(REPLACE(REPLACE(" + column + ",'" + noCode + "',''),'.','')))  = 0"
                            + " AND (LENGTH(REPLACE(" + column + ",'" + noCode + "','')) - LENGTH(REPLACE(REPLACE(" + column + ",'" + noCode + "',''),'`','')))  = 0 AND (LENGTH(REPLACE(" + column + ",'" + noCode + "','')) - LENGTH(REPLACE(REPLACE(" + column + ",'" + noCode + "',''),'~','')))  = 0";
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
                System.out.println(noSql);
                result = yhrPageMapper.querySqlRetnString(noSql);
                return getNewCode(result, ton);
            }
        }
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
            if (orgNo.getNoNumber() == -1) {
                code += "01";
            } else if (orgNo.getNoNumber() == 1) {
                code += "00001";
            } else if (orgNo.getNoNumber() == 2) {
                code += "001";
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

}
