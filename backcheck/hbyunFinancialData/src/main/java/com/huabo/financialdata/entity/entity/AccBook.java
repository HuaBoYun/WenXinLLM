package com.huabo.financialdata.entity.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.huabo.financialdata.entity.base.BaseDbSource;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * @author lee
 */
@Data
@TableName("TBL_ACCBOOK")
@Schema(name="账套类 TBL_ACCBOOK对象 ")
public class AccBook extends BaseDbSource implements Serializable {
    private static final long serialVersionUID = -3442723266244523288L;


    /**
     * 账套ID
     */
    @Schema(name = "账套ID")
    @TableId
    private String bookid;

    /**
     * 账套名称
     */
    @Schema(name = "账套名称")
    private String bookname;

    /**
     * 所属公司ID
     */
    @Schema(name = "所属公司ID")
    private BigDecimal orgid;

    /**
     * 所属公司名称
     */
    @Schema(name = "所属公司名称")
    private String orgname;

    /**
     * 账户ID
     */
    @Schema(name = "账套id")
    private String acctid;

    /**
     * 账套描述
     */
    @Schema(name = "账套描述")
    private String bookdesc;

    /**
     * 年份年份
     */
    @Schema(name = "账套描述")
    private String bookyear;

    /**
     * 资产负责表地址
     */
    @Schema(name = "资产负责表地址")
    private String balancesheeturl;

    /**
     * 利润表地址
     */
    @Schema(name = "利润表地址")
    private String incomestatementsurl;

    /**
     * 现金流量表地址
     */
    @Schema(name = "现金流量表地址")
    private String cashflowstatementsurl;


}
