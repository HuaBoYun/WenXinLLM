package com.huabo.financialdata.entity.dto.accBook;

import com.huabo.financialdata.entity.base.BasePageDO;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 账簿管理 - 分页查询 - 请求参数
 *
 * @author lee
 * @version 1.0.0
 **/
@Data
@Schema(name="账簿管理 - 分页查询 - 请求参数")
public class AccountBookListQuery extends BasePageDO implements Serializable {
    private static final long serialVersionUID = -3423741026300417212L;

    @Schema(name = "账簿名称")
    private String bookName;

    @Schema(name = "企业名称")
    private String orgName;

    @Schema(name = "年份(2000~9999)")
    private String bookYear;


    /**
     * 员工ID
     */
    private BigDecimal staffId;

    /**
     * 当前用户所属组织
     */
    private BigDecimal linkOrgId;


    /**
     * 当前用户选择的组织ID
     */
    private BigDecimal currentOrgId;

}
