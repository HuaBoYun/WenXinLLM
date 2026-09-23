package com.management.accountant.oracle.entity.budget;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.io.Serializable;

/**
 * 维度成员导入DTO
 */
@Data
@ColumnWidth(18)
public class BudgetDimensionMemberImportDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ExcelProperty("成员编码")
    @ColumnWidth(20)
    private String memberCode;

    @ExcelProperty("成员名称")
    @ColumnWidth(25)
    private String memberName;

    @ExcelProperty("排序号")
    private Integer sortOrder;

    @ExcelProperty("是否启用")
    private String isActive;

    @ExcelProperty("描述")
    @ColumnWidth(30)
    private String dimensionDescription;
}

