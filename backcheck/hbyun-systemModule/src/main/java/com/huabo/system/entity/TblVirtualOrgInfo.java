package com.huabo.system.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 虚拟组织信息
 * </p>
 *
 * @author lhp
 * @since 2025-09-03
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("TBL_VIRTUAL_ORG_INFO")
@Schema(name="TblVirtualOrgInfo对象", description="虚拟组织信息")
public class TblVirtualOrgInfo implements Serializable {

    private static final long serialVersionUID = 1L;

      @Schema(name="主键")
        @TableId("FID")
      private String fid;

      @Schema(name="虚拟组织名称")
      @TableField("VIRTUALNAME")
    private String virtualname;

      @Schema(name="组织主键")
      @TableField("ORGID")
    private BigDecimal orgid;

      @Schema(name="排序")
      @TableField("SORT")
    private Integer sort;

      @Schema(name="创建人")
      @TableField("CREATOR")
    private BigDecimal creator;

      @Schema(name="修改人")
      @TableField("MODIFIER")
    private BigDecimal modifier;

      @Schema(name="创建时间")
      @TableField("CREATIONTIME")
    private Date creationtime;

      @Schema(name="修改时间")
      @TableField("MODIFIEDTIME")
    private Date modifiedtime;

      @Schema(name="删除标志 -1已删除，其他正常")
      @TableField("DELETEFLAG")
    private Integer deleteflag;
      
      
      @TableField(exist = false)
      private TblOrganization org;
      
      


}
