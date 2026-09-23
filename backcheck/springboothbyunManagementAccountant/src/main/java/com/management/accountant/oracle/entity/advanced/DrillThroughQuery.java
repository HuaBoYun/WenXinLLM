package com.management.accountant.oracle.entity.advanced;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 穿透查询实体类
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Data
@TableName("TBL_DRILL_THROUGH_QUERY")
public class DrillThroughQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 查询ID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String queryId;

    /**
     * 查询名称
     */
    private String queryName;

    /**
     * 查询编码
     */
    private String queryCode;

    /**
     * 源数据(JSON格式)
     */
    private String sourceData;

    /**
     * 钻取路径(JSON数组)
     */
    private String drillPath;

    /**
     * 钻取层级
     */
    private Integer drillLevel;

    /**
     * 当前层级
     */
    private Integer currentLevel;

    /**
     * 查询状态: PENDING-待执行, RUNNING-执行中, COMPLETED-已完成, FAILED-失败
     */
    private String queryStatus;

    /**
     * 查询结果(JSON格式)
     */
    private String queryResult;

    /**
     * 执行时间(毫秒)
     */
    private Long executionTime;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 删除标志(0-未删除, 1-已删除)
     */
    private Integer delFlag;

    // ========== 显式Getter和Setter方法，防止Lombok处理失败 ==========
    public String getQueryId() {
        return queryId;
    }
    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }
}

