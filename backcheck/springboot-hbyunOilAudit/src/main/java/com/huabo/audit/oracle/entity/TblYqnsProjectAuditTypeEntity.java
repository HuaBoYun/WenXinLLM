package com.huabo.audit.oracle.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.baomidou.mybatisplus.extension.incrementer.OracleKeyGenerator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.audit.config.IgnoreSwaggerParameter;
import com.huabo.audit.oracle.entity.base.BaseReservedProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.context.annotation.Bean;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author GJ.C
 * @CLASS_NAME: TblYqnsAuditMethodMaintenanEntity
 * @PACKAGE_NAME: com.huabo.audit.oracle.entity
 * @date 2023/10/18 12:01.
 * @version: V1.0
 * @description: 央企内审-基础配置-工程审计类型
 */
@Data
@EqualsAndHashCode(callSuper = false)
@KeySequence(value = "AUDIT_OVERSEE_RECORDS_SEQ")
@TableName("TBL_YQNS_PROJ_AUDIT_TYPE")
@Schema(name="TblYqnsProjectAuditType对象", description="央企内审-基础配置-工程审计类型")
public class TblYqnsProjectAuditTypeEntity extends BaseReservedProperty implements Serializable {

    @Bean
    public OracleKeyGenerator genkey() {
        return new OracleKeyGenerator();
    }

    /**
     * 工程审计类型主键ID
     */
    @TableId(value = "ID",type = IdType.INPUT)
    @Schema(name = "工程审计类型主键ID")
    private Long id;
    
    @Schema(name = "编号")
    @TableField(value = "NO")
    private String no;

    /**
     * 类型名称
     */
    @Schema(name = "类型名称")
    @TableField("TYPENAME")
    private String typeName ;


    /**
     * 关联审计方法
     */
    @Schema(name = "关联审计方法")
    @TableField("METHODMAINTAINID")
    private String methodMaintainId ;


    /**
     * 工程审计模板ID
     */
    @Schema(name = "工程审计模板ID")
    @TableField("TEMPLATEID")
    private String templateId ;



    /**
     * 创建人
     */
    @Schema(name = "创建人")
    @TableField("CREATEUSER")
    private String createUser;


    /**
     * 创建时间
     */
    @Schema(name = "创建时间")
    @TableField("CREATETIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTime;

    /**
     * 修改人
     */
    @Schema(name = "修改人")
    @TableField("UPDATEUSER")
    private String updateUser;

    /**
     * 修改时间
     */
    @Schema(name = "修改时间")
    @TableField("UPDATETIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 审计方法维护
     */
    @Schema(name = "审计方法维护")
    @TableField(exist = false,typeHandler = JacksonTypeHandler.class)
    @IgnoreSwaggerParameter
    private TblYqnsAuditMethodMaintainEntity auditMethodMaintainEntity ;


    /**
     * 工程审计类型-Name
     */
    @Schema(name = "工程审计类型-Name")
    @TableField(exist = false,typeHandler = JacksonTypeHandler.class)
    private List<TblYqnsProjectAuditTypeNameEntity> auditTypeNameEntityList ;



    /**
     * 状态（0：模板，1：已绑定） 新增默认：0
     */
    @Schema(name = "状态（0：模板，1：已绑定） 新增默认：0")
    @TableField("STATUS")
    private String STATUS;

    /**
     * 状态 0：模板
     */
    public static String STATUS_ZERO="0";

    /**
     * 状态 1：已绑定
     */
    public static String STATUS_ONE="1";



    /**
     * 绑定的工程审计类型ID（模板ID）
     */
    @Schema(name = "绑定的工程审计类型ID（模板ID）")
    @TableField("TYPEID")
    private String typeId;

}
