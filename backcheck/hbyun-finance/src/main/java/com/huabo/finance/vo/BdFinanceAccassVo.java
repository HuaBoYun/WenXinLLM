package com.huabo.finance.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 会计辅助信息
 * </p>
 *
 * @author L
 * @since 2025-04-20
 */
@Data
  @EqualsAndHashCode(callSuper = false)
@Schema(name="BdFinanceAccass对象", description="会计辅助信息")
public class BdFinanceAccassVo extends BaseVo implements Serializable {

    private static final long serialVersionUID = 1L;

      @Schema(name = "辅助类型")
    private String asstype;

      @Schema(name = "辅助名称")
    private String assname;

      @Schema(name = "描述")
    private String assdes;

      @Schema(name = "辅助核算项主键 --必传")
    private String pkAccassitem;

      @Schema(name = "描述")
    private String assdd;

      @Schema(name = "数据来源 0=本级产生;1=上级下发;2=下级上报;3=本级产生已上报下发;-1=系统预置;")
    private String dataoriginflag;

      @Schema(name = "所属采集方案")
    private String fplanid;

}
