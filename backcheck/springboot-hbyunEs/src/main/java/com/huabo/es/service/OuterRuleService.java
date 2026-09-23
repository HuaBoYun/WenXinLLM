package com.huabo.es.service;

import com.huabo.es.domain.OuterRule;
import org.frameworkset.elasticsearch.entity.ESDatas;

import java.util.List;

/**
 * @ClassName : OuterRuleService
 * @Description : 外规数据业务
 * @Author : zhibo.cao
 * @Date: 2023-03-15 18:09:55
 */
public interface OuterRuleService {

    /**
     * es数据查询
     *
     * @param keyWord
     * @param pageNum
     * @param pageSize
     * @return
     */
    ESDatas<OuterRule> getOuterRuleESDatas(String keyWord, Long pageNum, Long pageSize);

    /**
     * 数据格式化
     *
     * @param outRuleEsData
     * @return
     */
    List<OuterRule> outRuleHandler(ESDatas<OuterRule> outRuleEsData);
}
