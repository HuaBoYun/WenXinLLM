package com.huabo.monitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.huabo.monitor.vo.param.fieldActivationVo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;

import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@TableName("TBL_GROUP_TESTPLAN")
@Schema(name="TblGroupTestplan对象")
@KeySequence(value="HIBERNATE_SEQUENCE")
public class TblGroupTestplan extends FlexibleFieldEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name="ID")
    @TableId(type = IdType.INPUT)
    @TableField("ID")
    private BigDecimal id;

    @Schema(name="计划编号")
    @TableField("PLANNUMBER")
    private String plannumber;

    @Schema(name="计划名称")
    @TableField("PLANNAME")
    private String planname;

    @Schema(name="计划年度")
    @TableField("PLANYEAR")
    private String planyear;

    @Schema(name="测试类型")
    @TableField("TESTTYPE")
    private String testtype;


//    @Schema(name="计划制定部门")
//    @TableField("PLANMADEDEP")
//    private String planmadedep;

    @Schema(name="计划开始时间")
    @TableField("STARTTIME")
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern="yyyy-MM-dd")
    private Date starttime;

    @Schema(name="计划结束时间")
    @TableField("ENDTIME")
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern="yyyy-MM-dd")
    private Date endtime;

    @Schema(name="负责人")
    @TableField("PLANLEADER")
    private String planleader;

    @Schema(name="开展费用")
    @TableField("PLANFEE")
    private BigDecimal planfee;

    @Schema(name="投入人力")
    @TableField("NUMBEROFPEOPLE")
    private String numberofpeople;

    @Schema(name="被测试机构")
    @TableField("TESTEDORGS")
    private String testedorgs;

    @Schema(name="被测试机构IDS")
    @TableField("TESTEDORGIDS")
    private String testedorgIds;
    
    
    @Schema(name="备注")
    @TableField("MEMO")
    private String memo;

    @Schema(name="计划状态")
    @TableField("STATUS")
    private String status;

    @Schema(name="计划制定部门id")
    @TableField("ORGID")
    private BigDecimal orgid;

    @Schema(name="创建人")
    @TableField("CREATID")
    private BigDecimal creatid;

    @Schema(name="负责人")
    @TableField("STAFFID")
    private BigDecimal staffid;

    @Schema(name="测试模板")
    @TableField("TESTTEMID")
    private BigDecimal testtemid;

    @TableField(exist=false)
    private TblTesttemple  testtemple;
    
   //中核密级
    //密级及查询条件
    @Schema(name = "密级主键")
    @TableField("SECRECTLEVELID")
    private BigDecimal secrectLevelId;
    
    @Schema(name = "知悉范围id")
    @TableField("STAFFSCOPEIDS")
    private String staffScopeIds;
    
    @Schema(name = "知悉范围名称")
    @TableField("STAFFSCOPENAMES")
    private String staffScopeNames;
 
 
    @Schema(name = "所属部门")
    @TableField("LINKDEPTID")
    @Column(name = "LINKDEPTID")
     private BigDecimal linkdeptid;
    
    
    @TableField("CREATETIME")
 	@Column(name = "CREATETIME")
 	@Schema(name = "创建时间")
     private Date createtime;
    
    @Schema(name="orgid集合")
    @TableField(exist=false)
    private List<BigDecimal> orgids;
    
    @Schema
    @TableField(exist=false)
    private Integer   authorityType;
    
    @TableField(value = "ISSUEDDATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Schema(name="下发时间")
    private Date issueddate;
 
    @TableField(value = "ISSUED_STAFFID")
    @Schema(name="下发人员id")
    private String issuedStaffid;
    
    @TableField(value = "ISSUED_STAFFNAME")
    @Schema(name="下发人员名字")
    private String issuedStaffName;

    @Schema(name="是否下发 1 下发 0未下发")
    @TableField(value = "TOISSUED")
    private BigDecimal toIssued; 
    
    @TableField(value = "ISSUED_UNIT")
    @Schema(name="下发单位id--对应下发人员id")
    private String lssuedUnit;
    
    @TableField(value = "ISSUED_UNITNAME")
    @Schema(name="下发单位名称--对应下发人员")
    private String issuedUnitName;
    
    @TableField(value = "SITUATIONDES")
    @Schema(name="情况说明")
    private String situationDes;
    
    @TableField(exist=false)
    @Schema(name="保存附件id")
    private String attids;
    
    @TableField(exist=false)
    @Schema(name="下发消息字符串")
    private String jsonString;
    
    @Schema(name="集团测试计划模板明细列表")
    @JsonProperty("detailsList") // 指定JSON字段名
    @JsonInclude(JsonInclude.Include.ALWAYS) // 总是包含该字段
    @TableField(exist=false)
    private List<TblGroupTemplateDetail> detailsList = new ArrayList<>();
}
