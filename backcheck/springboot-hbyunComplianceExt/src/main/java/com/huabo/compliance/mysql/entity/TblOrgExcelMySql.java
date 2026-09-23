package com.huabo.compliance.mysql.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("TBL_ORG_EXCEL")
@Schema(name="TblOrgExcel")
public class TblOrgExcelMySql {

    private static final long serialVersionUID = 4861445276995566607L;
    private Integer excelId;
    private Integer orgId;
    private Integer startYear;
    private Integer endYear;
    private String functionName;
    private Integer startRows;
}
