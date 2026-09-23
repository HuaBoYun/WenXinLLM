package com.huabo.cybermonitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hbfk.util.PageInfo;
import com.huabo.cybermonitor.entity.*;
import com.huabo.cybermonitor.mapper.FormControeleMapper;
import com.huabo.cybermonitor.mapper.FormControllogMapper;
import com.huabo.cybermonitor.mapper.FormControlruleMapper;
import com.huabo.cybermonitor.mapper.RuleDetpMapper;
import com.huabo.cybermonitor.service.IFormControlruleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.cybermonitor.util.DateUtils;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author kangjx
 * @since 2022-07-13
 */
@Service
public class FormControlruleServiceImpl extends ServiceImpl<FormControlruleMapper, FormControlrule> implements IFormControlruleService {

    @Autowired
    FormControlruleMapper formControlruleMapper;

    @Autowired
    FormControeleMapper formControeleMapper;

    @Autowired
    RuleDetpMapper ruleDetpMapper;

    @Autowired
    FormControllogMapper formControllogMapper;



    @Override
    public void findPageInfo(PageInfo<FormControlrule> pageInfo)
            throws Exception {
        String sql = "FROM TblFormControlrule where 1 = 1 ";
        String sqlCount = "SELECT COUNT(*) FROM TblFormControlrule where 1 = 1 ";
        FormControlrule tfcr = pageInfo.getCondition();
      /*  if(tfcr.getLinkOrgId() != null){
            sql += " AND tblOrganizationByOrgid.orgid = :linkOrgId";
            sqlCount += " AND tblOrganizationByOrgid.orgid = :linkOrgId";
        }
        if(tfcr.getLinkDeptId() != null){
            sql += " AND tblOrganizationByDeptid.orgid = :linkDeptId";
            sqlCount += " AND tblOrganizationByDeptid.orgid = :linkDeptId";
        }
        if(tfcr.getRuleno() != null){
            tfcr.setRuleno("%"+tfcr.getRuleno()+"%");
            sql += " AND ruleno LIKE :ruleno";
            sqlCount += " AND ruleno LIKE :ruleno";
        }
        sql += " ORDER BY createtime DESC";
     //   pageInfo.setTlist(this.tblFormControlRuleDao.selectListByPageInfo(pageInfo,sql));
     //   pageInfo.setTotalRecord(this.tblFormControlRuleDao.selectCountByPageInfo(pageInfo,sqlCount));
*/
    }


    @Override
    @Transactional
    public String insertFormControlRule(FormControlrule rule,
                                        String[] replacekey, String[] replacekvalue, String[] replacetype,
                                        BigDecimal eleId, String reorg) throws Exception {
        String ruleSql = rule.getRulesql();


        List<FormControele> formeleList = new ArrayList<FormControele>(0);


        FormControele controEle = null;
        if(replacekey != null){
            for (int i = 0; i < replacekey.length; i++) {

                controEle = new FormControele();
                controEle.setEleid(eleId);


                controEle.setReplacekey(replacekey[i]);
                controEle.setReplacekvalue(replacekvalue[i]);
                controEle.setReplacetype(replacetype[i]);
                switch (replacetype[i]) {
                    case "文本":
                        ruleSql = ruleSql.replace(replacekey[i], "'测试'");
                        break;
                    case "时间":
                        ruleSql = ruleSql.replace(replacekey[i], "TO_DATE('"+DateUtils.getNowTime()+"','yyyy-MM-dd')");
                        break;
                    case "数字":
                        ruleSql = ruleSql.replace(replacekey[i], "0");
                        break;
                    default:
                        break;
                }
                formeleList.add(controEle);

            }
        }

        System.out.println(ruleSql);
        String result = this.formControlruleMapper.executeSql(ruleSql);

        formControlruleMapper.insert(rule);

        for (FormControele formControele : formeleList) {
                formControele.setRuleid(rule.getRuleid());
                formControeleMapper.insert(formControele);

        }

        if(reorg != null && !"".equals(reorg)){

            String[] orgids = reorg.split(",");
            RuleDetp trd = null;
            for (int i = 0; i < orgids.length; i++) {
                trd = new RuleDetp();

                trd.setOrgid(new BigDecimal(orgids[i]));
                trd.setRuleid(rule.getRuleid());
                this.ruleDetpMapper.insert(trd);
            }
        }
        return rule.getRuleid().toString();
    }



