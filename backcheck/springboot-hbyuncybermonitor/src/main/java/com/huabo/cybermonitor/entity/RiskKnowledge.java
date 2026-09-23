package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("GZCT_RISK_KNOWLEDGE")
public class RiskKnowledge {
    @TableId(value = "RISK_KNOWLEDGE_ID", type = IdType.ASSIGN_UUID)
    private String riskKnowledgeId;
    @TableField("ENTERPRISE_ID")
    private String enterpriseId;
    @TableField("ENTERPRISE_NAME")
    private String enterpriseName;
    @TableField("TITLE")
    private String title;
    @TableField("KNOWLEDGE_TYPE")
    private String knowledgeType;
    @TableField("RISK_CATEGORY")
    private String riskCategory;
    @TableField("KEYWORD")
    private String keyword;
    @TableField("AUTHOR")
    private String author;
    @TableField("SUMMARY")
    private String summary;
    @TableField("CONTENT")
    private String content;
    @TableField("VIEW_COUNT")
    private Integer viewCount;
    @TableField("LIKE_COUNT")
    private Integer likeCount;
    @TableField("STATUS")
    private String status;
    @TableField("SOURCE")
    private String source;
    @TableField("TAGS")
    private String tags;
    @TableField("PUBLISH_DATE")
    private LocalDate publishDate;
    @TableField("REMARKS")
    private String remarks;
    @TableField("CREATE_BY")
    private String createBy;
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;
    @TableField("UPDATE_BY")
    private String updateBy;
    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
    @TableField("DELETED")
    @TableLogic
    private Boolean deleted;
}
