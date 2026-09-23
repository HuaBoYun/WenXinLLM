package com.huabo.audit.oracle.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.huabo.audit.oracle.entity.base.BaseReservedProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.baomidou.mybatisplus.extension.incrementer.OracleKeyGenerator;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author GJ.C
 * @CLASS_NAME: TblYqnsAuditMethodMaintenanEntity
 * @PACKAGE_NAME: com.huabo.audit.oracle.entity
 * @date 2023/10/18 12:01.
 * @version: V1.0
 * @description: 央企内审-审计实施-我的底稿
 */
@Data
@EqualsAndHashCode(callSuper = false)
@KeySequence(value = "AUDIT_OVERSEE_RECORDS_SEQ")
@TableName("TBL_YQNS_AUDIT_MY_MANUSCRIPT")
@Schema(name="TblYqnsAuditMyManuscriptEntity对象", description="央企内审-审计实施-我的底稿")
public class TblYqnsAuditMyManuscriptEntity extends BaseReservedProperty implements Serializable {

	public static final String tableId = "150";
	
    @Bean
    public OracleKeyGenerator genkey() {
        return new OracleKeyGenerator();
    }

    /**
     * 我的底稿 主键ID
     */
    @TableId(value = "ID",type = IdType.INPUT)
    @Schema(name = "我的底稿主键ID")
    private Long id;



    /**
     * 底稿编号 自动生成
     */
    @Schema(name = "底稿编号")
    @TableField("DRAFTNUMBER")
    private String draftNumber ;
    
    @Schema(name = "底稿名称")
    @TableField("DRAFTNAME")
    private String draftName ;

    /**
     * 是否汇总底稿-标识
     */
    @Schema(name = "是否汇总底稿-标识")
    @TableField("SUMMARYDRAFTMARK")
    private String summaryDraftMark;


    /**
     * 被审计单位名称
     */
    @Schema(name = "被审计单位名称")
    @TableField("AUDITEENAME")
    private String auditeeName ;


    /**
     * 被审计单位名称
     */
    @Schema(name = "被审计单位名称ID")
    @TableField("AUDITEENAMEID")
    private String auditeeNameId ;

    @Schema(name = "专业科室人员")
    @TableField("THEDEPTSTAFFID")
    private BigDecimal thedeptstaffid;
    
    @Schema(name = "专业科室人员姓名")
    @TableField("THEDEPTSTAFFNAME")
    private String thedeptstaffname;

    /**
     * 审计项目名称
     */
    @Schema(name = "审计项目名称")
    @TableField("PROJECTNAME")
    private String projectName ;


    /**
     * 审计事项
     */
    @Schema(name = "审计事项")
    @TableField("AUDITMATTERS")
    private String auditMatters ;



    /**
     * 基础工作
     */
    @Schema(name = "'基础工作'")
    @TableField("BASICWORK")
    private String basicWork ;

    /**
     * 审计证据是否准确、真实、合法
     */
    @Schema(name = "审计证据是否准确、真实、合法")
    @TableField("EVIDENCEACCURATE")
    private String evidenceAccurate ;



    /**
     * 审计查证事实描述是否详尽、充分
     */
    @Schema(name = "审计查证事实描述是否详尽、充分")
    @TableField("DGVERIFICATIONDESCRIPTION")
    private String dgverificationDescription ;


    /**
     * 审计结论是否客观、公正
     */
    @Schema(name = "审计结论是否客观、公正")
    @TableField("DGAUDITCONCLUSION")
    private String dgauditConclusion ;

    /**
     * 审计处理意见及建议是否正确、具有可操作性和建设性
     */
    @Schema
    @TableField("DGHANDLINGOPINIONS")
    private String dghandlingOpinions ;

    /**
     * 是否是问题底稿
     */
    @Schema(name = "是否是问题底稿")
    @TableField("PROBLEMDRAFT")
    private String probleMdraft ;




    /**
     * 我的底稿-下方审计查证事实List
     */
    @Schema(name = "我的底稿-下方审计查证事实List")
    @TableField(exist = false,typeHandler = JacksonTypeHandler.class)
    private List<TblYqnsAuditMyManuVerifyEntity> auditMyManuVerifyEntityList ;


    /**
     * 基础设置 - 类型id
     */
    @Schema(name = "基础设置 - 类型id")
    @TableField("TYPEID")
    private String typeId ;


    /**
     * 基础设置 - 模板id
     */
    @Schema(name = "基础设置 - 模板id")
    @TableField("TEMPLATEID")
    private String templateId ;

    /**
     * 创建人
     */
    @Schema(name = "创建人")
    @TableField("CREATEUSER")
    private String createUser;

    
    @Schema(name = "创建人名称")
    @TableField(exist = false)
    private String createUsername;
    
    @Schema(name = "导出格式的创建时间")
    @TableField(exist = false)
    private String exportDate;



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

    @Schema(name = "项目id")
    @TableField("PROJECTID")
    private BigDecimal projectId;

    @Schema(name = "预留字段1")
    @TableField("TEXT")
    private String text;

    @Schema(name = "预留字段2")
    @TableField("TEXT2")
    private String text2;

    @Schema(name = "审批状态 1-审批中、2-需调整、6-已完成")
    @TableField("STATUS")
    private Integer status;

    @Schema(name = "附件List")
    @TableField(exist = false)
    private List<TblAttachment> attList;
    
    @Schema(name = "审计工作记录List")
    @TableField(exist = false)
    private List<TblYqnsAuditWorkRecordsEntity> workList;
    
    @Schema(name = "审计工作记录主键")
    @TableField(exist = false)
    private String reportIds;
    
    @Schema(name = "附件ids")
    @TableField("ATTIDS")
    private String attIds;
    
    @Schema(name = "复核人id")
    @TableField("FHSTAFFID")
    private String fhstaffid ;
    
    @Schema(name = "复核人姓名")
    @TableField("FHSTAFFNAME")
    private String fhstaffname ;
    
    
    @Schema(name = "查询参数")
    @TableField(exist = false)
    private String isall;
    
    
    @Schema(name = "专业科室审核时间")
    @TableField(exist = false)
    private Date zyksshDate;
    
    @Schema(name = "专业科室审核人")
    @TableField(exist = false)
    private String zyksshPeople;
    
    @Schema(name = "修改后上报时间") 
    @TableField(exist = false)
    private Date fqrShDate;

    
    @Schema(name = "专业科室复核时间")
    @TableField(exist = false)
    private Date zyksfhDate;
    
    @Schema(name = "专业科室复核意见")
    @TableField(exist = false)
    private String zyksfhCommont;
    
    @Schema(name = "专业科室复核意见")
    @TableField(exist = false)
    private String type;

}
