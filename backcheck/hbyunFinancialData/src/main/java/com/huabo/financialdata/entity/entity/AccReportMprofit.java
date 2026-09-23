package com.huabo.financialdata.entity.entity;

import com.huabo.financialdata.entity.base.BaseDbSource;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.NumberFormat;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name=" 会计期间表 TBL_ACC_PERIOD 实体类")
public class AccReportMprofit extends BaseDbSource implements java.io.Serializable {

    // Fields



    @Schema(name = "id")
    private BigDecimal id;

    @Schema(name = "item")
    private String item;
    /**
     * 当年主营业务收入 --
     */
    @NumberFormat(pattern = "#,###.00")
    @Schema(name = "当年主营业务收入")
    private Double curyearBys;

    /**
     * 主营业务收入计算公式
     */
    @Schema(name = "主营业务收入计算公式")
    private String bysFormula;

    /**
     * 当年本年利润 --
     */
    @Schema(name = "当年本年利润")
    @NumberFormat(pattern = "#,###.00")
    private Double curyearBnlj;

    /**
     * 本年利润计算公式
     */
    @Schema(name = "本年利润计算公式")
    private String bnljFormula;

    /**
     * 第1个月数据
     */
    @NumberFormat(pattern = "#,###.00")
    private Double month1;
    /**
     * 第2个月数据
     */
    @NumberFormat(pattern = "#,###.00")
    private Double month2;
    /**
     * 第3个月数据
     */
    @NumberFormat(pattern = "#,###.00")
    private Double month3;
    /**
     * 第4个月数据
     */
    @NumberFormat(pattern = "#,###.00")
    private Double month4;
    /**
     * 第5个月数据
     */
    @NumberFormat(pattern = "#,###.00")
    private Double month5;
    /**
     * 第6个月数据
     */
    @NumberFormat(pattern = "#,###.00")
    private Double month6;
    /**
     * 第7个月数据
     */
    @NumberFormat(pattern = "#,###.00")
    private Double month7;
    /**
     * 第8个月数据
     */
    @NumberFormat(pattern = "#,###.00")
    private Double month8;
    /**
     * 第9个月数据
     */
    @NumberFormat(pattern = "#,###.00")
    private Double month9;
    /**
     * 第10个月数据
     */
    @NumberFormat(pattern = "#,###.00")
    private Double month10;
    /**
     * 第11个月数据
     */
    @NumberFormat(pattern = "#,###.00")
    private Double month11;
    /**
     * 第12个月数据
     */
    @NumberFormat(pattern = "#,###.00")
    private Double month12;

}
