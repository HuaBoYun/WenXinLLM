package com.huabo.monitor.mysql.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author huabo
 * @since 2022-04-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_NBSJ_SHEET_REPORT")
@Schema(name="TblNbsjSheetReportMySql对象")
public class TblNbsjSheetReportMySql implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("REPORTID")
    private Integer reportid;

    @TableField("REPORTCONTENT")
    private String reportcontent;

    @TableField("REPORTORGIDS")
    private String reportorgids;

    @TableField("SHEETID")
    private Long sheetid;

    @TableField("tblNbsjSheet")
    private TblNbsjSheetMySql tblNbsjSheet;

}
