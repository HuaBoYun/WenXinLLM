package com.huabo.fxgl.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author LiYe
 * @since 2022-08-05
 */
@Data
@TableName("TBL_BUG_CRITERION")
public class BugCriterion implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId
    private BigDecimal bugid;

    private BigDecimal bugcriid;
}
