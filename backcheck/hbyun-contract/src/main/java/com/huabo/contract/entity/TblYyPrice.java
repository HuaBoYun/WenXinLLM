package com.huabo.contract.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
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
 * @since 2022-04-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_YY_PRICE")
@Schema(name="TblYyPrice对象")
public class TblYyPrice implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "PRICEID",type = IdType.INPUT)
    private BigDecimal priceid;

    @TableField("INTERFACENAME")
    private String interfacename;

    @TableField("ACCOUNTRULE")
    private String accountrule;

    @TableField("PRICE")
    private BigDecimal price;

    @TableField("ANNUALPRICE")
    private String annualprice;

    @TableField("COMPANYID")
    private BigDecimal companyid;

    @TableField("HBPRICE")
    private BigDecimal hbprice;


}
