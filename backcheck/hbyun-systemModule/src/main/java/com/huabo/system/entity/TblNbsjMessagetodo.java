package com.huabo.system.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author huabo
 * @since 2022-05-23
 */
@Data
@TableName("TBL_NBSJ_MESSAGETODO")
@Schema(name="存储待办发送至OA记录", description="存储待办发送至OA记录")
public class TblNbsjMessagetodo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Schema(name="主键ID")
    @TableId("ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "select HIBERNATE_SEQUENCE.nextval from dual")
    @Column(name = "ID")
    private BigDecimal id;

    @Schema(name="模块名称：审计取证单、项目资料准备、审计报告")
    @TableField("MODELNAME")
    @Column(name = "MODELNAME")
    private String modelName;

    @Schema(name="来源系统")
    @Column(name = "APPNAME")
    @TableField("APPNAME")
    private String appName;

    @Schema(name="唯一标识")
    @Column(name = "MODELID")
    @TableField("MODELID")
    private String modelId;

    @Schema(name="标题")
    @Column(name = "SUBJECT")
    @TableField("SUBJECT")
    private String subject;

    @Schema(name="链接")
    @Column(name = "LINK")
    @TableField("LINK")
    private String link;

    @Schema(name="待办类型(1:审批待办，2.通知待办)")
    @Column(name = "TYPE")
    @TableField("TYPE")
    private Integer type;

    @Schema(name="操作类型（1：删除待办，2：删除指定待办所属人）")
    @Column(name = "OPTTYPE")
    @TableField("OPTTYPE")
    private String optType;

    @Schema(name="关键字")
    @Column(name = "KEY")
    @TableField("KEY")
    private String key;

    @Schema(name="待办所属对象")
    @Column(name = "TARGETS")
    @TableField("TARGETS")
    private String targets;
    
    @Schema(name="发起人")
    @Column(name = "CREATENAME")
    @TableField("CREATENAME")
    private String createName;
 
    @Schema(name="修改时间",hidden=true)
    @Column(name = "UPDATETIMR")
    @TableField("UPDATETIMR")
    private Date updateTime;
    
    @Schema(name="创建时间",hidden=true)
    @Column(name = "CREATETIME")
    @TableField("CREATETIME")
    private Date createTime;
    
    @Schema(name="状态 1.未处理  2.已处理 3.已经查看")
    @Column(name = "STATE")
    @TableField("STATE")
    private Integer state;
}
