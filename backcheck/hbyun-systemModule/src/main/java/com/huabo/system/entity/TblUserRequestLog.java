package com.huabo.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户请求日志实体类
 * 对应数据库表: TBL_USER_REQUEST_LOG
 * 
 * @author Augment Agent
 * @date 2025-10-20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("TBL_USER_REQUEST_LOG")
public class TblUserRequestLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     * 使用雪花算法生成，适配达梦数据库
     */
    @TableId(value = "ID", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户ID
     */
    @TableField("USERID")
    private Long userId;

    /**
     * 用户名称
     */
    @TableField("USERNAME")
    private String userName;

    /**
     * 用户账号
     */
    @TableField("USERACCOUNT")
    private String userAccount;

    /**
     * 客户端IP地址
     */
    @TableField("IP")
    private String ip;

    /**
     * 请求时间
     */
    @TableField("REQUESTTIME")
    private Date requestTime;

    /**
     * 响应结束时间
     */
    @TableField("ENDTIME")
    private Date endTime;

    /**
     * 请求耗时（毫秒）
     */
    @TableField("REQUESTDURATIONMS")
    private Long requestDurationMs;

    /**
     * 请求报文（JSON格式）
     */
    @TableField("REQUESTPAYLOAD")
    private String requestPayload;

    /**
     * 响应报文（JSON格式）
     */
    @TableField("RESPONSEPAYLOAD")
    private String responsePayload;

    /**
     * 执行的SQL语句集合（JSON格式）
     */
    @TableField("EXECUTEDSQL")
    private String executedSql;

    /**
     * 链路追踪ID
     */
    @TableField("TRACEID")
    private String traceId;

    /**
     * 请求类和方法名
     */
    @TableField("REQUESTCLASSMETHOD")
    private String requestClassMethod;

    /**
     * 操作描述
     */
    @TableField("ACTIONDESCRIPTION")
    private String actionDescription;

    /**
     * 项目模块
     */
    @TableField("PROJECTMODULE")
    private String projectModule;

    /**
     * 业务模块
     */
    @TableField("BUSINESSMODULE")
    private String businessModule;

    /**
     * 子业务模块
     */
    @TableField("SUBBUSINESSMODULE")
    private String subBusinessModule;

    /**
     * 异常信息
     */
    @TableField("ERROR")
    private String error;

    /**
     * 异常标志（0:正常, 1:异常）
     */
    @TableField("ERROR_FLAG")
    private Integer errorFlag;

    /**
     * 创建时间
     */
    @TableField("CREATEDAT")
    private Date createdAt;

}

