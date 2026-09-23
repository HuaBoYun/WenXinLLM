package com.huabo.fxgl.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Transient;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.huabo.fxgl.util.IgnoreSwaggerParameter;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

/**
 * <p>
 * 
 * </p>
 *
 * @author LiYe
 * @since 2022-08-01
 */
@Data
@ToString
@KeySequence(value = "SEQ_RISK_CTR", dbType = DbType.ORACLE)
@TableName("TBL_RISK")
@Schema(name="TBL_RISK", description="风险表")
public class Risk  extends FlexibleFieldEntity implements Serializable {

    private static final long serialVersionUID = 1L;


    public static final String YIBAN="1";

    public static final String ZHONGDA="2";
    /**
     *
     */
    @Schema(name="业务创建相关信息")
    @TableField(exist = false)
    @IgnoreSwaggerParameter
    private Flow flow;
    /**
     * 风险类别
     */
	@Schema(name="风险类别")
    @TableField(value = "riskcatid", property = "riskcategory.riskcatid", exist = false)
	@IgnoreSwaggerParameter
    private Riskcategory riskcategory;
    /**
     * ID
     */
    @TableId(type = IdType.INPUT)
	@Schema(name="主键ID")
    private BigDecimal riskid;

    /**
     * 风险编号
     */
    @Parameter(description="风险编号")
    @Schema(name="风险编号")
    private String risknumber;
    
    /**
     * 风险名称
     */
	@Schema(name="风险名称")
    @Parameter(description="风险名称")
    private String riskname;


    /**
     * 风险描述
     */
	@Schema(name="风险描述")
    private String riskdes;

    /**
     * 责任部门
     */
	@Schema(name="责任部门")
    @Parameter(description="责任部门")
    private String belongsto;

    /**
     * 备注
     */
	@Schema(name="备注")
    private String memo;

    /**
     * 风险分类ID
     */
    @Parameter(description="风险分类ID--二级分类id")
	@Schema(name="风险分类ID")
    private BigDecimal riskcatid;
    
    @Parameter(description="二级分类名称")
  	@Schema(name="风险分类ID")
    private String riskcatnametwo;

	@Schema
    private BigDecimal inriskdb;

    /**
     * 版本
     */
	@Schema(name="版本")
    private String version;

    /**
     * 公司ID
     */
	@Schema(name="公司ID归属单位")
    private String unit;

    /**
     * 相关部门
     */
	@Schema(name="相关部门")
    private String reorg;

	@Schema
    private String subsys;

    /**
     * 创建时间
     */
	@Schema(name="创建时间")
    private Date riskcreatedt;

    /**
     * 用户
     */
	@Schema(name="用户")
    private BigDecimal staffid;

	@Schema
    private String risklerabilitynumber;

	@Schema
    private String risklerabilityname;

	@Schema
    private String risklerabilitydes;

    
	@Schema
    private BigDecimal yduserid;

	@Schema(name="审计程序")
    private String riskprogram;

    @Schema(name="外部规定")
    private String riskexternal;

    @Schema(name="公司规定")
    private String riskcompany;

    @Schema(name="合规红线")
    private String riskcompliance;

    @Schema(name="风险领域")
    private String riskcatname;

    @Schema(name="风险领域Id")
    private BigDecimal riskcatidone;

	
	/**
     * 风险级别
     */
    @Parameter(description="风险来源 内部风险/外部风险")
    @Schema(name="风险来源")
    private String risklevel;

	@Schema
    @TableField(exist = false)
	@IgnoreSwaggerParameter
    private Staff staff;
	
	

	@Schema(name="风险等级")
    @TableField(exist = false)
    private String level;//风险等级

	@Schema(name="策略")
    @TableField(exist = false)
    private String copingPlot;//策略

	@Schema
    @TableField(exist = false)
    private Set Flows = new HashSet(0);

	@Schema(name="责任部门名称")
    @TableField(exist = false)
	@Transient
    private String zrbmName;    //责任部门名称

	@Schema(name="相关部门名称")
    @TableField(exist = false)
	@Transient
    private String xgbmName;    //相关部门名称

	@Schema(name="所属机构名称/回溯单位")
    @TableField(exist = false)
    private String ssjgName;    //所属机构名称

	@Schema(name="所属风险名称")
    @TableField(exist = false)
    private String ssfxName;    //所属风险名称

	@Schema(name="风险应对人员名称")
    @TableField(exist = false)
    private String ydusername;  //风险应对人员名称
	
	
	
	
	@Schema(name="审批状态")
    private String status;
	
	@Schema
	@TableField(exist = false)
    private String riskcopingid;
	
	@Schema(name="版本管理是否展示 1-展示 0-不展示")
	private Integer iscurrentversion;

	@Schema(name="关联-原风险ID")
	private BigDecimal riskextid;

	@Schema(name="关联-原风险名称")
	@TableField(exist = false)
	private String riskextname;
	
	@Schema(name="关联-风险类型名称")
	@TableField(exist = false)
	private String riskcatidname;
	
