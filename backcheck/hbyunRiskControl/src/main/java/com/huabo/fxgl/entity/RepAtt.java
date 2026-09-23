package com.huabo.fxgl.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 
 * </p>
 *
 * @author LiYe
 * @since 2022-08-18
 */
@Data
@TableName("TBL_REP_ATT")
@AllArgsConstructor
@NoArgsConstructor
public class RepAtt implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigDecimal reportid;

    private BigDecimal attid;

}
