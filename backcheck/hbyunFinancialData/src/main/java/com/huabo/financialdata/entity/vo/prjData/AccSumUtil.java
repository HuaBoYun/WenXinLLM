package com.huabo.financialdata.entity.vo.prjData;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.NumberFormat;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccSumUtil implements java.io.Serializable {

    // Fields
    @Schema(name = "科目编码")
    private String accid;
    @Schema(name = "月份信息 会计期间")
    private String amonth;
    //@NumberFormat(style=Style.NUMBER)
    @NumberFormat(pattern = "#,###.00")
    private Double qcmd;
    @NumberFormat(pattern = "#,###.00")
    private Double qcmc;
    @NumberFormat(pattern = "#,###.00")
    private Double bqmd;
    @NumberFormat(pattern = "#,###.00")
    private Double bqmc;
    @NumberFormat(pattern = "#,###.00")
    private Double qmmd;
    @NumberFormat(pattern = "#,###.00")
    private Double qmmc;
    private String fname;
    @NumberFormat(pattern = "#,###.00")
    private Double qcmdF;
    @NumberFormat(pattern = "#,###.00")
    private Double qcmcF;
    @NumberFormat(pattern = "#,###.00")
    private Double bqmdF;
    @NumberFormat(pattern = "#,###.00")
    private Double bqmcF;
    @NumberFormat(pattern = "#,###.00")
    private Double qmmdF;
    @NumberFormat(pattern = "#,###.00")
    private Double qmmcF;
    private Double num;
    private long ayear;
    @NumberFormat(pattern = "#,###.00")
    private Double ljmd;
    @NumberFormat(pattern = "#,###.00")
    private Double ljmc;
    @NumberFormat(pattern = "#,###.00")
    private Double ljmdF;
    @NumberFormat(pattern = "#,###.00")
    private Double ljmcF;
    private String qcdc;
    private String qmdc;
}
