package com.huabo.audit.oracle.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Id;



/**
 * 操作导航消息表
 *
 * @TableName TBL_YQNS_OPERATE
 */
@TableName("TBL_YQNS_OPERATE")
@Data
@Schema(name="实体类")
@Accessors(chain = true)
public class TblYqnsOperate implements Serializable { 

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public static final String CWXMAPBID = "1466";//财务审计项目安排模块id
	public static final String CWXMAPBNAME = "财务审计项目安排";//财务审计项目安排模块名称
	
	
	public static final String CWXMRYSBID = "592539330277445";//财务项目人员上报模块id
	public static final String CWXMRYSBNAME = "财务项目人员上报";//财务项目人员上报模块名称
	
	
	 
	
    @Schema(name = "主键")
    @TableId(value = "OPERID")
    @Id
    private BigDecimal operid;

    @Schema(name = "任务名称")
    @TableField(value = "RWMC")
    private String rwmc;
    
    @Schema(name = "所属模块ID")
    @TableField(value = "SSMKID")
    private String ssmkid;

    @Schema(name = "所属模块")
    @TableField(value = "SSMK")
    private String ssmk;
    
    
    @Schema(name = "关联项目/计划id")
    @TableField(value = "FORMID")
    private BigDecimal formid;
    
    
    @Schema(name = "关联项目/计划名称")
    @TableField(value = "FORMNAME")
    private String formname;
    
    
    @Schema(name = "上一步关联id")
    @TableField(value = "PRAENTID")
    private BigDecimal parentid;


    @Schema(name = "单位id")
    @TableField(value = "ORGID")
    private BigDecimal orgid;
 
    @Schema(name = "单位名称")
    @TableField(value = "ORGNAME")
    private String orgname;

    
  
 
    @Schema(name = "创建人")
    @TableField(value = "CREATESTAFFID")
    private BigDecimal createstaffid;
    

    @Schema(name = "创建人名称")
    @TableField(value = "CREATENAME")
    private String createname;

    @Schema(name = "任务人员id")
    @TableField(value = "RWUSERID")
    private String rwuserid;
    
    @Schema(name = "任务人员名称")
    @TableField(value = "RWUSERNAME")
    private String rwusername;


    @Schema(name = "创建时间")
    @TableField(value = "CREATEDATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createdate;

    
    @Schema(name = "状态（1完成  0其他办理中）")
    @TableField(value = "STATUS")
    private Integer status;
    
    @Schema(name = "任务人员名称")
    @TableField(exist = false)
    private String realname;

}

