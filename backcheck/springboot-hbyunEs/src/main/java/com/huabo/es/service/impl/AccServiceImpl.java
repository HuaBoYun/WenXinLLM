package com.huabo.es.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.huabo.es.service.AccService;
import org.frameworkset.elasticsearch.boot.BBossESStarter;
import org.frameworkset.elasticsearch.client.ClientInterface;
import org.frameworkset.elasticsearch.entity.ESDatas;
import org.frameworkset.elasticsearch.entity.MetaMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName : AccServiceImpl
 * @Description : 账簿业务接口
 * @Author : zhibo.cao
 * @Date: 2023-03-29 11:01:11
 */
@Service
public class AccServiceImpl implements AccService {
    private Logger logger = LoggerFactory.getLogger(AccServiceImpl.class);
    @Autowired
    private BBossESStarter bbossESStarter;
    private String mappath = "esmapper/ESAccMapper.xml";

    @Override
    public ESDatas<MetaMap> getAccESDatas(String keyWord, Long pageNum, Long pageSize) {
        ClientInterface clientUtil = bbossESStarter.getConfigRestClient(mappath);
        // 内规查询 startPage * size
        Map<String, Object> accParams = new HashMap<String, Object>();
        accParams.put("keyword", keyWord);
        accParams.put("from", (pageNum - 1) * pageSize);
        accParams.put("size", pageSize);
        // 分页获取内规,分页指定最大10000条
        ESDatas<MetaMap> accMap = clientUtil.searchList("acc/_search", "accQuery", accParams, MetaMap.class);
        return accMap;
    }

    @Override
    public List<MetaMap> accHandler(ESDatas<MetaMap> accEsData) {
        List<MetaMap> accs = CollUtil.newArrayList();
        List<MetaMap> datas = accEsData.getDatas();
        if (CollUtil.isEmpty(datas)) {
            return accs;
        }
        for (MetaMap data : datas) {
            List<Object> highlight = data.getHighlight()
                    .entrySet().stream()
                    .filter(e -> StrUtil.equals(e.getKey(), "fullFileds"))
                    .map(Map.Entry::getValue).collect(Collectors.toList());
            data.put("highlights", StrUtil.join(",", highlight));
            accs.add(data);
        }
        return accs;
    }
}
