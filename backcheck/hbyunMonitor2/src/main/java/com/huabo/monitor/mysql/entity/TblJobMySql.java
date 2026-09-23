package com.huabo.monitor.mysql.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("TBL_JOB")
@Schema(name="TblJobMySql")
public class TblJobMySql implements Serializable {

    @TableId("JOBID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select HIBERNATE_SEQUENCE.nextval from dual")
    @Column(name="JOBID")
    private BigDecimal jobid;
    @TableField("JOBNAME")
    private String jobname;
    @TableField("COMPANYID")
    private String companyId;
    
    @TableField("DATASOURCE")
    private String datasource;
    
    @TableField("HISTORYCODE")
    private String historycode;
    
    @TableField("CODE")
    private String code;
    
    @TableField("DESCRIPTION")
    private String description;
    
    @TableField("CATEGORYID")
    private String categoryId;
    
    @TableField("CATEGORYNAME")
    private String categoryName;
    
    @TableField("CREATETIME")
    private Date createTime;
    
    @TableField("STATUS")
    private String status;
    
    @Column(name = "PKYMJOBID")
    private String pkymJobId;
    
    @Transient
    private String pkymOrgId;
}
