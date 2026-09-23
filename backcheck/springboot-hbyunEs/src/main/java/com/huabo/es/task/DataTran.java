package com.huabo.es.task;

import com.huabo.es.service.trans.AccAssDataTran;
import com.huabo.es.service.trans.EsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @ClassName : DataTran
 * @Description : TODO
 * @Author : zhibo.cao
 * @Date: 2023-03-27 19:14:39
 */
@Component
public class DataTran {
    private Logger logger = LoggerFactory.getLogger(DataTran.class);
    @Resource
    private AccAssDataTran accAssDataTran;
    @Resource
    private EsService esService;

    /**
     * 凌晨1点50处理索引
     */
    @Scheduled(cron = "0 59 1 ? * * ")
    public void dataTranIndice() {
        logger.info("开始重建索引");
        esService.createAccIndix();
//        esService.createInnerIndix();
//        esService.createOuterIndix();
        logger.info("结束重建索引");
    }

    //     @Scheduled(cron = "0 0/5 * * * ?")
    @Scheduled(cron = "0 0 2 * * ?")
    public void accDataTran() {
        logger.info("开始账簿数据同步");
        accAssDataTran.init();
        logger.info("结束账簿数据同步");
    }
}
