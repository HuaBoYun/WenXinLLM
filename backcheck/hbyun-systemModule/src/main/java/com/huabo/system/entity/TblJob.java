package com.huabo.system.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Transient;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("TBL_JOB")
@Schema(name="TblJob")
public class TblJob implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableId(value="JOBID",type = IdType.INPUT)
    @Schema(name="主键")
    private BigDecimal jobid;
    
    @TableField("JOBNAME")
    @Schema(name="岗位名称")
    private String jobname;
    
    @TableField("COMPANYID")
    @Schema(name="所属公司ID")
    private String companyId;
    
    @TableField("DATASOURCE")
    @Schema(name="来源")
    private String datasource;
    
    @TableField("HISTORYCODE")
    @Schema(name="历史ID")
    private String historycode;
    
    @TableField("CODE")
    @Schema(name="编号")
    private String code;
    
    @TableField("DESCRIPTION")
    @Schema(name="职位描述")
    private String description;
    
    @TableField("CATEGORYID")
    @Schema(name="职级ID")
    private String categoryId;
    
    @TableField("CATEGORYNAME")
    @Schema(name="职级分类名称")
    private String categoryName;
    
    @TableField("CREATETIME")
    @Schema(name="创建时间")
    private Date createTime;
    
    @TableField("STATUS")
    @Schema(name="状态")
    private String status;
    
    @Column(name = "PKYMJOBID")
    @Schema(name="关联流程平台ID")
    private String pkymJobId;
    
    @Transient
    private String pkymOrgId;
    
    @Transient
    private String jlongName;
    
    @Transient
    private String orgName;
}
