package com.huabo.fxgl.service.impl;

import com.huabo.fxgl.entity.AutonoInfo;
import com.huabo.fxgl.entity.OrgNo;
import com.huabo.fxgl.mapper.AutonoInfoMapper;
import com.huabo.fxgl.service.IAutonoInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.huabo.fxgl.util.SysConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.huabo.fxgl.mapper.AutonoInfoMapper;
import com.huabo.fxgl.service.IAutonoInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LiYe
 * @since 2022-08-15
 */
@Service
@Slf4j
public class AutonoInfoServiceImpl extends ServiceImpl<AutonoInfoMapper, AutonoInfo> implements IAutonoInfoService {

    @Autowired
    private AutonoInfoMapper autonoInfoMapper;

    @Override
    public String findFlowNextId(String tblName, String column, String orgCol, BigDecimal orgid, Integer noId, String chChoiceCol, String choiceVal, String bjf) {
        Integer isUse = this.getIsUseAutoNoInfo(orgid);
        if(isUse == 0){
            return "-1";//该组织没有使用自定义编码
        }else{
            OrgNo orgNo = autonoInfoMapper.getCodeRule(orgid,noId);
            String orgNocode = orgNo.getNocode();
            String orgNosepartor = orgNo.getNosepartor();
            if(orgNo == null){
                return "-1";
            }else{
                String result = null;
                String jgf = orgNo.getNocode();
                if(jgf.indexOf("_") != -1){
                    jgf=jgf.replace("_","/_");
                }
                if(orgNo.getNosepartor()!=null){
                    if("Oracle".equals(SysConfig.get("databaseType"))){
                        if(jgf.indexOf("_") != -1){
                            if("_".equals(orgNo.getNosepartor())){
                                jgf += "/"+orgNo.getNosepartor();
                            }else{
                                jgf += orgNo.getNosepartor();
                            }
                            jgf += "%' escape '/";
                        }else{
                            if("_".equals(orgNo.getNosepartor())){
                                jgf += "/"+orgNo.getNosepartor() + "%' escape '/";
                            }else{
                                jgf += orgNo.getNosepartor()+"%";
                            }
                        }
                    }else{
                        jgf += "%";
                    }
                }else{
                    if("Oracle".equals(SysConfig.get("databaseType"))){
                        if(jgf.indexOf("_") != -1){
                            jgf += "%' escape '/";
                        }else{
                            jgf += "%";
                        }
                    }else{
                        jgf += "%";
                    }
                }
                if(chChoiceCol != null){
                    chChoiceCol = " AND " + chChoiceCol;
                    if("包含".equals(bjf)){
                        bjf = "LIKE";
                        choiceVal = "'%"+choiceVal+"%'";
                    }else if("等于".equals(bjf)){
                        bjf="=";
                        choiceVal = "'"+choiceVal+"'";
                    }
                }
                //查询是否存在规则编号 ， 没有就新增 有就在此基础上加1

                result = autonoInfoMapper.selectUniqueColumn( tblName, column,  orgCol,
                         orgid, noId, chChoiceCol, choiceVal, bjf, orgNocode, orgNosepartor,jgf);
                return getNewCode(result,orgNo);
            }
        }
    }

    @Override
    public Integer getIsUseAutoNoInfo(BigDecimal orgid) {
        return autonoInfoMapper.getIsUseAutoNoInfo(orgid);
    }

