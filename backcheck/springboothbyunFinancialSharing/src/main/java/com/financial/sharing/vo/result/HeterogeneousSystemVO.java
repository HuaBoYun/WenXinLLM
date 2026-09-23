package com.financial.sharing.vo.result;

import lombok.Data;
import java.util.Date;
import java.util.Map;

/**
 * 异构系统VO
 */
@Data
public class HeterogeneousSystemVO {
    
    /**
     * 系统ID
     */
    private String systemId;
    
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
     * 系统状态
     */
    private String systemStatus;
    
    /**
     * 状态描述
     */
    private String statusDesc;
    
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
     * 数据格式
     */
    private String dataFormat;
    
    /**
     * 同步频率
     */
    private String syncFrequency;
    
    /**
     * 最后同步时间
     */
    private Date lastSyncTime;
    
    /**
     * 同步状态
     */
    private String syncStatus;
    
    /**
     * 总同步次数
     */
    private Long totalSyncCount;
    
    /**
     * 成功同步次数
     */
    private Long successSyncCount;
    
    /**
     * 失败同步次数
     */
    private Long failSyncCount;
    
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
     * 注册时间
     */
    private Date registerTime;
    
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
     * 备注
     */
    private String remark;
}
