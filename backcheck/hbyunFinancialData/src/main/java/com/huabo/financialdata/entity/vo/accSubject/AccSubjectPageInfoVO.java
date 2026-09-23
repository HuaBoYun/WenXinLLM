package com.huabo.financialdata.entity.vo.accSubject;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 会计科目 - 分页列表 -返回参数
 *
 * @author lee
 * @version 1.0.0
 **/
@Data
@Schema(name="会计科目 - 分页列表查询 - 返回参数")
public class AccSubjectPageInfoVO implements Serializable {
    private static final long serialVersionUID = -5581937958011525412L;

    @Schema(name = "科目编码")
    private String accId;

    @Schema(name = "科目名称")
    private String accName;

    @Schema(name = "科目全名称")
    private String accAllName;

    @Schema(name = "科目方向")
    private String dc;

    @Schema(name = "科目方向枚举描述")
    private String dcName;

    @Schema(name = "科目类别")
    private String typeName;

    @Schema(name = "上级科目编码")
    private String highAccId;

    @Schema(name = "上级科目名称")
    private String highAccName;

    @Schema(name = "科目类别编号")
    private String typeId;

    @Schema(name = "是否最底层科目")
    private Integer dcAccStatus;

    @Schema(name = "科目级别")
    private Integer grade;

    @Schema(name = "是否现金或现金等价物")
    private Integer sicash;

    @Schema(name = "是否是标准科目")
    private Integer bzkmStatus;

    @Schema(name = "备注")
    private String des;

    @Schema(name = "年份")
    private Integer year;

}
