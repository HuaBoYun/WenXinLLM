package com.hbfk.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 工作移交记录表
 * </p>
 *
 * @author lhp
 * @since 2025-01-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "用户信息实体TblTransferWork")
public class TblTransferWorkUtils implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private BigDecimal transferid;

    @Schema(description = "创建人")
    private BigDecimal createstaffid;

    @Schema(description = "创建人姓名")
    private String createstaffname;

    @Schema(description = "单据隶属部门")
    private BigDecimal linkdeptid;

    @Schema(description = "单据隶属公司")
    private BigDecimal linkorgid;

    @Schema(description = "移交人主键")
    private BigDecimal transferstaffid;

    @Schema(description = "移交人姓名")
    private String transefrstaffname;

    @Schema(description = "对接人主键")
    private BigDecimal dockstaffid;

    @Schema(description = "对接人姓名")
    private String dockstaffname;

    @Schema(description = "移交时间")
    private Date transfertime;

    @Schema(description = "移交失效时间")
    private Date transferlosetime;

    @Schema(description = "移交对接生效审批状态")
    private Integer tranenablestatus;

    @Schema(description = "移交对接失效审批状态")
    private Integer trandeprecatedstatus;

    @Schema(description = "对接状态，1-启用，2-弃用 ，默认0-未生效")
    private Integer transtatus;

    @Schema(description = "工作移交原因")
    private String tranenablereason;

    private String trandeprecatedreason;

    @Schema(description = "移交部门数据")
    private String tranorgidstrs;


}
