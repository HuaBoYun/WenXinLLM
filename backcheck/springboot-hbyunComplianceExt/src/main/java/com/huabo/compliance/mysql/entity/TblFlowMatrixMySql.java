package com.huabo.compliance.mysql.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

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
@Schema(name="TblFlowMatrixMySql对象")
public class TblFlowMatrixMySql implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("FLOWID")
    private BigDecimal flowid;

    @TableField("CONMATID")
    private BigDecimal conmatid;


}
