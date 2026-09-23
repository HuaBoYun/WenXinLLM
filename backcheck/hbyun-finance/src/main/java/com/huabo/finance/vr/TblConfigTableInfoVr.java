package com.huabo.finance.vr;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 辅助账余额
 * </p>
 *
 * @author L
 * @since 2025-04-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(name="TblConfigTableInfo", description="业务数据表配置信息")
public class TblConfigTableInfoVr implements Serializable {

	private static final long serialVersionUID = 1L;

    @Schema(name = "主键")
    private String fid;

    @Schema(name = "表注释")
    private String fname;

    @Schema(name = "目标端数据库表名")
    private String oursTableName;

    @Schema(name = "源端数据库表名")
    private String outsTableName;

    @Schema(name = "所属方案")
    private String planId;

    @Schema(name = "方案名称")
    private String planName;
    
    @Schema(name = "数据源主键")
    private String dataConfig;
    
    @Schema(name = "数据源名称")
    private String datatext; 
    
    @Schema(name = "采集方式类型 1-仅采集，2-仅导入，3-采集和导入")
    private Integer financeType;
    
    @Schema(name = "表单状态 0-草稿  1-已启用  2-已弃用 3-已发布")
    private Integer fstatus;
    
    @Schema(name = "创建时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Schema(name = "修改时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date modifyTime;

    @Schema(name = "创建人")
    private BigDecimal createUserId;

    @Schema(name = "修改人")
    private BigDecimal modifyUserId;

    @Schema(name = "所属公司")
    private BigDecimal linkOrgId;

    @Schema(name = "所属部门")
    private BigDecimal linkdeptid;

    @Schema(name = "采集sql主键")
    private String sqlText;
    
    private BdFinancedateRecordVr brv;
    
}
