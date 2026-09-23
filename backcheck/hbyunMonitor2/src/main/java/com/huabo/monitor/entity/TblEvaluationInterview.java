package com.huabo.monitor.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 *
 * @author CJ
 * @since 2024-12-16
 */
@Data
@TableName("TBL_EVALUATIONINTERVIEW")
@Schema(name="TblEvaluationInterview 评价访谈对象")
@KeySequence(value="HIBERNATE_SEQUENCE")
public class TblEvaluationInterview extends FlexibleFieldEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(type= IdType.INPUT)
    @Schema(name="评价访谈ID")
	@TableField("ID")
    private BigDecimal id;
    
    @Schema(name="创建人")
    @TableField("CREATESTAFFID")
    private BigDecimal createstaffid;
    
    @TableField("CREATETIME")
 	@Column(name = "CREATETIME")
 	@Schema(name = "创建时间")
     private Date createtime;

    @Schema(name="关联公司")
    @TableField("UNIT")
    private BigDecimal unit;
    
    @Schema(name="时间")
    @TableField("TIMES")
    @JsonFormat(pattern = "yyyy-MM-dd")
   	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date times;

    @Schema(name="被访谈人")
    @TableField("INTERVIEWEE")
    private String interviewee;
    
    @Schema(name="隶属部门")
    @TableField("DEPT")
    private BigDecimal dept;
    
    @Schema(name="职务")
    @TableField("POST")
    private String post;
    
    @Schema(name="联系方式")
    @TableField("CONTACT")
    private String contact ;
    
    @Schema(name="业务模块")
    @TableField("MODULE")
    private String module ;
    
    @Schema(name="提问人")
    @TableField("QUESTIONER")
    private String questioner ;
	
    @Schema(name="记录人")
    @TableField("STAFFID")
    private BigDecimal staffid ;
    
    @Schema(name="参加人")
    @TableField("PARTICIPANTS")
    private String participants ;
    
    
    @Schema(name="对应控制标准表")
    @TableField("STANDARDTABLE")
    private String standardtable ;

    @Schema(name="一般访谈内容")
    @TableField("CONTENT")
    private String content ;
    
    @Schema(name="总结")
    @TableField("SUMMARY")
    private String summary ;
   
    @Schema(name="隶属部门名称")
    @TableField(exist=false)
    private String deptname;
    
    
    @Schema(name="记录人姓名")
    @TableField(exist=false)
    private String staffname ;
    
    
    @Schema(name="附件")
    @TableField(exist=false)
    private String attids;
    
    
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
  private BigDecimal linkDeptId;
    


   	    
}