    @Override
    public String findNumberLevelNexidByParent(Integer noId, String parentNumberCol, String parentTblName, String parentIdCol, String parentId, String chilNumberCol, String chilTblName, String chilOrgCol, BigDecimal orgid, Map<String, String> choiceMap) {
        Integer isUse = this.getIsUseAutoNoInfo(orgid);
        if(isUse == 0){
            return "-1";//该组织没有使用自定义编码
        }else{
            OrgNo orgNo = autonoInfoMapper.getCodeRule(orgid,noId);
            String orgNosepartor = orgNo.getNosepartor();
            if(orgNo == null){
                return "-1";
            }else{
                String parentNumber = autonoInfoMapper.selectUniqueColumn1(parentNumberCol,parentTblName,parentIdCol,parentId);
                log.info("----------------------------------------parentNumber: " + parentNumber);
                String jgf = parentNumber;
                //Oracle数据库特殊字符处理
                jgf = parentNumber.replace("_","/_");
                if(orgNo.getNosepartor()!=null){
                    if("Oracle".equals(SysConfig.get("databaseType"))){
                        if(parentNumber.indexOf("_") != -1){
                            if("_".equals(orgNo.getNosepartor())){
                                jgf += "/"+orgNo.getNosepartor();
                            }else{
                                jgf += orgNo.getNosepartor();
                            }
                            jgf += "%' escape '/";
                        }else{
                            if("_".equals(orgNo.getNosepartor())){
                                jgf += "/"+orgNo.getNosepartor() + "%' escape '/";
                            }else{
                                jgf += orgNo.getNosepartor()+"%";
                            }
                        }
                    }else{
                        jgf += "%";
                    }

                    String choiceSql = "";
                    if(choiceMap != null && choiceMap.size() != 0){
                        for (Map.Entry<String, String> entry : choiceMap.entrySet()) {
                            choiceSql += " AND "+entry.getKey()+" = '"+entry.getValue()+"'";
                        }
                    }
                    String code = autonoInfoMapper.findNumberLevelNexidByParent(noId, parentNumberCol, parentTblName, parentIdCol, parentId, chilNumberCol, chilTblName, chilOrgCol, orgid, choiceMap,orgNosepartor,choiceSql,jgf,parentNumber);
                    return getNewCodeByParentNumber(code,orgNo,parentNumber);
                }else{
                    if("Oracle".equals(SysConfig.get("databaseType"))){
                        if(parentNumber.indexOf("_") != -1){
                            jgf += "%' escape '/";
                        }else{
                            jgf += "%";
                        }
                    }else{
                        jgf += "%";
                    }
                    String choiceSql = "";
                    if(choiceMap != null && choiceMap.size() != 0){
                        for (Map.Entry<String, String> entry : choiceMap.entrySet()) {
                            choiceSql += " AND "+entry.getKey()+" = '"+entry.getValue()+"'";
                        }
                    }
                    String code = autonoInfoMapper.findNumberLevelNexidByParent(noId, parentNumberCol, parentTblName, parentIdCol, parentId, chilNumberCol, chilTblName, chilOrgCol, orgid, choiceMap,orgNosepartor,choiceSql,jgf,parentNumber);
                    return getNewCodeByParentNumber(code,orgNo,parentNumber);
                }
            }
        }
    }

    @Override
    public String findRootNumberByParentId(String chilNumberCol, String chilTblName, String chilParentCol, String parentIdCol, String parentTblName, String parnetOrgCol, BigDecimal orgid, Integer noId, String middleTblname, String middleChilCol, String middleParentCol, String type) {
        Integer isUse = this.getIsUseAutoNoInfo(orgid);
        if(isUse == 0){
            return "-1";//该组织没有使用自定义编码
        }else{
            OrgNo orgNo = autonoInfoMapper.getCodeRule(orgid,noId);
            String orgNocode = orgNo.getNocode();
            String orgNosepartor = orgNo.getNosepartor();
            String result = null;
            String jgf = orgNo.getNocode();
            if(jgf.indexOf("_") != -1){
                jgf=jgf.replace("_","/_");
            }
            if(orgNo.getNosepartor()!=null){
                if("Oracle".equals(SysConfig.get("databaseType"))){
                    if(jgf.indexOf("_") != -1){
                        if("_".equals(orgNo.getNosepartor())){
                            jgf += "/"+orgNo.getNosepartor();
                        }else{
                            jgf += orgNo.getNosepartor();
                        }
                        jgf += "%' escape '/";
                    }else{
                        if("_".equals(orgNo.getNosepartor())){
                            jgf += "/"+orgNo.getNosepartor() + "%' escape '/";
                        }else{
                            jgf += orgNo.getNosepartor()+"%";
                        }
                    }
                }else{
                    jgf += orgNo.getNosepartor()+"%";
                }
            }else{
                if("Oracle".equals(SysConfig.get("databaseType"))){
                    if(jgf.indexOf("_") != -1){
                        jgf += "%' escape '/";
                    }else{
                        jgf += "%";
                    }
                }else{
                    jgf += "%";
                }
            }
            result = autonoInfoMapper.findRootNumberByParentId( chilNumberCol,chilTblName,  chilParentCol,
                     parentIdCol,  parentTblName,  parnetOrgCol,
                     orgid,  noId,  middleTblname,
                     middleChilCol,  middleParentCol,
                     type,orgNosepartor,orgNocode,jgf);
            return getNewCode(result,orgNo);
        }
    }

