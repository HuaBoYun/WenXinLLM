package com.huabo.es.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.huabo.es.domain.InnerRule;
import com.huabo.es.service.InnerRuleService;
import org.frameworkset.elasticsearch.boot.BBossESStarter;
import org.frameworkset.elasticsearch.client.ClientInterface;
import org.frameworkset.elasticsearch.entity.ESDatas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName : InnerRuleServiceImpl
 * @Description : TODO
 * @Author : zhibo.cao
 * @Date: 2023-03-15 18:22:22
 */
@Service
public class InnerRuleServiceImpl implements InnerRuleService {
    @Autowired
    private BBossESStarter bbossESStarter;
    private String mappath = "esmapper/ESRuleMapper.xml";

    @Override
    public ESDatas<InnerRule> getInnerRuleESDatas(String keyWord, Long num, Long size) {
        ClientInterface clientUtil = bbossESStarter.getConfigRestClient(mappath);
        // 内规查询 startPage * size
        Map<String, Object> inRuleParams = new HashMap<String, Object>();
        inRuleParams.put("keyword", keyWord);
        inRuleParams.put("from", (num - 1) * size);
        inRuleParams.put("size", size);
        // 分页获取内规,分页指定最大10000条
        ESDatas<InnerRule> inRuleEsData = clientUtil.searchList("innerrule/_search", "innerRuleQuery", inRuleParams, InnerRule.class);
        return inRuleEsData;
    }

    @Override
    public List<InnerRule> inRuletoHanlder(ESDatas<InnerRule> inRuleEsData) {
        List<InnerRule> inRules = CollUtil.newArrayList();
        List<InnerRule> datas = inRuleEsData.getDatas();
        if (CollUtil.isEmpty(datas)) {
            return inRules;
        }
        for (InnerRule innerRule : datas) {
            if (ObjectUtil.isNotEmpty(innerRule.getHighlight())) {
                Map<String, List<Object>> highlight = innerRule.getHighlight();
                List<Object> bodyinfo = highlight.get("bodyinfo");
                if (CollUtil.isNotEmpty(bodyinfo)) {
                    innerRule.setBodyinfo(bodyinfo.get(0).toString());
                }
                List<Object> rulecode = highlight.get("rulecode");
                if (CollUtil.isNotEmpty(rulecode)) {
                    innerRule.setRulecode(rulecode.get(0).toString());
                }
                List<Object> rulename = highlight.get("rulename");
                if (CollUtil.isNotEmpty(rulename)) {
                    innerRule.setRulename(rulename.get(0).toString());
                }
                List<Object> rulenumber = highlight.get("rulenumber");
                if (CollUtil.isNotEmpty(rulenumber)) {
                    innerRule.setRulename(rulenumber.get(0).toString());
                }
                List<Object> innruletype = highlight.get("innruletype");
                if (CollUtil.isNotEmpty(innruletype)) {
                    innerRule.setInnruletype(innruletype.get(0).toString());
                }
            }
            inRules.add(innerRule);
        }
        return inRules;
    }
}