    @Override
    @Transactional
    public String modifyFormControlRule(FormControlruleDto rule,
                                        String[] replacekey, String[] replacekvalue, String[] replacetype,
                                        BigDecimal eleId, String[] reid, String reorg) throws Exception {
        String ruleSql = rule.getRulesql();


        List<FormControele> formeleList = new ArrayList<FormControele>(0);


        FormControele controEle = null;
        if(replacekey != null){
            for (int i = 0; i < replacekey.length; i++) {

                controEle = new FormControele();
                controEle.setEleid(eleId);
                controEle.setRuleid(rule.getRuleid());

                controEle.setReplacekey(replacekey[i]);
                controEle.setReplacekvalue(replacekvalue[i]);
                controEle.setReplacetype(replacetype[i]);
                switch (replacetype[i]) {
                    case "文本":
                        ruleSql = ruleSql.replace(replacekey[i], "'测试'");
                        break;
                    case "时间":
                        ruleSql = ruleSql.replace(replacekey[i], "TO_DATE('"+DateUtils.getNowTime()+"','yyyy-MM-dd')");
                        break;
                    case "数字":
                        ruleSql = ruleSql.replace(replacekey[i], "0");
                        break;
                    default:
                        break;
                }
                if(reid[i] != null && !"".equals(reid[i])) {
                    controEle.setReid(new BigDecimal(reid[i]));
                }
                formeleList.add(controEle);

            }
        }

        System.out.println(ruleSql);
        String result = this.formControlruleMapper.executeSql(ruleSql);
        FormControlrule oldRule = this.formControlruleMapper.selectById(rule.getRuleid());
        oldRule.setRuleno(rule.getRuleno());
        oldRule.setRulename(rule.getRulename());
        oldRule.setRulememo(rule.getRulememo());
        oldRule.setReturnresult(rule.getReturnresult());
        oldRule.setRulesql(rule.getRulesql());
        oldRule.setRulestatus(rule.getRulestatus());
        oldRule.setRuletip(rule.getRuletip());
        this.formControlruleMapper.updateById(oldRule);

        for (FormControele formControele : formeleList) {

            if(formControele.getReid() != null ){

                formControeleMapper.updateById(formControele);
            }else{
                formControeleMapper.insert(formControele);
            }
        }

        if(reorg != null && !"".equals(reorg)){
            QueryWrapper qw=new QueryWrapper();
            qw.eq("RULEID",rule.getRuleid());
            this.ruleDetpMapper.delete(qw);
            String[] orgids = reorg.split(",");
            RuleDetp trd = null;
            for (int i = 0; i < orgids.length; i++) {
                trd = new RuleDetp();

                trd.setOrgid(new BigDecimal(orgids[i]));
                trd.setRuleid(rule.getRuleid());
                this.ruleDetpMapper.insert(trd);
            }
        }
        return rule.getRuleid().toString();
    }

    @Override
    public BigDecimal calculateFormula(String calFormula, String valjson, String eletext) throws Exception {
        calFormula = calFormula.replace("%", "/100").replace("除法", "/").replace("乘法", "*").replace("加法", "+").replace("减法", "-");
        return this.formControlruleMapper.calculateFormula(calFormula);
    }

    @Override
    public void modifyFormStatus(String ruleid, String status) throws Exception {
        this.formControlruleMapper.Update_CONTROLRULE(ruleid,status);
    }


    @Override
    @Transactional
    public String checkFlowFromRule(String jsonArry, String formId, String userName,BigDecimal orgId)
            throws Exception {
        String result = "0";
        String ruleIdsSql;

            ruleIdsSql = "SELECT WMSYS.WM_CONCAT(TFC.RULEID) FROM TBL_FORM_CONTROLRULE TFC LEFT JOIN TBL_FORM_CONTROELE TFE ON TFC.RULEID = TFE.RULEID LEFT JOIN TBL_FORM_ELEMENTS TFM ON TFE.ELEID = TFM.ELEID WHERE TFE.ELEID IS NOT NULL AND (TFC.DEPTID =  "+orgId+" OR TFC.DEPTID IN (SELECT ORGID FROM TBL_ORGANIZATION WHERE ORGTYPE > 0 AND ORGTYPE < 100 START WITH ORGID = "+orgId+" CONNECT BY PRIOR FATHERORGID = ORGID)) AND TFM.FORMID = "+formId;
        String ruleIds = this.formControlruleMapper.executeSql(ruleIdsSql);
        if(ruleIds != null && !"".equals(ruleIds)){
            String ruleListHql = "SELECT * FROM TBL_FORM_CONTROLRULE WHERE ruleid in ("+ruleIds+") AND rulestatus  = 1";
            List<FormControlrule> ruleList = this.formControlruleMapper.findListByIds(ruleListHql);
            result = this.executeRuleSql(jsonArry,ruleList,userName);
        }
        return result;
    }


