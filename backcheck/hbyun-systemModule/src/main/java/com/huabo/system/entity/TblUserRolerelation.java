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
 * 用户角色关系表
 * </p>
 *
 * @author lhp
 * @since 2024-12-30
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("TBL_USER_ROLERELATION")
@Schema(name="TblUserRolerelation对象", description="用户角色关系表")
public class TblUserRolerelation implements Serializable {

    private static final long serialVersionUID = 1L;

      @Schema(name="主键")
        @TableId("RELAID")
      private BigDecimal relaid;

      @Schema(name="员工主键")
      @TableField("STAFFID")
    private BigDecimal staffid;

    @TableField("ROLEID")
    private BigDecimal roleid;

      @Schema(name="上一任员工主键")
      @TableField("PRESTAFFID")
    private BigDecimal prestaffid;

      @Schema(name="创建时间")
      @TableField("CREATETIME")
    private Date createtime;

      @Schema(name="取消授权时间")
      @TableField("ENDTIME")
    private Date endtime;

      @Schema(name="是否可以操作之前授权角色用户的数据，1-是，0否 默认1")
      @TableField("ISCANPRE")
    private Integer iscanpre;

      @Schema(name="启用弃用状态 1-启用 0-弃用 默认1")
      @TableField("STATUS")
    private Integer status;

      @Schema(name="创建人主键")
      @TableField("CREATESTAFFID")
    private BigDecimal createstaffid;

      @Schema(name="创建人姓名")
      @TableField("CREATESTAFFNAME")
    private String createstaffname;


}
