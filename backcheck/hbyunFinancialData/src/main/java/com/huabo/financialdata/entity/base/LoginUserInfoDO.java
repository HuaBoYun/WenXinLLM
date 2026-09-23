package com.huabo.financialdata.entity.base;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 当前登录用户信息
 *
 * @author lee
 * @version 1.0.0
 **/
@Data
public class LoginUserInfoDO implements Serializable {

    /**
     * 员工ID
     */
    private BigDecimal staffId;

    /**
     * 当前用户所属组织
     */
    private BigDecimal linkOrgId;


    /**
     * 当前用户选择的组织ID
     */
    private BigDecimal currentOrgId;
    
    //用户真实姓名
    private String realName;
}
