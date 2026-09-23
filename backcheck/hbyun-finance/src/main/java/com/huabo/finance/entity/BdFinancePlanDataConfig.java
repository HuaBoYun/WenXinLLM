package com.huabo.finance.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 财务数据采集配置信息表
 * </p>
 *
 * @author L
 * @since 2025-03-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("BD_FINANCEPLAN_DATACONFIG")
@Schema(name="BdFinancePlanDataConfig对象", description="采集方案数据源配置关联表")
public class BdFinancePlanDataConfig implements Serializable {

    private static final long serialVersionUID = 1L;

      @Schema(name = "主键")
        @TableId("FID")
      private String fid;

      @Schema(name = "方案主键")
      @TableField("PLANID")
    private String planid;

      @Schema(name = "数据源主键")
      @TableField("DATACONFIG")
    private String dataconfig;

      @Schema(name = "数据源类型 1-财务数据源 2-业务数据源")
      @TableField("FTYPE")
    private Integer ftype;

      @Schema(name = "名称")
      @TableField("FNAME")
    private String fname;

}
