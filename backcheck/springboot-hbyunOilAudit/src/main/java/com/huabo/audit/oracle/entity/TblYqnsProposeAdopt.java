package com.huabo.audit.oracle.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @ Author: Striker dev@example.com
 * @ Date: 2023-09-07 15:58
 * @ TODO:
 **/

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_YQNS_PROPOSE_ADOPT")
@Schema(name="是否采纳主键", description="TBL_YQNS_PROPOSE_ADOPT")
public class TblYqnsProposeAdopt implements Serializable {

    private static final long serialVersionUID = 1L;

    
    /**
     * 问题整改主键
     */
    @Schema(name = "采纳主键")
    @TableId(value = "ADOPTID",type = IdType.INPUT)
    private String adoptId;
    
    @TableField(value = "ID")
    @Schema(name = "建议表主键ID")
    private BigDecimal id;
    
    /**
     * 问题整改主键
     */
    @Schema(name = "问题整改主键")
    @TableField(value = "WTZGID")
    private BigDecimal wtzgid;
    
    @Schema(name="是否采纳，1-是，0-否")
    @TableField("ISADOPT ")
    private Integer isAdopt;
    
    @Schema(name="直接经济成果类型")
    @TableField("ZJJJCGLX ")
    private String zjjjcglx;
    
    @Schema(name="直接经济成果金额（元）")
    @TableField("ZJJJCGJE ")
    private BigDecimal zjjjcgje;
    
    @Schema(name="其他经济成果类型")
    @TableField("QTJJCGLX ")
    private String qtjjcglx;
    
    @Schema(name="其他经济成果金额（元）")
    @TableField("QTJJCGJE ")
    private BigDecimal qtjjcgje;
    
    @Schema(name="不采纳原因")
    @TableField("NOTREASON ")
    private String notReason;

}
