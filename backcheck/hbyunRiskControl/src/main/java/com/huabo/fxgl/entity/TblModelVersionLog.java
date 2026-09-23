package com.huabo.fxgl.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 模型版本操作日志表实体类
 * 
 * @author 华博云
 * @date 2025-09-30
 */
@Data
@Accessors(chain = true)
@TableName("TBL_MODEL_VERSION_LOG")
@Schema(name="TblModelVersionLog对象", description="模型版本操作日志表")
public class TblModelVersionLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "日志ID")
    @TableId(value = "LOG_ID", type = IdType.ASSIGN_ID)
    private String logId;

    @Schema(name = "模型ID")
    @TableField("MODEL_ID")
    private String modelId;

    @Schema(name = "版本ID")
    @TableField("VERSION_ID")
    private String versionId;

    @Schema(name = "操作类型(CREATE/UPDATE/DELETE/PUBLISH/ARCHIVE/ROLLBACK)")
    @TableField("OPERATION_TYPE")
    private String operationType;

    @Schema(name = "操作详情JSON")
    @TableField("OPERATION_DETAIL")
    private String operationDetail;

    @Schema(name = "旧值")
    @TableField("OLD_VALUE")
    private String oldValue;

    @Schema(name = "新值")
    @TableField("NEW_VALUE")
    private String newValue;

    @Schema(name = "操作结果(SUCCESS/FAILED)")
    @TableField("OPERATION_RESULT")
    private String operationResult;

    @Schema(name = "错误信息")
    @TableField("ERROR_MESSAGE")
    private String errorMessage;

    @Schema(name = "操作人")
    @TableField("OPERATION_USER")
    private String operationUser;

    @Schema(name = "操作时间")
    @TableField("OPERATION_TIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date operationTime;

    @Schema(name = "IP地址")
    @TableField("IP_ADDRESS")
    private String ipAddress;

    @Schema(name = "用户代理")
    @TableField("USER_AGENT")
    private String userAgent;

    /**
     * 操作类型枚举
     */
    public static class OperationType {
        public static final String CREATE = "CREATE";      // 创建
        public static final String UPDATE = "UPDATE";      // 更新
        public static final String DELETE = "DELETE";      // 删除
        public static final String PUBLISH = "PUBLISH";    // 发布
        public static final String ARCHIVE = "ARCHIVE";    // 归档
        public static final String ROLLBACK = "ROLLBACK";  // 回滚
    }

    /**
     * 操作结果枚举
     */
    public static class OperationResult {
        public static final String SUCCESS = "SUCCESS";    // 成功
        public static final String FAILED = "FAILED";      // 失败
    }
}
