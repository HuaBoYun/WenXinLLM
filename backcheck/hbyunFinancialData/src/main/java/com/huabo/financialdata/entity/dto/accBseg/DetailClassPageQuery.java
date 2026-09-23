package com.huabo.financialdata.entity.dto.accBseg;

import com.huabo.financialdata.entity.base.BasePageDO;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 明细分类账 - 分页列表查询 - 请求参数
 *
 * @author lee
 * @version 1.0.0
 **/
@Data
@Schema(name="明细分类账 - 分页列表查询 - 请求参数")
public class DetailClassPageQuery extends BasePageDO implements Serializable {

    @Schema(name = "科目编码")
    private String accId;

    @Schema(name = "科目名称")
    private String accName;

    @Schema(name = "月份 - 最小")
    private Integer minMonth;

    @Schema(name = "月份 - 最大")
    private Integer maxMonth;

    @Schema(name = "凭证日期")
    private Date pzDate;

    @Schema(name = "行文本")
    private String lineText;

    @Schema(name = "借方")
    private BigDecimal md;

    @Schema(name = "贷方")
    private BigDecimal mc;
}
