package com.huabo.audit.oracle.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.huabo.audit.config.IgnoreSwaggerParameter;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 描述: 底稿
 * author: ziyao
 * date: 2022-04-20
 */
@TableName("TBL_NBSJ_TEMPLETE")
@Data
@Schema(name="审计模板实体类")
@Accessors(chain = true)
@Table(name = "TBL_NBSJ_TEMPLETE")
public class TblNbsjTempleteEntity {
	public static final Integer NORMAL = 1;  //启用
	public static final Integer UNNORMAL=-1; //禁用
	
	public static final Integer MB_TYPE = 0;   //审计模板
	public static final Integer ZY_TYPE = 1;  // 指引模板
	public static final Integer COPY_TYPE=2; // 审计经验库

    @TableId(value = "TEMPLETEID", type= IdType.INPUT)
    @Schema(name = "模板id")
    @Column(name = "TEMPLETEID")
    @Id
    private BigDecimal templeteId;

    @TableField(value = "TEMPLETECODE")
    @Schema(name = "模板编号")
    @Column(name = "TEMPLETECODE")
    private String templeteCode;

    @TableField(value = "TEMPLETENAME")
    @Column(name = "TEMPLETENAME")
    @Schema(name = "模板名称")
    private String templeteName;

    @TableField(value = "TEMPLETETYPE")
    @Column(name = "TEMPLETETYPE")
    @Schema(name = "审计类型", required = true)
    private String templeteType;

    @TableField(value = "TEMPLETEDESC")
    @Column(name = "TEMPLETEDESC")
    @Schema(name = "模板说明")
    private String templeteDesc;

    @TableField(value = "STAFFID")
    @Column(name = "STAFFID")
    @Schema(name="创建人id",hidden=true)
    private BigDecimal staffId;

    @TableField(value = "CREATEDATE")
    @Column(name = "CREATEDATE")
    @Schema(name="创建日期",hidden=true)
    private Date createDate;

    @TableField(value = "UPDATEDATE")
    @Column(name = "UPDATEDATE")
    @Schema(name="修改日期",hidden=true)
    private Date updateDate;

    @TableField(value = "UPDATESTAFFID")
    @Column(name = "UPDATESTAFFID")
    @Schema(name="修改人id",hidden=true)
    private BigDecimal updateStaffId;

    @TableField(value = "STATUS")
    @Column(name = "STATUS")
    @Schema(name="状态：启用：1、禁用“-1",hidden=true)
    private Integer status;

    @TableField(value = "TEMPTYPE")
    @Column(name = "TEMPTYPE")
    @Schema(name = "模板类型：审计模板：0、指引模板：1、审计经验库：2", required = true)
    private String tempType;

    @TableField(value = "ORGID")
    @Column(name = "ORGID")
    @Schema(name="模板所属公司id",hidden=true)
    private BigDecimal orgId;
    
    @Schema(name="涉及单位",hidden=true)
    @IgnoreSwaggerParameter
    @TableField(exist = false)
    @Transient
    private String temorgname;
    
    @Schema(name="涉及单位的id",hidden=true)
    @IgnoreSwaggerParameter
    @TableField(exist = false)
    @Transient
    private String temorgids;
    
    @IgnoreSwaggerParameter
    @Schema(name="创建人",hidden=true)
    @TableField(exist = false)
    @Transient
    private String createstaffname;
    
    @Schema
    @IgnoreSwaggerParameter
    @TableField(exist = false)
    @Transient
    private Set<TblOrganization> organizations;
    
}
