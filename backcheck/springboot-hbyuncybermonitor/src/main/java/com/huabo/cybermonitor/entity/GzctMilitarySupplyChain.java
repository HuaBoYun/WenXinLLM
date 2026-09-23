package com.huabo.cybermonitor.entity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.extension.activerecord.Model;
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_MILITARY_SUPPLY_CHAIN")
public class GzctMilitarySupplyChain extends Model<GzctMilitarySupplyChain> {
    @TableId(value = "CHAIN_ID", type = IdType.ASSIGN_UUID) private String chainId;
    @TableField("COMPANY_ID") private String companyId;
    @TableField("COMPANY_NAME") private String companyName;
    @TableField("SUPPLIER_NAME") private String supplierName;
    @TableField("MATERIAL_TYPE") private String materialType;
    @TableField("IS_DOMESTIC") private String isDomestic;
    @TableField("COUNTRY_ORIGIN") private String countryOrigin;
    @TableField("DEPENDENCY_LEVEL") private String dependencyLevel;
    @TableField("IS_SINGLE_SOURCE") private String isSingleSource;
    @TableField("RISK_LEVEL") private String riskLevel;
    @TableField("CREATE_TIME") private LocalDateTime createTime;
    @TableField("UPDATE_TIME") private LocalDateTime updateTime;
    /** 组织路径 */
    @TableField("ORG_PATH")
    private String orgPath;
}
