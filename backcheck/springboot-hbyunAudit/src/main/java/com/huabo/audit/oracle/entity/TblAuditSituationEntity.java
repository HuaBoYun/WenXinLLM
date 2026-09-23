package com.huabo.audit.oracle.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import tk.mybatis.mapper.annotation.KeySql;
import tk.mybatis.mapper.code.ORDER;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * 审计情况统计表
 * 20230804
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_AUDIT_SITUATION")
@Schema(name="审计情况统计表-父表", description="TBL_AUDIT_SITUATION")
//@KeySequence(value="HIBERNATE_SEQUENCE",clazz=Integer.class) //value为数据库中生成的序列名，class指主键属性类型
public class TblAuditSituationEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "审计情况统计表ID")
    //@KeySql(sql = "select HIBERNATE_SEQUENCE.nextval from dual", order = ORDER.DEFAULT)
    @TableId(type=IdType.INPUT)  //注意主键类型要指定为Input
    private BigDecimal id;

    @Schema(name = "内审机构名称")
    @TableField("SITUATIONNAME")
    private String situationName;

    @Schema(name = "直接领导")
    @TableField("SITUATIONLEADER")
    private String situationLeader;

    @Schema(name = "判断审计委员会，1：是，0：否")
    @TableField("SITUATIONCOMMITTEE")
    private Integer situationCommittee;

    @Schema(name = "审计中心或区域审计中心，1：是，0：否")
    @TableField("SITUATIONAUDITCENTER")
    private Integer situationAuditCenter;

    @Schema(name = "年份")
    @TableField("SITUATIONYEAR")
    private String situationYear;

    @Schema(name = "创建人")
    @TableField("CREATESTAFFID")
    private BigDecimal createStaffid;

    @Schema(name = "创建时间")
    @TableField("CREATEDTIME")
    //@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE_TIME)
    private Date createTime;

    @Transient
    @TableField(exist = false)
    @Schema(name = "创建人名称")
    private String createStaffName;

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
