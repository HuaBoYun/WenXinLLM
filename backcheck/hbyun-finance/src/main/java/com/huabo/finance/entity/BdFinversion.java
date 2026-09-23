package com.huabo.finance.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.finance.config.IgnoreSwaggerParameter;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 财务数据版本
 * </p>
 *
 * @author L
 * @since 2025-03-10
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("BD_FINVERSION")
@Schema(name="BdFinversion对象", description="财务数据版本")
public class BdFinversion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "主键")
    @TableId("FID")
    private String fid;

    @Schema(name = "文本")
    @TableField("HANDTEXT")
    private String handtext;

    @Schema(name = "父级主键")
    @TableField("PID")
    private String pid;

    @Schema(name = "排序")
    @TableField("SORT")
    private BigDecimal sort;

    @Schema(name = "创建人")
    @TableField("CREATOR")
    private BigDecimal creator;

    @Schema(name = "修改人")
    @TableField("MODIFIER")
    private BigDecimal modifier;

    @Schema(name = "创建时间")
    @TableField("CREATIONTIME")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date creationtime;

    @Schema(name = "修改时间")
    @TableField(value = "MODIFIEDTIME")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date modifiedtime;

    @Schema(name = "分布式 	0=本级产生;1=上级下发;2=下级上报;3=本级产生已上报下发;-1=系统预置;")
    @TableField("DATAORIGINFLAG")
    private BigDecimal dataoriginflag;
      
      
    @IgnoreSwaggerParameter
    @Schema(name = "子集")
    @TableField(exist = false)
    private List<BdFinversion> childrenList = null;

}
