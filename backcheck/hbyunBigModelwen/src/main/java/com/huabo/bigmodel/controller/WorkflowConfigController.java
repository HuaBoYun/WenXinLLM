package com.huabo.bigmodel.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.huabo.bigmodel.common.Result;
import com.huabo.bigmodel.entity.AiWorkflowConfig;
import com.huabo.bigmodel.mapper.AiWorkflowConfigMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * AI办公工作流配置控制器
 * 业务人员在 AI 办公页画布上编排的工作流（触发关键字+步骤链），
 * 前端编辑器以"整份配置"为粒度同步：list 拉取、save-all 全量覆盖保存。
 */
@Slf4j
@RestController
@RequestMapping("/v1/ai/workflow-config")
@Tag(name = "AI办公工作流配置", description = "工作流(触发关键字+步骤链)的持久化管理接口")
public class WorkflowConfigController {

    /** 单用户工作流数量上限（防御性，正常个位数） */
    private static final int MAX_WORKFLOWS = 50;
    /** 单工作流步骤数量上限 */
    private static final int MAX_STEPS = 20;

    @Autowired
    private AiWorkflowConfigMapper workflowMapper;

    /**
     * 某用户的工作流清单（按更新时间倒序）
     */
    @GetMapping("/list")
    @Operation(summary = "获取工作流配置列表（按用户）")
    public Result<List<AiWorkflowConfig>> list(
            @RequestParam(required = false) String userId) {
        try {
            LambdaQueryWrapper<AiWorkflowConfig> wrapper =
                    new LambdaQueryWrapper<AiWorkflowConfig>()
                            .orderByDesc(AiWorkflowConfig::getUpdateTime);
            if (userId != null && !userId.trim().isEmpty()) {
                wrapper.eq(AiWorkflowConfig::getUserId, userId);
            }
            return Result.success(workflowMapper.selectList(wrapper));
        } catch (Exception e) {
            log.error("获取工作流配置列表失败", e);
            return Result.error("获取工作流列表失败: " + e.getMessage());
        }
    }

    /**
     * 全量保存（整份覆盖该用户的配置）：
     * 前端编辑器每次保存把当前全部工作流发来，服务端删旧插新一次完成。
     * id 沿用前端生成的稳定ID（画布坐标/步骤结构都在 steps JSON 里）。
     */
    @PostMapping("/save-all")
    @Operation(summary = "全量保存工作流配置（覆盖该用户全部配置）")
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> saveAll(@RequestBody JSONObject body) {
        try {
            String userId = body.getString("userId");
            if (userId == null || userId.trim().isEmpty()) {
                return Result.error("userId 不能为空");
            }
            String userName = body.getString("userName");
            JSONArray workflows = body.getJSONArray("workflows");
            if (workflows == null) {
                return Result.error("workflows 不能为空");
            }
            if (workflows.size() > MAX_WORKFLOWS) {
                return Result.error("工作流数量超过上限（" + MAX_WORKFLOWS + "）");
            }
            List<AiWorkflowConfig> entities = new ArrayList<>();
            Date now = new Date();
            for (int i = 0; i < workflows.size(); i++) {
                JSONObject w = workflows.getJSONObject(i);
                if (w == null) continue;
                String name = w.getString("name");
                if (name == null || name.trim().isEmpty()) {
                    return Result.error("第 " + (i + 1) + " 个工作流缺少名称");
                }
                JSONArray steps = w.getJSONArray("steps");
                if (steps == null || steps.isEmpty()) {
                    return Result.error("工作流「" + name + "」至少需要一个步骤");
                }
                if (steps.size() > MAX_STEPS) {
                    return Result.error("工作流「" + name + "」步骤数超过上限（" + MAX_STEPS + "）");
                }
                AiWorkflowConfig entity = new AiWorkflowConfig();
                entity.setId(w.getString("id"));
                entity.setUserId(userId.trim());
                entity.setUserName(userName);
                entity.setName(name.trim());
                Boolean enabled = w.getBoolean("enabled");
                entity.setEnabled(enabled == null || enabled ? 1 : 0);
                JSONArray keywords = w.getJSONArray("keywords");
                entity.setKeywords(keywords == null ? "[]" : JSON.toJSONString(keywords));
                entity.setSteps(JSON.toJSONString(steps));
                entity.setCreateTime(now);
                entity.setUpdateTime(now);
                entities.add(entity);
            }
            // 整份覆盖：先清该用户旧配置再插入（事务内，失败整体回滚）
            workflowMapper.delete(new LambdaQueryWrapper<AiWorkflowConfig>()
                    .eq(AiWorkflowConfig::getUserId, userId.trim()));
            for (AiWorkflowConfig entity : entities) {
                workflowMapper.insert(entity);
            }
            log.info("工作流配置已保存: userId={}, count={}", userId, entities.size());
            return Result.success();
        } catch (Exception e) {
            log.error("保存工作流配置失败", e);
            return Result.error("保存失败: " + e.getMessage());
        }
    }

    /**
     * 删除单个工作流
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除单个工作流")
    public Result<Void> delete(@PathVariable String id) {
        try {
            workflowMapper.deleteById(id);
            return Result.success();
        } catch (Exception e) {
            log.error("删除工作流失败", e);
            return Result.error("删除失败: " + e.getMessage());
        }
    }
}
