package com.huabo.financialdata.entity.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * GBI导入批次表
 * </p>
 *
 * @author 
 * @since 2024-04-21
 */
@Getter
@Setter
@TableName("TBL_GBI_BATCH")
@Schema(name="TblGbiBatch对象", description="GBI导入批次表")
public class GbiBatch implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name="主键id")
    @TableId("ID")
    private String id;

    @Schema(name="appId")
    @TableField("APP_ID")
    private String appId;

    @Schema(name="批次编号，目前暂时和id一样，之后可支持批次多文件")
    @TableField("BATCH_NO")
    private String batchNo;

    @Schema(name="原文件名称")
    @TableField("ATTNAME")
    private String attname;

    @Schema(name="文件ftp地址")
    @TableField("ATTPATH")
    private String attpath;

    @Schema(name="文件大小")
    @TableField("ATTSIZE")
    private BigDecimal attsize;

    @Schema(name="用户id")
    @TableField("USER_ID")
    private BigDecimal userId;

    @Schema(name="创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("CREATE_TIME")
    private Date createTime;

    @Schema(name="企业")
    @TableField("CREATE_COMPANY")
    private String createCompany;

    @Schema(name="部门")
    @TableField("CREATE_DEPT")
    private String createDept;
}
