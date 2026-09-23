package com.huabo.system.entity.flow;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

//流程任务表
@Data
public class FlowTask implements Serializable {
	private static final long serialVersionUID = -8370543792319392318L;

	 /**
     * 任务主键
     */
    private String id;

    /**
     * 实例进程
     */
    private String processId;

    /**
     * 任务编码
     */
    private String enCode;

    /**
     * 任务标题
     */
    private String fullName;

    /**
     * 紧急程度
     */
    private Integer flowUrgent;

    /**
     * 流程主键
     */
    private String flowId;

    /**
     * 流程编码
     */
    private String flowCode;

    /**
     * 流程名称
     */
    private String flowName;

    /**
     * 流程类型
     */
    private Integer flowType;

    /**
     * 流程分类
     */
    private String flowCategory;

    /**
     * 流程表单
     */
    private String flowForm;

    /**
     * 表单内容
     */
    private String flowFormContentJson;

    /**
     * 流程模板
     */
    private String flowTemplateJson;

    /**
     * 流程版本
     */
    private String flowVersion;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 当前步骤
     */
    private String thisStep;

    /**
     * 当前步骤Id
     */
    private String thisStepId;

    /**
     * 重要等级
     */
    private String grade;

    /**
     * 任务状态 0-草稿、1-处理、2-通过、3-驳回、4-撤销、5-终止
     */
    private Integer status;

    /**
     * 完成情况
     */
    private Integer completion;

    /**
     * 描述
     */
    private String description;

    /**
     * 父节点id
     */
    private String parentId;

    /**
     * 节点主键
     */
    private String taskNodeId;

    /**
     * 表单分类(1.系统表单 2.自定义表单)
     */
    private Integer formType;

    /**
     * 是否批量（0：否，1：是）
     */
    private Integer isBatch;

    /**
     * 排序码
     */
    private Long sortCode;

    /**
     * 有效标志
     */
    private Integer enabledMark;

    /**
     * 同步异步（0：同步，1：异步）
     */
    private Integer isAsync;
    
    /**
     * 冻结审批
     */
    private String rejectId;

    /**
     * 创建时间
     */
    private Date creatorTime;

    /**
     * 创建用户
     */
    private String creatorUserId;

    /**
     * 修改时间
     */
    private Date lastModifyTime;

    /**
     * 修改用户
     */
    private String lastModifyUserId;

    /**
     * 删除标志
     */
    private Integer deleteMark;

    /**
     * 删除时间
     */
    private Date deleteTime;

    /**
     * 删除用户
     */
    private String deleteUserId;
}
