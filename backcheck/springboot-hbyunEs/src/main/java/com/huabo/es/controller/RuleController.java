package com.huabo.es.controller;

import java.util.List;

import javax.annotation.Resource;

import org.frameworkset.elasticsearch.entity.ESDatas;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.huabo.es.domain.InnerRule;
import com.huabo.es.domain.OuterRule;
import com.huabo.es.domain.RuleEntity;
import com.huabo.es.service.InnerRuleService;
import com.huabo.es.service.OuterRuleService;
import com.huabo.es.utils.page.TableDataInfo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * @ClassName : RuleController
 * @Description : 内外规则接口
 * @Author : zhibo.cao
 * @Date: 2023-03-07 21:57:11
 */
@Tag(name="内外规则es接口",description="内外规则es接口")
@RestController
@RequestMapping("/es/rule")
public class RuleController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(RuleController.class);

    @Resource
    private OuterRuleService outerRuleService;
    @Resource
    private InnerRuleService innerRuleService;

    /**
     * 外轨分页查询
     */
    @Operation(summary = "外轨分页查询")
    @PostMapping("/outDataByPage")
    public TableDataInfo outDataByPage(@RequestBody RuleEntity rule) {
        List<OuterRule> outRules = CollUtil.newArrayList();
        if (StrUtil.isEmpty(rule.getKeyWord())) {
            return getDataTable(outRules);
        }
        if (rule.getPageNum() == null || rule.getPageNum() == 0L) {
            rule.setPageNum(1L);
        }
        if (rule.getPageSize() == null) {
            rule.setPageSize(20L);
        }
        ESDatas<OuterRule> outRuleEsData = outerRuleService.getOuterRuleESDatas(rule.getKeyWord(), rule.getPageNum(), rule.getPageSize());
        outRules = outerRuleService.outRuleHandler(outRuleEsData);

        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(HttpStatus.OK.value());
        rspData.setMsg("查询成功");
        rspData.setRows(outRules);
        rspData.setTotal(outRuleEsData.getTotalSize());
        return rspData;
    }


    /**
     * 内轨分页查询
     */
    @Operation(summary = "内轨分页查询")
    @PostMapping("/inDataByPage")
    public TableDataInfo inDataByPage(@RequestBody RuleEntity rule) {
        List<InnerRule> inRules = CollUtil.newArrayList();
        if (StrUtil.isEmpty(rule.getKeyWord())) {
            return getDataTable(inRules);
        }
        if (rule.getPageNum() == null || rule.getPageNum() == 0L) {
            rule.setPageNum(1L);
        }
        if (rule.getPageSize() == null) {
            rule.setPageSize(20L);
        }

        ESDatas<InnerRule> inRuleEsData = innerRuleService.getInnerRuleESDatas(rule.getKeyWord(), rule.getPageNum(), rule.getPageSize());
        inRules = innerRuleService.inRuletoHanlder(inRuleEsData);

        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(HttpStatus.OK.value());
        rspData.setMsg("查询成功");
        rspData.setRows(inRules);
        rspData.setTotal(inRuleEsData.getTotalSize());
        return rspData;
    }


}
