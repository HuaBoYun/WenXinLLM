package com.huabo.fxgl.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
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
@KeySequence(value = "SEQ_RISK_CTR", dbType = DbType.ORACLE)
@TableName("TBL_AUTONO_INFO")
public class AutonoInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    //编号的唯一标识符
    @TableId(type = IdType.INPUT)
    private BigDecimal noid;

    //编号的名称
    private String noname;

    //编号前缀
    private String nodefaultcode;

    //父编号ID
    private BigDecimal parentid;

    //起始编号
    private String nodefaultsepartor;

    private BigDecimal nodefaultnumber;

    //版本UID
    private Long nodefaultsuffix;


}
