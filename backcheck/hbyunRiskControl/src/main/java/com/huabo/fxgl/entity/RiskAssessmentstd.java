package com.huabo.fxgl.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 风险评估标准
 * </p>
 *
 * @author LiYe
 * @since 2022-08-01
 */
@Data
@ToString
@TableName(value = "TBL_RISK_ASSESSMENTSTD", resultMap = "RM_RISK_ASSESSMENTSTD")
@KeySequence(value = "SEQ_RISK_CTR", dbType = DbType.ORACLE)
//风险评估模板
public class RiskAssessmentstd extends FlexibleFieldEntity  implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @Schema(name="主键ID")
    @TableId(value = "ASSSTDID",type = IdType.INPUT)
    private BigDecimal assstdid;

    /**
     * 编号
     */
	@Schema(name="编号")
    private String assnumber;

    /**
     * 名称
     */
	@Schema(name="名称")
    private String assname;

    /**
     * 描述
     */
	@Schema(name="描述")
    private String assdes;

    /**
     * 备注
     */
	@Schema(name="备注")
    private String memo;

    /**
     * 状态
     */
	@Schema(name="状态")
    private Integer assstatus;

	@Schema(name="公司ID")
    private BigDecimal companyid;

	@Schema(name="风险发生频率集合")
    @TableField(exist = false)
    private List<RiskPossibility> possibilities = new ArrayList<RiskPossibility>();

	@Schema(name="风险发生影响集合")
    @TableField(exist = false)
    private List<RiskInfludegree> riskInfludegrees = new ArrayList<RiskInfludegree>();

	@Schema
    @TableField(exist = false)
    private List<RiskLevelmapping> levelMappings = new ArrayList<RiskLevelmapping>();

	


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
  private BigDecimal linkdeptid;
    
    @Schema(name="创建人")
    @TableField("CREATESTAFFID")
    private BigDecimal createstaffid;
    
    @TableField("CREATETIME")
 	@Schema(name = "创建时间")
     private Date createtime;

	public RiskAssessmentstd() {
	}

	public RiskAssessmentstd(RiskAssessmentstd r) {
		super();
		this.assnumber = r.assnumber;
		this.assname = r.assname;
		this.assdes = r.assdes;
		this.memo = r.memo;
		this.assstatus = r.assstatus;
		this.companyid = r.companyid;
		this.secrectLevelId=r.secrectLevelId;
	}



}