    private String getNewCode(String result,OrgNo orgNo ){
        String code;
        if(result == null||"".equals(result)){
            code = orgNo.getNocode();
            if(orgNo.getNosepartor() != null){
                code += orgNo.getNosepartor();
            }
            if (orgNo.getNonumber()!=null) {
                if(orgNo.getNonumber().intValue()==-1){
                    code += "01";
                }else if(orgNo.getNonumber().intValue()==1){
                    code+="00001";
                }else if(orgNo.getNonumber().intValue()==2){
                    code+="001";
                }
            }
        }else{
            if(orgNo.getNosepartor() == null){
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
                String num = result.replace(orgNo.getNocode()+orgNo.getNosepartor(),"");
                Integer no = Integer.parseInt(num);
                no++;
                int nolength = num.length()-no.toString().length();
                code = orgNo.getNocode()+orgNo.getNosepartor();
                for (int i = 0; i < nolength; i++) {
                    code += "0";
                }
                code += no;
            }
        }
        return code;
    }

    private String getNewCodeByParentNumber(String result,OrgNo orgNo,String parentNumber){
        log.info("--------------------------------------result：" + result);
        log.info("--------------------------------------orgNo：" + orgNo);
        log.info("--------------------------------------parentNumber：" + parentNumber);
        String code;

        if(result == null||"".equals(result)||"null".equals(result)){
            code = parentNumber;
            if(orgNo.getNosepartor()!=null){
                code += orgNo.getNosepartor();
                
                System.out.println("code3==============================================");
            }
            if (orgNo.getNonumber()!=null) {
                if(orgNo.getNonumber().intValue()==-1){
                    code += "01";
                    
                    System.out.println("code4==============================================");
                }else if(orgNo.getNonumber().intValue()==1){
                    code+="00001";
                }else if(orgNo.getNonumber().intValue()==2){
                    code+="001";
                }
            }
            System.out.println("code1=============================================="+code);
        }else{
            String num = result.replace(parentNumber, "");
            if(orgNo.getNosepartor()!=null){
                parentNumber += orgNo.getNosepartor();
                num = num.substring(1, num.length());
            }
            Integer no = Integer.parseInt(num);
            no++;
            int nolength = num.length()-no.toString().length();
            for (int i = 0; i < nolength; i++) {
                parentNumber += "0";
            }
            code = parentNumber + no;
            System.out.println("code2=============================================="+code);
        }

        return code;
    }



    /**
     * 传入参数说明
     * tabName ----  插入编号所在的表  列入 tbl_flow
     * column --- 编号的列名 例如 tbl_flow 表中的 FLOWNUMBER
     * orgCol --- 编号所在的组织的列名  列入 tbl_flow 表中 COMPANY
     * orgid -----  组织ID    例：114411
     * noId  ------ TBL_AUTONO_INFO的主键标识 列入 流程编号就是3
     * chChoiceCol  -------    插入编号所在的表添加额外条件的列名，例如风险分类里的 MODULETYPE
     * choiceVal   ---------   插入编号所在的表添加额外条件的值，例如风险分类里的 MODULETYPE 的值FXSJK
     */
    @Override
    public String selectFlowNextId(String tblName, String column, String orgCol,
                                   BigDecimal orgid, Integer noId, String chChoiceCol,
                                   String choiceVal, String bjf) throws Exception {
        //chChoiceCol 、 choiceVal 、bjf 字段都是 null

        //获得Organization表ISAUTONUMBER字段的值
        Integer isUse = this.getIsUseAutoNoInfo1(orgid);
        //判断该组织有没有使用自定义编码
        if (isUse == 0) {
            return "-1";
        } else {

            //返回一个TblOrgNoId类 可以获得编号前缀（如：AP）
            OrgNo orgNo = this.getCodeRule(orgid, noId);
            if (orgNo == null) {
                return "-1";
            } else {

                //此时，orgNo不为null

                String noSql = null;
                String result = null;
                String jgf = orgNo.getNocode(); //AP

                //获取Nocode中字符'_'的索引值
                if (jgf.indexOf("_") != -1) {
                    jgf = jgf.replace("_", "/_");
                }


                //判断用户有没有使用分隔符
                if (orgNo.getNosepartor() != null) {
                    if (jgf.indexOf("_") != -1) {
                        if ("_".equals(orgNo.getNosepartor())) {
                            jgf += "/" + orgNo.getNosepartor();
                        } else {
                            jgf += orgNo.getNosepartor();
                        }
                        jgf += "%' escape '/";
                    } else {
                        if ("_".equals(orgNo.getNosepartor())) {
                            jgf += "/" + orgNo.getNosepartor() + "%' escape '/";
                        } else {
                            jgf += orgNo.getNosepartor() + "%";
                        }
                    }
                } else {
                    if (jgf.indexOf("_") != -1) {
                        jgf += "%' escape '/";
                    } else {
                        jgf += "%";
                    }
                }

                if (chChoiceCol != null) {
                    if ("包含".equals(bjf)) {
                        bjf = "LIKE";
                        choiceVal = "'%" + choiceVal + "%'";
                    } else if ("等于".equals(bjf)) {
                        bjf = "=";
                        choiceVal = "'" + choiceVal + "'";
                    }
                }

                System.out.println(noSql);

                //查询是否存在规则编号 ，没有就新增,有就在此基础上加1
                result = this.selectUniqueColumn( column,tblName, orgCol,orgNo.getNocode(), jgf, orgNo.getNosepartor(),
                        chChoiceCol, bjf, choiceVal,orgid);

                return this.getNewCode1(result, orgNo);
            }

        }

    }

