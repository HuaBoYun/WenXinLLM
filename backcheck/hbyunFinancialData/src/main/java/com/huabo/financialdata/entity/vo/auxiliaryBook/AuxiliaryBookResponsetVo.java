package com.huabo.financialdata.entity.vo.auxiliaryBook;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 辅助账  分页查询    返回参数
 * </p>
 *
 * @author Mr.xiang
 * @since 2022-11-21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name="辅助账 - 分页列表 - 返回参数")
public class AuxiliaryBookResponsetVo {

    @Schema(name = "辅助账类型")
    private String assType;

    @Schema(name = "辅助账ID")
    private String assId;

    @Schema(name = "辅助项名称")
    private String assName;

    @Schema(name = "辅助项描述")
    private String assDes;

    @Schema(name = "辅助级别")
    private String assLevel;

    @Schema(name = "辅助项上级ID")
    private String assSjbm;

    @Schema(name = "初期借方余额")
    private String qcmd;

    @Schema(name = "初期贷方余额")
    private String qcmc;

    @Schema(name = "借方发生额")
    private String bqmd;

    @Schema(name = "贷方发生额")
    private String bqmc;

    @Schema(name = "期末借方余额")
    private String qmmd;

    @Schema(name = "期末贷方余额")
    private String qmmc;

    @Schema(name = "余额方向")
    private String qcdc;

    @Schema(name = "期末方向 D,借 C 贷,0 平")
    private String qmdc;

    @Schema(name = "科目名称")
    private String accName;

    @Schema(name = "会计期间")
    private String aMonth;

    @Schema(name = "累计借方金额，1月到当月amonth 累计金额")
    private String ljmd;
    @Schema(name = "累计贷方金额")
    private String ljmc;
    
    private Integer aid;
    
    private String acid;

}
