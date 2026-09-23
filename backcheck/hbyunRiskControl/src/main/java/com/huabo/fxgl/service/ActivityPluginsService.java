package com.huabo.fxgl.service;

/**
 * @author zuoshun
 * @version V1.0
 * @Package com.huabo.fxgl.service
 * @date 2022/8/18 14:41
 */
public interface ActivityPluginsService {

    public final static String OFF="002";
    public final static String ON="001";
    public final static String ERROR="003";

    public String getoNState(String busType);

}
