package com.huabo.system.entity;

import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 计费流水记录表
 * 记录每次API调用产生的计费明细
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_BILLING_RECORD")
@Schema(name="TblFeeRecord对象", description="计费流水记录表")
public class TblFeeRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name="主键")
    @TableId(value="ID", type = IdType.INPUT)
    private BigDecimal id;

    @Schema(name="用户ID")
    @TableField("STAFF_ID")
    private String staffId;

    @Schema(name="用户姓名")
    @TableField("STAFF_NAME")
    private String staffName;

    @Schema(name="部门ID")
    @TableField("DEPT_ORG_ID")
    private BigDecimal deptOrgId;

    @Schema(name="公司ID")
    @TableField("COMPANY_ORG_ID")
    private BigDecimal companyOrgId;

    @Schema(name="集团ID")
    @TableField("GROUP_ORG_ID")
    private BigDecimal groupOrgId;

    @Schema(name="页面路由")
    @TableField("PAGE_ROUTE")
    private String pageRoute;

    @Schema(name="接口功能描述")
    @TableField("API_SUMMARY")
    private String apiSummary;

    @Schema(name="接口URL")
    @TableField("API_URL")
    private String apiUrl;

    @Schema(name="计费金额")
    @TableField("FEE_AMOUNT")
    private BigDecimal feeAmount;

    @Schema(name="价格快照ID")
    @TableField("STANDARD_VERSION_ID")
    private BigDecimal standardVersionId;

    @Schema(name="关联TBL_SYSTEM_RIGHT.ID")
    @TableField("RIGHT_ID")
    private BigDecimal rightId;

    @Schema(name="大模块名称")
    @TableField("MODULE_NAME")
    private String moduleName;

    @Schema(name="小模块名称")
    @TableField("SUB_MODULE_NAME")
    private String subModuleName;

    @Schema(name="页面名称")
    @TableField("PAGE_NAME")
    private String pageName;

    @Schema(name="计费时间")
    @TableField("CREATE_TIME")
    private Date createTime;
}