	@Schema(name="关联-公司名称")
	@TableField(exist = false)
	private String unitname;
	
	@Schema(name="关联-创建人名称")
	@TableField(exist = false)
	private String staffname;

	@Schema(name = "下拉-制/修订 1-制定 2-修订")
	private Integer revisiontype;

	@Schema(name="风险原因")
	private String riskcause;

	@Schema(name="合规义务")
	private String complianceobligation;
	
	
	@Schema(name="风险状态：0已经关闭  1 未关闭  2 开启  用于重大风险月度评估关闭风险")
    private BigDecimal riskstatus;
	
	
	 @Schema(name="关闭审批状态")
	 private BigDecimal closestatus;
	 
		
	 @Schema(name="业务名称")
	 @TableField(exist=false)
	 private String  businessname;
	 
	 @Schema(name="流程名称")
	 @TableField(exist=false)
	 private String  flowname;
	 
	 
	 @Schema(name="查询条件-开始时间")
	 @TableField(exist=false)
	 private String  startdate;
	 
	 @Schema(name="查询条件-结束时间")
	 @TableField(exist=false)
	 private String  enddate;
	 


	    //密级及查询条件
	    @Schema(name = "密级主键")
	    @TableField("SECRECTLEVELID")
	    private BigDecimal secrectLevelId;
	    
	    @Schema(name = "知悉范围id")
	    @TableField("STAFFSCOPEIDS")
	    private String staffScopeIds;
	    
	    @Schema(name = "知悉范围名称")
	    @TableField("STAFFSCOPENAMES")
	    private String staffScopeNames;
	 
	    @Schema(name = "所属部门")
	    @TableField("LINKDEPTID")
	    @Column(name = "LINKDEPTID")
	    private BigDecimal linkDeptId;
	    @Schema(name = "所属部门name")
	    @TableField(exist=false)
	    private String linkDeptName;
	    
		@Schema(name="公司责任领导")
	    @TableField("LEADERSHIP")
	    @Column(name = "LEADERSHIP")
	    private String leadership;
	 
		@Schema(name="配合单位或部门")
	    @TableField("COOPERATEORG")
	    @Column(name = "COOPERATEORG")
	    private String cooperateOrg;
		
		
		@Schema(name="公司责任领导NAME")
	    @TableField(exist=false)
	    private String leadershipName;
	 
	@Schema(name="配合单位或部门NAME")
	@TableField("COOPERATEORGNAME")
	private String cooperateOrgName;

	@Schema(name="四级风险")
	@Parameter(description="四级风险")
	@TableField("LEVELFOURRISK")
	private String levelFourRisk;

	@Schema(name="列表增加所属部门--查询当前所属部门的上级部门")
	@TableField(exist = false)
	private String superiorDepartment;

	@Schema(name="首页穿透年度条件")
	@TableField(exist = false)
	private String year;//

	@Schema(name="风险应对状态")
	@TableField(exist = false)
	private String copingStatus;//
	
	
	  @Schema(name = "查询条件中的风险等级")
	  @TableField(exist=false)
	  private String cxlevel;
	  
	  
	  @Schema(name = "左侧树条件部门钻取下级节点")
	  @TableField(exist=false)
	  private List<BigDecimal> deptIds;
	  
	  @Schema(name = "左侧树条件公司钻取下级节点")
	  @TableField(exist=false)
	  private List<BigDecimal> unitIds;
	  
	  
		@Schema(name="月度评估最新上报月份")
		@TableField(exist = false)
		private String reportmonth;//
		
		
		@Schema(name="列表责任单位")
		@TableField(exist = false)
		private String zrdw;//
		
		@Schema(name="列表责任部门")
		@TableField(exist = false)
		private String zrbm;//

	@Schema(name = "查询条件中的风险状态")
	@TableField(exist = false)
	private String pgStatus;

	@Schema(name="关联风险模型库")
	@TableField(value = "stepid")
	private String stepid;

	@Schema(name = "风险模型库关联数据源")
	@TableField(exist = false)
	private BigDecimal bookid;

	@Schema(name = "风险模型库sql")
	@TableField(exist = false)
	private String sql;

	@Schema(name = "风险模型库模型名称")
	@TableField(exist = false)
	private String steptitle;

	@Schema(name = "查询多个风险等级，首页热图")
	@TableField(exist = false)
	private List<String> cxlevelList;

	@Schema(name = "风险TOP10类型")
	@TableField("TOP10TYPE")
	@Column(name = "TOP10TYPE")
	private String top10type;

	@Schema(name = "风险TOP10ID")
	@TableField("TOP10ID")
	@Column(name = "TOP10ID")
	private String top10id;


	@Schema(name = "风险顺序")
	@TableField("RISKORDER")
	@Column(name = "RISKORDER")
	private String riskorder;


	@Schema(name = "风险等级")
	@TableField(exist = false)
	private String size;

	@Schema(name = "是否已评估")
	@TableField(exist = false)
	private String sfypg;

}