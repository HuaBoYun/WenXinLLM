package com.huabo.finance.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;

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
 * 公司采集配置方案信息表
 * </p>
 *
 * @author L
 * @since 2025-03-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("BD_FINANCEPLAN")
@Schema(name="BdFinanceplan对象", description="公司采集配置方案信息表")
public class BdFinanceplan implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "主键")
    @TableId("FID")
    private String fid;

    @Schema(name = "方案名称")
    @TableField("FNAME")
    private String fname;

    @Schema(name = "数据源主键")
    @TableField("DBCONFIGID")
    private String dbconfigid;

    @Schema(name = "财务版本信息主键")
    @TableField("FVERSIONID")
    private String fversionid;

    @Schema(name = "配置公司主键")
    @TableField("FINANCEORGID")
    private BigDecimal financeorgid;
    
    @Schema(name = "采集方案方式  1-全量 ，2-增量")
    @TableField("FINANCETYPE")
    private Integer financetype;
    
    
    @Schema(name = "采集范围 1-财务  2-业务  3-全部")
    @TableField("FINANCERANGE")
    private Integer financeRange;
    
    @Schema(name = "采集方案状态  0-未启用 1-启用 2-弃用")
    @TableField("FSTATUS")
    private Integer fstatus;

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

    
    @Schema(name = "数据源配置信息集合")
    @TableField(exist = false)
    private List<BdFinancePlanDataConfig> dataConfigList;
    
    
    
    

}
