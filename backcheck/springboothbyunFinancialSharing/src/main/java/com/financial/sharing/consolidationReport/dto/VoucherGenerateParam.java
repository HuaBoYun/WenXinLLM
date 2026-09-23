package com.financial.sharing.consolidationReport.dto;

import lombok.Data;

import java.util.Date;

/**
 * 抵消凭证生成参数DTO
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Data
public class VoucherGenerateParam {

    /**
     * 模型ID
     */
    private String modelId;

    /**
     * 期间
     */
    private String period;

    /**
     * 凭证日期
     */
    private Date voucherDate;

    /**
     * 是否重新生成(true:删除旧数据重新生成, false:追加生成)
     */
    private Boolean regenerate;
}

