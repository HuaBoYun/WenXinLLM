package com.huabo.financialdata.entity.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * GBI上传的表信息
 * </p>
 *
 * @author 
 * @since 2024-04-21
 */
@Getter
@Setter
@TableName("TBL_GBI_TABLE")
@Schema(name="TblGbiTable对象", description="GBI上传的表信息")
public class GbiTable implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name="主键id")
    @TableId("ID")
    private String id;

    @Schema(name="APPID")
    @TableField("APP_ID")
    private String appId;

    @Schema(name="批次id")
    @TableField("BATCH_ID")
    private String batchId;

    @Schema(name="批次编号")
    @TableField("BATCH_NO")
    private String batchNo;

    @Schema(name="客户定义表名称")
    @TableField("CUSTOM_TABLE")
    private String customTable;

    @Schema(name="描述")
    @TableField("DESCRIPTION")
    private String description;

    @Schema(name="真实库定义的表名称")
    @TableField("TABLE_NAME")
    private String tableName;

    @Schema(name="用户id")
    @TableField("USER_ID")
    private BigDecimal userId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(name="创建时间")
    @TableField("CREATE_TIME")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(name="修改时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;

    @Schema(name="企业")
    @TableField("CREATE_COMPANY")
    private String createCompany;

    @Schema(name="部门")
    @TableField("CREATE_DEPT")
    private String createDept;

    @Schema(name="顺序")
    @TableField("SORT")
    private Integer sort;

    @Schema(name="总行数")
    @TableField("ROW_NUM")
    private Integer rowNum;

    @Schema(name="总列数")
    @TableField("CELL_NUM")
    private Integer cellNum;

}
