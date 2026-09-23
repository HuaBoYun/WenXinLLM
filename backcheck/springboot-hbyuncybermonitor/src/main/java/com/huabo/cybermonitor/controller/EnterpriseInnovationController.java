package com.huabo.cybermonitor.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.cybermonitor.entity.*;
import com.huabo.cybermonitor.mapper.*;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.util.R;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.*;
import io.swagger.v3.oas.annotations.Operation;

@Tag(name = "企业创新管理", description = "研发项目、知识产权、技术合作、成果转化、创新团队、技术评估")
@RestController
@RequestMapping("/v1/enterprise/innovation")
@Slf4j
public class EnterpriseInnovationController {

    @Autowired private GzctRdProjectMapper rdProjectMapper;
    @Autowired private GzctPatentMapper patentMapper;
    @Autowired private GzctTechCooperationMapper techCooperationMapper;
    @Autowired private GzctAchievementTransformationMapper achievementMapper;
    @Autowired private GzctInnovationTeamMapper innovationTeamMapper;
    @Autowired private GzctTechEvaluationMapper techEvaluationMapper;

    // ==================== 研发项目管理 ====================

    @Operation(summary = "projectList")
    @PostMapping("/project/list")
    public R<PageResult<GzctRdProject>> projectList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            LambdaQueryWrapper<GzctRdProject> w = new LambdaQueryWrapper<>();
            if (params.get("enterpriseId") != null && StringUtils.isNotBlank(params.get("enterpriseId").toString())) w.eq(GzctRdProject::getEnterpriseId, params.get("enterpriseId").toString());
            if (params.get("projectNo") != null && StringUtils.isNotBlank(params.get("projectNo").toString())) w.like(GzctRdProject::getProjectNo, params.get("projectNo").toString());
            if (params.get("projectName") != null && StringUtils.isNotBlank(params.get("projectName").toString())) w.like(GzctRdProject::getProjectName, params.get("projectName").toString());
            if (params.get("projectType") != null && StringUtils.isNotBlank(params.get("projectType").toString())) w.eq(GzctRdProject::getProjectType, params.get("projectType").toString());
            if (params.get("status") != null && StringUtils.isNotBlank(params.get("status").toString())) w.eq(GzctRdProject::getStatus, params.get("status").toString());
            if (params.get("leader") != null && StringUtils.isNotBlank(params.get("leader").toString())) w.like(GzctRdProject::getLeader, params.get("leader").toString());
            if (params.get("priority") != null && StringUtils.isNotBlank(params.get("priority").toString())) w.eq(GzctRdProject::getPriority, params.get("priority").toString());
            w.orderByDesc(GzctRdProject::getCreateTime);
            Page<GzctRdProject> page = rdProjectMapper.selectPage(new Page<>(pn, ps), w);
            return R.success(PageResult.of(page));
        } catch (Exception e) { log.error("查询研发项目列表失败", e); return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "projectDetail")
    @GetMapping("/project/{id}")
    public R<GzctRdProject> projectDetail(@PathVariable String id) { try { return R.success(rdProjectMapper.selectById(id)); } catch (Exception e) { return R.fail("查询失败"); } }

    @Operation(summary = "新增")
    @PostMapping("/project")
    public R<Boolean> addProject(@RequestBody GzctRdProject record) { try { record.setCreateTime(LocalDateTime.now()); rdProjectMapper.insert(record); return R.success(true); } catch (Exception e) { return R.fail("新增失败：" + e.getMessage()); } }

    @Operation(summary = "更新")
    @PutMapping("/project/{id}")
    public R<Boolean> updateProject(@PathVariable String id, @RequestBody GzctRdProject record) { try { record.setId(id); record.setUpdateTime(LocalDateTime.now()); rdProjectMapper.updateById(record); return R.success(true); } catch (Exception e) { return R.fail("更新失败：" + e.getMessage()); } }

    @Operation(summary = "删除")
    @DeleteMapping("/project/{id}")
    public R<Boolean> deleteProject(@PathVariable String id) { try { return R.success(rdProjectMapper.deleteById(id) > 0); } catch (Exception e) { return R.fail("删除失败"); } }

    @Operation(summary = "批量操作")
    @PostMapping("/project/batch/delete")
    public R<Boolean> batchDeleteProject(@RequestBody Map<String, Object> params) { try { List<String> ids = (List<String>) params.get("ids"); if (ids == null || ids.isEmpty()) return R.fail("请选择要删除的记录"); rdProjectMapper.deleteBatchIds(ids); return R.success(true); } catch (Exception e) { return R.fail("批量删除失败：" + e.getMessage()); } }

    // ==================== 知识产权管理 ====================

    @Operation(summary = "patentList")
    @PostMapping("/patent/list")
    public R<PageResult<GzctPatent>> patentList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            LambdaQueryWrapper<GzctPatent> w = new LambdaQueryWrapper<>();
            if (params.get("enterpriseId") != null && StringUtils.isNotBlank(params.get("enterpriseId").toString())) w.eq(GzctPatent::getEnterpriseId, params.get("enterpriseId").toString());
            if (params.get("patentNo") != null && StringUtils.isNotBlank(params.get("patentNo").toString())) w.like(GzctPatent::getPatentNo, params.get("patentNo").toString());
            if (params.get("patentName") != null && StringUtils.isNotBlank(params.get("patentName").toString())) w.like(GzctPatent::getPatentName, params.get("patentName").toString());
            if (params.get("patentType") != null && StringUtils.isNotBlank(params.get("patentType").toString())) w.eq(GzctPatent::getPatentType, params.get("patentType").toString());
            if (params.get("status") != null && StringUtils.isNotBlank(params.get("status").toString())) w.eq(GzctPatent::getStatus, params.get("status").toString());
            if (params.get("inventor") != null && StringUtils.isNotBlank(params.get("inventor").toString())) w.like(GzctPatent::getInventor, params.get("inventor").toString());
            w.orderByDesc(GzctPatent::getCreateTime);
            Page<GzctPatent> page = patentMapper.selectPage(new Page<>(pn, ps), w);
            return R.success(PageResult.of(page));
        } catch (Exception e) { log.error("查询知识产权列表失败", e); return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "patentDetail")
    @GetMapping("/patent/{id}")
    public R<GzctPatent> patentDetail(@PathVariable String id) { try { return R.success(patentMapper.selectById(id)); } catch (Exception e) { return R.fail("查询失败"); } }

    @Operation(summary = "新增")
    @PostMapping("/patent")
    public R<Boolean> addPatent(@RequestBody GzctPatent record) { try { record.setCreateTime(LocalDateTime.now()); patentMapper.insert(record); return R.success(true); } catch (Exception e) { return R.fail("新增失败：" + e.getMessage()); } }

    @Operation(summary = "更新")
    @PutMapping("/patent/{id}")
    public R<Boolean> updatePatent(@PathVariable String id, @RequestBody GzctPatent record) { try { record.setId(id); record.setUpdateTime(LocalDateTime.now()); patentMapper.updateById(record); return R.success(true); } catch (Exception e) { return R.fail("更新失败：" + e.getMessage()); } }

    @Operation(summary = "删除")
    @DeleteMapping("/patent/{id}")
    public R<Boolean> deletePatent(@PathVariable String id) { try { return R.success(patentMapper.deleteById(id) > 0); } catch (Exception e) { return R.fail("删除失败"); } }

    @Operation(summary = "批量操作")
    @PostMapping("/patent/batch/delete")
    public R<Boolean> batchDeletePatent(@RequestBody Map<String, Object> params) { try { List<String> ids = (List<String>) params.get("ids"); if (ids == null || ids.isEmpty()) return R.fail("请选择要删除的记录"); patentMapper.deleteBatchIds(ids); return R.success(true); } catch (Exception e) { return R.fail("批量删除失败：" + e.getMessage()); } }

    // ==================== 技术合作管理 ====================

    @Operation(summary = "cooperationList")
    @PostMapping("/cooperation/list")
    public R<PageResult<GzctTechCooperation>> cooperationList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            LambdaQueryWrapper<GzctTechCooperation> w = new LambdaQueryWrapper<>();
            if (params.get("enterpriseId") != null && StringUtils.isNotBlank(params.get("enterpriseId").toString())) w.eq(GzctTechCooperation::getEnterpriseId, params.get("enterpriseId").toString());
            if (params.get("cooperationNo") != null && StringUtils.isNotBlank(params.get("cooperationNo").toString())) w.like(GzctTechCooperation::getCooperationNo, params.get("cooperationNo").toString());
            if (params.get("partner") != null && StringUtils.isNotBlank(params.get("partner").toString())) w.like(GzctTechCooperation::getPartner, params.get("partner").toString());
            if (params.get("cooperationType") != null && StringUtils.isNotBlank(params.get("cooperationType").toString())) w.eq(GzctTechCooperation::getCooperationType, params.get("cooperationType").toString());
            if (params.get("status") != null && StringUtils.isNotBlank(params.get("status").toString())) w.eq(GzctTechCooperation::getStatus, params.get("status").toString());
            if (params.get("manager") != null && StringUtils.isNotBlank(params.get("manager").toString())) w.like(GzctTechCooperation::getManager, params.get("manager").toString());
            w.orderByDesc(GzctTechCooperation::getCreateTime);
            Page<GzctTechCooperation> page = techCooperationMapper.selectPage(new Page<>(pn, ps), w);
            return R.success(PageResult.of(page));
        } catch (Exception e) { log.error("查询技术合作列表失败", e); return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "cooperationDetail")
    @GetMapping("/cooperation/{id}")
    public R<GzctTechCooperation> cooperationDetail(@PathVariable String id) { try { return R.success(techCooperationMapper.selectById(id)); } catch (Exception e) { return R.fail("查询失败"); } }

    @Operation(summary = "新增")
    @PostMapping("/cooperation")
    public R<Boolean> addCooperation(@RequestBody GzctTechCooperation record) { try { record.setCreateTime(LocalDateTime.now()); techCooperationMapper.insert(record); return R.success(true); } catch (Exception e) { return R.fail("新增失败：" + e.getMessage()); } }

    @Operation(summary = "更新")
    @PutMapping("/cooperation/{id}")
    public R<Boolean> updateCooperation(@PathVariable String id, @RequestBody GzctTechCooperation record) { try { record.setId(id); record.setUpdateTime(LocalDateTime.now()); techCooperationMapper.updateById(record); return R.success(true); } catch (Exception e) { return R.fail("更新失败：" + e.getMessage()); } }

    @Operation(summary = "删除")
    @DeleteMapping("/cooperation/{id}")
    public R<Boolean> deleteCooperation(@PathVariable String id) { try { return R.success(techCooperationMapper.deleteById(id) > 0); } catch (Exception e) { return R.fail("删除失败"); } }

    @Operation(summary = "批量操作")
    @PostMapping("/cooperation/batch/delete")
    public R<Boolean> batchDeleteCooperation(@RequestBody Map<String, Object> params) { try { List<String> ids = (List<String>) params.get("ids"); if (ids == null || ids.isEmpty()) return R.fail("请选择要删除的记录"); techCooperationMapper.deleteBatchIds(ids); return R.success(true); } catch (Exception e) { return R.fail("批量删除失败：" + e.getMessage()); } }

    // ==================== 成果转化管理 ====================

    @Operation(summary = "achievementList")
    @PostMapping("/achievement/list")
    public R<PageResult<GzctAchievementTransformation>> achievementList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            LambdaQueryWrapper<GzctAchievementTransformation> w = new LambdaQueryWrapper<>();
            if (params.get("enterpriseId") != null && StringUtils.isNotBlank(params.get("enterpriseId").toString())) w.eq(GzctAchievementTransformation::getEnterpriseId, params.get("enterpriseId").toString());
            if (params.get("achievementNo") != null && StringUtils.isNotBlank(params.get("achievementNo").toString())) w.like(GzctAchievementTransformation::getAchievementNo, params.get("achievementNo").toString());
            if (params.get("achievementName") != null && StringUtils.isNotBlank(params.get("achievementName").toString())) w.like(GzctAchievementTransformation::getAchievementName, params.get("achievementName").toString());
            if (params.get("achievementType") != null && StringUtils.isNotBlank(params.get("achievementType").toString())) w.eq(GzctAchievementTransformation::getAchievementType, params.get("achievementType").toString());
            if (params.get("status") != null && StringUtils.isNotBlank(params.get("status").toString())) w.eq(GzctAchievementTransformation::getStatus, params.get("status").toString());
            if (params.get("manager") != null && StringUtils.isNotBlank(params.get("manager").toString())) w.like(GzctAchievementTransformation::getManager, params.get("manager").toString());
            w.orderByDesc(GzctAchievementTransformation::getCreateTime);
            Page<GzctAchievementTransformation> page = achievementMapper.selectPage(new Page<>(pn, ps), w);
            return R.success(PageResult.of(page));
        } catch (Exception e) { log.error("查询成果转化列表失败", e); return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "achievementDetail")
    @GetMapping("/achievement/{id}")
    public R<GzctAchievementTransformation> achievementDetail(@PathVariable String id) { try { return R.success(achievementMapper.selectById(id)); } catch (Exception e) { return R.fail("查询失败"); } }

    @Operation(summary = "新增")
    @PostMapping("/achievement")
    public R<Boolean> addAchievement(@RequestBody GzctAchievementTransformation record) { try { record.setCreateTime(LocalDateTime.now()); achievementMapper.insert(record); return R.success(true); } catch (Exception e) { return R.fail("新增失败：" + e.getMessage()); } }

    @Operation(summary = "更新")
    @PutMapping("/achievement/{id}")
    public R<Boolean> updateAchievement(@PathVariable String id, @RequestBody GzctAchievementTransformation record) { try { record.setId(id); record.setUpdateTime(LocalDateTime.now()); achievementMapper.updateById(record); return R.success(true); } catch (Exception e) { return R.fail("更新失败：" + e.getMessage()); } }

    @Operation(summary = "删除")
    @DeleteMapping("/achievement/{id}")
    public R<Boolean> deleteAchievement(@PathVariable String id) { try { return R.success(achievementMapper.deleteById(id) > 0); } catch (Exception e) { return R.fail("删除失败"); } }

    @Operation(summary = "批量操作")
    @PostMapping("/achievement/batch/delete")
    public R<Boolean> batchDeleteAchievement(@RequestBody Map<String, Object> params) { try { List<String> ids = (List<String>) params.get("ids"); if (ids == null || ids.isEmpty()) return R.fail("请选择要删除的记录"); achievementMapper.deleteBatchIds(ids); return R.success(true); } catch (Exception e) { return R.fail("批量删除失败：" + e.getMessage()); } }

    // ==================== 创新团队管理 ====================

    @Operation(summary = "teamList")
    @PostMapping("/team/list")
    public R<PageResult<GzctInnovationTeam>> teamList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            LambdaQueryWrapper<GzctInnovationTeam> w = new LambdaQueryWrapper<>();
            if (params.get("enterpriseId") != null && StringUtils.isNotBlank(params.get("enterpriseId").toString())) w.eq(GzctInnovationTeam::getEnterpriseId, params.get("enterpriseId").toString());
            if (params.get("teamNo") != null && StringUtils.isNotBlank(params.get("teamNo").toString())) w.like(GzctInnovationTeam::getTeamNo, params.get("teamNo").toString());
            if (params.get("teamName") != null && StringUtils.isNotBlank(params.get("teamName").toString())) w.like(GzctInnovationTeam::getTeamName, params.get("teamName").toString());
            if (params.get("researchDirection") != null && StringUtils.isNotBlank(params.get("researchDirection").toString())) w.eq(GzctInnovationTeam::getResearchDirection, params.get("researchDirection").toString());
            if (params.get("status") != null && StringUtils.isNotBlank(params.get("status").toString())) w.eq(GzctInnovationTeam::getStatus, params.get("status").toString());
            if (params.get("leader") != null && StringUtils.isNotBlank(params.get("leader").toString())) w.like(GzctInnovationTeam::getLeader, params.get("leader").toString());
            w.orderByDesc(GzctInnovationTeam::getCreateTime);
            Page<GzctInnovationTeam> page = innovationTeamMapper.selectPage(new Page<>(pn, ps), w);
            return R.success(PageResult.of(page));
        } catch (Exception e) { log.error("查询创新团队列表失败", e); return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "teamDetail")
    @GetMapping("/team/{id}")
    public R<GzctInnovationTeam> teamDetail(@PathVariable String id) { try { return R.success(innovationTeamMapper.selectById(id)); } catch (Exception e) { return R.fail("查询失败"); } }

    @Operation(summary = "新增")
    @PostMapping("/team")
    public R<Boolean> addTeam(@RequestBody GzctInnovationTeam record) { try { record.setCreateTime(LocalDateTime.now()); innovationTeamMapper.insert(record); return R.success(true); } catch (Exception e) { return R.fail("新增失败：" + e.getMessage()); } }

    @Operation(summary = "更新")
    @PutMapping("/team/{id}")
    public R<Boolean> updateTeam(@PathVariable String id, @RequestBody GzctInnovationTeam record) { try { record.setId(id); record.setUpdateTime(LocalDateTime.now()); innovationTeamMapper.updateById(record); return R.success(true); } catch (Exception e) { return R.fail("更新失败：" + e.getMessage()); } }

    @Operation(summary = "删除")
    @DeleteMapping("/team/{id}")
    public R<Boolean> deleteTeam(@PathVariable String id) { try { return R.success(innovationTeamMapper.deleteById(id) > 0); } catch (Exception e) { return R.fail("删除失败"); } }

    @Operation(summary = "批量操作")
    @PostMapping("/team/batch/delete")
    public R<Boolean> batchDeleteTeam(@RequestBody Map<String, Object> params) { try { List<String> ids = (List<String>) params.get("ids"); if (ids == null || ids.isEmpty()) return R.fail("请选择要删除的记录"); innovationTeamMapper.deleteBatchIds(ids); return R.success(true); } catch (Exception e) { return R.fail("批量删除失败：" + e.getMessage()); } }

    // ==================== 技术评估管理 ====================

    @Operation(summary = "evaluationList")
    @PostMapping("/evaluation/list")
    public R<PageResult<GzctTechEvaluation>> evaluationList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            LambdaQueryWrapper<GzctTechEvaluation> w = new LambdaQueryWrapper<>();
            if (params.get("enterpriseId") != null && StringUtils.isNotBlank(params.get("enterpriseId").toString())) w.eq(GzctTechEvaluation::getEnterpriseId, params.get("enterpriseId").toString());
            if (params.get("evaluationNo") != null && StringUtils.isNotBlank(params.get("evaluationNo").toString())) w.like(GzctTechEvaluation::getEvaluationNo, params.get("evaluationNo").toString());
            if (params.get("techName") != null && StringUtils.isNotBlank(params.get("techName").toString())) w.like(GzctTechEvaluation::getTechName, params.get("techName").toString());
            if (params.get("evaluationType") != null && StringUtils.isNotBlank(params.get("evaluationType").toString())) w.eq(GzctTechEvaluation::getEvaluationType, params.get("evaluationType").toString());
            if (params.get("status") != null && StringUtils.isNotBlank(params.get("status").toString())) w.eq(GzctTechEvaluation::getStatus, params.get("status").toString());
            if (params.get("evaluator") != null && StringUtils.isNotBlank(params.get("evaluator").toString())) w.like(GzctTechEvaluation::getEvaluator, params.get("evaluator").toString());
            w.orderByDesc(GzctTechEvaluation::getCreateTime);
            Page<GzctTechEvaluation> page = techEvaluationMapper.selectPage(new Page<>(pn, ps), w);
            return R.success(PageResult.of(page));
        } catch (Exception e) { log.error("查询技术评估列表失败", e); return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "evaluationDetail")
    @GetMapping("/evaluation/{id}")
    public R<GzctTechEvaluation> evaluationDetail(@PathVariable String id) { try { return R.success(techEvaluationMapper.selectById(id)); } catch (Exception e) { return R.fail("查询失败"); } }

    @Operation(summary = "新增")
    @PostMapping("/evaluation")
    public R<Boolean> addEvaluation(@RequestBody GzctTechEvaluation record) { try { record.setCreateTime(LocalDateTime.now()); techEvaluationMapper.insert(record); return R.success(true); } catch (Exception e) { return R.fail("新增失败：" + e.getMessage()); } }

    @Operation(summary = "更新")
    @PutMapping("/evaluation/{id}")
    public R<Boolean> updateEvaluation(@PathVariable String id, @RequestBody GzctTechEvaluation record) { try { record.setId(id); record.setUpdateTime(LocalDateTime.now()); techEvaluationMapper.updateById(record); return R.success(true); } catch (Exception e) { return R.fail("更新失败：" + e.getMessage()); } }

    @Operation(summary = "删除")
    @DeleteMapping("/evaluation/{id}")
    public R<Boolean> deleteEvaluation(@PathVariable String id) { try { return R.success(techEvaluationMapper.deleteById(id) > 0); } catch (Exception e) { return R.fail("删除失败"); } }

    @Operation(summary = "批量操作")
    @PostMapping("/evaluation/batch/delete")
    public R<Boolean> batchDeleteEvaluation(@RequestBody Map<String, Object> params) { try { List<String> ids = (List<String>) params.get("ids"); if (ids == null || ids.isEmpty()) return R.fail("请选择要删除的记录"); techEvaluationMapper.deleteBatchIds(ids); return R.success(true); } catch (Exception e) { return R.fail("批量删除失败：" + e.getMessage()); } }

    // ==================== 统计接口 ====================

    @Operation(summary = "")
    @GetMapping("/statistics")
    public R<Map<String, Object>> statistics() {
        try {
            Map<String, Object> stats = new HashMap<>();
            // 在研项目数
            long activeProjects = rdProjectMapper.selectCount(new LambdaQueryWrapper<GzctRdProject>().eq(GzctRdProject::getStatus, "进行中"));
            stats.put("activeProjects", activeProjects);
            // 专利总数
            long totalPatents = patentMapper.selectCount(new LambdaQueryWrapper<GzctPatent>().ne(GzctPatent::getStatus, "已失效"));
            stats.put("totalPatents", totalPatents);
            // 研发投入(万)
            List<GzctRdProject> projects = rdProjectMapper.selectList(null);
            BigDecimal rdInvestment = projects.stream().filter(p -> p.getBudget() != null).map(GzctRdProject::getBudget).reduce(BigDecimal.ZERO, BigDecimal::add);
            stats.put("rdInvestment", rdInvestment);
            // 成果转化数
            long achievements = achievementMapper.selectCount(new LambdaQueryWrapper<GzctAchievementTransformation>().eq(GzctAchievementTransformation::getStatus, "已转化"));
            stats.put("achievements", achievements);
            // 额外统计
            stats.put("totalProjects", rdProjectMapper.selectCount(null));
            stats.put("authorizedPatents", patentMapper.selectCount(new LambdaQueryWrapper<GzctPatent>().eq(GzctPatent::getStatus, "已授权")));
            stats.put("cooperationCount", techCooperationMapper.selectCount(new LambdaQueryWrapper<GzctTechCooperation>().eq(GzctTechCooperation::getStatus, "执行中")));
            stats.put("teamCount", innovationTeamMapper.selectCount(new LambdaQueryWrapper<GzctInnovationTeam>().eq(GzctInnovationTeam::getStatus, "活跃").or().eq(GzctInnovationTeam::getStatus, "项目中")));
            stats.put("evaluationCount", techEvaluationMapper.selectCount(new LambdaQueryWrapper<GzctTechEvaluation>().eq(GzctTechEvaluation::getStatus, "已完成")));
            return R.success(stats);
        } catch (Exception e) { log.error("查询创新统计失败", e); return R.fail("统计查询失败：" + e.getMessage()); }
    }

    // ==================== 导出接口 ====================

    @Operation(summary = "导出")
    @GetMapping("/project/export")
    public void exportProject(HttpServletResponse response) {
        try {
            List<GzctRdProject> list = rdProjectMapper.selectList(new LambdaQueryWrapper<GzctRdProject>().orderByDesc(GzctRdProject::getCreateTime));
            SXSSFWorkbook wb = new SXSSFWorkbook(100);
            SXSSFSheet sheet = wb.createSheet("研发项目");
            String[] headers = {"项目编号","项目名称","项目类型","负责人","团队规模","项目预算(万)","已用预算(万)","进度(%)","开始日期","预计结束","状态","优先级"};
            SXSSFRow headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) headerRow.createCell(i).setCellValue(headers[i]);
            for (int i = 0; i < list.size(); i++) {
                GzctRdProject item = list.get(i);
                SXSSFRow row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(item.getProjectNo() != null ? item.getProjectNo() : "");
                row.createCell(1).setCellValue(item.getProjectName() != null ? item.getProjectName() : "");
                row.createCell(2).setCellValue(item.getProjectType() != null ? item.getProjectType() : "");
                row.createCell(3).setCellValue(item.getLeader() != null ? item.getLeader() : "");
                row.createCell(4).setCellValue(item.getTeamSize() != null ? item.getTeamSize() : 0);
                row.createCell(5).setCellValue(item.getBudget() != null ? item.getBudget().toString() : "0");
                row.createCell(6).setCellValue(item.getUsedBudget() != null ? item.getUsedBudget().toString() : "0");
                row.createCell(7).setCellValue(item.getProgress() != null ? item.getProgress() : 0);
                row.createCell(8).setCellValue(item.getStartDate() != null ? item.getStartDate().toString() : "");
                row.createCell(9).setCellValue(item.getEndDate() != null ? item.getEndDate().toString() : "");
                row.createCell(10).setCellValue(item.getStatus() != null ? item.getStatus() : "");
                row.createCell(11).setCellValue(item.getPriority() != null ? item.getPriority() : "");
            }
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("研发项目数据.xlsx", "UTF-8"));
            wb.write(response.getOutputStream());
            wb.close();
        } catch (Exception e) { log.error("导出研发项目失败", e); }
    }

    @Operation(summary = "导出")
    @GetMapping("/patent/export")
    public void exportPatent(HttpServletResponse response) {
        try {
            List<GzctPatent> list = patentMapper.selectList(new LambdaQueryWrapper<GzctPatent>().orderByDesc(GzctPatent::getCreateTime));
            SXSSFWorkbook wb = new SXSSFWorkbook(100);
            SXSSFSheet sheet = wb.createSheet("知识产权");
            String[] headers = {"专利号","专利名称","专利类型","发明人","申请人","申请日期","公开日期","授权日期","有效期","状态","维护费用"};
            SXSSFRow headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) headerRow.createCell(i).setCellValue(headers[i]);
            for (int i = 0; i < list.size(); i++) {
                GzctPatent item = list.get(i);
                SXSSFRow row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(item.getPatentNo() != null ? item.getPatentNo() : "");
                row.createCell(1).setCellValue(item.getPatentName() != null ? item.getPatentName() : "");
                row.createCell(2).setCellValue(item.getPatentType() != null ? item.getPatentType() : "");
                row.createCell(3).setCellValue(item.getInventor() != null ? item.getInventor() : "");
                row.createCell(4).setCellValue(item.getApplicant() != null ? item.getApplicant() : "");
                row.createCell(5).setCellValue(item.getApplicationDate() != null ? item.getApplicationDate().toString() : "");
                row.createCell(6).setCellValue(item.getPublicationDate() != null ? item.getPublicationDate().toString() : "");
                row.createCell(7).setCellValue(item.getAuthorizationDate() != null ? item.getAuthorizationDate().toString() : "");
                row.createCell(8).setCellValue(item.getValidityPeriod() != null ? item.getValidityPeriod() + "年" : "");
                row.createCell(9).setCellValue(item.getStatus() != null ? item.getStatus() : "");
                row.createCell(10).setCellValue(item.getMaintenanceFee() != null ? item.getMaintenanceFee().toString() : "0");
            }
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("知识产权数据.xlsx", "UTF-8"));
            wb.write(response.getOutputStream());
            wb.close();
        } catch (Exception e) { log.error("导出知识产权失败", e); }
    }

    @Operation(summary = "导出技术合作")
    @GetMapping("/cooperation/export")
    public void exportCooperation(HttpServletResponse response) {
        try {
            List<GzctTechCooperation> list = techCooperationMapper.selectList(new LambdaQueryWrapper<GzctTechCooperation>().orderByDesc(GzctTechCooperation::getCreateTime));
            SXSSFWorkbook wb = new SXSSFWorkbook(100);
            SXSSFSheet sheet = wb.createSheet("技术合作");
            String[] headers = {"合作编号","合作项目","合作伙伴","伙伴类型","合作类型","负责人","投资金额(万)","预期收益(万)","合作期限(年)","开始日期","结束日期","执行进度(%)","合作状态","风险等级"};
            SXSSFRow headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) headerRow.createCell(i).setCellValue(headers[i]);
            for (int i = 0; i < list.size(); i++) {
                GzctTechCooperation item = list.get(i);
                SXSSFRow row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(item.getCooperationNo() != null ? item.getCooperationNo() : "");
                row.createCell(1).setCellValue(item.getCooperationName() != null ? item.getCooperationName() : "");
                row.createCell(2).setCellValue(item.getPartner() != null ? item.getPartner() : "");
                row.createCell(3).setCellValue(item.getPartnerType() != null ? item.getPartnerType() : "");
                row.createCell(4).setCellValue(item.getCooperationType() != null ? item.getCooperationType() : "");
                row.createCell(5).setCellValue(item.getManager() != null ? item.getManager() : "");
                row.createCell(6).setCellValue(item.getInvestmentAmount() != null ? item.getInvestmentAmount().toString() : "0");
                row.createCell(7).setCellValue(item.getExpectedReturn() != null ? item.getExpectedReturn().toString() : "0");
                row.createCell(8).setCellValue(item.getCooperationPeriod() != null ? item.getCooperationPeriod() : 0);
                row.createCell(9).setCellValue(item.getStartDate() != null ? item.getStartDate().toString() : "");
                row.createCell(10).setCellValue(item.getEndDate() != null ? item.getEndDate().toString() : "");
                row.createCell(11).setCellValue(item.getProgress() != null ? item.getProgress() : 0);
                row.createCell(12).setCellValue(item.getStatus() != null ? item.getStatus() : "");
                row.createCell(13).setCellValue(item.getRiskLevel() != null ? item.getRiskLevel() : "");
            }
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("技术合作数据.xlsx", "UTF-8"));
            wb.write(response.getOutputStream());
            wb.close();
        } catch (Exception e) { log.error("导出技术合作失败", e); }
    }

    @Operation(summary = "导出成果转化")
    @GetMapping("/achievement/export")
    public void exportAchievement(HttpServletResponse response) {
        try {
            List<GzctAchievementTransformation> list = achievementMapper.selectList(new LambdaQueryWrapper<GzctAchievementTransformation>().orderByDesc(GzctAchievementTransformation::getCreateTime));
            SXSSFWorkbook wb = new SXSSFWorkbook(100);
            SXSSFSheet sheet = wb.createSheet("成果转化");
            String[] headers = {"成果编号","成果名称","成果类型","研发团队","转化负责人","技术水平","市场潜力","转化投资(万)","预期收益(万)","转化进度(%)","转化状态","投资回报率(%)"};
            SXSSFRow headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) headerRow.createCell(i).setCellValue(headers[i]);
            for (int i = 0; i < list.size(); i++) {
                GzctAchievementTransformation item = list.get(i);
                SXSSFRow row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(item.getAchievementNo() != null ? item.getAchievementNo() : "");
                row.createCell(1).setCellValue(item.getAchievementName() != null ? item.getAchievementName() : "");
                row.createCell(2).setCellValue(item.getAchievementType() != null ? item.getAchievementType() : "");
                row.createCell(3).setCellValue(item.getResearchTeam() != null ? item.getResearchTeam() : "");
                row.createCell(4).setCellValue(item.getManager() != null ? item.getManager() : "");
                row.createCell(5).setCellValue(item.getTechnologyLevel() != null ? item.getTechnologyLevel() : 0);
                row.createCell(6).setCellValue(item.getMarketPotential() != null ? item.getMarketPotential() : "");
                row.createCell(7).setCellValue(item.getInvestmentAmount() != null ? item.getInvestmentAmount().toString() : "0");
                row.createCell(8).setCellValue(item.getExpectedRevenue() != null ? item.getExpectedRevenue().toString() : "0");
                row.createCell(9).setCellValue(item.getTransformationProgress() != null ? item.getTransformationProgress() : 0);
                row.createCell(10).setCellValue(item.getStatus() != null ? item.getStatus() : "");
                row.createCell(11).setCellValue(item.getRoi() != null ? item.getRoi().toString() : "");
            }
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("成果转化数据.xlsx", "UTF-8"));
            wb.write(response.getOutputStream());
            wb.close();
        } catch (Exception e) { log.error("导出成果转化失败", e); }
    }

    @Operation(summary = "导出创新团队")
    @GetMapping("/team/export")
    public void exportTeam(HttpServletResponse response) {
        try {
            List<GzctInnovationTeam> list = innovationTeamMapper.selectList(new LambdaQueryWrapper<GzctInnovationTeam>().orderByDesc(GzctInnovationTeam::getCreateTime));
            SXSSFWorkbook wb = new SXSSFWorkbook(100);
            SXSSFSheet sheet = wb.createSheet("创新团队");
            String[] headers = {"团队编号","团队名称","研究方向","负责人","团队规模","高级人员","平均年龄","成立日期","在研项目","完成项目","团队效率(%)","创新指数","团队状态","年度预算(万)"};
            SXSSFRow headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) headerRow.createCell(i).setCellValue(headers[i]);
            for (int i = 0; i < list.size(); i++) {
                GzctInnovationTeam item = list.get(i);
                SXSSFRow row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(item.getTeamNo() != null ? item.getTeamNo() : "");
                row.createCell(1).setCellValue(item.getTeamName() != null ? item.getTeamName() : "");
                row.createCell(2).setCellValue(item.getResearchDirection() != null ? item.getResearchDirection() : "");
                row.createCell(3).setCellValue(item.getLeader() != null ? item.getLeader() : "");
                row.createCell(4).setCellValue(item.getMemberCount() != null ? item.getMemberCount() : 0);
                row.createCell(5).setCellValue(item.getSeniorCount() != null ? item.getSeniorCount() : 0);
                row.createCell(6).setCellValue(item.getAverageAge() != null ? item.getAverageAge() : 0);
                row.createCell(7).setCellValue(item.getEstablishDate() != null ? item.getEstablishDate().toString() : "");
                row.createCell(8).setCellValue(item.getCurrentProjects() != null ? item.getCurrentProjects() : 0);
                row.createCell(9).setCellValue(item.getCompletedProjects() != null ? item.getCompletedProjects() : 0);
                row.createCell(10).setCellValue(item.getTeamEfficiency() != null ? item.getTeamEfficiency() : 0);
                row.createCell(11).setCellValue(item.getInnovationScore() != null ? item.getInnovationScore() : 0);
                row.createCell(12).setCellValue(item.getStatus() != null ? item.getStatus() : "");
                row.createCell(13).setCellValue(item.getBudget() != null ? item.getBudget().toString() : "0");
            }
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("创新团队数据.xlsx", "UTF-8"));
            wb.write(response.getOutputStream());
            wb.close();
        } catch (Exception e) { log.error("导出创新团队失败", e); }
    }

    @Operation(summary = "导出技术评估")
    @GetMapping("/evaluation/export")
    public void exportEvaluation(HttpServletResponse response) {
        try {
            List<GzctTechEvaluation> list = techEvaluationMapper.selectList(new LambdaQueryWrapper<GzctTechEvaluation>().orderByDesc(GzctTechEvaluation::getCreateTime));
            SXSSFWorkbook wb = new SXSSFWorkbook(100);
            SXSSFSheet sheet = wb.createSheet("技术评估");
            String[] headers = {"评估编号","技术名称","评估类型","申请人","评估专家","评估日期","技术成熟度(%)","市场潜力","商业价值(万)","风险等级","综合评分","评估建议","评估状态"};
            SXSSFRow headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) headerRow.createCell(i).setCellValue(headers[i]);
            for (int i = 0; i < list.size(); i++) {
                GzctTechEvaluation item = list.get(i);
                SXSSFRow row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(item.getEvaluationNo() != null ? item.getEvaluationNo() : "");
                row.createCell(1).setCellValue(item.getTechName() != null ? item.getTechName() : "");
                row.createCell(2).setCellValue(item.getEvaluationType() != null ? item.getEvaluationType() : "");
                row.createCell(3).setCellValue(item.getApplicant() != null ? item.getApplicant() : "");
                row.createCell(4).setCellValue(item.getEvaluator() != null ? item.getEvaluator() : "");
                row.createCell(5).setCellValue(item.getEvaluationDate() != null ? item.getEvaluationDate().toString() : "");
                row.createCell(6).setCellValue(item.getTechMaturity() != null ? item.getTechMaturity() : 0);
                row.createCell(7).setCellValue(item.getMarketPotential() != null ? item.getMarketPotential() : 0);
                row.createCell(8).setCellValue(item.getCommercialValue() != null ? item.getCommercialValue().toString() : "0");
                row.createCell(9).setCellValue(item.getRiskLevel() != null ? item.getRiskLevel() : "");
                row.createCell(10).setCellValue(item.getOverallScore() != null ? item.getOverallScore().toString() : "0");
                row.createCell(11).setCellValue(item.getRecommendation() != null ? item.getRecommendation() : "");
                row.createCell(12).setCellValue(item.getStatus() != null ? item.getStatus() : "");
            }
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("技术评估数据.xlsx", "UTF-8"));
            wb.write(response.getOutputStream());
            wb.close();
        } catch (Exception e) { log.error("导出技术评估失败", e); }
    }
}
