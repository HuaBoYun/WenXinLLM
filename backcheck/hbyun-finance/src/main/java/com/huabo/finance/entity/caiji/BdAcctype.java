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
 * 科目类型
 * </p>
 *
 * @author L
 * @since 2025-03-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("BD_ACCTYPE")
@Schema(name="BdAcctype对象", description="科目类型")
public class BdAcctype implements Serializable {

    private static final long serialVersionUID = 1L;

      @Schema(name = "主键")
      @TableField("PK_ACCTYPE")
    private String pkAcctype;

      @Schema(name = "科目类型编码")
      @TableField("CCODE")
    private String ccode;

      @Schema(name = "科目类型名称")
      @TableField("NAME")
    private String name;

      @Schema(name = "余额方向0=借;1=贷;")
      @TableField("BALANORIENT")
    private String balanorient;

      @Schema(name = "平衡公式位置0=左;1=右;")
      @TableField("BALANPOSITION")
    private String balanposition;

      @Schema(name = "科目类型性质 	0=普通;1=损益;2=政府预算收支;3=资产;4=负债;5=权益;6=成本;7=共同")
      @TableField("TYPE")
    private String type;

      @Schema(name = "数据来源 0=本级产生;1=上级下发;2=下级上报;3=本级产生已上报下发;-1=系统预置;-2=数据采集")
      @TableField("DATAORIGINFLAG")
    private String dataoriginflag;

      @Schema(name = "序号")
      @TableField("CODE")
    private String code;

      @Schema(name = "政府预算")
      @TableField("BALANGROUP")
    private String balangroup;

      @Schema(name = "所属采集方案")
      @TableField("FPLANID")
    private String fplanid;


}
