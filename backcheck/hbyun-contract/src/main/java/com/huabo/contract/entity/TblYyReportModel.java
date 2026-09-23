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
    @TableName("TBL_YY_REPORT_MODEL")
@Schema(name="TblYyReportModel对象")
public class TblYyReportModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "REPORTID" ,type = IdType.INPUT)
    private BigDecimal reportid;

    @TableField("REPORTNAME")
    private String reportname;

    @TableField("REPORTURL")
    private String reporturl;

    @TableField("ANNUALPRICE")
    private BigDecimal annualprice;

    @TableField("REPORTPRICE")
    private BigDecimal reportprice;

    @TableField("ORGID")
    private BigDecimal orgid;

    @TableField("STAFFID")
    private BigDecimal staffid;

    @TableField("PRICEID")
    private String priceid;


}
