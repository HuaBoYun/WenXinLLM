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
    @TableName("BD_FINANCEDATE")
@Schema(name="BdFinancedate对象", description="财务数据采集配置信息表")
public class BdFinancedate implements Serializable {

    private static final long serialVersionUID = 1L;

      @Schema(name = "主键")
        @TableId("FID")
      private String fid;

      @Schema(name = "数据源名称")
      @TableField("FINTEXT")
    private String fintext;

      @Schema(name = "备注")
      @TableField("FINMEMO")
    private String finmemo;

      @Schema(name = "源数据库类型	Oracle数据库-Oracle，Mysql数据库-Mysql，达梦数据库-DM，sqlserver数据库-SqlServer，OceanBase数据库-OceanBase")
      @TableField("FINANCEDBTYPE")
    private String financedbtype;

      @Schema(name = "源数据库实例")
      @TableField("FINANCEDBEXPM")
    private String financedbexpm;

      @Schema(name = "数据库连接地址")
      @TableField("FINANCECONN")
    private String financeconn;

      @Schema(name = "用户名")
      @TableField("FINANCEUSER")
    private String financeuser;

      @Schema(name = "密码")
      @TableField("FINANCEPWD")
    private String financepwd;

      @Schema(name = "端口")
      @TableField("FINANCEPORT")
    private String financeport;

      @Schema(name = "状态 1-启用，0-未启用，2弃用")
      @TableField("STATUS")
    private Integer status;

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


}
