package com.financial.sharing.dto.param;

import lombok.Data;

/**
 * 核销记录查询参数
 * @author system
 * @since 2025-01-13
 */
@Data
public class WriteOffRecordQueryParam {

    /** 当前页码 */
    private Integer pageNo = 1;

    /** 每页条数 */
    private Integer pageSize = 20;

    /** 核销单号(模糊查询) */
    private String writeOffNo;

    /** 供应商ID */
    private String supplierId;

    /** 核销状态(0:待核销,1:已核销,2:已撤销) */
    private Integer writeOffStatus;
}
