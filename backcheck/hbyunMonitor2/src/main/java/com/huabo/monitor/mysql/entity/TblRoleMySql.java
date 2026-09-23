package com.huabo.monitor.mysql.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 角色表管理
 * rid:主键ID,自动增长；
 * rname:角色名称；
 * rdesc:角色描述；
 * rstatus:角色状态，是否启用
 * </p>
 *
 * @author huabo
 * @since 2021-10-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_ROLE")
@Schema(name="TblRoleMySql对象", description="角色表管理 rid:主键ID,自动增长；rname:角色名称；rdesc:角色描述；rstatus:角色状态，是否启用")
public class TblRoleMySql implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String DEL_YES = "1";
    public static final String DEL_NO = "0";


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select HIBERNATE_SEQUENCE.nextval from dual")
    @TableId("RID")
    private BigDecimal rid;

    @TableField("RNAME")
    @Schema(name="角色名称")
    private String rname;

    @TableField("RDESC")
    private String rdesc;

    @TableField("RSTATUS")
    private String rstatus;

    @TableField("COMPANYID")
    private BigDecimal companyid;

    @TableField("PKYMROLEID")
    private String pkYmRoleId;
    
    @Transient
    private String pkYmOrgId;
    
    @Transient
    private String orgName;
    
    // private TblRole tblRole;
}
