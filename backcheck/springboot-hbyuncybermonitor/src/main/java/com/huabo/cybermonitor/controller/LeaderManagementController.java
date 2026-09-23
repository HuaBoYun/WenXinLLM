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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.*;
import io.swagger.v3.oas.annotations.Operation;

@Tag(name = "负责人管理", description = "负责人信息、考核、发展、监管")
@RestController
@RequestMapping("/v1/leader")
@Slf4j
public class LeaderManagementController {

    @Autowired private GzctLeaderInfoMapper leaderInfoMapper;
    @Autowired private GzctLeaderEvaluationMapper leaderEvaluationMapper;
    @Autowired private GzctLeaderDevelopmentMapper leaderDevelopmentMapper;
    @Autowired private GzctLeaderSupervisionMapper leaderSupervisionMapper;
    @Autowired private GzctLeaderResumeMapper leaderResumeMapper;
    @Autowired private GzctLeaderPositionMapper leaderPositionMapper;
    @Autowired private GzctLeaderCertificateMapper leaderCertificateMapper;
    @Autowired private GzctLeaderArchiveMapper leaderArchiveMapper;
    @Autowired private GzctLeaderChangeRecordMapper leaderChangeRecordMapper;
    @Autowired private GzctEvaluationIndicatorMapper evaluationIndicatorMapper;

