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
 * 模型版本比较记录表实体类
 * 
 * @author 华博云
 * @date 2025-09-30
 */
@Data
@Accessors(chain = true)
@TableName("TBL_MODEL_VERSION_COMPARE")
@Schema(name="TblModelVersionCompare对象", description="模型版本比较记录表")
public class TblModelVersionCompare implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "比较ID")
    @TableId(value = "COMPARE_ID", type = IdType.ASSIGN_ID)
    private String compareId;

    @Schema(name = "模型ID")
    @TableField("MODEL_ID")
    private String modelId;

    @Schema(name = "源版本ID")
    @TableField("SOURCE_VERSION_ID")
    private String sourceVersionId;

    @Schema(name = "目标版本ID")
    @TableField("TARGET_VERSION_ID")
    private String targetVersionId;

    @Schema(name = "比较类型(FIELD/SQL/CONFIG)")
    @TableField("COMPARE_TYPE")
    private String compareType;

    @Schema(name = "字段名称")
    @TableField("FIELD_NAME")
    private String fieldName;

    @Schema(name = "源值")
    @TableField("SOURCE_VALUE")
    private String sourceValue;

    @Schema(name = "目标值")
    @TableField("TARGET_VALUE")
    private String targetValue;

    @Schema(name = "差异类型(ADDED/MODIFIED/DELETED/UNCHANGED)")
    @TableField("DIFF_TYPE")
    private String diffType;

    @Schema(name = "差异详情JSON")
    @TableField("DIFF_DETAIL")
    private String diffDetail;

    @Schema(name = "创建人")
    @TableField("CREATE_USER")
    private String createUser;

    @Schema(name = "创建时间")
    @TableField("CREATE_TIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 比较类型枚举
     */
    public static class CompareType {
        public static final String FIELD = "FIELD";    // 字段比较
        public static final String SQL = "SQL";        // SQL比较
        public static final String CONFIG = "CONFIG";  // 配置比较
    }

    /**
     * 差异类型枚举
     */
    public static class DiffType {
        public static final String ADDED = "ADDED";        // 新增
        public static final String MODIFIED = "MODIFIED";  // 修改
        public static final String DELETED = "DELETED";    // 删除
        public static final String UNCHANGED = "UNCHANGED"; // 未变更
    }
}
