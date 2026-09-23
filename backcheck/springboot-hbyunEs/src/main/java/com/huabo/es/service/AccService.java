package com.huabo.es.service;

import org.frameworkset.elasticsearch.entity.ESDatas;
import org.frameworkset.elasticsearch.entity.MetaMap;

import java.util.List;

/**
 * @ClassName : AccService
 * @Description : 账簿业务
 * @Author : zhibo.cao
 * @Date: 2023-03-29 10:58:36
 */
public interface AccService {
    /**
     * 获取es数据
     *
     * @param keyWord
     * @param pageNum
     * @param pageSize
     * @return
     */
    ESDatas<MetaMap> getAccESDatas(String keyWord, Long pageNum, Long pageSize);

    /**
     * es数据处理
     *
     * @param accEsData
     * @return
     */
    List<MetaMap> accHandler(ESDatas<MetaMap> accEsData);
}
