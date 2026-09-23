package com.huabo.audit.oracle.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Transient;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.audit.config.IgnoreSwaggerParameter;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;

/**
 * 描述:
 * author: ziyao
 * date: 2022-04-20
 */
@TableName("TBL_NBSJ_SHEET")
@Data
@Schema(name="实体类")
@Accessors(chain = true)
public class TblNbsjSheetEntity {
	
	public final static Integer STATE1 = 1;//未审核
    public final static Integer STATE2 = 2;//审核中
    public final static Integer STATE3 = 3;//审核驳回
    public final static Integer STATE4 = 4;//审核完成
    public final static Integer STATE5 = 5;//需调整
	public final static String SHEETID = "10";

    @Schema(name = "主键")
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY,generator = "select HIBERNATE_SEQUENCE.nextval from dual")
    @TableId("sheetid")
    @TableField("sheetid")
    @Column(name = "sheetid")
    private BigDecimal sheetId;

    @TableField(value = "sheetcode")
    @Column(name = "sheetcode")
    @Schema(name = "底稿编号")
    private String sheetCode;

    @TableField(value = "sheetname")
    @Column(name = "sheetname")
    @Schema(name = "底稿名称")
    private String sheetName;

    @TableField(value = "sheettarget")
    @Column(name = "sheettarget")
    @Schema
    private String sheetTarget;

    @TableField(value = "auditorg")
    @Column(name = "auditorg")
    @Schema(name = "被审计单位")
    private BigDecimal auditOrg;

    @TableField(exist = false)
    @Schema(name="创建人",hidden=true)
    private TblStaff createStaffObj;
    
    @Column(name = "CREATESTAFF")
    @Schema(name = "创建人ID")
    private BigDecimal createstaff;

    @TableField(value = "createtime")
    @Column(name = "createtime")
    @Schema(name="创建时间",hidden=true)
    private Date createTime;

    @TableField(value = "projectid")
    @Column(name = "projectid")
    @Schema(name = "所属项目id")
    private BigDecimal projectId;

    @TableField(value = "updatetime")
    @Column(name = "updatetime")
    @Schema(name="修改时间",hidden=true)
    
    private Date updateTime;

    @TableField(value = "riskattrbution")
    @Column(name = "riskattrbution")
    @Schema(name = "风险归属")
    private String riskAttrbution;

    @TableField(value = "businessaffiliation")
    @Column(name = "businessaffiliation")
    @Schema(name = "审计事项")
    private String businessAffiliation;

    @TableField(value = "state")
    @Column(name = "state")
    @Schema(name="审核状态 1 未审核;2 审核中;3 审核驳回;4 审核完成;5 需调整",hidden=true)
    private Integer state;

    @TableField(value = "auditdesc")
    @Column(name = "auditdesc")
    @Schema(name = "审计程序执行过程")
    private String auditDesc;

    @TableField(value = "auditcourse")
    @Column(name = "auditcourse")
    @Schema(name = "审计意见及建议")
    private String auditCourse;

    @TableField(value = "auditdiscoverable")
    @Column(name = "auditdiscoverable")
    @Schema(name = "审计发现")
    private String auditDiscoverable;

    @TableField(value = "status")
    @Column(name = "status")
    @Schema(name = "确认状态")
    private Integer status;

    @TableField(value = "approver")
    @Column(name = "approver")
    @Schema(name="审批人名称记录",hidden=true)
    private String approver;

    @TableField(value = "risklevel")
    @Column(name = "risklevel")
    @Schema(name = "是否发现问题")
    private String riskLevel;

    @TableField(value = "programid")
    @Column(name = "programid")
    @Schema
    private BigDecimal proGramid;

    @TableField(value = "questitle")
    @Column(name = "questitle")
    @Schema(name = "问题标题")
    private String quesTitle;

    @TableField(value = "comptime")
    @Column(name = "comptime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(name="底稿完成时间",hidden=true)
    
    private Date compTime;

    @TableField(value = "targetname")
    @Column(name = "targetname")
    @Schema(name = "审计分项")
    private String targetName; 

    @TableField(value = "businesstype")
    @Column(name = "businesstype")
    @Schema(name = "业务单元")
    private String businessType;

    @TableField(value = "suditprocess")
    @Column(name = "suditprocess")
    @Schema(name = "审计程序")
    private String suditProcess;

    @TableField(value = "sjbwl")
    @Column(name = "sjbwl")
    @Schema(name = "审计备忘录")
    private String sjbwl;

    @TableField(value = "targetid")
    @Column(name = "targetid")
    @Schema(name = "任务id")
    private BigDecimal targetId;

    @TableField(value = "auditstaffid")
    @Column(name = "auditstaffid")
    @Schema
    private BigDecimal auditStaffId;

    @TableField(value = "belongtype")
    @Column(name = "belongtype")
    @Schema(name = "所属类型")
    private Integer belongType;

    @TableField(value = "detailtype")
    @Column(name = "detailtype")
    @Schema(name = "问题类型")
    private Integer detailType;

    @TableField(value = "nozgreasion")
    @Column(name = "nozgreasion")
    @Schema
    private String nozgreasion;

    @TableField(value = "relatedmoney")
    @Column(name = "relatedmoney")
    @Schema(name = "涉及金额")
    private Double relatedMoney;

    @TableField(value = "hgdetailtype")
    @Column(name = "hgdetailtype")
    @Schema(name = "合规详细类型")
    private Integer hgDetailType;

    @TableField(value = "procategories")
    @Column(name = "procategories")
    @Schema(name = "国资委问题类别")
    private Integer procategories;

    @TableField(value = "assqrback")
    @Column(name = "assqrback")
    @Schema
    private String assqrback;
    @Transient
    @TableField(exist = false)
    @Schema(name = "项目名称")
    private String projectName;
    @Transient
    @TableField(exist = false)
    @Schema(name = "被审计单位")
    private String auditedUnit;
    @Transient
    @TableField(exist = false)
    @Schema(name = "审计人员")
    private String auditStaff;
   
    @TableField(value = "firststaffid")
    @Column(name = "firststaffid")
    @Schema(name = "第一复核人")
    private BigDecimal firststaffid;
    
    @TableField(value = "secondstaffid")
    @Column(name = "secondstaffid")
    @Schema(name = "第二复核人")
    private BigDecimal secondstaffid;
    @Transient
    @TableField(exist = false)
    private TblNbsjProject project;
    @Transient
    @TableField(exist = false)
    @Schema(hidden=true)
    private TblOrganization organization;
    @Transient
    @TableField(exist = false)
    @Schema(hidden=true)
    
    private TblStaff tblnbsjstaffs;
    @Transient
    @TableField(exist = false)
    @Schema(hidden=true)
    
    private List<TblNbsjQuestionEntity>  questions;
    @Transient
    @TableField(exist = false)
    @Schema(hidden=true)
   
    private Set<TblAttachment> tblAttachments;
    @Transient
    @TableField(exist = false)
    @Schema(name="拟稿人",hidden=true)
    private String realname;
    @Transient
    @TableField(exist = false)
    @Schema(name="被审计单位",hidden=true)
    private String orgname;
    
    @TableField(value = "YJFH")
    @Column(name = "YJFH")
    @Schema(name = "一级复核人姓名")
    private String yjfh;
    
    @TableField(value = "EJFH")
    @Column(name = "EJFH")
    @Schema(name = "二级复核人姓名")
    private String ejfh;
    
    
    @TableField(value = "INTERNALTYPE")
    @Column(name = "INTERNALTYPE")
    @Schema(name = "内部问题类型")
    private String internalType;
    
    @TableField(value = "RELATIONSHEETIDS")
    @Column(name = "RELATIONSHEETIDS")
    @Schema(name = "汇总底稿关联sheetid")
    private String relationsheetids;
    
    @TableField(value = "HZDG")
    @Column(name = "HZDG")
    @Schema(name = "是否是汇总底稿")
    private String hzdg;
    @Transient
    @TableField(exist = false)
    @Schema(name="被审计人",hidden=true)
    private String auditusername;
    @Transient
    @TableField(exist = false)
    private Integer tsize;
    @Transient
    @TableField(exist = false)
    private Integer sfssqr;//是否事实确认
    @Transient
    @TableField(exist = false)
    private Integer sfzg; //是否整改
    
    @TableField(value = "ORGIDS")
    @Column(name = "ORGIDS")
	 @Schema(name = "被审计对象id")
	 private String orgIds;

	 @TableField(value = "ORGIDNAMES")
     @Column(name = "ORGIDNAMES")
	 @Schema(name = "被审计对象名称")
	 private String orgIdNames;
	
	@TableField(value = "ISTYPE")
    @Schema(name = "数据来源：内控：1、审计：2、外部:3")
    private String istype;

	@TableField(exist = false)
    @Schema(name = "项目编号")
    private String projectCode;
    @TableField(value = "operateId")
	@Schema(name = "模板主键")
    private String operateId;
    
    @TableField(value = "GLID")
    @Schema(name = "关联审计或内控问题id")
    private String glid;
    
    @TableField(exist = false)
    @Schema(name = "关联审计取证单ids")
    private String certificateIds;
    
    @TableField(exist = false)
    @Schema(name = "关联缺陷ids")
    private String bugIds;
    
    @TableField(exist = false)
    @Schema(name = "创建时间年月日")
    private String createTimeStr;

    @TableField(exist = false)
    @Schema(name = "被审计对象类型 1公司 2部门 3用户")
    @Transient
    private Integer auditObjectType;
    
    @TableField(exist = false)
    @Schema(name = "附件集合")
    private String attids;
    
    @Schema(name = "创建人员部门")
    @TableField("ORGID")
    private BigDecimal orgid;

    @Schema(name = "密级主键")
    @TableField("SECRECTLEVELID")
    private BigDecimal secrectLevelId;

    @Schema(name = "知悉范围 多个逗号分隔")
    @TableField("STAFFSCOPEIDS")
    private String staffScopeIds;

    @Schema(name = "知悉访问人员姓名 多个逗号分隔")
    @TableField("STAFFSCOPENAMES")
    private String staffScopeNames;

    @Schema(name = "底稿类型")
    @TableField("SHEETTYPE")
    private String sheettype;
    
    //创建人ID
    @TableField(exist = false)
    private BigDecimal createstaffid;
    
    
    @TableField(value = "IFSBG")
    @Column(name = "IFSBG")
    @Schema(name = "标记是否上报告：1否，其他：是")
    private Integer ifsbg;
    
}
