package com.huabo.contract.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author huabo
 * @since 2022-03-22
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("TBL_LEGAL_ARBITRATION_ATT")
@Schema(name="TblLegalArbitrationAtt对象")
public class TblLegalArbitrationAtt implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("ATTID")
    private BigDecimal attid;

    @TableField("ARBITRAID")
    private BigDecimal arbitraid;


}
