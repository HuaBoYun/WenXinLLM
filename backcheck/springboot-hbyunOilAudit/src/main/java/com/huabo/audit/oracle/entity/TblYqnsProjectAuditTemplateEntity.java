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
 * @CLASS_NAME: TblYqnsProjectAuditTemplateEntity
 * @PACKAGE_NAME: com.huabo.audit.oracle.entity
 * @date 2023/10/18 12:01.
 * @version: V1.0
 * @description: 央企内审-基础配置-工程审计模板
 */
@Data
@EqualsAndHashCode(callSuper = false)
@KeySequence(value = "AUDIT_OVERSEE_RECORDS_SEQ")
@TableName("TBL_YQNS_PROJ_AUDIT_TEMPLATE")
@Schema(name="TblYqnsProjectAuditTemplate对象", description="央企内审-基础配置-工程审计模板")
public class TblYqnsProjectAuditTemplateEntity extends BaseReservedProperty implements Serializable {

    @Bean
    public OracleKeyGenerator genkey() {
        return new OracleKeyGenerator();
    }

    /**
     * 工程审计模板主键ID
     */
    @TableId(value = "ID",type = IdType.INPUT)
    @Schema(name = "工程审计模板主键ID")
    private Long id;

    @Schema(name = "编号")
    @TableField(value = "NO")
    private String no;
    
    /**
     * 名称
     */
    @Schema(name = "模板名称")
    @TableField("TEMPLATENAME")
    private String templateName ;


    /**
     *  工程审计类型
     */
    @Schema(name = "关联审计类型(类型模板原id)")
    @TableField("TYPEIDS")
    private String typeIds ;


    /**
     *  关联审计类型(模板复制id)
     */
    @Schema(name = "关联审计类型(模板复制id)")
    @TableField("TYPECPIDS")
    private String typeCpIds;


    /**
     *  模板复制id
     */
    @Schema(name = "模板复制id")
    @TableField("TEMPLATEID")
    private String templateId;


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
     * 工程审计类型明细(关联审计类型)
     */
    @Schema(name = "工程审计类型-关联审计类型")
    @TableField(exist = false,typeHandler = JacksonTypeHandler.class)
    @IgnoreSwaggerParameter
    private List<TblYqnsProjectAuditTypeEntity> auditTypeList;
}
