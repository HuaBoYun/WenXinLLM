package com.huabo.financialdata.entity.vo.accBkpf;

import com.huabo.financialdata.entity.base.BaseDbSource;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 凭证库  分页查询   请求参数
 * </p>
 *
 * @author Mr.xiang
 * @since 2022-11-22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name="凭证库 - 分页列表 - 请求参数")
public class  AccBkpfRequestVo extends BaseDbSource {

    @Schema(name = "所属模块,   智能监控 - znjk 合同管理-htgl 内控合规 -nkhg 系统设置-xtsz 智能审计-znsj 智能分析-znfx ")
    private String mty;

    @Schema(name = "查询条件，科目名称-ACCNAME1，科目编号-ACCID，凭证日期-PZ_DATE，抬头文本-LINETEXT，借方金额-MD，贷方金额-MD，凭证号-PZH，凭证类型-PZTYPE，附件数-FJ，期间-AMONTH，财务主管-CWZG，记账人-JZR，出纳人-CNR，审核人-SHR，制单人-ZDR")
    private String type;

    @Schema(name = "查询条件 大于，小于，等于，不等于，包含，不包含")
    private String status;

    @Schema(name = "科目名称")
    private String accName;

    @Schema(name = "科目编号")
    private String accid;

    @Schema(name = "月份")
    private String month;

    @Schema(name = "之前的查询条件，追加")
    private String wh1;

    @Schema(name = "账套年份")
    private Integer bookYear;

    @Schema(name = "查询条件最大月份")
    private String maxMonth;

    @Schema(name = "查询条件最小月份")
    private String minMonth;

    @Schema(name = "每页数量")
    private Integer pageSize;

    @Schema(name = "当前页数")
    private Integer pageNumber;

    @Schema(name = "账套名")
    private String book;

    @Schema(name = "万能搜索查询条件")
    private String wnss;

    @Schema(name = "凭证日期")
    private String pzDate;

    @Schema(name = "抬头文本")
    private String lineText;


    @Schema(name = "科目名称长文本")
    private String accNameOne;

    @Schema(name = "借方金额")
    private String md;

    @Schema(name = "贷方金额")
    private String mc;

    @Schema(name = "凭证号")
    private String pzh;

    @Schema(name = "凭证类型")
    private String pzType;

    @Schema(name = "附件数量")
    private String fj;

    @Schema(name = "财务主管")
    private String cwzg;

    @Schema(name = "记账人")
    private String jzrl;

    @Schema(name = "出纳人")
    private String cnr;
    @Schema(name = "审核人")
    private String shrs;
    @Schema(name = "制单人")
    private String zdr;

}
