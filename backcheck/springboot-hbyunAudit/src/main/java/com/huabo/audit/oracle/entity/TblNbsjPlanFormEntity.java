package com.huabo.audit.oracle.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@TableName("TBL_REPORTTEMPLE")
@Data
@Schema(name="实体类")
@Accessors(chain = true)
public class TblNbsjPlanFormEntity {
	public final int PFSTATE1 = 1;//已发起审批
    public final int PFSTATE2 = 2;//需调整
    public final int PFSTATE3 = 3;//已通过
    public final int PFSTATE4 = 4;//已终止

    @TableId(value = "pfId", type= IdType.INPUT)
	@Schema
	private BigDecimal pfId;
	
	@TableField(value = "pfYear")
	@Schema
	private int pfYear;
	
	@TableField(value = "pfName")
	@Schema
	private String pfName;
	
	@TableField(value = "pfCost")
	@Schema
	private Double pfCost;
	
	@TableField(value = "pfStarDate")
	@Schema
	private Date pfStarDate;
	
	@TableField(value = "pfEndDate")
	@Schema
	private Date pfEndDate;
	
    @TableField(value = "pfBusiness")
	@Schema
	private String pfBusiness;
    
    @TableField(value = "pfCompanyId")
	@Schema
	private String pfCompanyId;
    
    @TableField(value = "pfRange")
	@Schema
	private String pfRange;
    
    @TableField(value = "pfDesc")
	@Schema
	private String pfDesc;
	
    @TableField(value = "pfStaffid")
	@Schema
	private BigDecimal pfStaffid;
    
    @TableField(value = "pfOption")
	@Schema
	private String pfOption;
    
    @TableField(value = "pfOrganizationDate")
	@Schema
	private Date pfOrganizationDate;
    
    @TableField(value = "pfCreateDate")
	@Schema
	private Date pfCreateDate;
    
    @TableField(value = "pfState")
	@Schema
	private int pfState;
    
    @TableField(value = "pfPlanId")
	@Schema
	private String pfPlanId;

	@TableField(exist = false)
	@Schema
	private Set<TblAttachment> tblAttachments;
    
}
