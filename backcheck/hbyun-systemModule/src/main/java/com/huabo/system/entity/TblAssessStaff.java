package com.huabo.system.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author huabo
 * @since 2022-04-29
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("TBL_ASSESS_STAFF")
@Schema(name="TblAssessStaff对象")
public class TblAssessStaff implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("ASSSTAFFID")
    private BigDecimal assstaffid;

    @TableField("ASSWEIGHT")
    private BigDecimal assweight;

    @TableField("MEMO")
    private String memo;

    @TableField("STAFFID")
    private BigDecimal staffid;

    @TableField("SCORE")
    private BigDecimal score;

    @TableField("REASON")
    private String reason;

    @TableField("ASSDATETIME")
    private LocalDateTime assdatetime;

    @TableField("ASSMARKID")
    private BigDecimal assmarkid;

    @TableField("STATUS")
    private BigDecimal status;

    @TableField("ORGID")
    private BigDecimal orgid;

    @TableField("ATTID")
    private BigDecimal attid;

    @TableField("EXAMINATION")
    private String examination;

    @TableField("SHEETID")
    private BigDecimal sheetid;


}
