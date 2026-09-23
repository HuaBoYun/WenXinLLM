package com.huabo.fxgl.dto.cjbdi;

import lombok.Data;
import java.util.List;

/**
 * CJBDI 查询请求参数
 */
@Data
public class CjbdiQueryParam {

    /** 监控企业ID（YyCompany的ID） */
    private Long companyId;

    /** 企业名称 */
    private String companyName;

    /** 统一社会信用代码 */
    private String creditCode;

    /** 要查询的数据类别ID列表 */
    private List<Long> categoryIds;

    /** 监控类型（gysjk/tzjgjk/xsgsjk/jzdsjk/khjk） */
    private String monitorType;
}

