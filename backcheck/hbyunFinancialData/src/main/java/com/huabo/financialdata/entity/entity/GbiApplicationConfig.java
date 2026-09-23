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

/**
 * <p>
 * 应用配置表
 * </p>
 *
 * @author 
 * @since 2024-04-20
 */
@Getter
@Setter
@TableName("TBL_GBI_APPLICATION_CONFIG")
@Schema(name="TblGbiApplicationConfig对象", description="应用配置表")
public class GbiApplicationConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId("ID")
    private String id;

    @Schema(name="1-自动联系上下文回答，2仅基于当前提问回答")
    @TableField("CONTINUOUS_QUESTIONING")
    private String continuousQuestioning;

    @Schema(name="用户id")
    @TableField("USER_ID")
    private BigDecimal userId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(name="创建时间")
    @TableField("CREATE_TIME")
    private Date createTime;

    @Schema(name="修改时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;

    @Schema(name="是否删除")
    @TableField("DELETED")
    private Integer deleted;

    @Schema(name="创建企业")
    @TableField("CREATE_COMPANY")
    private String createCompany;

    @Schema(name="创建部门")
    @TableField("CREATE_DEPT")
    private String createDept;
}
