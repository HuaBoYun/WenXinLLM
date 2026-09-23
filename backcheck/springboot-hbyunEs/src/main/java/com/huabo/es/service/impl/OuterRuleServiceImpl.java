package com.huabo.es.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.huabo.es.domain.OuterRule;
import com.huabo.es.service.OuterRuleService;
import org.frameworkset.elasticsearch.boot.BBossESStarter;
import org.frameworkset.elasticsearch.client.ClientInterface;
import org.frameworkset.elasticsearch.entity.ESDatas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName : OuterRuleServiceImpl
 * @Description : TODO
 * @Author : zhibo.cao
 * @Date: 2023-03-15 18:12:14
 */
@Service
public class OuterRuleServiceImpl implements OuterRuleService {
    @Autowired
    private BBossESStarter bbossESStarter;
    private String mappath = "esmapper/ESRuleMapper.xml";

    @Override
    public ESDatas<OuterRule> getOuterRuleESDatas(String keyWord, Long pageNum, Long pageSize) {
        ClientInterface clientUtil = bbossESStarter.getConfigRestClient(mappath);
        // 外规则查询
        Map<String, Object> outRuleParams = new HashMap<String, Object>();
        outRuleParams.put("keyword", keyWord);
        outRuleParams.put("from", (pageNum - 1) * pageSize);
        outRuleParams.put("size", pageSize);
        // 分页获取内规,分页指定最大10000条
        ESDatas<OuterRule> outRuleEsData = clientUtil.searchList("outerrule/_search", "outerRuleQuery", outRuleParams, OuterRule.class);
        return outRuleEsData;
    }

    @Override
    public List<OuterRule> outRuleHandler(ESDatas<OuterRule> outRuleEsData) {
        List<OuterRule> outRules = CollUtil.newArrayList();
        List<OuterRule> datas = outRuleEsData.getDatas();
        if (CollUtil.isEmpty(datas)) {
            return outRules;
        }
        for (OuterRule outerRule : datas) {
            if (ObjectUtil.isNotEmpty(outerRule.getHighlight())) {
                Map<String, List<Object>> highlight = outerRule.getHighlight();
                List<Object> bodyinfo = highlight.get("bodyinfo");
                if (CollUtil.isNotEmpty(bodyinfo)) {
                    outerRule.setBodyinfo(bodyinfo.get(0).toString());
                }
                List<Object> summaryinfo = highlight.get("summaryinfo");
                if (CollUtil.isNotEmpty(summaryinfo)) {
                    outerRule.setSummaryinfo(summaryinfo.get(0).toString());
                }
                List<Object> rulename = highlight.get("rulename");
                if (CollUtil.isNotEmpty(rulename)) {
                    outerRule.setRulename(rulename.get(0).toString());
                }
                List<Object> rulenumber = highlight.get("rulenumber");
                if (CollUtil.isNotEmpty(rulenumber)) {
                    outerRule.setRulename(rulenumber.get(0).toString());
                }
            }
            outRules.add(outerRule);
        }
        return outRules;
    }

}
