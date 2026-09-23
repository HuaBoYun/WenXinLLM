package com.huabo.cybermonitor.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.cybermonitor.util.BaseVo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 企业信息查询VO
 *
 * @author system
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name="企业信息查询参数")
public class EnterpriseInfoQueryVO extends BaseVo {

    @Schema(name = "企业名称")
    private String enterpriseName;

    @Schema(name = "统一社会信用代码")
    private String creditCode;

    @Schema(name = "企业类型：STATE_OWNED-国有独资，STATE_HOLDING-国有控股，STATE_PARTICIPATING-国有参股")
    private String enterpriseType;

    @Schema(name = "监管层级：CENTRAL-中央，LOCAL-地方")
    private String supervisionLevel;

    @Schema(name = "企业状态：NORMAL-正常，CANCELLED-注销，MERGED-合并，SUSPENDED-暂停")
    private String enterpriseStatus;

    @Schema(name = "上市状态：LISTED-已上市，UNLISTED-未上市")
    private String listingStatus;

    @Schema(name = "法定代表人")
    private String legalRepresentative;

    @Schema(name = "行业分类代码")
    private String industryCode;

    @Schema(name = "地区代码")
    private String regionCode;

    @Schema(name = "母公司ID")
    private String parentEnterpriseId;

    @Schema(name = "成立开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private String establishStartDate;

    @Schema(name = "成立结束日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private String establishEndDate;

    @Schema(name = "创建开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private String createStartTime;

    @Schema(name = "创建结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private String createEndTime;

    @Schema(name = "注册资本最小值（万元）")
    private String minRegisteredCapital;

    @Schema(name = "注册资本最大值（万元）")
    private String maxRegisteredCapital;

    @Schema(name = "排序字段")
    private String orderBy;

    @Schema(name = "排序方向：ASC-升序，DESC-降序")
    private String orderDirection;
}
