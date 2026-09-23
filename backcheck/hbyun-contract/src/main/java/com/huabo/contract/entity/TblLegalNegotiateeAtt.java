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
 * @since 2022-03-18
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("TBL_LEGAL_NEGOTIATEE_ATT")
@Schema(name="TblLegalNegotiateeAtt对象")
public class TblLegalNegotiateeAtt implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("ATTID")
    private BigDecimal attid;

    @TableField("NEGOTIAID")
    private BigDecimal negotiaid;


}
