package com.huabo.financialdata.config.log;

import lombok.Data;

/**
 * 日志信息类
 *
 * @author lee
 * @version 1.0.0
 **/
@Data
public class CalorieLogInfo {

    /**
     * 发生时间
     */
    private String accDate;
    /**
     * 信息码
     */
    private int infoCode;
    /**
     * 信息名称
     */
    private String infoName;
    /**
     * 信息简述
     */
    private String infoTitle;
    /**
     * 信息详细信息
     */
    private String infoDesc;
    /**
     * 用户定义信息
     */
    private String infoUsrDesc;
}
