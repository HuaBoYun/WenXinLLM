package com.huabo.audit.oracle.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.huabo.audit.config.IgnoreSwaggerParameter;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.util.BaseVo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <p>
 * 整改清单表
 * </p>
 *
 * @author LHP
 * @since 2023-11-16
 */
@Data
public class TblZgzzIssuesilistVo extends BaseVo implements Serializable {

	 private static final long serialVersionUID = 1L;
	    
	    @Schema(name = "整改清单主键")
	    private String issuesId;

	      @Schema(name = "业务编号")
	    private String issuesCode;

	      @Schema(name = "审计内控关联表单外键")
	    private BigDecimal quesitionId;

	      @Schema(name = "业务名称")
	    private String issuesName;

	      @Schema(name = "创建人")
	    private BigDecimal createStaff;

	      @Schema(name = "创建时间")
	      @JSONField(format = "yyyy-MM-dd")
	      @DateTimeFormat(pattern="yyyy-MM-dd")
	    private Date createTime;

	      @Schema(name = "关联项目主键")
	    private BigDecimal projectId;

	      @Schema(name = "修改时间")
	      @JSONField(format = "yyyy-MM-dd")
	      @DateTimeFormat(pattern="yyyy-MM-dd")
	    private Date updateTime;

	      @Schema(name = "事项")
	    private String issuesItem;

	      @Schema(name = "问题标题")
	    private String issuesTitle;

	      @Schema(name = "审计执行过程")
	    private String programProcess;

	      @Schema(name = "问题详情")
	    private String questionMemo;

	      @Schema(name = "审计意见及建议")
	    private String opinions;

	      @Schema(name = "业务类别 1-审计 2-风控 3-外部")
	    private Integer issuesType;
	      
	      @Schema(name = "审批状态 0-未整改 1-审批中 ，2-已退回 3-已撤销 6-已完成 ，7-整改中 ，8-整改完成，9-未销号问题 、 10-再次整改,11-关闭")
	    private Integer status;

	      @Schema(name = "历史版本")
	    private Integer issuesVersion;

	      @Schema(name = "变更前的主键")
	    private String issuesParent;
	      
	      @Schema(name = "所属公司")
	    private BigDecimal linkOrgId;
	      
	      @Schema(name = "所属部门")
	    private BigDecimal linkDeptId;
	      
	      @Schema(name = "附件列表")
	      @Transient
	      @IgnoreSwaggerParameter
	    private List<TblAttachment> attList = new ArrayList<TblAttachment>(0);
	    
	      @Schema(name = "项目名称")
	    private String projectName;
	    
	      @Schema(name = "项目编号")
	    private String projectNo;
	    
	      @Schema(name = "创建人姓名")
	    private String createStaffName;
	    
	      @Schema(name = "审计对象名称")
	    private String auditObjectName;
	      
	      @Schema(name = "被审计对象类型 1公司 2部门 3用户")
	    private Integer auditObjectType;
	      
	      @Schema(name = "被审计对象主键")
	    private BigDecimal auditObjectId;
	      
	      @Schema(name = "责任人")
	    private BigDecimal responsiblePerson;
	      
	      @Schema(name = "责任部门")
	    private BigDecimal responsibleDept;
	      
	      @Schema(name = "责任人名称")
	      private String responsiblePersonName;
	        
	        @Schema(name = "责任部门名称")
	      private String responsibleDeptName;
	    
	      @Schema(name = "整改方案主键，查询整改方案相关整改清单时使用")
	      @IgnoreSwaggerParameter
	      private String planId;
	      
	    @Schema(name = "筛选条件创建时间开始时间")
	    @DateTimeFormat(pattern="yyyy-MM-dd")
	    @JSONField(format = "yyyy-MM-dd")
	    private Date startDate;
	    @Schema(name = "筛选条件创建时间结束时间")
	    @DateTimeFormat(pattern="yyyy-MM-dd")
	    @JSONField(format = "yyyy-MM-dd")
	    private Date endDate;
	    
	    @Schema(name = "多个单据状态查询，用逗号拼接")
	    private String statusStr;
	    
	    @Schema(name = "历史状态用于还原")
	    private Integer historyStatus;
	    
	    @Schema(name = "再次整改前的整改清单信息")
	    @IgnoreSwaggerParameter
	    private TblZgzzIssuesilistVo parentIssues;
	    
	    @Schema(name = "所有整改落实信息")
	    @Transient
	    @IgnoreSwaggerParameter
	    private List<TblRectificationIssuesVo> relaList = new ArrayList<TblRectificationIssuesVo>(0);
	    
	    @Schema(name = "所有整改落实信息数量")
	    @Transient
	    @IgnoreSwaggerParameter
	    private Integer relaCount;

		public String getStatusResultStr(Integer currentStatus) {
			String statusStr = "";
			//审批状态 0-未整改 1-审批中 ，2-已退回 3-已撤销 6-已完成 ，7-整改中 ，8-整改完成，9-未销号问题
			switch (currentStatus) {
				case 1:
					statusStr = "审批中";
					break;
				case 2:
					statusStr = "已退回";
					break;
				case 3:
					statusStr = "已撤销";
					break;
				case 6:
					statusStr = "审批已完成";
					break;
				case 7:
					statusStr = "整改中";
					break;
				case 8:
					statusStr = "整改已完成";
					break;
				case 9:
					statusStr = "未销号";
					break;
				case 10:
					statusStr = "再次整改";
					break;
				case 11:
					statusStr = "关闭";
					break;
				default:
					statusStr = "未整改";
					break;
			}
			
			return statusStr;
		}
		
		@Schema(name = "排序 1升序 2降序")
	    private Integer sortFlag;
		
		@Schema(name = "排序字段")
	    private String sortField;
		
		@Schema(name = "整改方案")
	    private String rectificationPlan;
		
		@Schema(name = "整改情况概述")
	    private String situationoverView;

		@Schema(name = "整改结论(来自整改落实表)")
		private String conclusion;

		@Schema(name = "是否销号 0-未销号 1-已销号(来自整改落实表)")
		private String isxh;

		@Schema(name = "整改截止时间(来自整改要求表)")
		private String deadline;

	@Schema(name = "密级主键")
	private BigDecimal secrectLevelId;
	      
	@Schema(name = "知悉范围 多个逗号分隔")
    private String staffScopeIds;
	      
	@Schema(name = "知悉访问人员姓名 多个逗号分隔")
	private String staffScopeNames;

	@Schema(name = "年度查询条件")
	private String year;

	@Schema(name = "主管部门名称查询条件")
	private String orgName;

	@Schema(name = "查询类型: yzg(已整改) | wzg(未整改) | zs(整改总数) | yxh(已销号) | wxh(未销号) | xhzs(销号总数)")
	private String queryType;

}
