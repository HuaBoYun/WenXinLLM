package com.huabo.fxgl.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 
 * </p>
 *
 * @author LiYe
 * @since 2022-08-01
 */
@Data
@ToString
@TableName("TBL_RISKEVENT_FLOW")
public class RiskeventFlow implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigDecimal riseveid;

    private BigDecimal flowid;

}
