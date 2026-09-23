package com.huabo.monitor.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

import javax.annotation.Resource;
import javax.persistence.Column;
import javax.persistence.PrePersist;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Transient;
import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.monitor.service.ITblStaffService;
import com.huabo.monitor.service.TblOrganizaService;
import com.huabo.monitor.vo.param.fieldActivationVo;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author yhr
 * @since 2022-08-26
 */
@Data
@TableName("TBL_ASSESS_PLAN")
@Schema(name="评价计划对象")
@KeySequence(value="HIBERNATE_SEQUENCE")
public class TblAssessPlan extends FlexibleFieldEntity implements Serializable {
	
    private static final long serialVersionUID = 1L;
    @TableId(type= IdType.INPUT)
    @Schema(name="评价计划ID")
    private BigDecimal assid;

    @TableField("ASSESSID")
    @Schema(name="评价计划编号")
    private String assessid;

    @TableField("STARTDATE")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(name="开始时间")
    private Date startdate;

    @TableField("ENDDATE")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(name="结束时间")
    private Date enddate;

    @TableField("MEMO")
    @Schema(name="备注")
    private String memo;

    @TableField("ASSSTATUS")
    @Schema(name="状态(1创建2启动)")
    private String assstatus;

    @TableField("ASSSTARTDAY")
    @Schema(name="发起日期")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date assstartday;

    @TableField("ASSOBJIDS")
    @Schema(name="评价对象IDs")
    private String assobjids;
    
    
    @TableField("ASSOBJNAMES")
    @Schema(name="评价对象名称")
    private String assobjnames;

    @TableField("ASSESSNAME")
    @Schema(name="评价计划名称")
    private String assessname;

    
    @TableField("CONTENT")
    @Schema(name="富文本")
    private String content;

    
    @TableField("STATUS")
    @Schema(name="审批状态")
    private Integer status;
    
    
   
    
    @TableField("LEADERID")
    @Schema(name="评价负责人")
    private BigDecimal leaderid;
    

    public BigDecimal getAssid() {
        return assid;
    }
 

    @Override
    public String toString() {
        return "TblAssess{" +
            "assid=" + assid +
            ", assessid=" + assessid +
            ", startdate=" + startdate +
            ", enddate=" + enddate +
            ", memo=" + memo +
            ", assstatus=" + assstatus +
            ", assstartday=" + assstartday +
            ", assessname=" + assessname +
            ", leaderid=" + leaderid +
        "}";
    }
    
    
    
    
    @Schema(name="评价小组名称")
    @TableField("ASSTEAMNAME")
    private String assteamname;
    
    @Schema(name="评价小组组长")
    @TableField("ASSTEAMLEAD")
    private String assteamlead;
    
    //组长名称
    @TableField(exist = false)
    private String assteamleadname;
    
    //组员ids
    @TableField(exist = false)
    private String assteammemberids;
    //组员名称
    @TableField(exist = false)
    private String assteammember;
    
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
    
    @TableField("CREATESTAFFID")
    @Schema(name="创建人")
    private BigDecimal createstaffid;
    
    @TableField("CREATETIME")
    @Schema(name="创建时间")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createtime;
    
    @Schema(name = "所属公司")
    @TableField("LINKORGID")
    @Column(name = "LINKORGID")
    private BigDecimal linkorgid;
    
    @Schema(name = "所属部门")
    @TableField("LINKDEPTID")
    @Column(name = "LINKDEPTID")
    private BigDecimal linkdeptid;
   
}
