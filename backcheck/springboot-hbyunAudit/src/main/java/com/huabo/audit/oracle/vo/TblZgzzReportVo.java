package com.huabo.audit.oracle.vo;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.huabo.audit.config.IgnoreSwaggerParameter;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.util.BaseVo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author LHP
 * @since 2024-02-21
 */
@Data
@Schema(name="TblZgzzReport对象", description="整改报告")
public class TblZgzzReportVo extends BaseVo implements Serializable {

	/**
	 *  TBL_ZGZZ_REPORTPLAN  --整改报告关联整改方案关系表
	 *  TBL_ZGZZ_REPORTISSUES  --整改报告关联整改落实关系表
	 *  TBL_ZGZZREPORT_ATT   --整改报告关联附件关系表
	 */
	
    private static final long serialVersionUID = 1L;

    @Schema(name = "整改报告主键")
    private String reportid;

    @Schema(name = "整改报告编号")
    private String reportcode;

    @Schema(name = "整改报告名称")
    private String reportname;

    @Schema(name = "报告类型 1-整改方案报告，2整改落实报告")
    private Integer reporttype;

    @Schema(name = "创建人")
    private BigDecimal createstaff;
    
    @Schema(name = "创建人姓名")
    private String createStaffName;

    @Schema(name = "报告编制部门")
    private BigDecimal linkdept;
    
    @Schema(name = "报告编制部门名称")
    private String linkDeptName;

    @Schema(name = "创建时间")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date createdate;

    @Schema(name = "所属公司")
    private BigDecimal linkorg;
    
    @Schema(name = "所属公司名称")
    private String linkOrgName;
    

    @Schema(name = "报告内容")
    private String reportcontect;
    
    @Schema(name = "审批状态 0-未评价 1-审批中 2-已退回 3-已撤销 6-已完成")
    private Integer status;
    
    @Schema(name = "关联的所有整改方案名称")
    private String planStrs;
    
    @Schema(name = "关联的所有整改方案主键")
    private String planIdStrs;
    
    @Schema(name = "所有整改方案关联的整改清单")
    private List<TblZgzzIssuesilistVo> issuesList = new ArrayList<TblZgzzIssuesilistVo>(0);
    
    @Schema(name = "附件列表")
    @Transient
    @IgnoreSwaggerParameter
  private List<TblAttachment> attList = new ArrayList<TblAttachment>(0);
    
    @Schema(name = "筛选条件创建时间开始时间")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private Date startDate;
    @Schema(name = "筛选条件创建时间结束时间")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private Date endDate;
    
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
    
    @Schema(name = "查询条件-所属单位")
    private BigDecimal orgid;

}
