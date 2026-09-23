package com.huabo.know.vo.param;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Schema(name="新建合同要素入参")
public class CreateTermsParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "条款标题")
    private String title;

    @Schema(name = "发布时间")
    private Date issueDate;

    @Schema(name = "发布年份")
    private String issueYear;

    @Schema(name = "条款内容")
    private String content;

    @Schema(name = "风险提示")
    private String riskTips;

    @Schema(name = "风险等级code")
    private String riskLevelCode;

    @Schema(name = "风险等级name")
    private String riskLevelName;

    @Schema(name = "合同类型code")
    private String contractTypeCode;

    @Schema(name = "合同类型name")
    private String contractTypeName;

    @Schema(name = "条款利益倾向方code")
    private String interestPartyCode;

    @Schema(name = "条款利益倾向方name")
    private String interestPartyName;

    @Schema(name = "适用行业code")
    private String industryTypeCode;

    @Schema(name = "适用行业name")
    private String industryTypeName;

    @Schema(name = "条款类型code")
    private String termsTypeCode;

    @Schema(name = "条款类型name")
    private String termsTypeName;
}
