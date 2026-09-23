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
 * @since 2022-05-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_FLOW_MATRIX")
@Schema(name="TblFlowMatrix对象", description="")
public class TblFlowMatrix implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("FLOWID")
    private BigDecimal flowid;

    @TableField("CONMATID")
    private BigDecimal conmatid;


}
