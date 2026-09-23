package com.huabo.financialdata.entity.dto.accSubject;

import com.huabo.financialdata.entity.base.BasePageDO;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 会计科目 - 分页列表请求参数
 *
 * @author lee
 * @version 1.0.0
 **/
@Data
@Schema(name="会计科目 - 分页列表查询 - 请求参数")
public class AccSubjectListPageQuery extends BasePageDO implements Serializable {
    private static final long serialVersionUID = -5581937958011525412L;

    @Schema(name = "科目编码")
    private String accId;

    @Schema(name = "科目名称")
    private String accName;

    @Schema(name = "科目全名称")
    private String accAllName;

    @Schema(name = "科目方向")
    private String dc;

    @Schema(name = "科目类别")
    private String typeName;

    @Schema(name = "上级科目编码")
    private String highAccId;

    @Schema(name = "科目类别编号")
    private String typeId;

    @Schema(name = "级次")
    private Integer igrade;

    @Schema(name = "是否最底层科目")
    private Integer dcAccStatus;

    @Schema(name = "年份")
    private Integer year;

    @Schema(name = "等于，不等于，小于，小于等于，大于等于，大于，包含，不包含")
    private String status;

    private Integer bookYear;

}