    private String executeRuleSql(String jsonArry, List<FormControlrule> ruleList, String userName) throws Exception {
        JSONArray jsonArray = JSONArray.fromObject(jsonArry);
        Object[] objs = null;
        Map<String,Object> valueMap = new HashMap<String,Object>(0);
        for (int i = 0; i < jsonArray.size() ; i++) {
            objs = jsonArray.getJSONArray(i).toArray();
            valueMap.put(objs[0].toString().toUpperCase(), objs[1]);
        }
        String ruleSql = null;
        String result = "0";
        String sqlResult;
        List<FormControllog> logList = new ArrayList<FormControllog>(0);
        FormControllog log = null;
        boolean flag = true;
        Object middelChoice;
        //FormControlrule 实体类缺少字段
        for (FormControlrule rule : ruleList) {
            ruleSql = rule.getRulesql();
//            for (FormControele eles : rule.getFormControeles()) {
//                if(eles.getReplacekvalue() != null){
//					/*if(valueMap.get(eles.getReplacekvalue()) == null || "".equals(valueMap.get(eles.getReplacekvalue()))){
//						//result = "监控字段所需字段"+eles.getReplacekvalue()+"不能为空";
//						//flag = false;
//						//break;
//					}*/
//                    switch (eles.getReplacetype()) {
//                        case "数字":
//                            if(valueMap.get(eles.getReplacekvalue()) == null || "".equals(valueMap.get(eles.getReplacekvalue()))){
//                                middelChoice = 0;
//                            }else {
//                                middelChoice = valueMap.get(eles.getReplacekvalue());
//                            }
//                            ruleSql = ruleSql.replace(eles.getReplacekey(),middelChoice+"");
//                            break;
//                        default:
//                            ruleSql = ruleSql.replace(eles.getReplacekey(), "'"+valueMap.get(eles.getReplacekvalue())+"'");
//                            break;
//                    }
//                }
//            }
//            if(flag){
//                log = new FormControllog();
//                log.setCreatedate(LocalDateTime.now());
//                sqlResult = this.formControlruleMapper.executeSql(ruleSql);
//                log.setReturndate(LocalDateTime.now());
//                log.setInparam(ruleSql);
//                log.setExecutestaff(userName);
//                log.setRuleno(rule.getRuleno());
//                log.setReturnresult(sqlResult);
//                log.setOperation("自定义表单规则校验");
//                logList.add(log);
//                if(!rule.getRetrunresult().equals(sqlResult)){
//                    result = rule.getRuletip()+"\n违反规则，规则编码："+rule.getRuleno()+"，规则名称："+rule.getRulename();
//                    break;
//                }
//            }else{
//                break;
//            }
        }
        for (FormControllog logs : logList) {
            this.formControllogMapper.insert(logs);
        }
        return result;
    }



    @Override
    public String checkFlowFromRuleByjsonArray(String jsonArry, String userName, BigDecimal orgid, String ruleNo) throws Exception {
        String result = "0";
        String ruleListHql = "Select * FROM TBL_FORM_CONTROLRULE WHERE ruleno = '"+ruleNo+"' AND rulestatus  = 1";
        List<FormControlrule> ruleList = this.formControlruleMapper.findListByIds(ruleListHql);
        result = this.executeRuleSql(jsonArry,ruleList,userName);
        return result;
    }



    @Override
    public void findFieldRulePageInfo(PageInfo<FormControlrule> pageInfo) throws Exception {
        String sql = "FROM TblFormControlrule where 1 = 1 ";
        String sqlCount = "SELECT COUNT(*) FROM TblFormControlrule where 1 = 1 ";
        FormControlrule tfcr = pageInfo.getCondition();
        if(tfcr.getRuleno() != null){
            tfcr.setRuleno("%"+tfcr.getRuleno()+"%");
            sql += " AND ruleno LIKE :ruleno";
            sqlCount += " AND ruleno LIKE :ruleno";
        }
        //实体类确实字段
//        if(tfcr.getFileId().compareTo(new BigDecimal("1")) == 0){
//            sql += " AND tblOutformField.fieldtype = 'NC6'";
//            sqlCount += " AND tblOutformField.fieldtype = 'NC6'";
//        }else{
//            sql += " AND tblOutformField.fieldid = :fileId";
//            sqlCount += " AND tblOutformField.fieldid = :fileId";
//        }
        sql += " ORDER BY createtime DESC";
        // 实体类缺少
//        pageInfo.setTlist(this.formControlruleMapper.selectListByPageInfo(pageInfo,sql));
//        pageInfo.setTotalRecord(this.formControlruleMapper.selectCountByPageInfo(pageInfo,sqlCount));
    }


    @Override
    public String findVideoPlayUrl(Integer videoId) throws Exception {
        return this.formControeleMapper.selectReportUrl(videoId);
    }

    @Override
    public String findDocFileName(Integer docId) throws Exception {
        return this.formControeleMapper.selectCOURSEWAREURL(docId);
    }

    @Override
    public List<Map<String,Object>> getLeftById(String ruleid) {
        return this.baseMapper.getLeftById(ruleid);
    }

    @Override
    @Transactional
    public int remove_fromRule(BigDecimal ruleid) {
       QueryWrapper queryWrapper=new QueryWrapper();
       queryWrapper.eq("RULEID",ruleid);
       this.formControeleMapper.delete(queryWrapper);
       this.ruleDetpMapper.delete(queryWrapper);

       return this.formControlruleMapper.deleteById(ruleid);
    }


}
