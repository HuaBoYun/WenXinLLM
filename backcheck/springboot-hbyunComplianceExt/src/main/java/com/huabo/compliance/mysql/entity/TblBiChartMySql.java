package com.huabo.compliance.mysql.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("TBL_BI_CHART")
@Schema(name="TblBiChartMySql")
public class TblBiChartMySql {
    @TableId("CHARTID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select HIBERNATE_SEQUENCE.nextval from dual")
    private BigDecimal chartid;
    // private TblBiPage tblBiPage;
    @TableField("CHARTNAME")
    private String chartname;
    @TableField("CHARTTYPE")
    private String charttype;
    @TableField("DATATYPE")
    private String datatype;
    @TableField("MEMO")
    private String memo;
    @TableField("POINTX")
    private Double pointx;
    @TableField("HEIGHT")
    private BigDecimal height;
    @TableField("WIDTH")
    private BigDecimal width;
    @TableField("TITLE")
    private String title;
    @TableField("POINTY")
    private Double pointy;
    @TableField("AXISX")
    private String axisx;
    @TableField("AXISY")
    private String axisy;
    @TableField("CONFIGURATION")
    private String configuration;
    @TableField("DIVID")
    private String divid;
    //private Set tblBiDatasources = new HashSet(0);
    @TableField("PAGETYPE")
    private String pageType;
}
