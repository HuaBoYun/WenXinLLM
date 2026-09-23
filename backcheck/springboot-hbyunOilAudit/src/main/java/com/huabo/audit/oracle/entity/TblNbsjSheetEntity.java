package com.huabo.audit.oracle.entity;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.huabo.audit.config.IgnoreSwaggerParameter;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

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

    @TableId(value = "sheetid", type = IdType.AUTO)
    @Schema(name = "主键")
    private Integer sheetId;

    @TableField(value = "sheetcode")
    @Schema(name = "底稿编号")
    private String sheetCode;

    @TableField(value = "sheetname")
    @Schema(name = "底稿名称")
    private String sheetName;

    @TableField(value = "sheettarget")
    @Schema
    private String sheetTarget;

    @TableField(value = "auditorg")
    @Schema(name = "被审计单位")
    private Integer auditOrg;

    @TableField(value = "createstaff")
    @Schema(name="创建人",hidden=true)
    @IgnoreSwaggerParameter
    private TblStaff createStaff;

    @TableField(value = "createtime")
    @Schema(name="创建时间",hidden=true)
    @IgnoreSwaggerParameter
    private Date createTime;

    @TableField(value = "projectid")
    @Schema(name = "所属项目id")
    private Integer projectId;

    @TableField(value = "updatetime")
    @Schema(name="修改时间",hidden=true)
    @IgnoreSwaggerParameter
    private Date updateTime;

    @TableField(value = "riskattrbution")
    @Schema(name = "风险归属")
    private String riskAttrbution;

    @TableField(value = "businessaffiliation")
    @Schema(name = "审计事项")
    private String businessAffiliation;

    @TableField(value = "state")
    @Schema(name="审核状态 1 未审核;2 审核中;3 审核驳回;4 审核完成;5 需调整",hidden=true)
    @IgnoreSwaggerParameter
    private Integer state;

    @TableField(value = "auditdesc")
    @Schema(name = "审计程序执行过程")
    private String auditDesc;

    @TableField(value = "auditcourse")
    @Schema(name = "审计意见及建议")
    private String auditCourse;

    @TableField(value = "auditdiscoverable")
    @Schema(name = "审计发现")
    private String auditDiscoverable;

    @TableField(value = "status")
    @Schema(name = "确认状态")
    private Integer status;

    @TableField(value = "approver")
    @Schema(name="审批人名称记录",hidden=true)
    private String approver;

    @TableField(value = "risklevel")
    @Schema(name = "是否发现问题")
    private String riskLevel;

    @TableField(value = "programid")
    @Schema
    private Integer proGramid;

    @TableField(value = "questitle")
    @Schema(name = "问题标题")
    private String quesTitle;

    @TableField(value = "comptime")
    @Schema(name="底稿完成时间",hidden=true)
    @IgnoreSwaggerParameter
    private Date compTime;

    @TableField(value = "targetname")
    @Schema(name = "审计分项")
    private String targetName; 

    @TableField(value = "businesstype")
    @Schema(name = "业务单元")
    private String businessType;

    @TableField(value = "suditprocess")
    @Schema(name = "审计程序")
    private String suditProcess;

    @TableField(value = "sjbwl")
    @Schema(name = "审计备忘录")
    private String sjbwl;

    @TableField(value = "targetid")
    @Schema(name = "任务id")
    private Integer targetId;

    @TableField(value = "auditstaffid")
    @Schema
    private Integer auditStaffId;

    @TableField(value = "belongtype")
    @Schema(name = "所属类型")
    private Integer belongType;

    @TableField(value = "detailtype")
    @Schema(name = "问题类型")
    private Integer detailType;

    @TableField(value = "nozgreasion")
    @Schema
    private String nozgreasion;

    @TableField(value = "relatedmoney")
    @Schema(name = "涉及金额")
    private Double relatedMoney;

    @TableField(value = "hgdetailtype")
    @Schema(name = "合规详细类型")
    private Integer hgDetailType;

    @TableField(value = "procategories")
    @Schema(name = "国资委问题类别")
    private Integer procategories;

    @TableField(value = "assqrback")
    @Schema
    private String assqrback;

    @TableField(exist = false)
    @Schema(name = "项目名称")
    private String projectName;

    @TableField(exist = false)
    @Schema(name = "被审计单位")
    private String auditedUnit;

    @TableField(exist = false)
    @Schema(name = "审计人员")
    private String auditStaff;
   
    @TableField(value = "firststaffid")
    @Schema(name = "第一复核人")
    private Integer firststaffid;
    
    @TableField(value = "secondstaffid")
    @Schema(name = "第二复核人")
    private Integer secondstaffid;
    
    @TableField(exist = false)
    @Schema(hidden=true)
    @IgnoreSwaggerParameter
    private TblNbsjProject project;
    
    @TableField(exist = false)
    @Schema(hidden=true)
    @IgnoreSwaggerParameter
    private TblOrganization organization;
    
    @TableField(exist = false)
    @Schema(hidden=true)
    @IgnoreSwaggerParameter
    private TblStaff tblnbsjstaffs;
    
    @TableField(exist = false)
    @Schema(hidden=true)
    @IgnoreSwaggerParameter
    private List<TblNbsjQuestionEntity>  questions;
    
    @TableField(exist = false)
    @Schema(hidden=true)
    @IgnoreSwaggerParameter
    private Set<TblAttachment> tblAttachments;
    
    @TableField(exist = false)
    @Schema(name="拟稿人",hidden=true)
    private String realname;
    
    @TableField(exist = false)
    @Schema(name="被审计单位",hidden=true)
    private String orgname;
    
    @TableField(value = "YJFH")
    @Schema(name = "一级复核人姓名")
    private String yjfh;
    
    @TableField(value = "EJFH")
    @Schema(name = "二级复核人姓名")
    private String ejfh;
    
    
    @TableField(value = "INTERNALTYPE")
    @Schema(name = "内部问题类型")
    private String internalType;
    
    @TableField(value = "RELATIONSHEETIDS")
    @Schema(name = "汇总底稿关联sheetid")
    private String relationsheetids;
    
    @TableField(value = "HZDG")
    @Schema(name = "是否是汇总底稿")
    private String hzdg;
    
    @TableField(exist = false)
    @Schema(name="被审计人",hidden=true)
    private String auditusername;
    
    private Integer tsize;
    
    private Integer sfssqr;//是否事实确认
    
    private Integer sfzg; //是否整改
    
    @TableField(value = "ORGIDS")
	 @Schema(name = "被审计对象id")
	 private String orgIds;

	 @TableField(value = "ORGIDNAMES")
	 @Schema(name = "被审计对象名称")
	 private String orgIdNames;
	
}
