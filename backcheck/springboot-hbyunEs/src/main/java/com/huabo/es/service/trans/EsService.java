package com.huabo.es.service.trans;

import org.frameworkset.elasticsearch.ElasticSearchHelper;
import org.frameworkset.elasticsearch.client.ClientInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @ClassName : EsService
 * @Description : es索引操作
 * @Author : zhibo.cao
 * @Date: 2023-03-27 19:31:31
 */
@Service
public class EsService {
    private Logger logger = LoggerFactory.getLogger(EsService.class);

    /**
     * 创建账簿索引
     */
    public void createAccIndix() {
        try {
            String acc = "acc";
            ClientInterface clientUtil = ElasticSearchHelper.getConfigRestClientUtil("esmapper/ESAccMapper.xml");
            if (clientUtil.existIndice(acc)) {
                clientUtil.dropIndice(acc);
            }
            clientUtil.createIndiceMapping(acc, "createAccIndice");
        } catch (Exception e) {
            logger.error("es报错{}", e);
        }
    }

    /**
     * 创建内规索引
     */
    public void createInnerIndix() {
        try {
            String innerrule = "innerrule";
            ClientInterface clientUtil = ElasticSearchHelper.getConfigRestClientUtil("esmapper/ESRuleMapper.xml");
            if (clientUtil.existIndice(innerrule)) {
                clientUtil.dropIndice(innerrule);
            }
            clientUtil.createIndiceMapping(innerrule, "createInnerRuleIndice");
        } catch (Exception e) {
            logger.error("es报错{}", e);
        }
    }

    /**
     * 创建外规索引
     */
    public void createOuterIndix() {
        try {
            String outerrule = "outerrule";
            ClientInterface clientUtil = ElasticSearchHelper.getConfigRestClientUtil("esmapper/ESRuleMapper.xml");
            if (clientUtil.existIndice(outerrule)) {
                clientUtil.dropIndice(outerrule);
            }
            clientUtil.createIndiceMapping(outerrule, "createOuterRuleIndice");
        } catch (Exception e) {
            logger.error("es报错{}", e);
        }
    }
}
