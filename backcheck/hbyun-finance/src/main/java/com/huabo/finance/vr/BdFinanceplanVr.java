package com.huabo.finance.vr;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.finance.entity.BdFinancedateRecord;

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
@Schema(name="BdFinanceplan对象", description="公司采集配置方案返回参数")
public class BdFinanceplanVr implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "主键")
    private String fid;

    @Schema(name = "方案名称")
    private String fname;

    @Schema(name = "数据源主键")
    private String dbconfigid;

    @Schema(name = "财务版本信息主键")
    private String fversionid;

    @Schema(name = "配置公司主键")
    private BigDecimal financeorgid;
    
    @Schema(name = "采集方案方式  1-全量 ，2-增量")
    private Integer financetype;

    @Schema(name = "所属公司")
    private BigDecimal linkorgid;

    @Schema(name = "所属部门")
    private BigDecimal linkdetpid;

    @Schema(name = "创建人")
    private BigDecimal creator;

    @Schema(name = "修改人")
    private BigDecimal modifier;

    @Schema(name = "创建时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date creationtime;
    
    @Schema(name = "采集范围 1-财务  2-业务  3-全部")
    private Integer financeRange;

    @Schema(name = "修改时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date modifiedtime;

    @Schema(name = "数据源名称")
    private String dbconfigname;
    
    @Schema(name = "财务版本信息名称")
    private String fversionname;
    
    @Schema(name = "配置公司名称")
    private String financeorgname;
    
    @Schema(name = "创建人姓名")
    private String creatorname;
    
    @Schema(name = "采集方案状态  0-未启用 1-启用 2-弃用")
    private Integer fstatus;
    
    
    private BdFinancedateRecord record;
    
    @Schema(name = "数据源列表")
    private List<BdFinancePlanDataConfigVr> dataConfigList;
    
}
