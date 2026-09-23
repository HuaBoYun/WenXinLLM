package com.huabo.log.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @Author: 61
 * @Description: 登录页请求实体
 */
@Data
public class LogPageReq {
    /**
     * 劳人号
     */
    @Schema(name = "劳人号")
    private String userName;
    /**
     * 用户名称
     */
    @Schema(name = "用户名称")
    private String realName;
    /**
     * 业务模块
     */
    @Schema(name = "业务模块")
    private String businessModule;
    /**
     * 请求时间起
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(name = "请求时间起 yyyy-MM-dd HH:mm:ss")
    private Date requestTimeStart;
    /**
     * 请求时间止
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(name = "请求时间止 yyyy-MM-dd HH:mm:ss")
    private Date requestTimeEnd;
    /**
     * 当前页
     */
    @Schema(name = "当前页")
    private Integer currentPage = 1;
    /**
     * 每页条数
     */
    @Schema(name = "每页条数")
    private Integer pageSize = 20;
    
    private String token;
}
