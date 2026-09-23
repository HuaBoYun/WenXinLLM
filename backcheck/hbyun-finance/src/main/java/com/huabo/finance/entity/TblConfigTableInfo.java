package com.huabo.finance.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

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
 * 业务数据库表信息
 * </p>
 *
 * @author L
 * @since 2025-03-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_CONFIG_TABLEINFO")
@Schema(name="TblConfigTableInfo对象", description="业务数据库表信息")
public class TblConfigTableInfo implements Serializable {

    private static final long serialVersionUID = 1L;

      @Schema(name = "主键")
        @TableId("FID")
      private String fid;

      @Schema(name = "表名称")
      @TableField("FNAME")
    private String fname;

      @Schema(name = "目标端表名")
      @TableField("OURSTABLENAME")
    private String oursTableName;

      @Schema(name = "源端表名")
      @TableField("OUTSTABLENAME")
    private String outsTableName;

      @Schema(name = "所属方案")
      @TableField("PLANID")
    private String planId;
      
      @Schema(name = "数据源主键")
      @TableField("DATACONFIG")
    private String dataConfig; 
      

      @Schema(name = "创建时间")
      @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
      @TableField("CREATETIME")
    private Date createTime;

      @Schema(name = "修改时间")
      @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
      @TableField("MODIFYTIME")
    private Date modifyTime;

      @Schema(name = "创建人")
      @TableField("CREATEUSERID")
    private BigDecimal createUserId;

      @Schema(name = "修改人")
      @TableField("MODIFYUSERID")
    private BigDecimal modifyUserId;

      @Schema(name = "所属公司")
      @TableField("LINKORGID")
    private BigDecimal linkOrgId;

      @Schema(name = "所属部门")
      @TableField("LINKDEPTID")
    private BigDecimal linkdeptid;

      @Schema(name = "采集sql")
      @TableField("SQLTEXT")
    private String sqlText;
      
      @Schema(name = "表单状态 0-草稿  1-已启用  2-已弃用 3-已发布")
      @TableField("FSTATUS")
    private Integer fstatus;
      
      @Schema(name = "采集方式类型 1-仅采集，2-仅导入，3-采集和导入")
      @TableField("FINANCETYPE")
    private Integer financeType;
      
      @TableField(exist = false)
    private List<TblConfigColumnInfo> colList;
      
      
      @Schema(name = "采集信息")
      @TableField(exist = false)
 	 private BdFinancedateRecord fr;
      
      
}
