package com.huabo.es.service;

import com.huabo.es.domain.InnerRule;
import org.frameworkset.elasticsearch.entity.ESDatas;

import java.util.List;

/**
 * @ClassName : InnerRuleService
 * @Description : 内规数据业务
 * @Author : zhibo.cao
 * @Date: 2023-03-15 18:09:37
 */
public interface InnerRuleService {

    /**
     * es数据查询
     *
     * @param keyWord
     * @param num
     * @param size
     * @return
     */
    ESDatas<InnerRule> getInnerRuleESDatas(String keyWord, Long num, Long size);

    /**
     * 数据格式化
     *
     * @param inRuleEsData
     * @return
     */
    List<InnerRule> inRuletoHanlder(ESDatas<InnerRule> inRuleEsData);

}
