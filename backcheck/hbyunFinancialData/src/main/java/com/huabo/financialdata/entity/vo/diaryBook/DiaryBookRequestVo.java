package com.huabo.financialdata.entity.vo.diaryBook;


import com.huabo.financialdata.entity.base.BaseDbSource;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 日记账 - 分页列表 - 请求参数
 *
 * @author Mr.xiang
 * @since 2022-10-20
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name="日记账 - 分页列表 - 请求参数")
public class DiaryBookRequestVo extends BaseDbSource {

    @Schema(name = "查询最大日期")
    private Integer maxMonth;

    @Schema(name = "查询最小日期")
    private Integer minMonth;

    private Integer resultmonth;

    @Schema(name = "科目编码")
    private String accid;

    @Schema(name = "所属期间")
    private Integer month;

    private Integer resultday;

    @Schema(name = "起始页数")
    private Integer pageNumber;

    @Schema(name = "每页数量")
    private Integer pageSize;

    @Schema(name = "查询条件，科目名称-ACCNAME1，科目编号-ACCID，凭证日期-PZ_DATE，抬头文本-LINETEXT，借方金额-MD，贷方金额-MC，凭证号-PZH，凭证类型-PZTYPE，附件数-FJ，期间-AMONTH，财务主管-CWZG，记账人-JZR，出纳人-CNR，审核人-SHR，制单人-ZDR")
    private String type;

    @Schema(name = "查询条件 大于，小于，等于，不等于，包含，不包含")
    private String status;

    @Schema(name = "查询参数")
    private String accName;

    @Schema(name = "借方金额moneyD存放借方本日总金额")
    private String moneyD;

    @Schema(name = "moneyC存放贷方本日总金额")
    private String moneyC;

    @Schema
    private String qc;

    @Schema(name = "所属模块,   智能监控 - znjk 合同管理-htgl 内控合规 -nkhg 系统设置-xtsz 智能审计-znsj 智能分析-znfx ")
    private String mty;

    @Schema(name = "查询条件sql语句")
    private String wh1;

    @Schema(name = "账套")
    private String book;

    @Schema(name = "账套年份")
    private Integer bookYear;

    @Schema(name = "公司名称")
    private String orgName;

    @Schema(name = "月份")
    private String amonth;

    private String choiceSearch;

    @Schema(name = "凭证日期")
    private Date pzDate;

    @Schema(name = "抬头文本")
    private String lineText;

    @Schema(name = "科目长名称")
    private String accNameOne;

    @Schema(name = "借方金额")
    private String md;

    @Schema(name = "贷方金额")
    private String mc;

    @Schema(name = "凭证号")
    private String pzh;

    @Schema(name = "(value)操作日记账、凭证库、明细账传入分录序号，同一凭证下用,线分割 不同凭证用~分割，示例 2,3~3,5  ")
    private Integer entryId;

}
