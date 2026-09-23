package com.huabo.audit.oracle.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.huabo.audit.config.IgnoreSwaggerParameter;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.util.BaseVo;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 整改F方案表
 * </p>
 *
 * @author LHP
 * @since 2023-11-22
 */
@Data
@Schema(name="TblZgzzRectificationplan对象", description="整改方案表")
public class TblZgzzRectificationplanVo extends BaseVo implements Serializable {

	private static final long serialVersionUID = 1L;

    @Schema(name = "整改方案主键")
    private String planId;

    @Schema(name = "整改方案编号")
    private String planCode;

    @Schema(name = "整改方案名称")
  private String planName;

    @Schema(name = "方案类别 1-审计 2-风控 3-外部")
  private Integer planType;

    @Schema(name = "关联项目主键")
  private BigDecimal projectId;

    @Schema(name = "截止时间")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
  private Date deadlineTime;

    @Schema(name = "备注")
  private String planMemo;

    @Schema(name = "整改责任人主键")
  private BigDecimal response;

    @Schema(name = "方案 状态 0-未审批 1-审批中 ，2-已退回 3-已撤销  6-审批完未启动  7-已启动未分派  8-已下发未启动 9-分派完成 开始整改 10-整改完成 11-关闭")
  private Integer status;

    @Schema(name = "创建时间")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
  private Date createTime;

    @Schema(name = "修改时间")
    @TableField("UPDATETIME")
    @Column(name = "UPDATETIME")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
  private Date updateTime;

    @Schema(name = "方案所属公司主键")
  private BigDecimal linkOrgId;

    @Schema(name = "数据所属部门主键")
    @TableField("LINKDEPTID")
    @Column(name = "LINKDEPTID")
  private BigDecimal linkDeptId;

    @Schema(name = "创建人主键")
  private BigDecimal createStaff;

    @Schema(name = "修改人主键")
  private BigDecimal updateStaff;
    
    @Schema(name = "项目名称")
  private String projectName;

    @Schema(name = "整改责任人姓名")
    private String zrrRealName;
    
    @Schema(name = "创建人姓名")
    private String createStaffName;
    
    @Schema(name = "整改经办人主键")
    private BigDecimal handlerId;
    
    @Schema(name = "整改经办人姓名")
    private String handlerName;
    
    @Schema(name = "附件列表")
    @IgnoreSwaggerParameter
    private List<TblAttachment> attList = new ArrayList<TblAttachment>(0);
    
    @Schema(name = "整改清单")
    @IgnoreSwaggerParameter
    private List<TblRectificationIssuesVo> issuesList = new ArrayList<TblRectificationIssuesVo>(0);
    
    @Schema(name = "筛选条件  截止日期开始时间")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
  private Date deadlineStart;
    
    @Schema(name = "筛选条件 截止日期结束时间")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
  private Date deadlineEnd;
    
    @Schema(name = "筛选条件 创建日期开始时间")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
  private Date createTimeStart;
    
    @Schema(name = "总分派记录数")
  private Integer totalRecord;
    
    @Schema(name = "已分派记录数")
    private Integer allocatedRecord;
    
    @Schema(name = "未分派记录数")
    private Integer unassignedRecord;
    
    @Schema(name = "整改方案查询类型 1-整改方案列表查询 ，2-整改分派列表查询 ，3-整改评价列表查询 , 4-整改报告查询整改方案列表")
    private Integer selectType;
    
    @Schema(name = "多个方案状态查询，用,拼接 示例：7,8,9")
    private String statusStrs;
    
    
    public String getPlanTypeStr(Integer planType) {
  	  String result = "";
  	  switch (planType) {
			case 1:
				result = "审计";
				break;
			case 2:
				result = "风险";
				break;
			case 3:
				result = "外部";
				break;
			default:
				break;
			}
  	  return result;
    }
    
    public String getStatuseStr(Integer status) {
  	  String result = "";
  	  switch (status) {
			case 1:
				result = "审批中";
				break;
			case 2:
				result = "已退回";
				break;
			case 3:
				result = "已撤销";
				break;
			case 6:
				result = "未下发";
				break;
			case 7:
				result = "未分派";
				break;
			case 8:
				result = "未启动";
				break;
			case 9:
				result = "开始整改";
				break;
			case 10:
				result = "整改完成";
				break;
			case 11:
				result = "关闭";
				break;
			case 12:
				result = "到期未整改";
				break;
			default:
				result = "未审批";
				break;
			}
  	  return result;
    }
    
    @Schema(name = "审计项目名称")
    private String sjname;
    @Schema(name = "内控项目名称")
    private String nkname;
    @Schema(name = "外部项目名称")
    private String wbname;
    
    @Schema(name = "整改方案百度编辑内用")
    private String zgcont;
    
    @Schema(name = "是否使用密级 0 不使用；1 使用")
    private Integer useSecrect;
    
    @Schema(name = "创建人-密级用")
    private BigDecimal secrectStaff;
    
    @Schema(name = "当前用户所能查看密级数据的范围主键")
    private String secrectScopeIds;
    
    @Schema(name = "密级主键")
    private BigDecimal secrectLevelId;
    
    @Schema(name = "知悉范围 多个逗号分隔")
    private String staffScopeIds;
    
    @Schema(name = "知悉访问人员姓名 多个逗号分隔")
    private String staffScopeNames;
    
    
    @Schema(name = "整改方式:按问题整改或按方案整改")
    private String zgfs;
      
}
