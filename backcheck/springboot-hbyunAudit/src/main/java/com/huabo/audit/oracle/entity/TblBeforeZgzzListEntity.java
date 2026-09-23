package com.huabo.audit.oracle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.huabo.audit.config.IgnoreSwaggerParameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

@TableName("TBL_BEFOREZGZZ_LIST")
@Data
@Schema(name="往期整改清单实体类")
public class TblBeforeZgzzListEntity {

	
	@TableId(value = "ID")
    @Schema(name = "序号")
    private String ID;

    @TableId(value = "FIRSTORG")
    @Schema(name = "一级单位")
    private String FIRSTORG;

    @TableId(value = "SPECIFICDEPT")
    @Schema(name = "具体责任单位")
    private String SPECIFICDEPT;

    @TableId(value = "SOURCEPROBLEM")
    @Schema(name = "问题来源")
    private String SOURCEPROBLEM;


    @TableId(value = "ISSUANCEYEAR")
    @Schema(name = "审计报告出具年份")
    private String ISSUANCEYEAR;

    @TableId(value = "PROBLEMTYPE")
    @Schema(name = "问题类别")
    private String PROBLEMTYPE;

    @TableId(value = "FIRSTTITLE")
    @Schema(name = "一级标题")
    private String FIRSTTITLE;

    @TableId(value = "SECONDTITLE")
    @Schema(name = "二级标题")
    private String SECONDTITLE;

    @TableId(value = "THIRDTITLE")
    @Schema(name = "三级标题")
    private String THIRDTITLE;

    @TableId(value = "AUDITREPORTDESC")
    @Schema(name = "在审计报告中的表述")
    private String AUDITREPORTDESC;

    @TableId(value = "SPECIFICDEPTLIST")
    @Schema(name = "具体责任单位")
    private String SPECIFICDEPTLIST;

    @TableId(value = "PROBLEMDESC")
    @Schema(name = "具体问题表述")
    private String PROBLEMDESC;

    @TableId(value = "PROBLEMMONEY")
    @Schema(name = "问题金额（万元）")
    private BigDecimal PROBLEMMONEY;

    @TableId(value = "SUPERMANAGDEPT")
    @Schema(name = "负有监督管理责任的主管部门（可以列出多个）")
    private String SUPERMANAGDEPT;

    @TableId(value = "CORTYPE")
    @Schema(name = "整改类型")
    private String CORTYPE;

    @TableId(value = "LAWSPOCOR")
    @Schema(name = "法规政策依据")
    private String LAWSPOCOR;

    @TableId(value = "CORREQUIRE")
    @Schema(name = "整改要求")
    private String CORREQUIRE;

    @TableId(value = "CORTIME")
    @Schema(name = "整改时限")
    private String CORTIME;

    @TableId(value = "CORSTANDARD")
    @Schema(name = "整改完成标准")
    private String CORSTANDARD;

    @TableId(value = "CORMEASURE")
    @Schema(name = "细化的整改措施")
    private String CORMEASURE;

    @TableId(value = "COMPLETIONTIME")
    @Schema(name = "对应的完成时间")
    private String COMPLETIONTIME;

    @TableId(value = "FIRSTPERSON")
    @Schema(name = "整改第一责任人")
    private String FIRSTPERSON;

    @TableId(value = "ASSISTLEADER")
    @Schema(name = "协助整改工作的领导")
    private String ASSISTLEADER;

    @TableId(value = "LEADERPERSON")
    @Schema(name = "牵头整改部门责任人及联系电话")
    private String LEADERPERSON;

    @TableId(value = "COOPERSON")
    @Schema(name = "配合整改部门责任人及联系电话")
    private String COOPERSON;


    @TableId(value = "AUDITPERSON")
    @Schema(name = "审计部门责任人及联系电话")
    private String AUDITPERSON;


    @TableId(value = "TAKEMEASURE")
    @Schema(name = "已采取的整改措施")
    private String TAKEMEASURE;

    @TableId(value = "ITEMQUANTITY")
    @Schema(name = "项目数（个）")
    private BigDecimal ITEMQUANTITY;

    @TableId(value = "PROCORMONEY")
    @Schema(name = "问题整改金额（万元）")
    private BigDecimal PROCORMONEY;


    @TableId(value = "RECOMONEY")
    @Schema(name = "追缴资金（万元）")
    private BigDecimal RECOMONEY;

    @TableId(value = "RETURNMONEY")
    @Schema(name = "归还原渠道（万元）")
    private BigDecimal RETURNMONEY;

    @TableId(value = "OVERALLAMOUNT")
    @Schema(name = "统筹盘活（万元）")
    private BigDecimal OVERALLAMOUNT;

    @TableId(value = "ACCDISBUR")
    @Schema(name = "加快拨付（万元）")
    private BigDecimal ACCDISBUR;

    @TableId(value = "REFUNDTAXES")
    @Schema(name = "退抵税费或补缴补发（万元）")
    private BigDecimal REFUNDTAXES;

    @TableId(value = "ADJUSTSTATEMENT")
    @Schema(name = "调整账表（万元）")
    private BigDecimal ADJUSTSTATEMENT;

    @TableId(value = "ADJUSTMONEY")
    @Schema(name = "终止或调整金融业务服务（万元）")
    private BigDecimal ADJUSTMONEY;

    @TableId(value = "SUPPAMOUNT")
    @Schema(name = "补办手续、重签协议、停止收费等加强管理（万元）")
    private BigDecimal SUPPAMOUNT;

    @TableId(value = "WAY")
    @Schema(name = "方式")
    private String WAY;

    @TableId(value = "AMOUNT")
    @Schema(name = "金额（万元）")
    private BigDecimal AMOUNT;

    @TableId(value = "LANDAREA")
    @Schema(name = "土地、森林等面积（公顷）")
    private BigDecimal LANDAREA;

    @TableId(value = "MINERESOURCE")
    @Schema(name = "矿产资源、产能等（万吨）")
    private BigDecimal MINERESOURCE;

    @TableId(value = "UNIT")
    @Schema(name = "单位（个）")
    private BigDecimal UNIT;

    @TableId(value = "FAMILY")
    @Schema(name = "家庭（户）")
    private BigDecimal FAMILY;

    @TableId(value = "NUMBERPEOPLE")
    @Schema(name = "人数（人）")
    private BigDecimal NUMBERPEOPLE;

    @TableId(value = "HOUSING")
    @Schema(name = "住房（套）")
    private BigDecimal HOUSING;

    @TableId(value = "CLEARFORM")
    @Schema(name = "情形")
    private String CLEARFORM;

    @TableId(value = "ACCNUMBERPEO")
    @Schema(name = "人数）")
    private String ACCNUMBERPEO;

    @TableId(value = "ACCQUANTITY")
    @Schema(name = "数量（个）")
    private BigDecimal ACCQUANTITY;

    @TableId(value = "FILENAME")
    @Schema(name = "（分修订、制定，文件名称）")
    private String FILENAME;

    @TableId(value = "NOTRECTREASON")
    @Schema(name = "未整改到位问题原因及下一步计划")
    private String NOTRECTREASON;

    @TableId(value = "CORSTATUS")
    @Schema(name = "是否已完成整改")
    private String CORSTATUS;

}
