package com.huabo.monitor.oracle.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("TBL_TESTTEMPLE")
public class TblTestTemplate implements Serializable {
    private static final long serialVersionUID = 1L;

    private BigDecimal testtemid;
    private String templeNumber;//模板编号
    private String templename;//模板名称
    private String memo;
    private TblStaff staff;//创建者id
    private Date createDateTime;//创建时间
    private Integer templeStatus;
    private String templeDesc;//模板说明
    private String source;//来源：自建、上级公司名称
    private String tblComany;//公司名称
}
