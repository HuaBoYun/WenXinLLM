package com.huabo.financialdata.entity.vo.accSumTotal;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 总分类账 - 分页列表查询 - 返回参数
 *
 * @author Mr.xiang
 * @since 2022-10-18
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name="总分类账 - 分页列表查询数据 - 返回参数")
public class AccSumTotalResponseVo implements Serializable {

    private static final long serialVersionUID = 4193499476539790852L;

    @Schema(name = "科目编码")
    private String accid;

    @Schema(name = "科目名称")
    private String accName;

    @Schema(name = "所属期间，月份")
    private String aMonth;

    @Schema(name = "期初借方金额")
    private String qcmd;

    @Schema(name = "期初贷方金额")
    private String qcmc;

    @Schema(name = "本期借方金额，当月累计")
    private String bqmd;

    @Schema(name = "本期贷方金额，当月累计")
    private String bqmc;

    @Schema(name = "累计借方金额，1月到当月amonth 累计金额")
    private String ljmd;

    @Schema(name = "累计贷方金额，1月到当月amonth 累计金额")
    private String ljmc;

    @Schema(name = "本期期末借方金额")
    private String qmmd;

    @Schema(name = "本期期末贷方金额")
    private String qmmc;

    @Schema(name = "期末方向")
    private String qmdc;

    @Schema(name = "所属年份")
    private String aYear;


}
