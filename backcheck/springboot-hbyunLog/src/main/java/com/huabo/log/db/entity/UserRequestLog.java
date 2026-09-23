package com.huabo.log.db.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.log.vo.OperationLog;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;


@Data
@TableName("TBL_USER_REQUEST_LOG")
public class UserRequestLog {

    @ExcelIgnore
    @Schema(name = "id")
    private Long id;

    @ExcelIgnore
    @Schema(name = "用户id")
    @TableField("USERID")
    private Long userId;

    @ColumnWidth(500)
    @ExcelProperty(value = "IP",index = 5)
    @Schema(name = "IP")
    @TableField("IP")
    private String ip;

    @ColumnWidth(300)
    @ExcelProperty(value = "用户名",index = 0)
    @Schema(name = "用户名")
    @TableField("USERNAME")
    private String username;

    @ColumnWidth(300)
    @ExcelProperty(value = "用户账户",index = 1)
    @Schema(name = "用户账户")
    @TableField("USERACCOUNT")
    private String userAccount;

    @ColumnWidth(500)
    @ExcelProperty(value = "请求时间",index = 2)
    @Schema(name = "请求时间")
    @TableField("REQUESTTIME")
    private Date requestTime;

    @ExcelIgnore
    @ExcelProperty("请求结束时间")
    @Schema(name = "请求结束时间")
    @TableField("ENDTIME")
    private Date endTime;

    @ExcelIgnore
    @Schema(name = "请求耗时（毫秒）")
    @TableField("REQUESTDURATIONMS")
    private Long requestDurationMs;

    @ExcelIgnore
    @Schema(name = "请求报文")
    @TableField("REQUESTPAYLOAD")
    private String requestPayload;

    @ExcelIgnore
    @Schema(name = "返回报文")
    @TableField("RESPONSEPAYLOAD")
    private String responsePayload;

    @ExcelIgnore
    @Schema(name = "执行sql")
    @TableField("EXECUTEDSQL")
    private String executedSql;

    @ExcelIgnore
    @Schema(name = "链路id")
    @TableField("TRACEID")
    private String traceId;

    @ExcelIgnore
    @Schema(name = "调用方法名称")
    @TableField("REQUESTCLASSMETHOD")
    private String requestClassMethod;

    @ExcelIgnore
    @Schema(name = "业务动作描述")
    @TableField("ACTIONDESCRIPTION")
    private String actionDescription;

    /**
     * 项目模块
     */
    @ExcelIgnore
    @Schema(name = "项目模块")
    @TableField("PROJECTMODULE")
    private String module;
    /**
     * 业务模块
     */
    @ColumnWidth(500)
    @ExcelProperty(value = "业务名称",index = 3)
    @Schema(name = "业务名称")
    @TableField("BUSINESSMODULE")
    private String businessModule;
    /**
     * 业务子模块
     */
    @ColumnWidth(500)
    @ExcelProperty(value = "子业务名称",index = 4)
    @Schema(name = "子业务名称")
    @TableField("SUBBUSINESSMODULE")
    private String subBusinessModule;

    @ExcelIgnore
    @Schema(name = "创建时间")
    @TableField(value = "CREATEDAT")
    private Date createdAt;

    /**
     * 异常信息
     */
    @Schema(name = "错误内容")
    @ColumnWidth(800)
    @ContentStyle(wrapped = true) // 设置自动换行
    @ExcelProperty(value = "错误内容")
    @TableField(value = "ERROR")
    private String error;

    /**
     * 异常标志 0：无异常 1：有异常
     */
    @Schema(name = "异常标志 0：无异常 1：有异常")
    @TableField(value = "ERROR_FLAG")
    @ExcelIgnore
    private Integer errorFlag;

    public UserRequestLog getUserRequestLogByOperationLog(OperationLog o) {
        UserRequestLog u = new UserRequestLog();
        u.setId(RandomUtil.uuLongId());
        u.setUserId(Long.parseLong(o.getUserId()));
        u.setIp(o.getIp().equals("[0:0:0:0:0:0:0:1]")?"127.0.0.1":o.getIp());
        u.setUsername(o.getUserName());
        u.setUserAccount(o.getUserAccount());
        u.setRequestTime(o.getRequestTime());
        u.setEndTime(o.getEndTime());
        u.setRequestDurationMs(o.getRequestDurationMs());
        u.setRequestPayload(o.getRequestPayload());
        u.setResponsePayload(o.getResponsePayload());
        u.setExecutedSql(o.getExecutedSql());
        u.setTraceId(o.getTraceId());
        u.setRequestClassMethod(o.getRequestClassMethod());
        u.setActionDescription(o.getActionDescription());
        u.setBusinessModule(o.getBusinessModule());
        u.setModule(o.getModule());
        u.setSubBusinessModule(o.getSubBusinessModule());
        u.setCreatedAt(new Date());
        u.setError(o.getError());
        u.setErrorFlag(o.getErrorFlag());
        return u;
    }
}
