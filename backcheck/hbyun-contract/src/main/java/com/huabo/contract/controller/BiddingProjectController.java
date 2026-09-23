package com.huabo.contract.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.hbfk.util.JsonBean;
import com.huabo.contract.entity.BiddingProject;
import com.huabo.contract.service.BiddingProjectService;
import com.huabo.contract.vo.BiddingProjectQueryParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 招投标项目管理控制器
 *
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Slf4j
@RestController
@RequestMapping("/bidding")
@Tag(name="招投标项目管理",description="招投标项目管理")
@Validated
public class BiddingProjectController {

    @Autowired
    private BiddingProjectService biddingProjectService;

    /**
     * 分页查询招投标项目列表
     *
     * @param param 查询参数
     * @return 分页结果
     */
    @PostMapping("/list")
    @Operation(summary = "分页查询招投标项目列表", description = "支持多条件查询和分页")
    public String getBiddingProjectList(@RequestBody BiddingProjectQueryParam param) {
        try {
            log.info("分页查询招投标项目列表，参数：{}", param);

            // 设置默认分页参数
            if (param.getPageNumber() == null || param.getPageNumber() <= 0) {
                param.setPageNumber(1);
            }
            if (param.getPageSize() == null || param.getPageSize() <= 0) {
                param.setPageSize(20);
            }

            PageInfo<BiddingProject> pageInfo = biddingProjectService.getBiddingProjectList(param);

            return JsonBean.success("查询成功", pageInfo);
        } catch (Exception e) {
            log.error("查询招投标项目列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID获取招投标项目详情
     *
     * @param id 主键ID
     * @return 招投标项目详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取招投标项目详情", description = "根据ID获取详细信息")
    public String getBiddingProjectById(@PathVariable Long id) {
        try {
            log.info("获取招投标项目详情，ID：{}", id);

            if (id == null) {
                return JsonBean.error("ID不能为空");
            }

            BiddingProject biddingProject = biddingProjectService.getBiddingProjectById(id);
            if (biddingProject == null) {
                return JsonBean.error("招投标项目不存在");
            }

            return JsonBean.success("查询成功", biddingProject);
        } catch (Exception e) {
            log.error("获取招投标项目详情失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 保存招投标项目（新增或修改）
     *
     * @param biddingProject 招投标项目
     * @return 保存结果
     */
    @RequestMapping(value = "/save", method = {RequestMethod.POST, RequestMethod.PUT})
    @Operation(summary = "保存招投标项目", description = "新增或修改招投标项目")
    public String saveBiddingProject(@RequestBody BiddingProject biddingProject) {
        try {
            log.info("保存招投标项目，项目：{}", biddingProject);

            // 校验必填字段
            if (!StringUtils.hasText(biddingProject.getProjectName())) {
                return JsonBean.error("项目名称不能为空");
            }

            // 校验招标编号唯一性
            if (StringUtils.hasText(biddingProject.getBiddingNo())) {
                boolean exists = biddingProjectService.existsBiddingNo(
                    biddingProject.getBiddingNo(), biddingProject.getId());
                if (exists) {
                    return JsonBean.error("招标编号已存在，请重新输入");
                }
            }

            boolean result = biddingProjectService.saveBiddingProject(biddingProject);
            if (result) {
                return JsonBean.success("保存成功");
            } else {
                return JsonBean.error("保存失败");
            }
        } catch (Exception e) {
            log.error("保存招投标项目失败", e);
            return JsonBean.error("保存失败：" + e.getMessage());
        }
    }

    /**
     * 更新招投标项目
     *
     * @param id 项目ID
     * @param biddingProject 招投标项目
     * @return 更新结果
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新招投标项目", description = "根据ID更新招投标项目")
    public String updateBiddingProject(@PathVariable Long id, @RequestBody BiddingProject biddingProject) {
        try {
            log.info("更新招投标项目，ID：{}，项目：{}", id, biddingProject);

            if (id == null) {
                return JsonBean.error("项目ID不能为空");
            }

            // 设置ID和更新时间
            biddingProject.setId(id);
            biddingProject.setUpdateTime(new Date());

            boolean result = biddingProjectService.saveBiddingProject(biddingProject);
            if (result) {
                return JsonBean.success("更新成功");
            } else {
                return JsonBean.error("更新失败");
            }
        } catch (Exception e) {
            log.error("更新招投标项目失败", e);
            return JsonBean.error("更新失败：" + e.getMessage());
        }
    }

    /**
     * 删除招投标项目
     *
     * @param id 主键ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除招投标项目", description = "根据ID删除招投标项目")
    public String deleteBiddingProject(@PathVariable Long id) {
        try {
            log.info("删除招投标项目，ID：{}", id);

            if (id == null) {
                return JsonBean.error("ID不能为空");
            }

            boolean result = biddingProjectService.deleteBiddingProject(id);
            if (result) {
                return JsonBean.success("删除成功");
            } else {
                return JsonBean.error("删除失败");
            }
        } catch (Exception e) {
            log.error("删除招投标项目失败", e);
            return JsonBean.error("删除失败：" + e.getMessage());
        }
    }

    /**
     * 批量删除招投标项目
     *
     * @param ids 主键ID列表
     * @return 删除结果
     */
    @PostMapping("/batchDelete")
    @Operation(summary = "批量删除招投标项目", description = "根据ID列表批量删除招投标项目")
    public String batchDeleteBiddingProject(@RequestBody List<Long> ids) {
        try {
            log.info("批量删除招投标项目，ID列表：{}", ids);

            if (ids == null || ids.isEmpty()) {
                return JsonBean.error("ID列表不能为空");
            }

            boolean result = biddingProjectService.batchDeleteBiddingProject(ids);
            if (result) {
                return JsonBean.success("批量删除成功");
            } else {
                return JsonBean.error("批量删除失败");
            }
        } catch (Exception e) {
            log.error("批量删除招投标项目失败", e);
            return JsonBean.error("批量删除失败：" + e.getMessage());
        }
    }

    /**
     * 获取可投标的项目列表
     *
     * @return 可投标的项目列表
     */
    @GetMapping("/canBidProjects")
    @Operation(summary = "获取可投标的项目列表", description = "获取所有可投标的项目")
    public String getCanBidProjects() {
        try {
            log.info("获取可投标的项目列表");

            List<BiddingProject> projects = biddingProjectService.getCanBidProjects();
            return JsonBean.success("查询成功", projects);
        } catch (Exception e) {
            log.error("获取可投标的项目列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取已过期的项目列表
     *
     * @return 已过期的项目列表
     */
    @GetMapping("/expiredProjects")
    @Operation(summary = "获取已过期的项目列表", description = "获取所有已过期的项目")
    public String getExpiredProjects() {
        try {
            log.info("获取已过期的项目列表");

            List<BiddingProject> projects = biddingProjectService.getExpiredProjects();
            return JsonBean.success("查询成功", projects);
        } catch (Exception e) {
            log.error("获取已过期的项目列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取重点项目列表
     *
     * @return 重点项目列表
     */
    @GetMapping("/keyProjects")
    @Operation(summary = "获取重点项目列表", description = "获取所有重点项目")
    public String getKeyProjects() {
        try {
            log.info("获取重点项目列表");

            List<BiddingProject> projects = biddingProjectService.getKeyProjects(null);
            return JsonBean.success("查询成功", projects);
        } catch (Exception e) {
            log.error("获取重点项目列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取紧急项目列表
     *
     * @return 紧急项目列表
     */
    @GetMapping("/urgentProjects")
    @Operation(summary = "获取紧急项目列表", description = "获取所有紧急项目")
    public String getUrgentProjects() {
        try {
            log.info("获取紧急项目列表");

            List<BiddingProject> projects = biddingProjectService.getUrgentProjects();
            return JsonBean.success("查询成功", projects);
        } catch (Exception e) {
            log.error("获取紧急项目列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取我参与的项目列表
     *
     * @param userId 用户ID
     * @return 我参与的项目列表
     */
    @GetMapping("/myParticipateProjects/{userId}")
    @Operation(summary = "获取我参与的项目列表", description = "获取指定用户参与的项目")
    public String getMyParticipateProjects(@PathVariable Long userId) {
        try {
            log.info("获取我参与的项目列表，用户ID：{}", userId);

            if (userId == null) {
                return JsonBean.error("用户ID不能为空");
            }

            List<BiddingProject> projects = biddingProjectService.getMyParticipateProjects(userId);
            return JsonBean.success("查询成功", projects);
        } catch (Exception e) {
            log.error("获取我参与的项目列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取中标项目列表
     *
     * @param userId 用户ID（可选）
     * @return 中标项目列表
     */
    @GetMapping("/winningProjects")
    @Operation(summary = "获取中标项目列表", description = "获取所有中标项目")
    public String getWinningProjects(@RequestParam(required = false) Long userId) {
        try {
            log.info("获取中标项目列表，用户ID：{}", userId);

            List<BiddingProject> projects = biddingProjectService.getWinningProjects(userId);
            return JsonBean.success("查询成功", projects);
        } catch (Exception e) {
            log.error("获取中标项目列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 更新项目状态
     *
     * @param request 更新请求
     * @return 更新结果
     */
    @PostMapping("/updateProjectStatus")
    @Operation(summary = "更新项目状态", description = "更新招投标项目的状态")
    public String updateProjectStatus(@RequestBody Map<String, Object> request) {
        try {
            Long id = Long.valueOf(request.get("id").toString());
            Integer projectStatus = (Integer) request.get("projectStatus");

            log.info("更新项目状态，ID：{}，项目状态：{}", id, projectStatus);

            if (id == null) {
                return JsonBean.error("ID不能为空");
            }
            if (projectStatus == null) {
                return JsonBean.error("项目状态不能为空");
            }

            boolean result = biddingProjectService.updateProjectStatus(id, projectStatus);
            if (result) {
                return JsonBean.success("状态更新成功");
            } else {
                return JsonBean.error("状态更新失败");
            }
        } catch (Exception e) {
            log.error("更新项目状态失败", e);
            return JsonBean.error("状态更新失败：" + e.getMessage());
        }
    }

    /**
     * 批量更新项目状态
     *
     * @param request 更新请求
     * @return 更新结果
     */
    @PostMapping("/batchUpdateProjectStatus")
    @Operation(summary = "批量更新项目状态", description = "批量更新招投标项目的状态")
    public String batchUpdateProjectStatus(@RequestBody Map<String, Object> request) {
        try {
            @SuppressWarnings("unchecked")
            List<Long> ids = (List<Long>) request.get("ids");
            Integer projectStatus = (Integer) request.get("projectStatus");

            log.info("批量更新项目状态，ID列表：{}，项目状态：{}", ids, projectStatus);

            if (ids == null || ids.isEmpty()) {
                return JsonBean.error("ID列表不能为空");
            }
            if (projectStatus == null) {
                return JsonBean.error("项目状态不能为空");
            }

            boolean result = biddingProjectService.batchUpdateProjectStatus(ids, projectStatus);
            if (result) {
                return JsonBean.success("批量状态更新成功");
            } else {
                return JsonBean.error("批量状态更新失败");
            }
        } catch (Exception e) {
            log.error("批量更新项目状态失败", e);
            return JsonBean.error("批量状态更新失败：" + e.getMessage());
        }
    }

    /**
     * 参与投标
     *
     * @param id 项目ID
     * @return 参与结果
     */
    @PostMapping("/participateBid/{id}")
    @Operation(summary = "参与投标", description = "标记项目为参与投标")
    public String participateBid(@PathVariable Long id) {
        try {
            log.info("参与投标，项目ID：{}", id);

            if (id == null) {
                return JsonBean.error("项目ID不能为空");
            }

            boolean result = biddingProjectService.participateBid(id);
            if (result) {
                return JsonBean.success("参与投标成功");
            } else {
                return JsonBean.error("参与投标失败");
            }
        } catch (Exception e) {
            log.error("参与投标失败", e);
            return JsonBean.error("参与投标失败：" + e.getMessage());
        }
    }

    /**
     * 取消参与投标
     *
     * @param id 项目ID
     * @return 取消结果
     */
    @PostMapping("/cancelParticipate/{id}")
    @Operation(summary = "取消参与投标", description = "取消项目的参与投标状态")
    public String cancelParticipate(@PathVariable Long id) {
        try {
            log.info("取消参与投标，项目ID：{}", id);

            if (id == null) {
                return JsonBean.error("项目ID不能为空");
            }

            boolean result = biddingProjectService.cancelParticipate(id);
            if (result) {
                return JsonBean.success("取消参与成功");
            } else {
                return JsonBean.error("取消参与失败");
            }
        } catch (Exception e) {
            log.error("取消参与投标失败", e);
            return JsonBean.error("取消参与失败：" + e.getMessage());
        }
    }

    /**
     * 标记为中标
     *
     * @param id 项目ID
     * @return 标记结果
     */
    @PostMapping("/markAsWinning/{id}")
    @Operation(summary = "标记为中标", description = "标记项目为中标状态")
    public String markAsWinning(@PathVariable Long id) {
        try {
            log.info("标记为中标，项目ID：{}", id);

            if (id == null) {
                return JsonBean.error("项目ID不能为空");
            }

            boolean result = biddingProjectService.markAsWinning(id);
            if (result) {
                return JsonBean.success("标记中标成功");
            } else {
                return JsonBean.error("标记中标失败");
            }
        } catch (Exception e) {
            log.error("标记为中标失败", e);
            return JsonBean.error("标记中标失败：" + e.getMessage());
        }
    }

    /**
     * 标记为未中标
     *
     * @param id 项目ID
     * @return 标记结果
     */
    @PostMapping("/markAsNotWinning/{id}")
    @Operation(summary = "标记为未中标", description = "标记项目为未中标状态")
    public String markAsNotWinning(@PathVariable Long id) {
        try {
            log.info("标记为未中标，项目ID：{}", id);

            if (id == null) {
                return JsonBean.error("项目ID不能为空");
            }

            boolean result = biddingProjectService.markAsNotWinning(id);
            if (result) {
                return JsonBean.success("标记未中标成功");
            } else {
                return JsonBean.error("标记未中标失败");
            }
        } catch (Exception e) {
            log.error("标记为未中标失败", e);
            return JsonBean.error("标记未中标失败：" + e.getMessage());
        }
    }

    /**
     * 模糊搜索招投标项目
     *
     * @param keyword 关键词
     * @param limit 限制数量
     * @return 招投标项目列表
     */
    @GetMapping("/search")
    @Operation(summary = "搜索招投标项目", description = "根据关键词模糊搜索招投标项目")
    public String searchBiddingProjects(@RequestParam String keyword,
                                       @RequestParam(defaultValue = "10") Integer limit) {
        try {
            log.info("搜索招投标项目，关键词：{}，限制数量：{}", keyword, limit);

            if (!StringUtils.hasText(keyword)) {
                return JsonBean.error("搜索关键词不能为空");
            }

            List<BiddingProject> projects = biddingProjectService.searchBiddingProjects(keyword, limit);
            return JsonBean.success("搜索成功", projects);
        } catch (Exception e) {
            log.error("搜索招投标项目失败", e);
            return JsonBean.error("搜索失败：" + e.getMessage());
        }
    }

    /**
     * 获取招投标项目统计数据
     *
     * @param param 查询参数
     * @return 统计数据
     */
    @PostMapping("/statistics")
    @Operation(summary = "获取招投标项目统计数据", description = "获取招投标项目的统计信息")
    public String getBiddingProjectStatistics(@RequestBody BiddingProjectQueryParam param) {
        try {
            log.info("获取招投标项目统计数据，参数：{}", param);

            Map<String, Object> statistics = biddingProjectService.getBiddingProjectStatistics(param);
            return JsonBean.success("查询成功", statistics);
        } catch (Exception e) {
            log.error("获取招投标项目统计数据失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取项目类型分布统计
     *
     * @param param 查询参数
     * @return 项目类型分布
     */
    @PostMapping("/projectTypeDistribution")
    @Operation(summary = "获取项目类型分布统计", description = "统计各项目类型的分布情况")
    public String getProjectTypeDistribution(@RequestBody BiddingProjectQueryParam param) {
        try {
            log.info("获取项目类型分布统计，参数：{}", param);

            List<Map<String, Object>> distribution = biddingProjectService.getProjectTypeDistribution(param);
            return JsonBean.success("查询成功", distribution);
        } catch (Exception e) {
            log.error("获取项目类型分布统计失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取项目状态分布统计
     *
     * @param param 查询参数
     * @return 项目状态分布
     */
    @PostMapping("/projectStatusDistribution")
    @Operation(summary = "获取项目状态分布统计", description = "统计各项目状态的分布情况")
    public String getProjectStatusDistribution(@RequestBody BiddingProjectQueryParam param) {
        try {
            log.info("获取项目状态分布统计，参数：{}", param);

            List<Map<String, Object>> distribution = biddingProjectService.getProjectStatusDistribution(param);
            return JsonBean.success("查询成功", distribution);
        } catch (Exception e) {
            log.error("获取项目状态分布统计失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取今日开标的项目列表
     *
     * @return 今日开标的项目列表
     */
    @GetMapping("/todayBidOpeningProjects")
    @Operation(summary = "获取今日开标的项目列表", description = "获取今天需要开标的项目")
    public String getTodayBidOpeningProjects() {
        try {
            log.info("获取今日开标的项目列表");

            List<BiddingProject> projects = biddingProjectService.getTodayBidOpeningProjects();
            return JsonBean.success("查询成功", projects);
        } catch (Exception e) {
            log.error("获取今日开标的项目列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取本周开标的项目列表
     *
     * @return 本周开标的项目列表
     */
    @GetMapping("/thisWeekBidOpeningProjects")
    @Operation(summary = "获取本周开标的项目列表", description = "获取本周需要开标的项目")
    public String getThisWeekBidOpeningProjects() {
        try {
            log.info("获取本周开标的项目列表");

            List<BiddingProject> projects = biddingProjectService.getThisWeekBidOpeningProjects();
            return JsonBean.success("查询成功", projects);
        } catch (Exception e) {
            log.error("获取本周开标的项目列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取即将截止的项目列表
     *
     * @param days 天数
     * @return 即将截止的项目列表
     */
    @GetMapping("/projectsExpiringSoon")
    @Operation(summary = "获取即将截止的项目列表", description = "获取即将截止的项目")
    public String getProjectsExpiringSoon(@RequestParam(defaultValue = "7") Integer days) {
        try {
            log.info("获取即将截止的项目列表，天数：{}", days);

            List<BiddingProject> projects = biddingProjectService.getProjectsExpiringSoon(days);
            return JsonBean.success("查询成功", projects);
        } catch (Exception e) {
            log.error("获取即将截止的项目列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 生成项目编号
     *
     * @return 项目编号
     */
    @GetMapping("/generateProjectNo")
    @Operation(summary = "生成项目编号", description = "自动生成项目编号")
    public String generateProjectNo() {
        try {
            log.info("生成招标编号");

            String biddingNo = biddingProjectService.generateBiddingNo();
            return JsonBean.success("生成成功", biddingNo);
        } catch (Exception e) {
            log.error("生成招标编号失败", e);
            return JsonBean.error("生成失败：" + e.getMessage());
        }
    }
}
