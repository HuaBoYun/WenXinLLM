package com.huabo.cybermonitor.common;

/**
 * 穿透式监管公共常量
 *
 * @author system
 * @since 2026-03-26
 */
public final class SupervisionConstants {

    private SupervisionConstants() {}

    // ==================== 审批状态 ====================
    public static final String APPROVAL_PENDING = "PENDING";
    public static final String APPROVAL_APPROVED = "APPROVED";
    public static final String APPROVAL_REJECTED = "REJECTED";

    // ==================== 项目状态 ====================
    public static final String STATUS_PLANNING = "PLANNING";
    public static final String STATUS_EXECUTING = "EXECUTING";
    public static final String STATUS_COMPLETED = "COMPLETED";
    public static final String STATUS_EXITED = "EXITED";

    // ==================== 核查任务状态 ====================
    public static final String TASK_CREATED = "CREATED";
    public static final String TASK_ASSIGNED = "ASSIGNED";
    public static final String TASK_INVESTIGATING = "INVESTIGATING";
    public static final String TASK_CONCLUDED = "CONCLUDED";
    public static final String TASK_CLOSED = "CLOSED";

    // ==================== 整改状态 ====================
    public static final String RECT_PENDING = "PENDING";
    public static final String RECT_IN_PROGRESS = "IN_PROGRESS";
    public static final String RECT_SUBMITTED = "SUBMITTED";
    public static final String RECT_VERIFIED = "VERIFIED";
    public static final String RECT_REJECTED = "REJECTED";

    // ==================== 预警等级 ====================
    public static final String WARNING_HIGH = "HIGH";
    public static final String WARNING_MEDIUM = "MEDIUM";
    public static final String WARNING_LOW = "LOW";

    // ==================== 数据来源 ====================
    public static final String DATA_SOURCE_INTERNAL = "INTERNAL";
    public static final String DATA_SOURCE_EXTERNAL = "EXTERNAL";

    // ==================== 默认分页 ====================
    public static final int DEFAULT_PAGE_SIZE = 15;
    public static final int DEFAULT_PAGE_NUM = 1;
}

