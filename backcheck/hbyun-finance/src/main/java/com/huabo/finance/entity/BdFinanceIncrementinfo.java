package com.huabo.finance.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

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
 * 增量采集编辑表
 * </p>
 *
 * @author L
 * @since 2025-03-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("BD_FINANCE_INCREMENTINFO")
@Schema(name="BdFinanceIncrementinfo对象", description="增量采集编辑表")
public class BdFinanceIncrementinfo implements Serializable {

    private static final long serialVersionUID = 1L;

      @Schema(name = "主键")
        @TableId("INCREMENTID")
      private String incrementid;

      @Schema(name = "所属方案")
      @TableField("PLANID")
    private String planid;

      @Schema(name = "所属配置sql")
      @TableField("INITSQLID")
    private String initsqlid;

      @Schema(name = "最后一次采集标识")
      @TableField("LASTINDEX")
    private String lastindex;

      @Schema(name = "创建时间")
      @TableField("CREATETIME")
      @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createtime;

}
