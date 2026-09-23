package com.huabo.monitor.oracle.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 等级维护
 *
 * @author SongXiangYing
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class TblAssesslevel implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    private BigDecimal asslevid;
    private String levelname;
    private String upperregiondes;
    private double levelupper;
    private double levellower;
    private String lowerregiondes;
    private String leveldes;
    private Date modifieddate;
    private String memo;
    private String tblComany;
}