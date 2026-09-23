package com.huabo.monitor.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.monitor.vo.param.fieldActivationVo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;

import org.springframework.format.annotation.DateTimeFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TblTestplanVo extends FlexibleFieldEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name="ID")
    @TableId(type = IdType.INPUT)
    private BigDecimal testplanid;

    @Schema(name="计划编号")
    private String plannumber;

    @Schema(name="计划名称")
    private String planname;

    @Schema(name="计划年度")
    private String planyear;

    @Schema(name="测试类型")
    private String testtype;

    private String planmadeorg;

    @Schema(name="计划制定部门")
    private String planmadedep;

    @Schema(name="计划开始时间")
    private Date starttime;

    @Schema(name="计划结束时间")
    private Date endtime;

    @Schema(name="负责人")
    private String planleader;

    @Schema(name="开展费用")
    private BigDecimal planfee;

    @Schema(name="投入人力")
    private String numberofpeople;

    @Schema(name="被测试机构")
    private String testedorgs;

    @Schema(name="备注")
    private String memo;

    @Schema(name="计划状态")
    private String planstatus;

    @Schema(name="计划制定部门id")
    private BigDecimal orgid;

    @Schema(name="创建人")
    private BigDecimal creatid;

    @Schema(name="负责人")
    private BigDecimal staffid;

    @Schema(name="测试模板")
    private BigDecimal testtemid;

    @Schema(name="评价计划id")
    private BigDecimal assid;
    
    @Schema(name="评价计划编号")
    private String assessid;
    
    @Schema(name="评价计划名称")
    private String assessname;
    
    @Schema(name = "密级主键")
    private BigDecimal secrectLevelId;
    
    @Schema(name = "知悉范围id")
    private String staffScopeIds;
    
    @Schema(name = "知悉范围名称")
    private String staffScopeNames;
 
    @Schema(name = "所属公司")
    private BigDecimal linkOrgId;
    
    @Schema(name = "所属部门")
     private BigDecimal linkDeptId;

    @Schema(name = "创建时间")
     private Date createtime;


    @Schema(name="退回状态")
    private BigDecimal returnstatus;

    private TblTesttemple  testtemple;
    
    private TblAssessPlan  assidtem;
    
    private TblGroupTestplan  groupPlan;
    
    
    @Schema(name = "关联集团测试计划id")
    @TableField("GROUPID")
    private BigDecimal groupid;
    
    @Schema(name="集团测试计划名称")
    @TableField(exist=false)
    private String groupPlanName;
    
    
    @Schema(name = "创建人名称")
	 private String  createName;
     
    @Schema(name="计划类型： 0  计划内   1  计划外")
    private Integer planType;
    
    
    @Schema(name="计划制定部门ID")
    private BigDecimal planmadedepId;
}
