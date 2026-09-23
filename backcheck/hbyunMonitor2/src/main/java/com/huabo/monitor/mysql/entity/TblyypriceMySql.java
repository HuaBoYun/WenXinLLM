package com.huabo.monitor.mysql.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Transient;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_YY_PRICE")
@Schema(name="TblYyUserQueryMySql对象")
public class TblyypriceMySql implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("PRICEID")
    private Integer id;
    @TableField("INTERFACENAME")
    private String interfacename;//接口名称
    @TableField("ACCOUNTRULE")
    private String accountrule;//计费规则
    @TableField("PRICE")
    private Double price;//单价
    @TableField("HBPRICE")
    private Double hbprice;//收款单价
    @TableField("ANNUALPRICE")
    private String annualprice;//包年价格

    @Transient
    private TblOrganizationMySql compay;


}
