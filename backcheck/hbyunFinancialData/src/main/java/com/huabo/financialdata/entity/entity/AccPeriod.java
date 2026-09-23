package com.huabo.financialdata.entity.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name=" 会计期间表 TBL_ACC_PERIOD 实体类")
public class AccPeriod implements Serializable {
    private static final long serialVersionUID = -3442723266244523288L;

    /**
     * 月份信息 会计期间
     */
    @Schema(name = "月份信息 会计期间")
    private String amonth;

    /**
     * 年份信息 会计年度
     */
    @Schema(name = "年份信息 会计年度")
    private String ayear;

    /**
     * 开始日期
     */
    @Schema(name = "开始日期")
    private Date startdate;

    /**
     * 结束日期
     */
    @Schema(name = "结束日期")
    private Date enddate;

    /**
     * 是否导入
     */
    @Schema(name = "是否导入")
    private String import_;

    /**
     * 凭证表名
     */
    @Schema(name = "凭证表名")
    private String voutbname;


}
