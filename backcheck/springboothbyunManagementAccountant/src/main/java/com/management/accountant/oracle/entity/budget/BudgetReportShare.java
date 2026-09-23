package com.management.accountant.oracle.entity.budget;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 报告分享记录实体
 */
@Data
@TableName("TBL_BUDGET_REPORT_SHARE")
public class BudgetReportShare {

    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    /** 报告ID */
    private String reportId;

    /** 报告名称（冗余） */
    private String reportName;

    /** 分享方式: LINK-链接分享, EMAIL-邮件分享, INTERNAL-内部分享 */
    private String shareType;

    /** 分享目标（邮箱/用户名/部门等） */
    private String shareTarget;

    /** 分享链接 */
    private String shareUrl;

    /** 分享权限: VIEW-仅查看, DOWNLOAD-可下载, EDIT-可编辑 */
    private String sharePermission;

    /** 过期时间 */
    private Date expireTime;

    /** 分享备注 */
    private String remark;

    /** 状态: 0-已失效, 1-有效 */
    private Integer status;

    /** 创建人 */
    private String createBy;

    /** 创建时间 */
    private Date createTime;

    /** 删除标志: 0-正常, 1-已删除 */
    private Integer delFlag;
}
