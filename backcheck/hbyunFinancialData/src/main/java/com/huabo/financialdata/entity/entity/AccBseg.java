package com.huabo.financialdata.entity.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.huabo.financialdata.entity.base.BaseDbSource;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 按月明细表
 * 此处表名  后面有年份数字，无法固定表面
 *
 * @author lee
 * @version 1.0.0
 **/
@Data
@Schema(name="按月明细表 TBL_ACC_BSEG对象 ")
public class AccBseg extends BaseDbSource implements Serializable {
    private static final long serialVersionUID = -7960083767625052920L;

    /**
     * 凭证号
     */
    @Schema(name = "凭证号")
    private String pzh;

    /**
     * 借方
     */
    @Schema(name = "借方")
    private BigDecimal md;

    /**
     * 借方 - 外币
     */
    @Schema(name = "账套ID")
    private BigDecimal mdF;

    /**
     * 贷方
     */
    @Schema(name = "贷方")
    private BigDecimal mc;

    /**
     * 贷方 - 外币
     */
    @Schema(name = "贷方- 外币")
    private BigDecimal mcF;

    /**
     * 行项目文本
     */
    @Schema(name = "行项目文本")
    private String lineText;

    /**
     * 外币名称
     */
    @Schema(name = "外币名称")
    private String fName;

    /**
     * 汇率
     */
    @Schema(name = "汇率")
    private double hl;

    /**
     * 支票号
     */
    @Schema(name = "支票号")
    private String zph;

    /**
     * 银行单据号
     */
    @Schema(name = "银行单据号")
    private String yhdjh;

    /**
     * 分录序号
     */
    @Schema(name = "分录序号")
    private BigDecimal entryid;

    /**
     * 科目编码
     */
    @Schema(name = "科目编码")
    private String accid;

    /**
     * 关联号
     */
    @Schema(name = "关联号")
    private String glh;

    /**
     * 月份
     */
    @Schema(name = "会计期间")
    private Integer amonth;

    /**
     * 年份
     */
    @Schema(name = "年份信息 会计年度")
    private Integer ayear;


}