    // ==================== 负责人信息 ====================
    @Operation(summary = "infoList")
    @PostMapping("/info/list")
    public R<PageResult<GzctLeaderInfo>> infoList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            LambdaQueryWrapper<GzctLeaderInfo> w = new LambdaQueryWrapper<>();
            String searchText = params.get("searchText") != null ? params.get("searchText").toString() : "";
            if (!searchText.isEmpty()) w.and(q -> q.like(GzctLeaderInfo::getLeaderName, searchText).or().like(GzctLeaderInfo::getEnterpriseName, searchText));
            if (params.get("leaderName") != null && StringUtils.isNotBlank(params.get("leaderName").toString())) w.like(GzctLeaderInfo::getLeaderName, params.get("leaderName").toString());
            if (params.get("status") != null && StringUtils.isNotBlank(params.get("status").toString())) w.eq(GzctLeaderInfo::getStatus, params.get("status").toString());
            w.orderByDesc(GzctLeaderInfo::getCreateTime);
            Page<GzctLeaderInfo> r = new GzctLeaderInfo().selectPage(new Page<>(pn, ps), w);
            PageResult<GzctLeaderInfo> pr = new PageResult<>();
            pr.setTotalRecord((int) r.getTotal()); pr.setCurrentPage((int) r.getCurrent()); pr.setPageNumber((int) r.getCurrent()); pr.setTotalPage((int) r.getPages()); pr.setPageSize((int) r.getSize()); pr.setTlist(r.getRecords());
            return R.success(pr);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }
    @Operation(summary = "infoDetail")
    @GetMapping("/info/{id}")
    public R<GzctLeaderInfo> infoDetail(@PathVariable String id) { try { return R.success(leaderInfoMapper.selectById(id)); } catch (Exception e) { return R.fail("查询失败"); } }
    @Operation(summary = "新增")
    @PostMapping("/info/add")
    public R<Boolean> addInfo(@RequestBody GzctLeaderInfo record) {
        try {
            record.setCreateTime(LocalDateTime.now());
            leaderInfoMapper.insert(record);
            // 级联：自动写入任职情况表
            if (StringUtils.isNotBlank(record.getPosition())) {
                GzctLeaderPosition pos = new GzctLeaderPosition();
                pos.setLeaderId(record.getId());
                pos.setLeaderName(record.getLeaderName());
                pos.setEnterpriseName(record.getEnterpriseName());
                pos.setPositionName(record.getPosition());
                pos.setPositionLevel("正职");
                pos.setDepartment("");
                pos.setAppointDate(record.getAppointDate());
                pos.setAppointReason("新任命");
                pos.setResponsibility("");
                pos.setStatus("在任");
                pos.setCreateTime(LocalDateTime.now());
                leaderPositionMapper.insert(pos);
            }
            // 级联：自动写入变更记录表
            GzctLeaderChangeRecord changeRecord = new GzctLeaderChangeRecord();
            changeRecord.setLeaderId(record.getId());
            changeRecord.setLeaderName(record.getLeaderName());
            changeRecord.setEnterpriseName(record.getEnterpriseName());
            changeRecord.setChangeType("新增");
            changeRecord.setChangeField("全部");
            changeRecord.setOldValue("");
            changeRecord.setNewValue(record.getPosition() + " - " + record.getEnterpriseName());
            changeRecord.setChangeReason("新增负责人");
            changeRecord.setChangeDate(record.getAppointDate() != null ? record.getAppointDate() : java.time.LocalDate.now());
            changeRecord.setOperator("系统");
            changeRecord.setCreateTime(LocalDateTime.now());
            leaderChangeRecordMapper.insert(changeRecord);
            return R.success(true);
        } catch (Exception e) {
            log.error("新增负责人失败", e);
            return R.fail("新增失败：" + e.getMessage());
        }
    }

    @Operation(summary = "更新")
    @PostMapping("/info/update")
    public R<Boolean> updateInfo(@RequestBody GzctLeaderInfo record) {
        try {
            // 查询旧数据，用于对比变更
            GzctLeaderInfo oldRecord = leaderInfoMapper.selectById(record.getId());
            record.setUpdateTime(LocalDateTime.now());
            leaderInfoMapper.updateById(record);
            if (oldRecord != null) {
                // 级联：职务变更 → 更新任职情况表
                boolean positionChanged = record.getPosition() != null && !record.getPosition().equals(oldRecord.getPosition());
                boolean statusChanged = record.getStatus() != null && !record.getStatus().equals(oldRecord.getStatus());
                if (positionChanged || statusChanged) {
                    // 将旧的任职记录标记为离任
                    LambdaQueryWrapper<GzctLeaderPosition> posWrapper = new LambdaQueryWrapper<>();
                    posWrapper.eq(GzctLeaderPosition::getLeaderId, record.getId()).eq(GzctLeaderPosition::getStatus, "在任");
                    List<GzctLeaderPosition> oldPositions = leaderPositionMapper.selectList(posWrapper);
                    for (GzctLeaderPosition oldPos : oldPositions) {
                        oldPos.setStatus("离任");
                        oldPos.setLeaveDate(java.time.LocalDate.now());
                        oldPos.setUpdateTime(LocalDateTime.now());
                        leaderPositionMapper.updateById(oldPos);
                    }
                    // 新建一条在任记录
                    if (positionChanged) {
                        GzctLeaderPosition newPos = new GzctLeaderPosition();
                        newPos.setLeaderId(record.getId());
                        newPos.setLeaderName(record.getLeaderName());
                        newPos.setEnterpriseName(record.getEnterpriseName());
                        newPos.setPositionName(record.getPosition());
                        newPos.setPositionLevel("正职");
                        newPos.setDepartment("");
                        newPos.setAppointDate(java.time.LocalDate.now());
                        newPos.setAppointReason("职务变更");
                        newPos.setResponsibility("");
                        newPos.setStatus("在任");
                        newPos.setCreateTime(LocalDateTime.now());
                        leaderPositionMapper.insert(newPos);
                    }
                }
                // 级联：写入变更记录
                if (positionChanged) {
                    GzctLeaderChangeRecord cr = new GzctLeaderChangeRecord();
                    cr.setLeaderId(record.getId());
                    cr.setLeaderName(record.getLeaderName());
                    cr.setEnterpriseName(record.getEnterpriseName());
                    cr.setChangeType("职务变更");
                    cr.setChangeField("职务");
                    cr.setOldValue(oldRecord.getPosition());
                    cr.setNewValue(record.getPosition());
                    cr.setChangeReason("职务调整");
                    cr.setChangeDate(java.time.LocalDate.now());
                    cr.setOperator("系统");
                    cr.setCreateTime(LocalDateTime.now());
                    leaderChangeRecordMapper.insert(cr);
                }
                if (statusChanged) {
                    GzctLeaderChangeRecord cr = new GzctLeaderChangeRecord();
                    cr.setLeaderId(record.getId());
                    cr.setLeaderName(record.getLeaderName());
                    cr.setEnterpriseName(record.getEnterpriseName());
                    cr.setChangeType("状态变化");
                    cr.setChangeField("状态");
                    cr.setOldValue(oldRecord.getStatus());
                    cr.setNewValue(record.getStatus());
                    cr.setChangeReason("状态变更");
                    cr.setChangeDate(java.time.LocalDate.now());
                    cr.setOperator("系统");
                    cr.setCreateTime(LocalDateTime.now());
                    leaderChangeRecordMapper.insert(cr);
                }
            }
            return R.success(true);
        } catch (Exception e) {
            log.error("更新负责人失败", e);
            return R.fail("更新失败：" + e.getMessage());
        }
    }
    @Operation(summary = "删除")
    @DeleteMapping("/info/{id}")
    public R<Boolean> deleteInfo(@PathVariable String id) { try { return R.success(leaderInfoMapper.deleteById(id) > 0); } catch (Exception e) { return R.fail("删除失败"); } }
    @Operation(summary = "批量操作")
    @PostMapping("/info/batch/delete")
    public R<Boolean> batchDeleteInfo(@RequestBody Map<String, Object> params) { try { List<String> ids = (List<String>) params.get("ids"); if (ids != null) leaderInfoMapper.deleteBatchIds(ids); return R.success(true); } catch (Exception e) { return R.fail("批量删除失败"); } }
    @Operation(summary = "导出")
    @GetMapping("/info/export")
    public void exportInfo(HttpServletResponse response) { try { List<GzctLeaderInfo> list = leaderInfoMapper.selectList(new LambdaQueryWrapper<GzctLeaderInfo>().orderByDesc(GzctLeaderInfo::getCreateTime)); response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"); response.setHeader("Content-Disposition", "attachment;filename=leader_info_" + System.currentTimeMillis() + ".xlsx"); org.apache.poi.xssf.streaming.SXSSFWorkbook workbook = new org.apache.poi.xssf.streaming.SXSSFWorkbook(); org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("负责人信息"); org.apache.poi.ss.usermodel.Row header = sheet.createRow(0); String[] headers = {"姓名","企业名称","职务","性别","学历","任命日期","状态","联系电话"}; for (int i = 0; i < headers.length; i++) header.createCell(i).setCellValue(headers[i]); for (int i = 0; i < list.size(); i++) { GzctLeaderInfo item = list.get(i); org.apache.poi.ss.usermodel.Row row = sheet.createRow(i + 1); row.createCell(0).setCellValue(item.getLeaderName() != null ? item.getLeaderName() : ""); row.createCell(1).setCellValue(item.getEnterpriseName() != null ? item.getEnterpriseName() : ""); row.createCell(2).setCellValue(item.getPosition() != null ? item.getPosition() : ""); row.createCell(3).setCellValue(item.getGender() != null ? item.getGender() : ""); row.createCell(4).setCellValue(item.getEducation() != null ? item.getEducation() : ""); row.createCell(5).setCellValue(item.getAppointDate() != null ? item.getAppointDate().toString() : ""); row.createCell(6).setCellValue(item.getStatus() != null ? item.getStatus() : ""); row.createCell(7).setCellValue(item.getPhone() != null ? item.getPhone() : ""); } workbook.write(response.getOutputStream()); workbook.close(); } catch (Exception e) { log.error("导出失败", e); } }
    @Operation(summary = "")
    @GetMapping("/info/statistics")
    public R<Map<String, Object>> infoStatistics() { try { Map<String, Object> result = new HashMap<>(); result.put("totalLeaders", leaderInfoMapper.selectCount(null)); result.put("activeLeaders", leaderInfoMapper.selectCount(new LambdaQueryWrapper<GzctLeaderInfo>().eq(GzctLeaderInfo::getStatus, "在任"))); List<GzctLeaderEvaluation> evals = leaderEvaluationMapper.selectList(null); double avg = evals.stream().filter(e -> e.getTotalScore() != null).mapToDouble(e -> e.getTotalScore().doubleValue()).average().orElse(0); result.put("avgScore", Math.round(avg * 10.0) / 10.0); result.put("excellentCount", leaderEvaluationMapper.selectCount(new LambdaQueryWrapper<GzctLeaderEvaluation>().eq(GzctLeaderEvaluation::getGrade, "优秀"))); return R.success(result); } catch (Exception e) { return R.fail("统计失败：" + e.getMessage()); } }

    // ==================== 负责人考核评价 ====================
    @Operation(summary = "evaluationList")
    @PostMapping("/evaluation/list")
    public R<PageResult<GzctLeaderEvaluation>> evaluationList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            LambdaQueryWrapper<GzctLeaderEvaluation> w = new LambdaQueryWrapper<>();
            String searchText = params.get("searchText") != null ? params.get("searchText").toString() : "";
            if (!searchText.isEmpty()) w.like(GzctLeaderEvaluation::getLeaderName, searchText).or().like(GzctLeaderEvaluation::getEnterpriseName, searchText);
            if (params.get("leaderName") != null && org.apache.commons.lang.StringUtils.isNotBlank(params.get("leaderName").toString())) w.like(GzctLeaderEvaluation::getLeaderName, params.get("leaderName").toString());
            if (params.get("assessmentYear") != null && org.apache.commons.lang.StringUtils.isNotBlank(params.get("assessmentYear").toString())) w.eq(GzctLeaderEvaluation::getAssessmentYear, params.get("assessmentYear").toString());
            w.orderByDesc(GzctLeaderEvaluation::getCreateTime);
            Page<GzctLeaderEvaluation> r = new GzctLeaderEvaluation().selectPage(new Page<>(pn, ps), w);
            // 字段映射: 将Entity字段映射到前端期望字段
            r.getRecords().forEach(item -> {
                item.setCompany(item.getEnterpriseName());
                item.setEvaluationType(item.getAssessmentType());
                item.setEvaluationPeriod(item.getAssessmentYear());
                item.setEvaluationDate(item.getEvaluateTime() != null ? item.getEvaluateTime().toLocalDate().toString() : "");
                item.setStatus("已完成");
            });
            PageResult<GzctLeaderEvaluation> pr = new PageResult<>();
            pr.setTotalRecord((int) r.getTotal()); pr.setCurrentPage((int) r.getCurrent()); pr.setPageNumber((int) r.getCurrent()); pr.setTotalPage((int) r.getPages()); pr.setPageSize((int) r.getSize()); pr.setTlist(r.getRecords());
            return R.success(pr);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }
    @Operation(summary = "evaluationDetail")
    @GetMapping("/evaluation/{id}")
    public R<GzctLeaderEvaluation> evaluationDetail(@PathVariable String id) { try { return R.success(leaderEvaluationMapper.selectById(id)); } catch (Exception e) { return R.fail("查询失败"); } }
    @Operation(summary = "新增")
    @PostMapping("/evaluation/add")
    public R<Boolean> addEvaluation(@RequestBody GzctLeaderEvaluation record) {
        try {
            record.setCreateTime(LocalDateTime.now());
            // 设置考核时间：如果前端未传则使用当前时间
            if (record.getEvaluateTime() == null) {
                record.setEvaluateTime(LocalDateTime.now());
            }
            // 根据总分自动计算等级（如果前端未传等级）
            if (record.getTotalScore() != null && (record.getGrade() == null || record.getGrade().isEmpty())) {
                double score = record.getTotalScore().doubleValue();
                if (score >= 90) record.setGrade("优秀");
                else if (score >= 80) record.setGrade("良好");
                else if (score >= 70) record.setGrade("合格");
                else record.setGrade("待改进");
            }
            leaderEvaluationMapper.insert(record);
            // 级联：自动写入档案管理表（考核档案）
            GzctLeaderArchive archive = new GzctLeaderArchive();
            archive.setLeaderId(record.getLeaderId());
            archive.setLeaderName(record.getLeaderName());
            archive.setEnterpriseName(record.getEnterpriseName());
            archive.setArchiveType("考核档案");
            archive.setArchiveNo("KH-" + record.getAssessmentYear() + "-" + System.currentTimeMillis() % 10000);
            archive.setArchiveTitle(record.getLeaderName() + " " + record.getAssessmentYear() + "年度" + (record.getAssessmentType() != null ? record.getAssessmentType() : "考核") + "档案");
            archive.setArchiveContent("考核等级：" + record.getGrade() + "，总分：" + record.getTotalScore());
            archive.setFileDate(java.time.LocalDate.now());
            archive.setKeeper("人事部");
            archive.setStatus("正常");
            archive.setCreateTime(LocalDateTime.now());
            leaderArchiveMapper.insert(archive);
            return R.success(true);
        } catch (Exception e) {
            log.error("新增考核评价失败", e);
            return R.fail("新增失败：" + e.getMessage());
        }
    }
    @Operation(summary = "更新")
    @PostMapping("/evaluation/update")
    public R<Boolean> updateEvaluation(@RequestBody GzctLeaderEvaluation record) { try { record.setUpdateTime(LocalDateTime.now()); leaderEvaluationMapper.updateById(record); return R.success(true); } catch (Exception e) { return R.fail("更新失败：" + e.getMessage()); } }
    @Operation(summary = "删除")
    @DeleteMapping("/evaluation/{id}")
    public R<Boolean> deleteEvaluation(@PathVariable String id) { try { return R.success(leaderEvaluationMapper.deleteById(id) > 0); } catch (Exception e) { return R.fail("删除失败"); } }
    @Operation(summary = "导出")
    @GetMapping("/evaluation/export")
    public void exportEvaluation(HttpServletResponse response) { try { List<GzctLeaderEvaluation> list = leaderEvaluationMapper.selectList(new LambdaQueryWrapper<GzctLeaderEvaluation>().orderByDesc(GzctLeaderEvaluation::getCreateTime)); response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"); response.setHeader("Content-Disposition", "attachment;filename=leader_eval_" + System.currentTimeMillis() + ".xlsx"); org.apache.poi.xssf.streaming.SXSSFWorkbook workbook = new org.apache.poi.xssf.streaming.SXSSFWorkbook(); org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("考核评价"); org.apache.poi.ss.usermodel.Row header = sheet.createRow(0); String[] headers = {"负责人","企业名称","考核年度","考核类型","总分","等级"}; for (int i = 0; i < headers.length; i++) header.createCell(i).setCellValue(headers[i]); for (int i = 0; i < list.size(); i++) { GzctLeaderEvaluation item = list.get(i); org.apache.poi.ss.usermodel.Row row = sheet.createRow(i + 1); row.createCell(0).setCellValue(item.getLeaderName() != null ? item.getLeaderName() : ""); row.createCell(1).setCellValue(item.getEnterpriseName() != null ? item.getEnterpriseName() : ""); row.createCell(2).setCellValue(item.getAssessmentYear() != null ? item.getAssessmentYear() : ""); row.createCell(3).setCellValue(item.getAssessmentType() != null ? item.getAssessmentType() : ""); row.createCell(4).setCellValue(item.getTotalScore() != null ? item.getTotalScore().doubleValue() : 0); row.createCell(5).setCellValue(item.getGrade() != null ? item.getGrade() : ""); } workbook.write(response.getOutputStream()); workbook.close(); } catch (Exception e) { log.error("导出失败", e); } }
    @Operation(summary = "")
    @GetMapping("/evaluation/statistics")
    public R<Map<String, Object>> evaluationStatistics() {
        try {
            Map<String, Object> result = new HashMap<>();
            long total = leaderEvaluationMapper.selectCount(null);
            long excellent = leaderEvaluationMapper.selectCount(new LambdaQueryWrapper<GzctLeaderEvaluation>().eq(GzctLeaderEvaluation::getGrade, "优秀"));
            long completed = leaderEvaluationMapper.selectCount(null);
            List<GzctLeaderEvaluation> allEvals = leaderEvaluationMapper.selectList(new LambdaQueryWrapper<GzctLeaderEvaluation>().isNotNull(GzctLeaderEvaluation::getTotalScore));
            double avg = allEvals.stream().filter(e -> e.getTotalScore() != null).mapToDouble(e -> e.getTotalScore().doubleValue()).average().orElse(0);
            result.put("totalEvaluations", total);
            result.put("avgScore", Math.round(avg * 10.0) / 10.0);
            result.put("excellentRate", total > 0 ? Math.round(excellent * 100.0 / total) : 0);
            result.put("completedCount", completed);
            result.put("excellentCount", excellent);
            return R.success(result);
        } catch (Exception e) { return R.fail("统计失败：" + e.getMessage()); }
    }

    // ==================== 考核评价Tab数据接口 ====================
    /**
     * 按Tab类型查询考核评价列表
     * tabType: performance(绩效考核), ability(能力评估), feedback(360度评价)
     */
    @Operation(summary = "evaluationTabList")
    @PostMapping("/evaluation/tab-list")
    public R<PageResult<GzctLeaderEvaluation>> evaluationTabList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 10;
            String tabType = params.get("tabType") != null ? params.get("tabType").toString() : "";
            LambdaQueryWrapper<GzctLeaderEvaluation> w = new LambdaQueryWrapper<>();
            // 根据tabType过滤不同考核类型
            switch (tabType) {
                case "performance": w.in(GzctLeaderEvaluation::getAssessmentType, "绩效考核", "年度考核", "季度考核", "专项考核"); break;
                case "ability": w.eq(GzctLeaderEvaluation::getAssessmentType, "能力评估"); break;
                case "feedback": w.eq(GzctLeaderEvaluation::getAssessmentType, "360度评价"); break;
                default: break;
            }
            String searchText = params.get("searchText") != null ? params.get("searchText").toString() : "";
            if (!searchText.isEmpty()) w.and(q -> q.like(GzctLeaderEvaluation::getLeaderName, searchText).or().like(GzctLeaderEvaluation::getEnterpriseName, searchText));
            w.orderByDesc(GzctLeaderEvaluation::getCreateTime);
            Page<GzctLeaderEvaluation> r = new GzctLeaderEvaluation().selectPage(new Page<>(pn, ps), w);
            r.getRecords().forEach(item -> {
                item.setCompany(item.getEnterpriseName());
                item.setEvaluationType(item.getAssessmentType());
                item.setEvaluationPeriod(item.getAssessmentYear());
                item.setEvaluationDate(item.getEvaluateTime() != null ? item.getEvaluateTime().toLocalDate().toString() : "");
                item.setStatus("已完成");
            });
            PageResult<GzctLeaderEvaluation> pr = new PageResult<>();
            pr.setTotalRecord((int) r.getTotal()); pr.setCurrentPage((int) r.getCurrent()); pr.setPageNumber((int) r.getCurrent()); pr.setTotalPage((int) r.getPages()); pr.setPageSize((int) r.getSize()); pr.setTlist(r.getRecords());
            return R.success(pr);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    /**
     * 单条考核记录导出 - 返回JSON数据，由前端生成Excel文件
     */
    @Operation(summary = "")
    @GetMapping("/evaluation/exportById")
    public R<Map<String, Object>> exportEvaluationById(@RequestParam("id") String id) {
        try {
            GzctLeaderEvaluation item = leaderEvaluationMapper.selectById(id);
            if (item == null) { return R.fail("记录不存在"); }
            Map<String, Object> data = new LinkedHashMap<>();
            data.put("负责人", item.getLeaderName() != null ? item.getLeaderName() : "");
            data.put("企业名称", item.getEnterpriseName() != null ? item.getEnterpriseName() : "");
            data.put("考核年度", item.getAssessmentYear() != null ? item.getAssessmentYear() : "");
            data.put("考核类型", item.getAssessmentType() != null ? item.getAssessmentType() : "");
            data.put("政治素质得分", item.getPoliticalScore() != null ? item.getPoliticalScore().toString() : "0");
            data.put("经营业绩得分", item.getEconomicScore() != null ? item.getEconomicScore().toString() : "0");
            data.put("管理能力得分", item.getManagementScore() != null ? item.getManagementScore().toString() : "0");
            data.put("廉洁自律得分", item.getIntegrityScore() != null ? item.getIntegrityScore().toString() : "0");
            data.put("总分", item.getTotalScore() != null ? item.getTotalScore().toString() : "0");
            data.put("考核等级", item.getGrade() != null ? item.getGrade() : "");
            data.put("考核人", item.getEvaluator() != null ? item.getEvaluator() : "");
            data.put("考核时间", item.getEvaluateTime() != null ? item.getEvaluateTime().toString() : "");
            return R.success(data);
        } catch (Exception e) {
            log.error("导出数据查询失败, id={}", id, e);
            return R.fail("导出失败：" + e.getMessage());
        }
    }

    // ==================== 考核指标管理 ====================
    @Operation(summary = "indicatorList")
    @PostMapping("/evaluation/indicator/list")
    public R<PageResult<GzctEvaluationIndicator>> indicatorList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 10;
            LambdaQueryWrapper<GzctEvaluationIndicator> w = new LambdaQueryWrapper<>();
            String searchText = params.get("searchText") != null ? params.get("searchText").toString() : "";
            if (!searchText.isEmpty()) w.like(GzctEvaluationIndicator::getIndicatorName, searchText);
            if (params.get("indicatorType") != null && StringUtils.isNotBlank(params.get("indicatorType").toString())) w.eq(GzctEvaluationIndicator::getIndicatorType, params.get("indicatorType").toString());
            w.orderByAsc(GzctEvaluationIndicator::getSortOrder);
            Page<GzctEvaluationIndicator> r = new GzctEvaluationIndicator().selectPage(new Page<>(pn, ps), w);
            PageResult<GzctEvaluationIndicator> pr = new PageResult<>();
            pr.setTotalRecord((int) r.getTotal()); pr.setCurrentPage((int) r.getCurrent()); pr.setPageNumber((int) r.getCurrent()); pr.setTotalPage((int) r.getPages()); pr.setPageSize((int) r.getSize()); pr.setTlist(r.getRecords());
            return R.success(pr);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "新增")
    @PostMapping("/evaluation/indicator/add")
    public R<Boolean> addIndicator(@RequestBody GzctEvaluationIndicator record) {
        try { record.setCreateTime(LocalDateTime.now()); evaluationIndicatorMapper.insert(record); return R.success(true); }
        catch (Exception e) { return R.fail("新增失败：" + e.getMessage()); }
    }

    @Operation(summary = "更新")
    @PostMapping("/evaluation/indicator/update")
    public R<Boolean> updateIndicator(@RequestBody GzctEvaluationIndicator record) {
        try { record.setUpdateTime(LocalDateTime.now()); evaluationIndicatorMapper.updateById(record); return R.success(true); }
        catch (Exception e) { return R.fail("更新失败：" + e.getMessage()); }
    }

    @Operation(summary = "删除")
    @DeleteMapping("/evaluation/indicator/{id}")
    public R<Boolean> deleteIndicator(@PathVariable String id) {
        try { return R.success(evaluationIndicatorMapper.deleteById(id) > 0); }
        catch (Exception e) { return R.fail("删除失败"); }
    }

    /**
     * 考核结果分析接口 - 返回各维度统计数据
     */
    @Operation(summary = "")
    @GetMapping("/evaluation/analysis")
    public R<Map<String, Object>> evaluationAnalysis() {
        try {
            Map<String, Object> result = new HashMap<>();
            List<GzctLeaderEvaluation> allEvals = leaderEvaluationMapper.selectList(null);
            // 等级分布
            Map<String, Long> gradeDistribution = new HashMap<>();
            gradeDistribution.put("优秀", allEvals.stream().filter(e -> "优秀".equals(e.getGrade())).count());
            gradeDistribution.put("良好", allEvals.stream().filter(e -> "良好".equals(e.getGrade())).count());
            gradeDistribution.put("合格", allEvals.stream().filter(e -> "合格".equals(e.getGrade())).count());
            gradeDistribution.put("待改进", allEvals.stream().filter(e -> "待改进".equals(e.getGrade())).count());
            result.put("gradeDistribution", gradeDistribution);
            // 各维度平均分
            Map<String, Double> dimensionAvg = new HashMap<>();
            dimensionAvg.put("politicalAvg", allEvals.stream().filter(e -> e.getPoliticalScore() != null).mapToDouble(e -> e.getPoliticalScore().doubleValue()).average().orElse(0));
            dimensionAvg.put("economicAvg", allEvals.stream().filter(e -> e.getEconomicScore() != null).mapToDouble(e -> e.getEconomicScore().doubleValue()).average().orElse(0));
            dimensionAvg.put("managementAvg", allEvals.stream().filter(e -> e.getManagementScore() != null).mapToDouble(e -> e.getManagementScore().doubleValue()).average().orElse(0));
            dimensionAvg.put("integrityAvg", allEvals.stream().filter(e -> e.getIntegrityScore() != null).mapToDouble(e -> e.getIntegrityScore().doubleValue()).average().orElse(0));
            result.put("dimensionAvg", dimensionAvg);
            // 分数段分布
            Map<String, Long> scoreDistribution = new HashMap<>();
            scoreDistribution.put("90-100", allEvals.stream().filter(e -> e.getTotalScore() != null && e.getTotalScore().doubleValue() >= 90).count());
            scoreDistribution.put("80-89", allEvals.stream().filter(e -> e.getTotalScore() != null && e.getTotalScore().doubleValue() >= 80 && e.getTotalScore().doubleValue() < 90).count());
            scoreDistribution.put("70-79", allEvals.stream().filter(e -> e.getTotalScore() != null && e.getTotalScore().doubleValue() >= 70 && e.getTotalScore().doubleValue() < 80).count());
            scoreDistribution.put("60-69", allEvals.stream().filter(e -> e.getTotalScore() != null && e.getTotalScore().doubleValue() >= 60 && e.getTotalScore().doubleValue() < 70).count());
            scoreDistribution.put("60以下", allEvals.stream().filter(e -> e.getTotalScore() != null && e.getTotalScore().doubleValue() < 60).count());
            result.put("scoreDistribution", scoreDistribution);
            // 总数据
            result.put("totalCount", allEvals.size());
            double avgScore = allEvals.stream().filter(e -> e.getTotalScore() != null).mapToDouble(e -> e.getTotalScore().doubleValue()).average().orElse(0);
            result.put("avgScore", Math.round(avgScore * 10.0) / 10.0);
            return R.success(result);
        } catch (Exception e) { return R.fail("分析失败：" + e.getMessage()); }
    }

    // ==================== 负责人发展 ====================
    @Operation(summary = "developmentList")
    @PostMapping("/development/list")
    public R<PageResult<GzctLeaderDevelopment>> developmentList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            LambdaQueryWrapper<GzctLeaderDevelopment> w = new LambdaQueryWrapper<>();
            String searchText = params.get("searchText") != null ? params.get("searchText").toString() : "";
            if (!searchText.isEmpty()) w.like(GzctLeaderDevelopment::getLeaderName, searchText).or().like(GzctLeaderDevelopment::getDevelopmentName, searchText);
            if (params.get("leaderName") != null && StringUtils.isNotBlank(params.get("leaderName").toString())) w.like(GzctLeaderDevelopment::getLeaderName, params.get("leaderName").toString());
            if (params.get("status") != null && StringUtils.isNotBlank(params.get("status").toString())) w.eq(GzctLeaderDevelopment::getStatus, params.get("status").toString());
            if (params.get("developmentType") != null && StringUtils.isNotBlank(params.get("developmentType").toString())) w.eq(GzctLeaderDevelopment::getDevelopmentType, params.get("developmentType").toString());
            w.orderByDesc(GzctLeaderDevelopment::getCreateTime);
            Page<GzctLeaderDevelopment> r = new GzctLeaderDevelopment().selectPage(new Page<>(pn, ps), w);
            // 字段映射: 将Entity字段映射到前端期望字段
            r.getRecords().forEach(item -> {
                item.setCompany(item.getEnterpriseName());
                item.setProgramName(item.getDevelopmentName());
                item.setTrainingType(item.getDevelopmentType());
            });
            PageResult<GzctLeaderDevelopment> pr = new PageResult<>();
            pr.setTotalRecord((int) r.getTotal());
            pr.setCurrentPage((int) r.getCurrent());
            pr.setPageNumber((int) r.getCurrent());
            pr.setTotalPage((int) r.getPages());
            pr.setPageSize((int) r.getSize());
            pr.setTlist(r.getRecords());
            return R.success(pr);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "按Tab类型查询发展列表")
    @PostMapping("/development/tab-list")
    public R<PageResult<GzctLeaderDevelopment>> developmentTabList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            String tabType = params.get("tabType") != null ? params.get("tabType").toString() : "";
            LambdaQueryWrapper<GzctLeaderDevelopment> w = new LambdaQueryWrapper<>();
            // 根据Tab类型过滤developmentType
            if (StringUtils.isNotBlank(tabType)) {
                w.eq(GzctLeaderDevelopment::getDevelopmentType, tabType);
            }
            String searchText = params.get("searchText") != null ? params.get("searchText").toString() : "";
            if (!searchText.isEmpty()) w.like(GzctLeaderDevelopment::getLeaderName, searchText).or().like(GzctLeaderDevelopment::getDevelopmentName, searchText);
            if (params.get("status") != null && StringUtils.isNotBlank(params.get("status").toString())) w.eq(GzctLeaderDevelopment::getStatus, params.get("status").toString());
            w.orderByDesc(GzctLeaderDevelopment::getCreateTime);
            Page<GzctLeaderDevelopment> r = new GzctLeaderDevelopment().selectPage(new Page<>(pn, ps), w);
            r.getRecords().forEach(item -> {
                item.setCompany(item.getEnterpriseName());
                item.setProgramName(item.getDevelopmentName());
                item.setTrainingType(item.getDevelopmentType());
            });
            return R.success(PageResult.of(r));
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "图表数据-培训类型分布")
    @GetMapping("/development/chart/type-distribution")
    public R<List<Map<String, Object>>> developmentTypeDistribution() {
        try {
            List<Map<String, Object>> result = new ArrayList<>();
            // 按developmentType分组统计
            List<GzctLeaderDevelopment> all = leaderDevelopmentMapper.selectList(null);
            Map<String, Long> typeCount = new HashMap<>();
            for (GzctLeaderDevelopment item : all) {
                String type = item.getDevelopmentType() != null ? item.getDevelopmentType() : "其他";
                typeCount.merge(type, 1L, Long::sum);
            }
            for (Map.Entry<String, Long> entry : typeCount.entrySet()) {
                Map<String, Object> map = new HashMap<>();
                map.put("name", entry.getKey());
                map.put("value", entry.getValue());
                result.add(map);
            }
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "图表数据-能力提升趋势")
    @GetMapping("/development/chart/progress-trend")
    public R<Map<String, Object>> developmentProgressTrend() {
        try {
            Map<String, Object> result = new HashMap<>();
            List<String> months = new ArrayList<>();
            List<Long> completedData = new ArrayList<>();
            List<Long> totalData = new ArrayList<>();
            // 按月统计最近12个月的完成数和总数
            java.time.LocalDate now = java.time.LocalDate.now();
            for (int i = 11; i >= 0; i--) {
                java.time.LocalDate monthStart = now.minusMonths(i).withDayOfMonth(1);
                java.time.LocalDate monthEnd = monthStart.plusMonths(1).minusDays(1);
                months.add(monthStart.getMonthValue() + "月");
                long monthTotal = leaderDevelopmentMapper.selectCount(
                    new LambdaQueryWrapper<GzctLeaderDevelopment>()
                        .le(GzctLeaderDevelopment::getStartDate, monthEnd)
                        .ge(GzctLeaderDevelopment::getStartDate, monthStart));
                long monthCompleted = leaderDevelopmentMapper.selectCount(
                    new LambdaQueryWrapper<GzctLeaderDevelopment>()
                        .eq(GzctLeaderDevelopment::getStatus, "已完成")
                        .le(GzctLeaderDevelopment::getStartDate, monthEnd)
                        .ge(GzctLeaderDevelopment::getStartDate, monthStart));
                totalData.add(monthTotal);
                completedData.add(monthCompleted);
            }
            result.put("months", months);
            result.put("totalData", totalData);
            result.put("completedData", completedData);
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "更新培训进度")
    @PostMapping("/development/update-progress")
    public R<Boolean> updateDevelopmentProgress(@RequestBody Map<String, Object> params) {
        try {
            String id = params.get("id") != null ? params.get("id").toString() : "";
            Integer progress = params.get("progress") != null ? Integer.parseInt(params.get("progress").toString()) : null;
            String status = params.get("status") != null ? params.get("status").toString() : null;
            if (StringUtils.isBlank(id)) return R.fail("ID不能为空");
            GzctLeaderDevelopment record = leaderDevelopmentMapper.selectById(id);
            if (record == null) return R.fail("记录不存在");
            if (progress != null) record.setProgress(progress);
            if (status != null) record.setStatus(status);
            if (progress != null && progress >= 100) record.setStatus("已完成");
            record.setUpdateTime(LocalDateTime.now());
            leaderDevelopmentMapper.updateById(record);
            return R.success(true);
        } catch (Exception e) { return R.fail("更新进度失败：" + e.getMessage()); }
    }

    @Operation(summary = "developmentDetail")
    @GetMapping("/development/{id}")
    public R<GzctLeaderDevelopment> developmentDetail(@PathVariable String id) {
        try { return R.success(leaderDevelopmentMapper.selectById(id)); } catch (Exception e) { return R.fail("查询失败"); }
    }
    @Operation(summary = "新增")
    @PostMapping("/development/add")
    public R<Boolean> addDevelopment(@RequestBody GzctLeaderDevelopment record) {
        try {
            record.setCreateTime(LocalDateTime.now());
            if (record.getProgress() == null) record.setProgress(0);
            if (record.getStatus() == null) record.setStatus("待开始");
            leaderDevelopmentMapper.insert(record);
            return R.success(true);
        } catch (Exception e) { return R.fail("新增失败：" + e.getMessage()); }
    }
    @Operation(summary = "更新")
    @PostMapping("/development/update")
    public R<Boolean> updateDevelopment(@RequestBody GzctLeaderDevelopment record) {
        try {
            record.setUpdateTime(LocalDateTime.now());
            leaderDevelopmentMapper.updateById(record);
            return R.success(true);
        } catch (Exception e) { return R.fail("更新失败：" + e.getMessage()); }
    }
    @Operation(summary = "删除")
    @DeleteMapping("/development/{id}")
    public R<Boolean> deleteDevelopment(@PathVariable String id) {
        try { return R.success(leaderDevelopmentMapper.deleteById(id) > 0); } catch (Exception e) { return R.fail("删除失败"); }
    }
    @Operation(summary = "导出")
    @GetMapping("/development/export")
    public void exportDevelopment(HttpServletResponse response) { try { List<GzctLeaderDevelopment> list = leaderDevelopmentMapper.selectList(new LambdaQueryWrapper<GzctLeaderDevelopment>().orderByDesc(GzctLeaderDevelopment::getCreateTime)); response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"); response.setHeader("Content-Disposition", "attachment;filename=leader_dev_" + System.currentTimeMillis() + ".xlsx"); org.apache.poi.xssf.streaming.SXSSFWorkbook workbook = new org.apache.poi.xssf.streaming.SXSSFWorkbook(); org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("负责人发展"); org.apache.poi.ss.usermodel.Row header = sheet.createRow(0); String[] headers = {"负责人","企业名称","发展类型","项目名称","培训机构","开始日期","结束日期","状态"}; for (int i = 0; i < headers.length; i++) header.createCell(i).setCellValue(headers[i]); for (int i = 0; i < list.size(); i++) { GzctLeaderDevelopment item = list.get(i); org.apache.poi.ss.usermodel.Row row = sheet.createRow(i + 1); row.createCell(0).setCellValue(item.getLeaderName() != null ? item.getLeaderName() : ""); row.createCell(1).setCellValue(item.getEnterpriseName() != null ? item.getEnterpriseName() : ""); row.createCell(2).setCellValue(item.getDevelopmentType() != null ? item.getDevelopmentType() : ""); row.createCell(3).setCellValue(item.getDevelopmentName() != null ? item.getDevelopmentName() : ""); row.createCell(4).setCellValue(item.getInstitution() != null ? item.getInstitution() : ""); row.createCell(5).setCellValue(item.getStartDate() != null ? item.getStartDate().toString() : ""); row.createCell(6).setCellValue(item.getEndDate() != null ? item.getEndDate().toString() : ""); row.createCell(7).setCellValue(item.getStatus() != null ? item.getStatus() : ""); } workbook.write(response.getOutputStream()); workbook.close(); } catch (Exception e) { log.error("导出失败", e); } }
    @Operation(summary = "统计数据")
    @GetMapping("/development/statistics")
    public R<Map<String, Object>> developmentStatistics() {
        try {
            Map<String, Object> result = new HashMap<>();
            List<GzctLeaderDevelopment> all = leaderDevelopmentMapper.selectList(null);
            long total = all.size();
            long completed = all.stream().filter(i -> "已完成".equals(i.getStatus())).count();
            long totalHours = all.stream().mapToLong(i -> i.getDuration() != null ? i.getDuration() : 0).sum();
            // 统计不重复的参训人数
            long participantCount = all.stream().map(GzctLeaderDevelopment::getLeaderId).filter(Objects::nonNull).distinct().count();
            result.put("totalPrograms", total);
            result.put("participantCount", participantCount > 0 ? participantCount : total);
            result.put("totalHours", totalHours);
            result.put("completionRate", total > 0 ? Math.round(completed * 100.0 / total) : 0);
            result.put("completedCount", completed);
            return R.success(result);
        } catch (Exception e) { return R.fail("统计失败：" + e.getMessage()); }
    }

    // ==================== 负责人监管 ====================
    @Operation(summary = "supervisionList")
    @PostMapping("/supervision/list")
    public R<PageResult<GzctLeaderSupervision>> supervisionList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            LambdaQueryWrapper<GzctLeaderSupervision> w = new LambdaQueryWrapper<>();
            String searchText = params.get("searchText") != null ? params.get("searchText").toString() : "";
            if (!searchText.isEmpty()) w.like(GzctLeaderSupervision::getLeaderName, searchText).or().like(GzctLeaderSupervision::getEnterpriseName, searchText);
            if (params.get("leaderName") != null && org.apache.commons.lang.StringUtils.isNotBlank(params.get("leaderName").toString())) w.like(GzctLeaderSupervision::getLeaderName, params.get("leaderName").toString());
            if (params.get("riskLevel") != null && org.apache.commons.lang.StringUtils.isNotBlank(params.get("riskLevel").toString())) w.eq(GzctLeaderSupervision::getRiskLevel, params.get("riskLevel").toString());
            if (params.get("supervisionType") != null && org.apache.commons.lang.StringUtils.isNotBlank(params.get("supervisionType").toString())) w.eq(GzctLeaderSupervision::getSupervisionType, params.get("supervisionType").toString());
            if (params.get("rectificationStatus") != null && org.apache.commons.lang.StringUtils.isNotBlank(params.get("rectificationStatus").toString())) w.eq(GzctLeaderSupervision::getRectificationStatus, params.get("rectificationStatus").toString());
            w.orderByDesc(GzctLeaderSupervision::getCreateTime);
            Page<GzctLeaderSupervision> r = new GzctLeaderSupervision().selectPage(new Page<>(pn, ps), w);
            // 字段映射: 将Entity字段映射到前端期望字段
            r.getRecords().forEach(item -> {
                item.setCompany(item.getEnterpriseName());
                item.setComplianceStatus(item.getRectificationStatus() != null ? item.getRectificationStatus() : "合规");
                item.setIssueCount(1);
                item.setStatus(item.getRectificationStatus() != null ? item.getRectificationStatus() : "进行中");
            });
            PageResult<GzctLeaderSupervision> pr = new PageResult<>();
            pr.setTotalRecord((int) r.getTotal()); pr.setCurrentPage((int) r.getCurrent()); pr.setPageNumber((int) r.getCurrent()); pr.setTotalPage((int) r.getPages()); pr.setPageSize((int) r.getSize()); pr.setTlist(r.getRecords());
            return R.success(pr);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }
    @Operation(summary = "supervisionDetail")
    @GetMapping("/supervision/{id}")
    public R<GzctLeaderSupervision> supervisionDetail(@PathVariable String id) { try { return R.success(leaderSupervisionMapper.selectById(id)); } catch (Exception e) { return R.fail("查询失败"); } }
    @Operation(summary = "新增")
    @PostMapping("/supervision/add")
    public R<Boolean> addSupervision(@RequestBody GzctLeaderSupervision record) { try { record.setCreateTime(LocalDateTime.now()); leaderSupervisionMapper.insert(record); return R.success(true); } catch (Exception e) { return R.fail("新增失败：" + e.getMessage()); } }
    @Operation(summary = "更新")
    @PostMapping("/supervision/update")
    public R<Boolean> updateSupervision(@RequestBody GzctLeaderSupervision record) { try { record.setUpdateTime(LocalDateTime.now()); leaderSupervisionMapper.updateById(record); return R.success(true); } catch (Exception e) { return R.fail("更新失败：" + e.getMessage()); } }
    @Operation(summary = "删除")
    @DeleteMapping("/supervision/{id}")
    public R<Boolean> deleteSupervision(@PathVariable String id) { try { return R.success(leaderSupervisionMapper.deleteById(id) > 0); } catch (Exception e) { return R.fail("删除失败"); } }
    @Operation(summary = "导出")
    @GetMapping("/supervision/export")
    public void exportSupervision(HttpServletResponse response) {
        org.apache.poi.xssf.streaming.SXSSFWorkbook workbook = null;
        try {
            List<GzctLeaderSupervision> list = leaderSupervisionMapper.selectList(
                new LambdaQueryWrapper<GzctLeaderSupervision>().orderByDesc(GzctLeaderSupervision::getCreateTime)
            );
            if (list == null) list = new java.util.ArrayList<>();

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("UTF-8");
            String fileName = java.net.URLEncoder.encode("负责人监管_" + System.currentTimeMillis(), "UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");

            workbook = new org.apache.poi.xssf.streaming.SXSSFWorkbook();
            org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("负责人监管");
            org.apache.poi.ss.usermodel.Row header = sheet.createRow(0);
            String[] headers = {"负责人", "企业名称", "监管类型", "发现问题", "风险等级", "整改状态", "监管人", "监管日期"};
            for (int i = 0; i < headers.length; i++) {
                header.createCell(i).setCellValue(headers[i]);
            }
            for (int i = 0; i < list.size(); i++) {
                GzctLeaderSupervision item = list.get(i);
                org.apache.poi.ss.usermodel.Row row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(item.getLeaderName() != null ? item.getLeaderName() : "");
                row.createCell(1).setCellValue(item.getEnterpriseName() != null ? item.getEnterpriseName() : "");
                row.createCell(2).setCellValue(item.getSupervisionType() != null ? item.getSupervisionType() : "");
                row.createCell(3).setCellValue(item.getFindingDesc() != null ? item.getFindingDesc() : "");
                row.createCell(4).setCellValue(item.getRiskLevel() != null ? item.getRiskLevel() : "");
                row.createCell(5).setCellValue(item.getRectificationStatus() != null ? item.getRectificationStatus() : "");
                row.createCell(6).setCellValue(item.getSupervisor() != null ? item.getSupervisor() : "");
                row.createCell(7).setCellValue(item.getSupervisionDate() != null ? item.getSupervisionDate().toString() : "");
            }
            workbook.write(response.getOutputStream());
            response.getOutputStream().flush();
        } catch (Exception e) {
            log.error("导出负责人监管数据失败", e);
            try {
                response.reset();
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"code\":0,\"msg\":\"导出失败：" + e.getMessage() + "\"}");
            } catch (Exception ex) {
                log.error("写入错误响应失败", ex);
            }
        } finally {
            if (workbook != null) {
                try { workbook.close(); } catch (Exception e) { log.error("关闭workbook失败", e); }
            }
        }
    }
    @Operation(summary = "")
    @GetMapping("/supervision/statistics")
    public R<Map<String, Object>> supervisionStatistics() {
        try {
            Map<String, Object> result = new HashMap<>();
            long total = leaderSupervisionMapper.selectCount(null);
            long highRisk = leaderSupervisionMapper.selectCount(new LambdaQueryWrapper<GzctLeaderSupervision>().eq(GzctLeaderSupervision::getRiskLevel, "高风险"));
            long completed = leaderSupervisionMapper.selectCount(new LambdaQueryWrapper<GzctLeaderSupervision>().eq(GzctLeaderSupervision::getRectificationStatus, "已整改"));
            result.put("totalSupervisions", total);
            result.put("riskCount", highRisk);
            result.put("complianceRate", total > 0 ? Math.round((total - highRisk) * 100.0 / total) : 100);
            result.put("qualifiedCount", completed);
            result.put("highRiskCount", highRisk);
            return R.success(result);
        } catch (Exception e) { return R.fail("统计失败：" + e.getMessage()); }
    }

    // ==================== 负责人履历 ====================
    @Operation(summary = "resumeList")
    @PostMapping("/resume/list")
    public R<PageResult<GzctLeaderResume>> resumeList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            LambdaQueryWrapper<GzctLeaderResume> w = new LambdaQueryWrapper<>();
            if (params.get("leaderName") != null && StringUtils.isNotBlank(params.get("leaderName").toString())) w.like(GzctLeaderResume::getLeaderName, params.get("leaderName").toString());
            if (params.get("leaderId") != null && StringUtils.isNotBlank(params.get("leaderId").toString())) w.eq(GzctLeaderResume::getLeaderId, params.get("leaderId").toString());
            w.orderByDesc(GzctLeaderResume::getCreateTime);
            Page<GzctLeaderResume> r = new GzctLeaderResume().selectPage(new Page<>(pn, ps), w);
            PageResult<GzctLeaderResume> pr = new PageResult<>();
            pr.setTotalRecord((int) r.getTotal()); pr.setCurrentPage((int) r.getCurrent()); pr.setPageNumber((int) r.getCurrent()); pr.setTotalPage((int) r.getPages()); pr.setPageSize((int) r.getSize()); pr.setTlist(r.getRecords());
            return R.success(pr);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }
    @Operation(summary = "resumeDetail")
    @GetMapping("/resume/{id}")
    public R<GzctLeaderResume> resumeDetail(@PathVariable String id) { try { return R.success(leaderResumeMapper.selectById(id)); } catch (Exception e) { return R.fail("查询失败"); } }
    @Operation(summary = "新增")
    @PostMapping("/resume/add")
    public R<Boolean> addResume(@RequestBody GzctLeaderResume record) { try { record.setCreateTime(LocalDateTime.now()); leaderResumeMapper.insert(record); return R.success(true); } catch (Exception e) { return R.fail("新增失败：" + e.getMessage()); } }
    @Operation(summary = "更新")
    @PostMapping("/resume/update")
    public R<Boolean> updateResume(@RequestBody GzctLeaderResume record) { try { record.setUpdateTime(LocalDateTime.now()); leaderResumeMapper.updateById(record); return R.success(true); } catch (Exception e) { return R.fail("更新失败：" + e.getMessage()); } }
    @Operation(summary = "删除")
    @DeleteMapping("/resume/{id}")
    public R<Boolean> deleteResume(@PathVariable String id) { try { return R.success(leaderResumeMapper.deleteById(id) > 0); } catch (Exception e) { return R.fail("删除失败"); } }

    // ==================== 负责人任职 ====================
    @Operation(summary = "positionList")
    @PostMapping("/position/list")
    public R<PageResult<GzctLeaderPosition>> positionList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            LambdaQueryWrapper<GzctLeaderPosition> w = new LambdaQueryWrapper<>();
            if (params.get("leaderName") != null && StringUtils.isNotBlank(params.get("leaderName").toString())) w.like(GzctLeaderPosition::getLeaderName, params.get("leaderName").toString());
            if (params.get("leaderId") != null && StringUtils.isNotBlank(params.get("leaderId").toString())) w.eq(GzctLeaderPosition::getLeaderId, params.get("leaderId").toString());
            w.orderByDesc(GzctLeaderPosition::getCreateTime);
            Page<GzctLeaderPosition> r = new GzctLeaderPosition().selectPage(new Page<>(pn, ps), w);
            PageResult<GzctLeaderPosition> pr = new PageResult<>();
            pr.setTotalRecord((int) r.getTotal()); pr.setCurrentPage((int) r.getCurrent()); pr.setPageNumber((int) r.getCurrent()); pr.setTotalPage((int) r.getPages()); pr.setPageSize((int) r.getSize()); pr.setTlist(r.getRecords());
            return R.success(pr);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }
    @Operation(summary = "positionDetail")
    @GetMapping("/position/{id}")
    public R<GzctLeaderPosition> positionDetail(@PathVariable String id) { try { return R.success(leaderPositionMapper.selectById(id)); } catch (Exception e) { return R.fail("查询失败"); } }
    @Operation(summary = "新增")
    @PostMapping("/position/add")
    public R<Boolean> addPosition(@RequestBody GzctLeaderPosition record) { try { record.setCreateTime(LocalDateTime.now()); leaderPositionMapper.insert(record); return R.success(true); } catch (Exception e) { return R.fail("新增失败：" + e.getMessage()); } }
    @Operation(summary = "更新")
    @PostMapping("/position/update")
    public R<Boolean> updatePosition(@RequestBody GzctLeaderPosition record) { try { record.setUpdateTime(LocalDateTime.now()); leaderPositionMapper.updateById(record); return R.success(true); } catch (Exception e) { return R.fail("更新失败：" + e.getMessage()); } }
    @Operation(summary = "删除")
    @DeleteMapping("/position/{id}")
    public R<Boolean> deletePosition(@PathVariable String id) { try { return R.success(leaderPositionMapper.deleteById(id) > 0); } catch (Exception e) { return R.fail("删除失败"); } }

    // ==================== 负责人证书 ====================
    @Operation(summary = "certificateList")
    @PostMapping("/certificate/list")
    public R<PageResult<GzctLeaderCertificate>> certificateList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            LambdaQueryWrapper<GzctLeaderCertificate> w = new LambdaQueryWrapper<>();
            if (params.get("leaderName") != null && StringUtils.isNotBlank(params.get("leaderName").toString())) w.like(GzctLeaderCertificate::getLeaderName, params.get("leaderName").toString());
            if (params.get("leaderId") != null && StringUtils.isNotBlank(params.get("leaderId").toString())) w.eq(GzctLeaderCertificate::getLeaderId, params.get("leaderId").toString());
            w.orderByDesc(GzctLeaderCertificate::getCreateTime);
            Page<GzctLeaderCertificate> r = new GzctLeaderCertificate().selectPage(new Page<>(pn, ps), w);
            PageResult<GzctLeaderCertificate> pr = new PageResult<>();
            pr.setTotalRecord((int) r.getTotal()); pr.setCurrentPage((int) r.getCurrent()); pr.setPageNumber((int) r.getCurrent()); pr.setTotalPage((int) r.getPages()); pr.setPageSize((int) r.getSize()); pr.setTlist(r.getRecords());
            return R.success(pr);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }
    @Operation(summary = "certificateDetail")
    @GetMapping("/certificate/{id}")
    public R<GzctLeaderCertificate> certificateDetail(@PathVariable String id) { try { return R.success(leaderCertificateMapper.selectById(id)); } catch (Exception e) { return R.fail("查询失败"); } }
    @Operation(summary = "新增")
    @PostMapping("/certificate/add")
    public R<Boolean> addCertificate(@RequestBody GzctLeaderCertificate record) { try { record.setCreateTime(LocalDateTime.now()); leaderCertificateMapper.insert(record); return R.success(true); } catch (Exception e) { return R.fail("新增失败：" + e.getMessage()); } }
    @Operation(summary = "更新")
    @PostMapping("/certificate/update")
    public R<Boolean> updateCertificate(@RequestBody GzctLeaderCertificate record) { try { record.setUpdateTime(LocalDateTime.now()); leaderCertificateMapper.updateById(record); return R.success(true); } catch (Exception e) { return R.fail("更新失败：" + e.getMessage()); } }
    @Operation(summary = "删除")
    @DeleteMapping("/certificate/{id}")
    public R<Boolean> deleteCertificate(@PathVariable String id) { try { return R.success(leaderCertificateMapper.deleteById(id) > 0); } catch (Exception e) { return R.fail("删除失败"); } }

    // ==================== 负责人档案 ====================
    @Operation(summary = "archiveList")
    @PostMapping("/archive/list")
    public R<PageResult<GzctLeaderArchive>> archiveList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            LambdaQueryWrapper<GzctLeaderArchive> w = new LambdaQueryWrapper<>();
            if (params.get("leaderName") != null && StringUtils.isNotBlank(params.get("leaderName").toString())) w.like(GzctLeaderArchive::getLeaderName, params.get("leaderName").toString());
            if (params.get("leaderId") != null && StringUtils.isNotBlank(params.get("leaderId").toString())) w.eq(GzctLeaderArchive::getLeaderId, params.get("leaderId").toString());
            w.orderByDesc(GzctLeaderArchive::getCreateTime);
            Page<GzctLeaderArchive> r = new GzctLeaderArchive().selectPage(new Page<>(pn, ps), w);
            PageResult<GzctLeaderArchive> pr = new PageResult<>();
            pr.setTotalRecord((int) r.getTotal()); pr.setCurrentPage((int) r.getCurrent()); pr.setPageNumber((int) r.getCurrent()); pr.setTotalPage((int) r.getPages()); pr.setPageSize((int) r.getSize()); pr.setTlist(r.getRecords());
            return R.success(pr);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }
    @Operation(summary = "archiveDetail")
    @GetMapping("/archive/{id}")
    public R<GzctLeaderArchive> archiveDetail(@PathVariable String id) { try { return R.success(leaderArchiveMapper.selectById(id)); } catch (Exception e) { return R.fail("查询失败"); } }
    @Operation(summary = "新增")
    @PostMapping("/archive/add")
    public R<Boolean> addArchive(@RequestBody GzctLeaderArchive record) { try { record.setCreateTime(LocalDateTime.now()); leaderArchiveMapper.insert(record); return R.success(true); } catch (Exception e) { return R.fail("新增失败：" + e.getMessage()); } }
    @Operation(summary = "更新")
    @PostMapping("/archive/update")
    public R<Boolean> updateArchive(@RequestBody GzctLeaderArchive record) { try { record.setUpdateTime(LocalDateTime.now()); leaderArchiveMapper.updateById(record); return R.success(true); } catch (Exception e) { return R.fail("更新失败：" + e.getMessage()); } }
    @Operation(summary = "删除")
    @DeleteMapping("/archive/{id}")
    public R<Boolean> deleteArchive(@PathVariable String id) { try { return R.success(leaderArchiveMapper.deleteById(id) > 0); } catch (Exception e) { return R.fail("删除失败"); } }

    // ==================== 负责人变更记录 ====================
    @Operation(summary = "更新")
    @PostMapping("/changeRecord/list")
    public R<PageResult<GzctLeaderChangeRecord>> changeRecordList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            LambdaQueryWrapper<GzctLeaderChangeRecord> w = new LambdaQueryWrapper<>();
            if (params.get("leaderName") != null && StringUtils.isNotBlank(params.get("leaderName").toString())) w.like(GzctLeaderChangeRecord::getLeaderName, params.get("leaderName").toString());
            if (params.get("leaderId") != null && StringUtils.isNotBlank(params.get("leaderId").toString())) w.eq(GzctLeaderChangeRecord::getLeaderId, params.get("leaderId").toString());
            w.orderByDesc(GzctLeaderChangeRecord::getCreateTime);
            Page<GzctLeaderChangeRecord> r = new GzctLeaderChangeRecord().selectPage(new Page<>(pn, ps), w);
            PageResult<GzctLeaderChangeRecord> pr = new PageResult<>();
            pr.setTotalRecord((int) r.getTotal()); pr.setCurrentPage((int) r.getCurrent()); pr.setPageNumber((int) r.getCurrent()); pr.setTotalPage((int) r.getPages()); pr.setPageSize((int) r.getSize()); pr.setTlist(r.getRecords());
            return R.success(pr);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }
    @Operation(summary = "更新")
    @GetMapping("/changeRecord/{id}")
    public R<GzctLeaderChangeRecord> changeRecordDetail(@PathVariable String id) { try { return R.success(leaderChangeRecordMapper.selectById(id)); } catch (Exception e) { return R.fail("查询失败"); } }
    @Operation(summary = "新增")
    @PostMapping("/changeRecord/add")
    public R<Boolean> addChangeRecord(@RequestBody GzctLeaderChangeRecord record) { try { record.setCreateTime(LocalDateTime.now()); leaderChangeRecordMapper.insert(record); return R.success(true); } catch (Exception e) { return R.fail("新增失败：" + e.getMessage()); } }
    @Operation(summary = "更新")
    @PostMapping("/changeRecord/update")
    public R<Boolean> updateChangeRecord(@RequestBody GzctLeaderChangeRecord record) { try { record.setUpdateTime(LocalDateTime.now()); leaderChangeRecordMapper.updateById(record); return R.success(true); } catch (Exception e) { return R.fail("更新失败：" + e.getMessage()); } }
    @Operation(summary = "删除")
    @DeleteMapping("/changeRecord/{id}")
    public R<Boolean> deleteChangeRecord(@PathVariable String id) { try { return R.success(leaderChangeRecordMapper.deleteById(id) > 0); } catch (Exception e) { return R.fail("删除失败"); } }
}
