package com.financial.sharing.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 凭证导入数据传输对象
 *
 * @author Financial Sharing System
 * @since 2025-12-31
 */
@Data
public class VoucherImportDTO {

    @ExcelProperty("凭证号")
    @ColumnWidth(20)
    private String voucherNo;

    @ExcelProperty("凭证类型")
    @ColumnWidth(15)
    private String voucherType;

    @ExcelProperty("凭证日期")
    @ColumnWidth(15)
    private Date voucherDate;

    @ExcelProperty("会计期间")
    @ColumnWidth(15)
    private String accountingPeriod;

    @ExcelProperty("摘要")
    @ColumnWidth(30)
    private String voucherDesc;

    @ExcelProperty("科目编码")
    @ColumnWidth(20)
    private String subjectCode;

    @ExcelProperty("科目名称")
    @ColumnWidth(30)
    private String subjectName;

    @ExcelProperty("借方金额")
    @ColumnWidth(15)
    private BigDecimal debitAmount;

    @ExcelProperty("贷方金额")
    @ColumnWidth(15)
    private BigDecimal creditAmount;

    @ExcelProperty("辅助核算")
    @ColumnWidth(20)
    private String auxiliaryAccounting;

    @ExcelProperty("备注")
    @ColumnWidth(30)
    private String remark;
}
