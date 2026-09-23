package com.huabo.audit.oracle.entity;
import javax.persistence.*;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 描述: 
 * author: ziyao
 * date: 2022-04-13
 */
@Table(name = "TBL_NBSJ_QUESTION")
@Data
@Schema(name="实体类")
@Accessors(chain = true)
public class TblNbsjQuestionEntity {
	
	public static final Integer STATUSYES=2; //已确认
	public static final Integer STATUSNO=1;//已拒绝
	public static final Integer NUSTATUS=0;//未确认
	public static final Integer GROUPYES=1;//已分组
	public static final Integer GROUPNO=0;//未分组
	public static final Integer FQNO=0;//未发起整改
	public static final Integer FQYES=1;//发起整改
    @TableField(exist = false)
    @Schema(name = "审计项目ID")
    @Transient
    private BigDecimal projectId;

    @TableField(exist = false)
    @Schema(name = "项目编号")
    @Transient
    private String projectCode;

    @TableField(exist = false)
    @Schema(name = "审计项目名称")
    @Transient
    private String projectName;

    @TableField(exist = false)
    @Schema(name = "计划年份")
    @Transient
    private String planYear;

    @TableField(exist = false)
    @Schema(name = "问题标题")
    @Transient
    private String quesTitle;

    @TableField(exist = false)
    @Schema(name = "问题描述")
    @Transient
    private String auditDesc;

    @TableField(exist = false)
    @Schema(name = "审计单位")
    @Transient
    private String auditUnit;

    @TableField(exist = false)
    @Schema(name = "被审计单位")
    @Transient
    private String auditedUnit;

    @TableField(exist = false)
    @Schema(name = "发现人")
    @Transient
    private String findPeople;

    @TableField(exist = false)
    @Schema(name = "我得底稿-审计事项")
    @Transient
    private String businessaffiliation;


    @TableField(exist = false)
    @Schema(name = "我得底稿-审计发现")
    @Transient
    private String auditDiscoverable;


    @TableField(exist = false)
    @Schema(name = "我得底稿-底稿编号")
    @Transient
    private String sheetcode;

    @TableField(exist = false)
    @Schema(name = "我得底稿-拟稿人")
    @Transient
    private String realname;

    @TableField(exist = false)
    @Schema(name = "我得底稿-被审计单位")
    @Transient
    private String orgname;

    @TableField(exist = false)
    @Schema(name = "我得底稿-是否是汇总底稿")
    @Transient
    private String hzdg;

    @TableField(exist = false)
    @Schema(name = "我得底稿-被审计对象id")
    @Transient
    private String orgids;

    @TableField(exist = false)
    @Schema(name = "我得底稿-被审计对象名称")
    @Transient
    private String orgidnames;

    @Schema(name = "问题编号")
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY,generator = "select HIBERNATE_SEQUENCE.nextval from dual")
    @TableId("questionid")
    @TableField("questionid")
    @Column(name = "questionid")
    private BigDecimal questionId;

    @TableField(value = "sheetid")
    @Schema(name = "表格编号")
    @Column(name = "sheetId")
    private BigDecimal sheetId;

    @TableField(value = "status")
    @Column(name = "status")
    @Schema(name = "状态")
    private Integer status;

    @TableField(value = "groupstatus")
    @Column(name = "groupstatus")
    @Schema
    private Integer groupStatus;

    @TableField(value = "recstatus")
    @Column(name = "recstatus")
    @Schema
    private Integer recStatus;

    @TableField(exist = false)
    @Schema
    @Transient
    private TblNbsjSheetEntity nbsjSheet;
    
    @TableField(exist = false)
    @Schema(name = "问题类型")
    @Transient
    private String internalType;
    
    @TableField(exist = false)
    @Transient
    private Integer ifsbg;


}
