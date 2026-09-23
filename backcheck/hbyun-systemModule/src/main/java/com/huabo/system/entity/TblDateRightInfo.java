package com.huabo.system.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Transient;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 *
 * @author huabo
 * @since 2021-10-20
 */
@Data
@TableName("TBL_SYSTEM_DATA_RIGHT")
@Schema(name="数据权限表", description="")
public class TblDateRightInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	
	@TableField("DEPTIDSTRS")
    @Schema(name="部门主键")
    private String deptIdStrs;
	
	@TableField("ROLEID")
    @Schema(name="角色主键")
    private BigDecimal roleId;
	
	@TableField("ORGID")
    @Schema(name="公司主键")
    private BigDecimal orgId;
	
	@Transient
    @TableField(exist = false)
    private String orgname;
    
    @TableField(exist = false)
    private List<TblOrganization> deptList = new ArrayList<TblOrganization>(0) ;
}
