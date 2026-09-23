package com.huabo.finance.entity.caiji;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 辅助核算
 * </p>
 *
 * @author L
 * @since 2025-03-28
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("BD_ACCASS")
@Schema(name="BdAccass对象", description="辅助核算")
public class BdAccass implements Serializable {

    private static final long serialVersionUID = 1L;

      @Schema(name = "主键")
      @TableField("PK_ACCASS")
    private String pkAccass;

      @Schema(name = "辅助项目")
      @TableField("PK_ENTITY")
    private String pkEntity;

      @Schema(name = "创建科目表")
      @TableField("PK_ACCCHART")
    private String pkAccchart;

      @Schema(name = "序号")
      @TableField("ID")
    private String id;

      @Schema(name = "允许为空")
      @TableField("ISEMPTY")
    private String isempty;

      @Schema(name = "允许录入非末级")
      @TableField("ISNONLEAFUSED")
    private String isnonleafused;

      @Schema(name = "汇总打印")
      @TableField("ISSUMPRINT")
    private String issumprint;

      @Schema(name = "余额方向控制")
      @TableField("ISBALANCECONTROL")
    private String isbalancecontrol;

      @Schema(name = "被引用数量")
      @TableField("NUM")
    private String num;

      @Schema(name = "数据来源0=本级产生;1=上级下发;2=下级上报;3=本级产生已上报下发;-1=系统预置;")
      @TableField("DATAORIGINFLAG")
    private String dataoriginflag;

      @Schema(name = "所属采集方案")
      @TableField("FPLANID")
    private String fplanid;

      @Schema(name = "覆盖所属关联表主键")
      @TableField("PK_COVERACCASOA")
    private String pkCoveraccasoa;


}
