package com.huabo.financialdata.entity.vo.prjData;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccSumIdUtil implements java.io.Serializable {

    // Fields

    @Schema(name = "科目编码")
    private String accid;
    @Schema(name = "月份信息 会计期间")
    private long amonth;

}
