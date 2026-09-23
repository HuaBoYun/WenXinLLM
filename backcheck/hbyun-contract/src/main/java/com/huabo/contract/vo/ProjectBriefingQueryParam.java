package com.huabo.contract.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 项目交底查询参数
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ProjectBriefingQueryParam implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 页码
     */
    private Integer pageNumber;

    /**
     * 页大小
     */
    private Integer pageSize;

    /**
     * 交底编号
     */
    private String briefingNo;

    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 交底标题
     */
    private String briefingTitle;

    /**
     * 交底类型(1:技术交底,2:安全交底,3:质量交底,4:进度交底,5:成本交底,6:其他交底)
     */
    private Integer briefingType;

    /**
     * 交底类型列表
     */
    private List<Integer> briefingTypeList;

    /**
     * 交底等级(1:一级,2:二级,3:三级,4:四级,5:五级)
     */
    private Integer briefingLevel;

    /**
     * 交底等级列表
     */
    private List<Integer> briefingLevelList;

    /**
     * 交底人ID
     */
    private Long brieferId;

    /**
     * 交底人姓名
     */
    private String brieferName;

    /**
     * 交底时间开始
     */
    private Date briefingTimeStart;

    /**
     * 交底时间结束
     */
    private Date briefingTimeEnd;

    /**
     * 接收人ID
     */
    private Long receiverId;

    /**
     * 接收人姓名
     */
    private String receiverName;

    /**
     * 接收时间开始
     */
    private Date receiveTimeStart;

    /**
     * 接收时间结束
     */
    private Date receiveTimeEnd;

    /**
     * 交底地点
     */
    private String briefingLocation;

    /**
     * 交底状态(1:草稿,2:待交底,3:已交底,4:已接收,5:已确认,6:已完成,7:已取消)
     */
    private Integer briefingStatus;

    /**
     * 交底状态列表
     */
    private List<Integer> briefingStatusList;

    /**
     * 确认人ID
     */
    private Long confirmerId;

    /**
     * 确认人姓名
     */
    private String confirmerName;

    /**
     * 确认时间开始
     */
    private Date confirmTimeStart;

    /**
     * 确认时间结束
     */
    private Date confirmTimeEnd;

    /**
     * 完成度最小值
     */
    private BigDecimal completionRateMin;

    /**
     * 完成度最大值
     */
    private BigDecimal completionRateMax;

    /**
     * 验收人ID
     */
    private Long acceptorId;

    /**
     * 验收人姓名
     */
    private String acceptorName;

    /**
     * 验收时间开始
     */
    private Date acceptanceTimeStart;

    /**
     * 验收时间结束
     */
    private Date acceptanceTimeEnd;

    /**
     * 优先级(1:高,2:中,3:低)
     */
    private Integer priority;

    /**
     * 优先级列表
     */
    private List<Integer> priorityList;

    /**
     * 是否紧急(0:否,1:是)
     */
    private Integer isUrgent;

    /**
     * 是否重要(0:否,1:是)
     */
    private Integer isImportant;

    /**
     * 关键词搜索
     */
    private String keyword;

    /**
     * 创建时间开始
     */
    private Date createTimeStart;

    /**
     * 创建时间结束
     */
    private Date createTimeEnd;

    /**
     * 更新时间开始
     */
    private Date updateTimeStart;

    /**
     * 更新时间结束
     */
    private Date updateTimeEnd;

    /**
     * 创建人
     */
    private Long createBy;

    /**
     * 更新人
     */
    private Long updateBy;

    /**
     * 排序字段
     */
    private String orderBy;

    /**
     * 排序方向(ASC/DESC)
     */
    private String orderDirection;

    /**
     * 部门ID
     */
    private Long departmentId;

    /**
     * 部门名称
     */
    private String departmentName;

    /**
     * 组织ID
     */
    private Long organizationId;

    /**
     * 组织名称
     */
    private String organizationName;

    /**
     * 数据权限用户ID
     */
    private Long dataPermissionUserId;

    /**
     * 数据权限部门ID列表
     */
    private List<Long> dataPermissionDeptIds;

    /**
     * 是否包含子部门
     */
    private Boolean includeSubDept;

    /**
     * 统计开始时间
     */
    private Date statisticsStartTime;

    /**
     * 统计结束时间
     */
    private Date statisticsEndTime;

    /**
     * 统计类型(1:按月,2:按季度,3:按年)
     */
    private Integer statisticsType;

    /**
     * 分组字段
     */
    private String groupBy;

    /**
     * 是否需要统计
     */
    private Boolean needStatistics;

    /**
     * 导出类型(1:Excel,2:PDF,3:Word)
     */
    private Integer exportType;

    /**
     * 导出字段列表
     */
    private List<String> exportFields;

    /**
     * 是否导出明细
     */
    private Boolean exportDetail;

    /**
     * 模板ID
     */
    private Long templateId;

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 自定义查询条件
     */
    private String customCondition;

    /**
     * 扩展参数
     */
    private String extendParams;

    /**
     * 参与人员
     */
    private String participants;

    /**
     * 执行情况
     */
    private String executionStatus;

    /**
     * 问题记录
     */
    private String issueRecords;

    /**
     * 整改措施
     */
    private String correctiveMeasures;

    /**
     * 验收结果
     */
    private String acceptanceResult;

    /**
     * 技术标准
     */
    private String technicalStandards;

    /**
     * 质量标准
     */
    private String qualityStandards;

    /**
     * 安全要求
     */
    private String safetyRequirements;

    /**
     * 进度要求
     */
    private String scheduleRequirements;

    /**
     * 成本控制要求
     */
    private String costControlRequirements;

    /**
     * 交底内容
     */
    private String briefingContent;

    /**
     * 交底要求
     */
    private String briefingRequirements;

    /**
     * 确认意见
     */
    private String confirmComments;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 附件路径
     */
    private String attachmentPath;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 删除标志(0:未删除,1:已删除)
     */
    private Integer delFlag;

    /**
     * 扩展字段1
     */
    private String extField1;

    /**
     * 扩展字段2
     */
    private String extField2;

    /**
     * 扩展字段3
     */
    private String extField3;

    /**
     * 扩展字段4
     */
    private String extField4;

    /**
     * 扩展字段5
     */
    private String extField5;
}
