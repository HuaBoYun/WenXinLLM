package com.huabo.system.entity;

import java.io.Serializable;
import java.math.BigDecimal;

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
    @TableName("TBL_ASSESS_MARK")
@Schema(name="TblAssessMark对象")
public class TblAssessMark implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("SCORE")
    private BigDecimal score;

    @TableField("MEMO")
    private String memo;

    @TableField("SUITABLE")
    private String suitable;

    @TableField("ASSELEID")
    private BigDecimal asseleid;

    @TableField("ASSMARKID")
    private BigDecimal assmarkid;

    @TableField("STAFFID")
    private BigDecimal staffid;

    @TableField("STATE")
    private String state;

    @TableField("ASSID")
    private Integer assid;

    @TableField("ASSORGID")
    private BigDecimal assorgid;

    @TableField("ASSESSTARGETID")
    private BigDecimal assesstargetid;


}
