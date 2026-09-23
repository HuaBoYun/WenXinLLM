package com.financial.sharing.vo.param;

import lombok.Data;
import java.util.Map;

/**
 * 异构系统注册参数
 */
@Data
public class HeterogeneousSystemRegisterParam {
    
    /**
     * 系统编码
     */
    private String systemCode;
    
    /**
     * 系统名称
     */
    private String systemName;
    
    /**
     * 系统类型
     */
    private String systemType;
    
    /**
     * 系统版本
     */
    private String systemVersion;
    
    /**
     * 系统描述
     */
    private String systemDescription;
    
    /**
     * 接口地址
     */
    private String apiUrl;
    
    /**
     * 认证方式
     */
    private String authType;
    
    /**
     * 认证信息
     */
    private Map<String, String> authInfo;
    
    /**
     * 数据格式
     */
    private String dataFormat;
    
    /**
     * 同步频率
     */
    private String syncFrequency;
    
    /**
     * 联系人
     */
    private String contactPerson;
    
    /**
     * 联系电话
     */
    private String contactPhone;
    
    /**
     * 联系邮箱
     */
    private String contactEmail;
    
    /**
     * 备注
     */
    private String remark;
}
