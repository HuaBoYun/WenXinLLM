package com.huabo.fxgl.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 
 * </p>
 *
 * @author LiYe
 * @since 2022-08-10
 */
@KeySequence(value = "SEQ_RISK_CTR", dbType = DbType.ORACLE)
@Data
@TableName("TBL_PROCESS_SETTING")
public class ProcessSetting implements Serializable {
    public  final static String OFF="OFF";//关闭
    public final static String ON="ON";//开启
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.INPUT)
    private BigDecimal settingid;

    private String status;

    private String module;

    private String remark;

    private BigDecimal orgid;

    private BigDecimal companyid;
}
