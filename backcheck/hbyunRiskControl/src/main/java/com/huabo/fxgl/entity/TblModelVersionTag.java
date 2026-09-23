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
 * 模型版本标签表实体类
 * 
 * @author 华博云
 * @date 2025-09-30
 */
@Data
@Accessors(chain = true)
@TableName("TBL_MODEL_VERSION_TAG")
@Schema(name="TblModelVersionTag对象", description="模型版本标签表")
public class TblModelVersionTag implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "标签ID")
    @TableId(value = "TAG_ID", type = IdType.ASSIGN_ID)
    private String tagId;

    @Schema(name = "版本ID")
    @TableField("VERSION_ID")
    private String versionId;

    @Schema(name = "标签名称")
    @TableField("TAG_NAME")
    private String tagName;

    @Schema(name = "标签颜色")
    @TableField("TAG_COLOR")
    private String tagColor;

    @Schema(name = "标签描述")
    @TableField("TAG_DESCRIPTION")
    private String tagDescription;

    @Schema(name = "创建人")
    @TableField("CREATE_USER")
    private String createUser;

    @Schema(name = "创建时间")
    @TableField("CREATE_TIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 预定义标签颜色
     */
    public static class TagColor {
        public static final String BLUE = "#1890ff";       // 蓝色
        public static final String GREEN = "#52c41a";      // 绿色
        public static final String ORANGE = "#fa8c16";     // 橙色
        public static final String RED = "#f5222d";        // 红色
        public static final String PURPLE = "#722ed1";     // 紫色
        public static final String CYAN = "#13c2c2";       // 青色
        public static final String GRAY = "#8c8c8c";       // 灰色
    }

    /**
     * 预定义标签名称
     */
    public static class TagName {
        public static final String STABLE = "稳定版";      // 稳定版本
        public static final String BETA = "测试版";        // 测试版本
        public static final String HOTFIX = "热修复";      // 热修复版本
        public static final String FEATURE = "功能版";     // 功能版本
        public static final String DEPRECATED = "已废弃";  // 已废弃版本
    }
}
