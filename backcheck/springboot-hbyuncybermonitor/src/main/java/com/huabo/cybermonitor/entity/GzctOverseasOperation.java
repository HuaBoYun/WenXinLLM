package com.huabo.cybermonitor.entity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;;
import java.math.BigDecimal;
import java.time.LocalDateTime;;
import com.baomidou.mybatisplus.extension.activerecord.Model;
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_OVERSEAS_OPERATION")
public class GzctOverseasOperation extends Model<GzctOverseasOperation> {
    @TableId(value = "ID", type = IdType.ASSIGN_UUID) private String id;
    @TableField("UNIT_ID") private String unitId;
    // 前端用 unitName，对应 companyName
    @TableField("COMPANY_NAME") private String companyName;
    @TableField("UNIT_NAME") private String unitName;
    @TableField("COUNTRY") private String country;
    @TableField("REVENUE") private BigDecimal revenue;
    // 前端用 netProfit，对应 profit
    @TableField("PROFIT") private BigDecimal profit;
    @TableField("NET_PROFIT") private BigDecimal netProfit;
    // 前端用 totalAssets，对应 assetTotal
    @TableField("ASSET_TOTAL") private BigDecimal assetTotal;
    @TableField("TOTAL_ASSETS") private BigDecimal totalAssets;
    @TableField("EMPLOYEE_COUNT") private Integer employeeCount;
    @TableField("REPORT_YEAR") private String reportYear;
    // 经营状态: NORMAL/RISK/EXCELLENT
    @TableField("OPERATION_STATUS") private String operationStatus;
    @TableField("CREATE_TIME") private LocalDateTime createTime;
    @TableField("UPDATE_TIME") private LocalDateTime updateTime;
}
