package com.huabo.financialdata.entity.vo.detailedBook;


import com.huabo.financialdata.entity.base.BaseDbSource;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 明细账 - 分页列表 - 请求参数
 *
 * @author Mr.xiang
 * @since 2022-10-18
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name="明细账 - 分页列表 - 请求参数")
public class DetailedBookRequestVo extends BaseDbSource {

    @Schema(name = "所属模块,   智能监控 - znjk 合同管理-htgl 内控合规 -nkhg 系统设置-xtsz 智能审计-znsj 智能分析-znfx ")
    private String mty;

    @Schema(name = "月份")
    private Integer month;

    @Schema(name = "最小月份")
    private Integer minMonth;

    @Schema(name = "最大月份")
    private Integer maxMonth;

    @Schema(name = "当前页数")
    private Integer pageNumber;

    @Schema(name = "每页数量")
    private Integer pageSize;

    @Schema(name = "查询条件字符串")
    private String str;

    @Schema(name = "查询条件，科目名称-ACCNAME1，科目编号-ACCID，凭证日期-PZ_DATE，抬头文本-LINETEXT，借方金额-MD，贷方金额-MC，凭证号-PZH，凭证类型-PZTYPE，附件数-FJ，期间-AMONTH，财务主管-CWZG，记账人-JZR，出纳人-CNR，审核人-SHR，制单人-ZDR")
    private String type;

    @Schema(name = "查询条件 大于，小于，等于，不等于，包含，不包含")
    private String status;

    @Schema(name = "查询参数")
    private String accName;

    private String qcMonth;

    private String resultMonth;

    @Schema(name = "账套")
    private String book;

    @Schema(name = "账套年份")
    private Integer bookYear;

    @Schema(name = "公司名称")
    private String orgName;

    @Schema(name = "科目编码")
    private String accid;

    private String choiceSearch;

    private String retu;

    @Schema(name = "凭证日期")
    private String pzDate;

    @Schema(name = "抬头文本")
    private String lineText;

    @Schema(name = "科目长名称")
    private String accNameOne;

    @Schema(name = "借方金额")
    private BigDecimal md;

    @Schema(name = "贷方金额")
    private BigDecimal mc;

    @Schema(name = "凭证号")
    private String pzh;

    @Schema(name = "(value)操作日记账、凭证库、明细账传入分录序号，同一凭证下用,线分割 不同凭证用~分割，示例 2,3~3,5  ")
    private String entryId;
}
