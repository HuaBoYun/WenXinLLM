package com.huabo.contract.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 文档归档查询参数
 *
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DocumentArchiveQueryParam implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 文档编号
     */
    private String documentNo;

    /**
     * 文档名称
     */
    private String documentName;

    /**
     * 文档类型(1:合同文件,2:技术文件,3:管理文件,4:财务文件)
     */
    private Integer documentType;

    /**
     * 文档分类
     */
    private String documentCategory;

    /**
     * 文件格式
     */
    private String fileFormat;

    /**
     * 归档人ID
     */
    private Long archiverId;

    /**
     * 访问级别(1:公开,2:内部,3:机密)
     */
    private Integer accessLevel;

    /**
     * 文档状态(1:有效,2:作废,3:归档)
     */
    private Integer documentStatus;

    /**
     * 关键词
     */
    private String keywords;

    /**
     * 归档开始日期
     */
    private Date archiveDateStart;

    /**
     * 归档结束日期
     */
    private Date archiveDateEnd;

    /**
     * 创建开始时间
     */
    private Date createTimeStart;

    /**
     * 创建结束时间
     */
    private Date createTimeEnd;

    /**
     * 页码
     */
    private Integer pageNumber = 1;

    /**
     * 每页大小
     */
    private Integer pageSize = 10;

    /**
     * 排序字段
     */
    private String orderBy = "create_time";

    /**
     * 排序方向 (ASC/DESC)
     */
    private String orderDirection = "DESC";

    /**
     * 获取排序SQL
     */
    public String getOrderBySql() {
        if (orderBy == null || orderBy.trim().isEmpty()) {
            return "create_time DESC";
        }
        
        String direction = "DESC";
        if ("ASC".equalsIgnoreCase(orderDirection)) {
            direction = "ASC";
        }
        
        return orderBy + " " + direction;
    }
}
