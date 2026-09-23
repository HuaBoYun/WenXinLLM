package com.huabo.audit.oracle.entity;

import com.baomidou.mybatisplus.annotation.*;
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
 * @CLASS_NAME: TblYqnsTemplateManagerEntity
 * @PACKAGE_NAME: com.huabo.audit.oracle.entity
 * @date 2023/10/28 14:40
 * @version: V1.0
 * @description: TODO 央企内审-基础配置-模板管理
 */
@Data
@EqualsAndHashCode(callSuper = false)
@KeySequence(value="AUDIT_OVERSEE_RECORDS_SEQ")
@TableName("TBL_YQNS_TEMPLATE_MANAGER")
@Schema(name="TblYqnsTemplateManagerEntity 对象", description="央企内审-基础配置-模板管理")
public class TblYqnsTemplateManagerEntity extends BaseReservedProperty implements Serializable {

    @Bean
    public OracleKeyGenerator genkey() {
        return new OracleKeyGenerator();
    }

    /**
     * 主键ID
     */
    @TableId(value = "ID",type = IdType.INPUT)
    @Schema(name = "主键ID")
    private Long id;

    @Schema(name = "编号")
    @TableField(value = "NO")
    private String no;
    
    /**
     * 模板名称
     */
    @Schema(name = "模板名称")
    @TableField("TEMPLATENAME")
    private String templateName ;


    /**
     * 备注
     */
    @Schema(name = "备注")
    @TableField("REMARKS")
    private String remarks ;



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



    @Schema(name = "附件主键集合")
    @TableField(exist = false)
    private List<String> attIds;

    @Schema(name = "附件集合")
    @TableField(exist = false)
    @IgnoreSwaggerParameter
    private List<TblAttachment> attachments;

}
