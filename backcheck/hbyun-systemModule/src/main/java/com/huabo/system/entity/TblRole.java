package com.huabo.system.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Transient;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 角色表管理
rid:主键ID,自动增长；
rname:角色名称；
rdesc:角色描述；
rstatus:角色状态，是否启用
 * </p>
 *
 * @author huabo
 * @since 2021-10-20
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("TBL_ROLE")
@Schema(name="TblRole对象", description="角色表管理 rid:主键ID,自动增长；rname:角色名称；rdesc:角色描述；rstatus:角色状态，是否启用")
public class TblRole implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String DEL_YES = "1";
    public static final String DEL_NO = "0";
    
    public static final String ADMINAME = "系统管理员";//超级管理员 授予业务中台系统中系统基础所有权限


    @TableId(value="RID",type = IdType.INPUT)
    @Schema(name="主键")
    private BigDecimal rid;

    @TableField("RNAME")
    @Schema(name="角色名称")
    private String rname;

    @TableField("RDESC")
    @Schema(name="角色描述")
    private String rdesc;

    @TableField("RSTATUS")
    @Schema(name="角色状态")
    private String rstatus;

    @TableField("COMPANYID")
    @Schema(name="隶属组织ID")
    private BigDecimal companyid;
    
    @TableField("PKYMROLEID")
    @Schema(name="关联业务中台ID")
    private String pkYmRoleId;
    
    @Transient
    @TableField(exist = false)
    @Schema(name="业务中台公司主键")
    private String pkYmOrgId;

    @Transient
    @TableField(exist = false)
    @Schema(name="隶属组织名称")
    private String orgName;
    
    
    @Transient
    @TableField(exist = false)
    @Schema(name="分配组织名称")
    private List<String> orgTreeName;
   
    
    @Transient
    @TableField(exist = false)
    @Schema(name="分配的部门主键逗号分隔")
    private String orgIdStrs;
    
    
    @Transient
    @TableField(exist = false)
    private String rlongName;
    
    @Transient
    @TableField(exist = false)
    private String deptIdStrs;
    @Transient
    @TableField(exist = false)
    private BigDecimal orgId;
    @Transient
    @TableField(exist = false)
    private BigDecimal roleId;
    
    @TableField(exist = false)
    private Integer rightCount;
    
    @Transient
    @TableField(exist = false)
    private List<TblOrganization> deptList = new ArrayList<TblOrganization>(0) ;
}
