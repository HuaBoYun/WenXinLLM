package com.huabo.monitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.monitor.vo.param.fieldActivationVo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;

import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author yhr
 * @since 2022-09-07
 */
@Data
@TableName("TBL_TESTPLAN")
@Schema(name="TblTestplan对象")
@KeySequence(value="HIBERNATE_SEQUENCE")
public class TblTestplan extends FlexibleFieldEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name="ID")
    @TableId(type = IdType.INPUT)
    @TableField("TESTPLANID")
    private BigDecimal testplanid;

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

    private String planmadeorg;

    @Schema(name="计划制定部门")
    @TableField("PLANMADEDEP")
    private String planmadedep;
    
    @Schema(name="计划制定部门ID")
    @TableField("PLANMADEDEPID")
    private BigDecimal planmadedepId;
    

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

    @Schema(name="备注")
    @TableField("MEMO")
    private String memo;

    @Schema(name="计划状态")
    @TableField("PLANSTATUS")
    private String planstatus;

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

    @Schema(name="退回状态")
    @TableField("RETURNSTATUS")
    private BigDecimal returnstatus;

    @Schema(name="评价计划id")
    @TableField("ASSID")
    private BigDecimal assid;
    
    @Schema(name="评价计划编号")
    @TableField(exist=false)
    private String assessid;
    
    @Schema(name="评价计划名称")
    @TableField(exist=false)
    private String assessname;
     
    @Schema(name="orgid集合")
    @TableField(exist=false)
    private List<BigDecimal> orgids;
    
    @Schema
    @TableField(exist=false)
    private Integer   authorityType;
  
    
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
 
    @Schema(name = "所属公司")
    @TableField("LINKORGID")
    @Column(name = "LINKORGID")
    private BigDecimal linkorgid;
    
    @Schema(name = "所属部门")
    @TableField("LINKDEPTID")
    @Column(name = "LINKDEPTID")
  private BigDecimal linkdeptid;
    @TableField("CREATETIME")
 	@Column(name = "CREATETIME")
 	@Schema(name = "创建时间")
     private Date createtime;
    
    @Schema(name = "关联集团测试计划id")
    @TableField("GROUPID")
    private BigDecimal groupid;
    
    @Schema(name = "集团下发标志")
    @TableField("ISGROUP")
    private BigDecimal isgroup;
    
    @Schema(name = "创建人创建之后需要下发给科室负责人  0 未下发 1已下发")
    @TableField("ISSSTATUS")
    private int issStatus;

    @TableField(value = "SITUATIONDES")
    @Schema(name="情况说明")
    private String situationDes;
    
    @TableField(exist=false)
    @Schema(name="保存附件id")
    private String attids;
    
    @Schema(name="被测试机构IDS")
    @TableField("TESTEDORGIDS")
    private String testedorgIds;
    
    
	public TblTestplan(TblGroupTestplan t ) {
		this.plannumber = t.getPlannumber();
		this.planname = t.getPlanname();
		this.planyear = t.getPlanyear();
		this.testtype = t.getTesttype();
		this.starttime = t.getStarttime();
		this.endtime = t.getEndtime();
		this.planfee = t.getPlanfee();
		this.numberofpeople = t.getNumberofpeople();
		this.memo = t.getMemo();
		this.testtemid = t.getTesttemid();
		this.groupid = t.getId();
	}
    
	public TblTestplan() {
	}
    
    @TableField(exist=false)
	 private TblAssessPlan  assidtem;
    
    @Schema(name="计划类型： 0  计划内   1  计划外")
    @TableField("PLANTYPE")
    private Integer planType;
    
    
    @Schema(name="集团测试计划编号")
    @TableField(exist=false)
    private String groupPlanNumber;
    
    @Schema(name="集团测试计划名称")
    @TableField(exist=false)
    private String groupPlanName;
    
    
    @Schema(name = "创建人名称")
    @TableField(exist=false)
	 private String  createName;
    
    @Schema(name = "拼接消息字符串")
    @TableField(exist=false)
	 private String  jsonString;
}
