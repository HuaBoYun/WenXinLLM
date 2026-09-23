package com.huabo.fxgl.entity;


import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;

@Data
@TableName("TBL_FILL_ISSUED")
@Schema(name="风险监督指标下发信息表")
public class TblFillIssued implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Schema(name = "主键")
    @TableId(value = "ID")
    @Id
    private BigDecimal id;
    
    @Schema(name = "关联风险监测填报表id")
    @TableField(value = "FILLID")
    private BigDecimal fillId;
    
    @Schema(name = "关联风险监督创建表id")
    @TableField(value = "CREATID")
    private BigDecimal creatId;
    
    @Schema(name = "关联下发部门ID")
    @TableField(value = "DEPTID")
    private BigDecimal deptId;
    
    @Schema(name = "版本id")
    @TableField(value = "VERSIONID")
    private BigDecimal versionId;
    
    @Schema(name = "下发部门人员")
    @TableField(value = "DEPTSTAFF")
    private BigDecimal deptStaff;
    
    
    @Schema(name = "下发部门人员备注")
    @TableField(value = "DEPTNOTES")
    private String deptNotes;
    
    
    @Schema(name = "下发部门日期")
    @TableField(value = "DEPTDATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date deptDate;
    
    @Schema(name = "创建日期")
    @TableField(value = "CREATETIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    
    @Schema(name = "下发部门姓名")
    @TableField(exist=false)
    private String deptName;
     
    
    @Schema(name = "下发部门人员姓名")
    @TableField(exist=false)
    private String deptStaffName;
   
    
     
}
