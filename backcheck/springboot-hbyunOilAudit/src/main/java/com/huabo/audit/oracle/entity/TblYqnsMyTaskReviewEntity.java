package com.huabo.audit.oracle.entity;

import com.baomidou.mybatisplus.annotation.*;
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
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author GJ.C
 * @CLASS_NAME: TblYqnsMyTaskReviewEntity
 * @PACKAGE_NAME: com.huabo.audit.oracle.entity
 * @date 2023/10/28 14:40
 * @version: V1.0
 * @description: 央企内审-审计实施-我的任务-审查
 */
@Data
@EqualsAndHashCode(callSuper = false)
@KeySequence(value="AUDIT_OVERSEE_RECORDS_SEQ")
@TableName("TBL_YQNS_MY_TASK_REVIEW")
@Schema(name="TBL_YQNS_MY_TASK_REVIEW 对象", description="央企内审-审计实施-我的任务-审查")
public class TblYqnsMyTaskReviewEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Bean
    public OracleKeyGenerator genkey() {
        return new OracleKeyGenerator();
    }

    /**
     * 审查主键ID
     */
    @TableId(value = "ID",type = IdType.INPUT)
    @Schema(name = "审查主键ID")
    private Long id;

    
    @Schema(name = "TblYqnsProjectAuditTemplate 主键")
    @TableField("TEMPLATEID")
    private Long templateId;
    
    @Schema(name = "当前审计实施项目 主键")
    @TableField("PROJECTID")
    private BigDecimal projectId;
    
    /**
     * 记事本
     */
    @Schema(name = "记事本")
    @TableField("NOTEPAD")
    private String notepad ;


    /**
     * 督导意见
     */
    @Schema(name = "督导意见")
    @TableField("SUPERVISIONOPINIONS")
    private String supervisionOpinions ;


    /**
     * 备注
     */
    @Schema(name = "备注")
    @TableField("REMARKS")
    private String remarks ;


    /**
     * 关联类型ID - 央企内审-基础配置-工程审计类型-name
     */
    @Schema(name = "关联类型ID - 央企内审-基础配置-工程审计类型-Name ")
    @TableField("TYPENAMEID")
    private String typeNameId ;


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
