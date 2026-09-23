package com.management.accountant.oracle.entity.budget;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.io.Serializable;

@Data
@ColumnWidth(18)
public class BudgetAccountExportDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ExcelProperty("科目编码")
    @ColumnWidth(20)
    private String accountCode;

    @ExcelProperty("科目名称")
    @ColumnWidth(25)
    private String accountName;

    @ExcelProperty("科目类型")
    private String accountType;

    @ExcelProperty("科目分类")
    private String accountCategory;

    @ExcelProperty("上级科目编码")
    @ColumnWidth(20)
    private String parentCode;

    @ExcelProperty("科目层级")
    private Integer accountLevel;

    @ExcelProperty("是否末级")
    private String isLeaf;

    @ExcelProperty("借贷方向")
    private String balanceDirection;

    @ExcelProperty("排序")
    private Integer sortOrder;

    @ExcelProperty("是否启用")
    private String isEnabled;

    @ExcelProperty("科目描述")
    @ColumnWidth(30)
    private String accountDescription;

    @ExcelProperty("备注")
    @ColumnWidth(25)
    private String remark;

    public static BudgetAccountExportDTO fromEntity(BudgetAccount a) {
        BudgetAccountExportDTO dto = new BudgetAccountExportDTO();
        dto.setAccountCode(a.getAccountCode());
        dto.setAccountName(a.getAccountName());
        dto.setAccountType(a.getAccountType());
        dto.setAccountCategory(a.getAccountCategory());
        dto.setParentCode(null);
        dto.setAccountLevel(a.getAccountLevel());
        dto.setIsLeaf(a.getIsLeaf() != null && a.getIsLeaf() == 1 ? "是" : "否");
        dto.setBalanceDirection(a.getBalanceDirection());
        dto.setSortOrder(a.getSortOrder());
        dto.setIsEnabled(a.getIsEnabled() != null && a.getIsEnabled() == 1 ? "启用" : "禁用");
        dto.setAccountDescription(a.getAccountDescription());
        dto.setRemark(a.getRemark());
        return dto;
    }

    public BudgetAccount toEntity() {
        BudgetAccount a = new BudgetAccount();
        a.setAccountCode(this.accountCode);
        a.setAccountName(this.accountName);
        a.setAccountType(this.accountType);
        a.setAccountCategory(this.accountCategory);
        a.setAccountLevel(this.accountLevel);
        a.setIsLeaf("是".equals(this.isLeaf) ? 1 : 0);
        a.setBalanceDirection(this.balanceDirection);
        a.setSortOrder(this.sortOrder);
        a.setIsEnabled(!"禁用".equals(this.isEnabled) ? 1 : 0);
        a.setAccountDescription(this.accountDescription);
        a.setRemark(this.remark);
        return a;
    }
}

