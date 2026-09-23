package com.huabo.compliance.mysql.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Transient;

/**
 * TblYyQueryPrice entity. @author MyEclipse Persistence Tools
 * 用户查询记录 与 接口中间关系表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_YY_QUERY_PRICE")
@Schema(name="TblYyQueryPriceMySql对象")
public class TblYyQueryPriceMySql implements java.io.Serializable {


    private static final long serialVersionUID = 4429474155929596869L;
    @Transient
    private TblYyQueryPriceIdMySql id;
    @Transient
    private TblyypriceMySql tblYyPrice;
    @Transient
    private TblYyUserQueryMySql tblYyUserQuery;

}