package com.huabo.compliance.service;
/**
* @description  
* @author   lyz
* @date 2022/4/14 11:51
*/
public interface ActivityPluginsService {
    public final static String OFF="002";
    public final static String ON="001";
    public final static String ERROR="003";

    /**
     * @Title: getoNState
     * @Description: TODO(获取某种业务类型是否开启流程)
     * @author songxiangying
     * @date 2017年2月8日下午3:09:03
     * @param busType
     * @return  001 开启 002 未开启 003业务类型不存在
     * @throws
     */
    public String getoNState(String busType);

}
