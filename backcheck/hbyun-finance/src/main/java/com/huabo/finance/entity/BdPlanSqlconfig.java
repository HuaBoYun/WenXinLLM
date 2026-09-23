package com.huabo.finance.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.sql.Clob;
import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 财务采方案配置sql语句
 * </p>
 *
 * @author L
 * @since 2025-03-12
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("BD_PLAN_SQLCONFIG")
@Schema(name="BdPlanSqlconfig对象", description="财务采方案配置sql语句")
public class BdPlanSqlconfig implements Serializable {

    private static final long serialVersionUID = 1L;

      @Schema(name = "主键")
        @TableId("FID")
      private String fid;

      @Schema(name = "sql标题")
      @TableField("FNAME")
    private String fname;

      @Schema(name = "sql内容")
      @TableField("FSQL")
    private String fsql;

      @Schema(name = "关联方案")
      @TableField("FPLANID")
    private String fplanid;
      
      @Schema(name = "特定where条件，不创建在原生sql")
      @TableField("SPECIFICITYCOL")
    private String specificityCol;

      @Schema(name = "初始化sql信息")
      @TableField("FINITSQLID")
    private String finitsqlid;

      @Schema(name = "所属公司")
      @TableField("LINKORGID")
    private BigDecimal linkorgid;

      @Schema(name = "所属部门")
      @TableField("LINKDETPID")
    private BigDecimal linkdetpid;

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
      @TableField("MODIFIEDTIME")
      @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date modifiedtime;


      @Schema(name = "增量标识列")
      @TableField("INCREMENTCOL")
    private String incrementcol;
}
