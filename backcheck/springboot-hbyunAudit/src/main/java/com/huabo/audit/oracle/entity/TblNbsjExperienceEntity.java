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

@TableName("TBL_NBSJ_EXPERIENCE")
@Data
@Schema(name="实体类")
@Accessors(chain = true)
public class TblNbsjExperienceEntity {
	
	public static final Integer NORMAL = 1;  //启用
	public static final Integer UNNORMAL=-1; //禁用
/*	public static final Integer ZY_TYPE = 1;  // 审计指引
	public static final Integer MB_TYPE=0;   //审计模板
	public static final Integer COPY_TYPE=2;
	*/
	private static final long serialVersionUID = 1L;

	@TableId(value = "templeteid", type= IdType.INPUT)
    @Schema
    private BigDecimal templeteId;

    @TableField(value = "templetecode")
    @Schema
    private String templeteCode;
    
    @TableField(value = "templetename")
    @Schema
    private String templeteName;
    
    @TableField(value = "templetedesc")
    @Schema
    private String templeteDesc;

    @TableField(exist = false)
    @Schema
    private TblStaff staff;

    @TableField(exist = false)
    @Schema
    private TblStaff updateStaff;
    
    @TableField(value = "temptype")
    @Schema
    private Integer temptype;
    
    @TableField(value = "status")
    @Schema
    private Integer status;
    
    @TableField(value = "updatetime")
    @Schema
    private Date updateTime;
    
    @TableField(value = "orgId")
    @Schema
    private BigDecimal orgId;

    @TableField(exist = false)
    @Schema
    private Set<TblOrganization> organizations;

    @TableField(exist = false)
    @Schema
    private Set<TblNbsjProject> tblprojects;
    
}
