package com.huabo.financialdata.entity.entity;

import com.huabo.financialdata.entity.base.BaseDbSource;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name=" 资产负债表 AccReportYbal")
public class AccReportYbal extends BaseDbSource implements java.io.Serializable {
    private static final long serialVersionUID = -3442723266244523288L;
// Fields

    @Schema
    private String zid;

    @Schema
    private String acc1;

    @Schema
    private String zc;

    @Schema
    private Double qcs1;

    @Schema
    private String jsgs1;

    @Schema
    private Double qms1;

    @Schema
    private String jsgs2;

    @Schema
    private String acc2;

    @Schema
    private String qy;

    @Schema
    private Double qcs2;

    @Schema
    private String jsgs3;

    @Schema
    private Double qms2;

    @Schema
    private String jsgs4;

    @Schema
    private String lineno1;

    @Schema
    private String lineno2;

}
