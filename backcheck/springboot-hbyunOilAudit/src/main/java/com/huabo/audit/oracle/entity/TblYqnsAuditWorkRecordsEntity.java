package com.huabo.audit.oracle.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Transient;

import com.huabo.audit.oracle.entity.base.BaseReservedProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.incrementer.OracleKeyGenerator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.audit.config.IgnoreSwaggerParameter;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Classname TblYqnsAuditWorkRecordsEntity
 * @Description TODO 审计工作记录Entity
 * @Date 2023/10/8 21:36
 * @Created by GJ.C
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_YQNS_AUDIT_WORK_RECORDS")
@Schema(name="TblYqnsAuditWorkRecords对象", description=" 央企内审-审计实施-审计工作记录") 
public class TblYqnsAuditWorkRecordsEntity extends BaseReservedProperty {

    @Bean
    public OracleKeyGenerator genkey() {
        return new OracleKeyGenerator();
    }

    /**
     * 审计工作记录主键ID
     */
    @TableId(value = "ID")
    @Schema(name = "审计工作记录主键ID")
    @Column(name = "ID")
    private Long id;
    
    @Schema(name = "编号")
    @TableField(value = "NO")
    private String no;

    /**
     * 被审计单位名称
     */
    @Schema(name = "被审计单位名称")
    @TableField("AUDITEENAME")
    private String auditeeName;


    /**
     * 被审计单位名称Id
     */
    @Schema(name = "被审计单位名称Id")
    @TableField("AUDITEENAMEID")
    private String auditeeNameId;




    /**
     * 审计实施时间
     */
    @Schema(name = "审计实施时间")
    @TableField("IMPLEMENTATIONTIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date implementationTime;


    /**
     * 审计项目名称
     */
    @Schema(name = "审计项目名称")
    @TableField("PROJECTNAME")
    private String projectName;


    /**
     * 分工负责审计内容
     */
    @Schema(name = "分工负责审计内容")
    @TableField("RESPONSIBLECONTENT")
    private String responsibleContent;


    /**
     * 审计内容和目标
     */
    @Schema(name = "审计内容和目标")
    @TableField("CONTENTOBJECTIVES")
    private String contentObjectives;


    /**
     * 执行的审计程序和工作过程
     */
    @Schema(name = "执行的审计程序和工作过程")
    @TableField("EXECUTEDPROCEDURESPROCESSES")
    private String executedProceduresProcesses;


    /**
     * 发现的疑点、线索及查证情况
     */
    @Schema(name = "发现的疑点、线索及查证情况")
    @TableField("VERIFICATIONSITUATION")
    private String verificationSituation;


    /**
     * 审计线索及数据来源
     */
    @Schema(name = "审计线索及数据来源")
    @TableField("CLUESSOURCES")
    private String cluesSources;


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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @TableField("CREATETIME")
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
     * 删除标记 默认：0 删除：1
     */
    @Schema(name = "删除标记 默认：0 删除：1")
    @TableField("DELFLAG")
    private String delFlag;

    /**
     * 项目id
     */
    @Schema(name = "项目id")
    @TableField("PROJECTID")
    private BigDecimal projectId;
 
    /**
     * 预留字段
     */
    @Schema(name = "预留字段")
    @TableField("RESERVED")
    private String reserved;

    @Schema(hidden=true)
    @IgnoreSwaggerParameter
    @Transient
    private List<TblAttachment> tblNoteAtts;
    
    @Schema(name = "审批状态")
    @TableField("STATUS")
    private String status;
    
    @Schema(name = "索引号")
    @TableField("INDEXNO")
    private String indexno;
    
    @TableField(exist = false)
    @Schema(name = "创建人名称")
    private String realname;
    

}
