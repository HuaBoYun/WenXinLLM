package com.huabo.audit.oracle.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.huabo.audit.config.IgnoreSwaggerParameter;
import com.baomidou.mybatisplus.annotation.TableId;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_NBSJ_MB")
@Schema(name="TBL_NBSJ_MB对象")
public class TblNbsjMb implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value="MBID",type= IdType.INPUT)
    //@GeneratedValue(strategy = GenerationType.IDENTITY,generator = "select HIBERNATE_SEQUENCE.nextval from dual")
  	@Column(name = "MBID")
    @Schema(name = "主键ID")
    @Id
    private BigDecimal mbid;

    @TableField("MBCODE")
    @Schema(name = "模板编号")
    @Column(name = "MBCODE")
    private String mbcode;

    @TableField("MBNAME")
    @Column(name = "MBNAME")
    @Schema(name = "模板名称")
    private String mbname;

    @TableField("AUDITTYPE")
    @Schema(name = "审计类型")
    private String audittype;

    @TableField("STAFFID")
    @Schema(name = "创建人ID")
    private BigDecimal staffid;

    @TableField("CREATEDTIME")
    @Schema(name="创建时间",hidden=true)
    @Column(name = "CREATEDTIME")
    private Date createdtime;


    @TableField("ORGID")
    @Schema(name="创建公司ID",hidden=true)
    private BigDecimal orgid;

   
    @TableField("UPDATEDTIME")
    @Schema(name="修改时间",hidden=true)
    private Date updatedtime;

  
    
    @Schema(name = "创建人实体")
    @Transient
    @IgnoreSwaggerParameter
    @TableField(exist = false)
    private TblStaff createStaff;

    @Schema(name = "密级主键")
    @TableField("SECRECTLEVELID")
    private BigDecimal secrectLevelId;

    @Schema(name = "知悉范围 多个逗号分隔")
    @TableField("STAFFSCOPEIDS")
    private String staffScopeIds;

    @Schema(name = "知悉访问人员姓名 多个逗号分隔")
    @TableField("STAFFSCOPENAMES")
    private String staffScopeNames;
    
    
    
   


}