    /**
     * 获取组织是否使用自定义编号
     *
     * @param orgid 组织Id
     * @return
     * @throws Exception
     */
    public Integer getIsUseAutoNoInfo1(BigDecimal orgid) throws Exception {

        return autonoInfoMapper.selectUniqueColumn2(orgid);
    }


    /**
     * 获取组织的编号设置信息
     *
     * @param orgid
     * @param noId
     * @return
     * @throws Exception
     */
    private OrgNo getCodeRule(BigDecimal orgid, Integer noId) throws Exception {


        OrgNo tblOrgNoId = new OrgNo();
        Map<String,Object> map =  new HashMap<>();

        //查询编号前缀
        map = autonoInfoMapper.selectCodeRule(orgid, noId);

        Set<Map.Entry<String, Object>> entries = map.entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            log.info("遍历：{} - {}", entry.getKey().getClass(), entry.getValue().getClass());
            if("CODE".equals(entry.getKey())){
                tblOrgNoId.setNocode( (String) entry.getValue());
            }
            if("SEPARTOR".equals(entry.getKey())){
                tblOrgNoId.setNosepartor( (String) entry.getValue());
            }
            if("NUMBER".equals(entry.getKey())){

                //先将BigDecimal转换为Integer类型
                tblOrgNoId.setNonumber(new BigDecimal(entry.getValue().toString()));
            }

        }
        log.info("{}",tblOrgNoId);
        log.info("{}",map );

        return tblOrgNoId;
    }


    /**
     * 查询是否存在规则编号
     *
     * @param tblName
     * @param column
     * @param orgCol
     * @param jgf
     * @param noSepartor
     * @param chChoiceCol
     * @param bjf
     * @return
     */
    private String selectUniqueColumn( String column,String tblName, String orgCol, String noCode, String jgf,
                                       String noSepartor, String chChoiceCol, String bjf,String choiceVal,BigDecimal orgid) {



        String result = autonoInfoMapper.selectUniqueColumn3(column,tblName, orgCol, noCode,jgf, noSepartor, chChoiceCol,
                bjf,choiceVal,orgid);

        return result;
    }

    /**
     * 产生新的编号不保持上下级关系的编号
     *
     * @param result
     * @param orgNo
     * @return
     */
    private String getNewCode1(String result, OrgNo orgNo) {
        String code;
        if (result == null || "".equals(result)) {
            code = orgNo.getNocode();
            if (orgNo.getNosepartor() != null) {
                code += orgNo.getNosepartor();
            }
            if (orgNo.getNonumber().equals(new BigDecimal(-1))) {
                code += "01";
            } else if (orgNo.getNonumber().equals(1)) {
                code += "00001";
            } else if (orgNo.getNonumber().equals(2)) {
                code += "001";
            }
        } else {
            if (orgNo.getNosepartor() == null) {
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
                String num = result.replace(orgNo.getNocode() + orgNo.getNosepartor(), "");
                Integer no = Integer.parseInt(num);
                no++;
                int nolength = num.length() - no.toString().length();
                code = orgNo.getNocode() + orgNo.getNosepartor();
                for (int i = 0; i < nolength; i++) {
                    code += "0";
                }
                code += no;
            }
        }
        return code;
    }

}
