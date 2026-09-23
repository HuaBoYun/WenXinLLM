package com.huabo.monitor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Transient;

/**
 * 描述: 
 * author: ziyao
 * date: 2022-04-13
 */
@TableName("TBL_NBSJ_QUESTION")
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
    private Integer projectId;

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

    @TableId(value = "questionid", type= IdType.AUTO)
    @Schema(name = "问题编号")
    private Integer questionId;

    @TableField(value = "sheetid")
    @Schema(name = "表格编号")
    private Integer sheetId;

    @TableField(value = "status")
    @Schema(name = "状态")
    private Integer status;

    @TableField(value = "groupstatus")
    @Schema
    private Integer groupStatus;

    @TableField(value = "recstatus")
    @Schema
    private Integer recStatus;
    
    @TableField(value = "nbsjSheet")
    @Schema
    @Transient
    private TblNbsjSheetEntity nbsjSheet;
    

}
