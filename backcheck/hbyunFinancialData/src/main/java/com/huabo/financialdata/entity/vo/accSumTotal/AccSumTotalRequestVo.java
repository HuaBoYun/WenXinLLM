package com.huabo.financialdata.entity.vo.accSumTotal;

import com.huabo.financialdata.entity.base.BaseDbSource;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 总分类账 - 分页列表查询 - 请求参数
 *
 * @author Mr.xiang
 * @since 2022-10-18
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name="总分类账 - 分页列表查询 - 请求参数")
public class AccSumTotalRequestVo extends BaseDbSource {

    @Schema(name = "所属模块,   智能监控 - znjk 合同管理-htgl 内控合规 -nkhg 系统设置-xtsz 智能审计-znsj 智能分析-znfx ")
    private String mty;

    @Schema(name = "当前页数")
    private Integer pageNumber;

    @Schema(name = "每页数量")
    private Integer pageSize;

    @Schema(name = "查询条件，科目名称-ACCNAME1，科目编号-ACCID，凭证日期-PZ_DATE，抬头文本-LINETEXT，借方金额-MD，贷方金额-MC，凭证号-PZH，凭证类型-PZTYPE，附件数-FJ，期间-AMONTH，财务主管-CWZG，记账人-JZR，出纳人-CNR，审核人-SHR，制单人-ZDR")
    private String type;

    @Schema(name = "查询条件 大于，小于，等于，不等于，包含，不包含")
    private String status;

    @Schema(name = "SQL语句拼接条件")
    private String str;

    @Schema(name = "查询开始月份")
    private Integer minMonth;

    @Schema(name = "查询结束月份")
    private Integer maxMonth;

    @Schema(name = "所属期间")
    private Integer amonth;

    @Schema(name = "查询参数")
    private String accName;

    @Schema(name = "查询账套")
    private String book;

    @Schema(name = "账套年份")
    private Integer bookyear;

    @Schema(name = "公司名称")
    private String orgname;

    @Schema(name = "科目长名称")
    private String accNameOne;

    @Schema(name = "公司编码")
    private String accid;

    @Schema(name = "所属年份")
    private Integer ayear;

    @Schema(name = "万能搜索查询条件")
    private String wnss;

}
