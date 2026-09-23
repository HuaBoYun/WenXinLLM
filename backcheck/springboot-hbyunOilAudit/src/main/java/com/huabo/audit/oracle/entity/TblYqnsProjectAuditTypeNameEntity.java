package com.huabo.audit.oracle.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.baomidou.mybatisplus.extension.incrementer.OracleKeyGenerator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.audit.config.IgnoreSwaggerParameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.context.annotation.Bean;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author GJ.C
 * @CLASS_NAME: TblYqnsAuditMethodMaintenanEntity
 * @PACKAGE_NAME: com.huabo.audit.oracle.entity
 * @date 2023/10/18 12:01.
 * @version: V1.0
 * @description: 央企内审-基础配置-工程审计类型-关联Name
 */
@Data
@EqualsAndHashCode(callSuper = false)
@KeySequence(value = "AUDIT_OVERSEE_RECORDS_SEQ")
@TableName("TBL_YQNS_PROJ_AUDIT_TYPE_NAME")
@Schema(name="TblYqnsProjectAuditType对象", description="央企内审-基础配置-工程审计类型-Name")
public class TblYqnsProjectAuditTypeNameEntity implements Serializable {

    @Bean
    public OracleKeyGenerator genkey() {
        return new OracleKeyGenerator();
    }

    /**
     * 审计方法维护主键-NameID
     */
    @TableId(value = "ID",type = IdType.INPUT)
    @Schema(name = "审计方法维护-Name主键ID")
    private Long id;

    /**
     * 名称
     */
    @Schema(name = "名称")
    @TableField("NAME")
    private String name ;


    /**
     * 关联的父级ID - 工程审计类型
     */
    @Schema(name = "关联的父级ID - 工程审计类型")
    @TableField("TYPEID")
    private String typeId ;



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
     * 央企内审-审计实施-我的任务-审查
     */
    @Schema(name = "央企内审-审计实施-我的任务-审查")
    @TableField(exist = false,typeHandler = JacksonTypeHandler.class)
    @IgnoreSwaggerParameter
    private TblYqnsMyTaskReviewEntity tblYqnsMyTaskReviewEntity ;


    @Schema(name = "央企内审-审计实施-督导任务内容")
    @TableField(exist = false,typeHandler = JacksonTypeHandler.class)
    @IgnoreSwaggerParameter
    private TblYqnsSjdd sjddrw;

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

}
