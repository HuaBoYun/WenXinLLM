package com.huabo.audit.oracle.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.incrementer.OracleKeyGenerator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.audit.config.IgnoreSwaggerParameter;

import com.huabo.audit.oracle.entity.base.BaseReservedProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 央企模块-审计整改-问题整改
 * @TableName TBL_YQNS_SJZG_WTZG
 */
@TableName(value ="TBL_YQNS_SJZG_WTZG")
@Data
public class TblYqnsSjzgWtzg extends BaseReservedProperty implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public static final Integer SHEETID = 195;

	/**
     * 问题整改主键
     */
    @Schema(name = "问题整改主键")
    @TableId(value = "WTZGID",type = IdType.INPUT)
    private BigDecimal wtzgid;

    /**
     * 移送内容ID
     */
    @Schema(name = "移送内容ID")
    @TableField(value = "YSNRID")
    private BigDecimal ysnrid;

    @Schema(name = "整改清单主键")
    @TableField(value = "ISSUESID")
    private BigDecimal issuesId;
    
    @TableField(value = "PROJECTID")
    @Schema(name = "审计项目ID")
    private BigDecimal projectId;
    
    @TableField(value = "PROJECTNAME")
    @Schema(name = "审计项目名称")
    private String projectName;
    
    /**
     * 整改分类
     */
    @Schema(name = "整改分类")
    @TableField(value = "WTZGFL")
    private String wtzgfl;
    
    /**
     * 整改状态
     */
    @Schema(name = "整改状态   3-整改完毕 ,2-正在整改 , 1-尚未开始整改 , 0-不接受审计意见  ")
    @TableField(value = "ZGZT")
    private String zgzt;
    
    /**
     * 当期整改状态
     */
    @Schema(name = "当期整改状态 3-整改完毕 ,2-正在整改 , 1-无法整改")
    @TableField(value = "DQZGZT")
    private String dqzgzt;
    
    /**
     * 当期直接经济成果类型
     */
    @Schema(name = "当期直接经济成果类型")
    @TableField(value = "DQZJJJCGTYPE")
    private String dqzjjjcgtype;
    
    /**
     * 当期直接经济成果（元）
     */
    @Schema(name = "当期直接经济成果（元）")
    @TableField(value = "DQZJJJCG")
    private BigDecimal dqzjjjcg;
    
    /**
     * 当期其他经济成果类型
     */
    @Schema(name = "当期其他经济成果类型")
    @TableField(value = "DQQTJJCGTYPE")
    private String dqqtjjcgtype;
    
    /**
     * 当期其他经济成果（元）
     */
    @Schema(name = "当期其他经济成果（元）")
    @TableField(value = "DQQTJJCG")
    private BigDecimal dqqtjjcg;
    
    /**
     * 当期整改描述
     */
    @Schema(name = "当期整改描述")
    @TableField(value = "DQZGMS")
    private String dqzgms;
    
    /**
     * 是否移送
     */
    @Schema(name = "是否移送，0否，1是")
    @TableField(value = "SFYS")
    private Integer sfys;
    
    @Schema(name = "是否复制 0-否  1-是")
    @TableField(value = "ISCOPY")
    private Integer iscopy;
    
    /**
     * 创建人
     */
    @Schema(name = "创建人")
    @TableField(value = "CJR")
    private String cjr;
    
    @Schema(name = "创建人主键")
    @TableField(value = "CJRID")
    private BigDecimal cjrId;
    
    
    @Schema(name = "审批状态  0-未审批 ，1-审批中、2-需调整、3-已通过、4-已终止、5-已跟踪、6-审批完成  ，7-完成")
    @TableField(value = "STATUS")
    private Integer status;
    
    @Schema(name = "回访审批状态  0-未审批 ，1-审批中、2-需调整、3-已通过、4-已终止、5-已跟踪、6-审批完成 ")
    @TableField(value = "HFSPSTATUS")
    private Integer hfspstatus;
    
    @Schema(name = "后续整改审批状态  0-未审批 ，1-审批中、2-需调整、3-已通过、4-已终止、5-已跟踪、6-审批完成 ")
    @TableField(value = "HXSPSTATUS")
    private Integer hxspstatus;
    
    /**
     * 创建时间
     */
    @Schema(name = "创建时间")
    @TableField(value = "CJSJ")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date cjsj;
    
    /**
     * 创建时间
     */
    @Schema(name = "修改时间")
    @TableField(value = "XGSJ")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date xgsj;
    
    @Schema(name = "所属部门主键")
    @TableField(value = "LINKDEPTID")
    private BigDecimal linkDeptId;
    
    @Schema(name = "所属公司主键")
    @TableField(value = "LINKORGID")
    private BigDecimal linkOrgId;
    
    /**
     * 当期整改金额
     */
    @Schema(name = "当期整改金额")
    @TableField(value = "DQZGJE")
    private BigDecimal dqzgje;
    
    @Schema(name = "已填报累计整改金额（元）")
    @TableField(value = "LJZGMONEY")
    private BigDecimal ljzgMoney;
    
    @Schema(name = "已填报累计经济成果（元）")
    @TableField(value = "LJJJCGMONEY")
    private BigDecimal ljjjcgMoney;
    
    @Schema(name = "主键集合")
    @TableField(exist = false)
    private List<String> ids;

    @Schema(name = "附件主键集合")
    @TableField(exist = false)
    private List<String> attIds;

    @Schema(name = "附件集合")
    @TableField(exist = false)
    @IgnoreSwaggerParameter
    private List<TblAttachment> attachments;
    
    @Schema(name = "移送内容")
    @TableField(exist = false)
    private TblYqnsSjzgYsnr tblYqnsSjzgYsnr;
    
    @Schema(name = "问题清单")
    @TableField(exist = false)
    private TblYqnsIssueListEntity tblIssueEntity;
    
    @Schema(name = "整改报告集合")
    @TableField(exist = false)
    private List<TblYqnsSjzgZgbg> zgbgList;
    
    @Schema(name = "整改投机信息集合")
    @TableField(exist = false)
    private List<TblYqnsSjzgSjzgtj> zgtjList;
    
    @Schema(name = "规章制度情况说明对象实体")
    @TableField(exist = false)
    private TblYqnsSjzgGzzdqksm tblYqnsSjzgGzzdqksm;
    
    @Schema(name = "审计要情附件集合")
    @TableField(exist = false)
    @IgnoreSwaggerParameter
    private List<TblAttachment> sjqyAttList;
    
    
    @Schema(name = "后续整改涉及发现问题数")
    @TableField(exist = false)
    private Integer wzgCount;
    
    @Schema(name = "已完成整改数")
    @TableField(exist = false)
    private Integer ywcCount;
    
    @Schema(name = "无法整改数")
    @TableField(exist = false)
    private Integer bzgCount;
    
    @Schema(name = "后续整改增在审批中的数据")
    @TableField(exist = false)
    private Integer spzCount;
    
    @Schema(name = "项目年度")
    @TableField(exist = false)
    private Integer planYear;
    
    @Schema(name = "项目年度")
    @TableField(exist = false)
    private Integer version;
    
    @Schema(name = "整改人主键")
    @TableField(exist = false)
    private BigDecimal rectPerson;
    
    @TableField(exist = false)
    @Schema(name = "整改人姓名")
    private String rectPersonName;
    
    @Schema(name = "下发时间")
    @TableField(exist = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date issueDate;
    
    @Schema(name = "审批通过时间")
    @TableField(exist = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date approvalDate;
   
}