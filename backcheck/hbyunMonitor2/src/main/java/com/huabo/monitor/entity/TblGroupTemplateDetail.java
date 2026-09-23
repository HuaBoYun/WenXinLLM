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
import com.huabo.monitor.vo.param.fieldActivationVo;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

 
@Data
@TableName("TBL_GROUP_TEMPLATEDETAIL")
@Schema(name="TblGroupTemateDetail对象")
@KeySequence(value="HIBERNATE_SEQUENCE")
public class TblGroupTemplateDetail  implements Serializable {

    private static final long serialVersionUID = 1L;
   
    @TableId(type= IdType.INPUT)
    @Schema(name="主键ID")
	@TableField("ID")
    private BigDecimal id;

    @Schema(name="集团测试计划ID")
    @TableField("GROUPID")
    private BigDecimal groupid;
     
    @Schema(name="测试模板ID")
    @TableField("TESTTEMPLETAID")
    private BigDecimal testtempletaid;
    
    @Schema(name="条目明细ID")
    @TableField("TYPEID")
    private BigDecimal typeid;
 
    @Schema(name="下发人员")
    @TableField("STAFFID")
    private BigDecimal staffid;
    
    @Schema(name="条目编号")
    @TableField(exist=false)
    private String typecode;
    
    @Schema(name="模板类型")
    @TableField(exist=false)
    private String typename;
    
    @Schema(name="下发人员姓名")
    @TableField(exist=false)
    private String realname;
}
