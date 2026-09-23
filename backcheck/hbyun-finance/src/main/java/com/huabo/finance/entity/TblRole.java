package com.huabo.finance.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
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
 * @author L
 * @since 2025-04-11
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("TBL_ROLE")
@Schema(name="TblRole对象", description="角色信息表")
public class TblRole implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId("RID")
      private String rid;

    @TableField("RNAME")
    private String rname;

    @TableField("RDESC")
    private String rdesc;

    @TableField("RSTATUS")
    private String rstatus;

    @TableField("COMPANYID")
    private String companyid;

    @TableField("PKYMROLEID")
    private String pkymroleid;


}
