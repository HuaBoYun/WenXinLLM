package com.huabo.bigmodel.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.huabo.bigmodel.entity.BusinessTemplate;
import com.huabo.bigmodel.mapper.BusinessTemplateMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 业务梳理模板初始化器
 * 启动时检测 ai_business_template 是否为空，若为空则插入 18 条系统模板元数据
 * （仅写入元数据，文档内容由前端首次访问时调用 /doc/seed 接口写入）
 */
@Slf4j
@Component
@Order(100)
public class BusinessTemplateInitializer implements ApplicationRunner {

    @Autowired
    private BusinessTemplateMapper templateMapper;

    @Override
    public void run(ApplicationArguments args) {
        try {
            Long count = templateMapper.selectCount(new LambdaQueryWrapper<>());
            if (count != null && count > 0) {
                log.info("[BusinessTemplate] 已存在 {} 条模板，跳过初始化", count);
                return;
            }
            log.info("[BusinessTemplate] 模板表为空，开始初始化系统模板元数据...");
            List<BusinessTemplate> seeds = buildSystemSeeds();
            Date now = new Date();
            int idx = 0;
            for (BusinessTemplate t : seeds) {
                t.setIsSystem(1);
                t.setStatus(1);
                t.setSortNo(++idx);
                t.setCreateTime(now);
                t.setUpdateTime(now);
                templateMapper.insert(t);
            }
            log.info("[BusinessTemplate] 系统模板初始化完成，共 {} 条", seeds.size());
        } catch (Exception e) {
            log.error("[BusinessTemplate] 初始化失败（可忽略：表可能尚未创建）: {}", e.getMessage());
        }
    }

    private List<BusinessTemplate> buildSystemSeeds() {
        // 与前端 public/business-docs/index.json 对齐
        List<BusinessTemplate> list = new ArrayList<>();
        list.add(seed("biz_tpl_doc01", "01", "采购管理流程",         "内控方案 · 采购循环",  "01-caigou.html"));
        list.add(seed("biz_tpl_doc02", "02", "销售管理流程",         "内控方案 · 销售循环",  "02-xiaoshou.html"));
        list.add(seed("biz_tpl_doc03", "03", "合同管理流程",         "内控方案 · 合同管理",  "03-hetong.html"));
        list.add(seed("biz_tpl_doc04", "04", "业务外包管理流程",     "内控方案 · 外包管理",  "04-waibao.html"));
        list.add(seed("biz_tpl_doc05", "05", "资金管理流程",         "内控方案 · 资金活动",  "05-zijin.html"));
        list.add(seed("biz_tpl_doc06", "06", "资产管理流程（存货）", "内控方案 · 资产存货",  "06-zichan.html"));
        list.add(seed("biz_tpl_doc07", "07", "财务报告流程",         "内控方案 · 财务报告",  "07-caiwu.html"));
        list.add(seed("biz_tpl_doc08", "08", "全面预算流程",         "内控方案 · 全面预算",  "08-yusuan.html"));
        list.add(seed("biz_tpl_doc09", "09", "担保合同管理流程",     "内控方案 · 担保业务",  "09-danbao.html"));
        list.add(seed("biz_tpl_doc10", "10", "组织架构流程",         "内控方案 · 组织架构",  "10-zuzhi.html"));
        list.add(seed("biz_tpl_doc11", "11", "人力资源流程",         "内控方案 · 人力资源",  "11-renli.html"));
        list.add(seed("biz_tpl_doc12", "12", "研究与开发管理流程",   "内控方案 · 研发管理",  "12-yanfa.html"));
        list.add(seed("biz_tpl_doc13", "13", "工程项目管理流程",     "内控方案 · 工程项目",  "13-gongcheng.html"));
        list.add(seed("biz_tpl_doc14", "14", "发展战略流程",         "内控方案 · 发展战略",  "14-zhanlve.html"));
        list.add(seed("biz_tpl_doc15", "15", "内部信息传递流程",     "内控方案 · 信息传递",  "15-xinxi.html"));
        list.add(seed("biz_tpl_doc16", "16", "企业文化流程",         "内控方案 · 企业文化",  "16-wenhua.html"));
        list.add(seed("biz_tpl_doc17", "17", "社会责任流程",         "内控方案 · 社会责任",  "17-shehui.html"));
        list.add(seed("biz_tpl_doc18", "18", "信息系统流程",         "内控方案 · 信息系统",  "18-xitong.html"));
        return list;
    }

    private BusinessTemplate seed(String id, String no, String title, String subtitle, String file) {
        BusinessTemplate t = new BusinessTemplate();
        t.setId(id);
        t.setTemplateNo(no);
        t.setTitle(title);
        t.setSubtitle(subtitle);
        t.setSourceFile(file);
        return t;
    }
}

