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
 * 审计督导任务内容
 *
 * @TableName TBL_YQNS_SJDDRW
 */
@TableName("TBL_YQNS_SJDDRW")
@Data
@Schema(name="实体类")
@Accessors(chain = true)
public class TblYqnsSjdd implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	 
	
    @Schema(name = "主键")
    @TableId(value = "RWID")
    @Id
    private BigDecimal rwid;
 
    

    /**
     * 审计实施方案中审计内容及重点
     */
    @Schema(name = "审计实施方案中审计内容及重点")
    @TableField("AUDITCONTENTANDFOCUS")
    private String auditContentAndFocus;




    /**
     * 审计任务执行情况
     */
    @Schema(name = "审计任务执行情况")
    @TableField("EXECUTIONSITUATION")
    private String executionSituation;




    /**
     * 审计组发现问题
     */
    @Schema(name = "审计组发现问题")
    @TableField("DISCOVERPROBLEMS")
    private String discoverProblems;

    /**
     * 存在问题及需协调解决的问题
     */
    @Schema(name = "存在问题及需协调解决的问题")
    @TableField("ISSUESANDCOORDINATION")
    private String issuesAndCoordination ;


    /**
     * 督导意见
     */
    @Schema(name = "督导意见")
    @TableField("SUPERVISIONOPINIONS")
    private String supervisionOpinions ;


    /**
     * 审计组是否采纳
     */
    @Schema(name = "审计组是否采纳")
    @TableField("ISADOPT")
    private String isAdopt;
  
    /**
     * 未采纳原因或采纳结果
     */
    @Schema(name = "未采纳原因或采纳结果")
    @TableField("ISADOPTRESULT")
    private String isAdoptResult;


    

    @Schema(name = "创建人")
    @TableField(value = "CREATESTAFFID")
    private BigDecimal createstaffid;



    @Schema(name = "创建时间")
    @TableField(value = "CREATEDATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createdate;

    
    @Schema(name = "状态")
    @TableField(value = "STATUS")
    private Integer status;
    
    
    @Schema(name = "关联项目id")
    @TableField("PROJECTID")
    private BigDecimal projectid;
    
    @Schema(name = "关联任务id")
    @TableField("TYPEID")
    private BigDecimal typeid;
    
    
    
}

