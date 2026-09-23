package com.huabo.financialdata.config.id;


import com.huabo.financialdata.config.exception.BizException;
import com.huabo.financialdata.config.log.LogFactory;
import com.huabo.financialdata.util.WebToolUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 这是一个获取Id值的Factory
 * 该服务需要两个参数，分别是区域编号（regionId）与工作服务编号（dataCenterId），
 * 数值将从IP V4的第三，第四位数据作为datacenterId与WorkerId
 *
 * @author lee
 * @version 1.0.0
 **/
public class IdFactory {
    Logger LOGGER = LoggerFactory.getLogger(IdFactory.class);

    /**
     * 数据中心ID， 数据范围[0,31]
     */
    private static long dataCenterId = 0;

    /**
     * 工作服务编号，数据范围[0,255]
     */
    private static long workerId = 0;

    static {
        try {
            String localIp = WebToolUtils.getLocalIp();
            if (StringUtils.isNotEmpty(localIp)) {
                String[] ip = WebToolUtils.getLocalIp().split("\\.");
                dataCenterId = Integer.valueOf(ip[2]) % 32;
                workerId = Integer.valueOf(ip[3]) % 256;
            }
        } catch (Exception e) {
            LogFactory.error("IdGenerator: IP解析失败", new BizException("IP解析失败", e));
        }
    }

    /**
     * ID生成
     *
     * @return 返回结果
     */
    public static long generateId() {
        return IdGenerator.getInstance(workerId, dataCenterId).generate();
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            System.out.println("id = [" + IdFactory.generateId() + "]");
        }
    }

}
