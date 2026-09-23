package com.huabo.compliance.mysql.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 *
 * </p>
 *
 * @author huabo
 * @since 2021-10-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_PROCESS_ANALYSIS")
@Schema(name="TblProcessAnalysisMySql对象")
public class TblProcessAnalysisMySql implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select ANALYSIS_SEQUENCE.nextval from dual")
    @Schema(name = "主键Id 自增")
    @TableId("ANALID")
    private BigDecimal analid;

    @Schema(name = "流程名称")
    @TableField("PROCESSNAME")
    private String processname;

    @Schema(name = "流程Id")
    @TableField("PROCESSID")
    private String processid;

    @Schema(name = "用户Id")
    @TableField("USERID")
    private String userid;

    @Schema(name = "角色名称")
    @TableField("ROLENAME")
    private String rolename;

    @Schema(name = "taskId")
    @TableField("USERTASKID")
    private String usertaskid;

    public final static String TYPE_ZCGL = "资产管理";
    public final static String URL_ZCGL = "/nbkz/zcgl/to_sptzgl_info?assetid=";//标识投资管理审批详情路径


    public final static String TYPE_CGGLL = "采购管理";
    public final static String URL_CGGL = "/nbkz/cggl/to_sptzgl_info?assetid=";//标识投资管理审批详情路径


    public final static String TYPE_CWGL = "财务管理";
    public final static String URL_CWGL = "/nbkz/cwgl/to_sptzgl_info?formId=";

    public final static String TYPE_YSGL = "预算管理";
    public final static String URL_YSGL = "/nbkz/ysgl/to_sptzgl_info?assetid=";

    public final static String CYHW_TYPE_JJHTJG = "朝阳环卫中心经济合同联审单机关";
    public final static String CYHW_URL_JJHTJG = "/cyhw/htlsjg/to_sptzgl_info?contractId=";


    public final static String CYHW_TYPE_JJHTJC = "合同管理";
    public final static String CYHW_URL_JJHTJC = "/cyhw/htlsjc/to_sptzgl_info?contractId=";

    public final static String CYHW_TYPE_YSZC = "合同管理";
    public final static String CYHW_URL_YSZC = "/cyhw/yszc/to_sptzgl_info?budgetId=";

    public final static String CYHW_TYPE_ZBJC = "朝阳环卫中心基层单位招标文件联审单";
    public final static String CYHW_URL_ZBJC = "/cyhw/zbjc/to_sptzgl_info?inspectionId=";


    public final static String CYHW_TYPE_ZBJG = "朝阳环卫中心机关招标文件联审单";
    public final static String CYHW_URL_ZBJG = "/cyhw/zbjg/to_sptzgl_info?inspectionId=";

    public final static String HBY_TYPE_CGGL = "采购申报表";
    public final static String HBY_URL_CGGL = "/nbkz/yszc/to_sptzgl_info?budgetId=";


    public final static String TYPE_FXYD = "风险应对";
    public final static String URL_FXYD = "/fxxt/fxyd/to_sptzgl_info?riskRadio=";//风险应对


    public final static String TYPE_SKGL = "收款管理";
    public final static String URL_SKGL = "/htgl/skgl/to_sptzgl_info?collectionId=";


    public final static String TYPE_FKGL = "付款管理";
    public final static String URL_FKGL = "/htgl/fkgl/to_sptzgl_info?parmentId=";


}
