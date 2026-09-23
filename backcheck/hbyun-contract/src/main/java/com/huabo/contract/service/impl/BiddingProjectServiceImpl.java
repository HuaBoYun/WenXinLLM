package com.huabo.contract.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.huabo.contract.entity.BiddingProject;
import com.huabo.contract.mapper.BiddingProjectMapper;
import com.huabo.contract.service.BiddingProjectService;
import com.huabo.contract.vo.BiddingProjectQueryParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 招投标项目表 服务实现类
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Slf4j
@Service
public class BiddingProjectServiceImpl extends ServiceImpl<BiddingProjectMapper, BiddingProject> implements BiddingProjectService {

    @Autowired
    private BiddingProjectMapper biddingProjectMapper;

    @Override
    public PageInfo<BiddingProject> getBiddingProjectList(BiddingProjectQueryParam param) {
        try {
            log.info("分页查询招投标项目列表，参数：{}", param);
            
            // 设置分页参数
            PageHelper.startPage(param.getPageNumber(), param.getPageSize());
            
            // 查询数据
            List<BiddingProject> list = biddingProjectMapper.selectBiddingProjectList(param);
            
            return new PageInfo<>(list);
        } catch (Exception e) {
            log.error("分页查询招投标项目列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveBiddingProject(BiddingProject biddingProject) {
        try {
            log.info("保存招投标项目，项目：{}", biddingProject);
            
            Date now = new Date();
            
            if (biddingProject.getId() == null) {
                // 新增
                if (!StringUtils.hasText(biddingProject.getBiddingNo())) {
                    biddingProject.setBiddingNo(generateBiddingNo());
                }
                // 设置项目ID（使用雪花算法生成）
                if (biddingProject.getProjectId() == null) {
                    biddingProject.setProjectId(System.currentTimeMillis());
                }
                biddingProject.setCreateTime(now);
                biddingProject.setUpdateTime(now);
                // 设置创建人和更新人（默认为系统用户）
                if (biddingProject.getCreateBy() == null) {
                    biddingProject.setCreateBy(1L);
                }
                if (biddingProject.getUpdateBy() == null) {
                    biddingProject.setUpdateBy(1L);
                }
                return this.save(biddingProject);
            } else {
                // 修改
                biddingProject.setUpdateTime(now);
                return this.updateById(biddingProject);
            }
        } catch (Exception e) {
            log.error("保存招投标项目失败", e);
            throw new RuntimeException("保存失败：" + e.getMessage());
        }
    }

    @Override
    public BiddingProject getBiddingProjectById(Long id) {
        try {
            log.info("根据ID获取招投标项目详情，ID：{}", id);
            return this.getById(id);
        } catch (Exception e) {
            log.error("获取招投标项目详情失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public BiddingProject getBiddingProjectByProjectNo(String projectNo) {
        try {
            log.info("根据项目编号获取招投标项目，项目编号：{}", projectNo);
            return biddingProjectMapper.selectByProjectNo(projectNo);
        } catch (Exception e) {
            log.error("根据项目编号获取招投标项目失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBiddingProject(Long id) {
        try {
            log.info("删除招投标项目，ID：{}", id);
            return this.removeById(id);
        } catch (Exception e) {
            log.error("删除招投标项目失败", e);
            throw new RuntimeException("删除失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteBiddingProject(List<Long> ids) {
        try {
            log.info("批量删除招投标项目，ID列表：{}", ids);
            if (CollectionUtils.isEmpty(ids)) {
                return false;
            }
            return this.removeByIds(ids);
        } catch (Exception e) {
            log.error("批量删除招投标项目失败", e);
            throw new RuntimeException("批量删除失败：" + e.getMessage());
        }
    }

    @Override
    public String generateBiddingNo() {
        try {
            // 生成招标编号：BID + 年月日 + 4位序号
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String dateStr = sdf.format(new Date());
            String prefix = "BID" + dateStr;

            // 查询当天最大序号
            int maxSeq = 0;
            try {
                String maxBiddingNo = biddingProjectMapper.getMaxBiddingNoByDate(prefix);
                if (maxBiddingNo != null && maxBiddingNo.length() >= prefix.length() + 4) {
                    String seqStr = maxBiddingNo.substring(prefix.length());
                    maxSeq = Integer.parseInt(seqStr);
                }
            } catch (Exception e) {
                log.warn("查询当天最大序号失败，使用默认值0", e);
            }

            String seq = String.format("%04d", maxSeq + 1);
            return prefix + seq;
        } catch (Exception e) {
            log.error("生成招标编号失败", e);
            throw new RuntimeException("生成招标编号失败：" + e.getMessage());
        }
    }

    @Override
    public boolean existsBiddingNo(String biddingNo, Long excludeId) {
        try {
            log.info("检查招标编号是否存在，招标编号：{}，排除ID：{}", biddingNo, excludeId);
            return biddingProjectMapper.existsBiddingNo(biddingNo, excludeId);
        } catch (Exception e) {
            log.error("检查招标编号是否存在失败", e);
            throw new RuntimeException("检查失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateProjectStatus(Long id, Integer projectStatus) {
        try {
            log.info("更新项目状态，ID：{}，项目状态：{}", id, projectStatus);
            
            BiddingProject project = new BiddingProject();
            project.setId(id);
            project.setProjectStatus(projectStatus);
            project.setUpdateTime(new Date());
            
            return this.updateById(project);
        } catch (Exception e) {
            log.error("更新项目状态失败", e);
            throw new RuntimeException("更新失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateProjectStatus(List<Long> ids, Integer projectStatus) {
        try {
            log.info("批量更新项目状态，ID列表：{}，项目状态：{}", ids, projectStatus);
            if (CollectionUtils.isEmpty(ids)) {
                return false;
            }
            
            int count = biddingProjectMapper.batchUpdateProjectStatus(ids, projectStatus, null);
            return count > 0;
        } catch (Exception e) {
            log.error("批量更新项目状态失败", e);
            throw new RuntimeException("批量更新失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateParticipateStatus(Long id, Integer isParticipate, Integer participateStatus) {
        try {
            log.info("更新参与状态，ID：{}，是否参与：{}，参与状态：{}", id, isParticipate, participateStatus);
            
            int count = biddingProjectMapper.updateParticipateStatus(id, isParticipate, participateStatus);
            return count > 0;
        } catch (Exception e) {
            log.error("更新参与状态失败", e);
            throw new RuntimeException("更新失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateParticipateStatus(List<Long> ids, Integer isParticipate, Integer participateStatus) {
        try {
            log.info("批量更新参与状态，ID列表：{}，是否参与：{}，参与状态：{}", ids, isParticipate, participateStatus);
            if (CollectionUtils.isEmpty(ids)) {
                return false;
            }
            
            int count = biddingProjectMapper.batchUpdateParticipateStatus(ids, isParticipate, participateStatus, null);
            return count > 0;
        } catch (Exception e) {
            log.error("批量更新参与状态失败", e);
            throw new RuntimeException("批量更新失败：" + e.getMessage());
        }
    }

    @Override
    public List<BiddingProject> getCanBidProjects() {
        try {
            log.info("获取可投标的项目列表");
            return biddingProjectMapper.selectCanBidProjects();
        } catch (Exception e) {
            log.error("获取可投标的项目列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<BiddingProject> getExpiredProjects() {
        try {
            log.info("获取已过期的项目列表");
            return biddingProjectMapper.selectExpiredProjects();
        } catch (Exception e) {
            log.error("获取已过期的项目列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<BiddingProject> getKeyProjects(BigDecimal minBudgetAmount) {
        try {
            log.info("获取重点项目列表，最小预算金额：{}", minBudgetAmount);
            if (minBudgetAmount == null) {
                minBudgetAmount = new BigDecimal("10000000"); // 默认1000万
            }
            return biddingProjectMapper.selectKeyProjects(minBudgetAmount);
        } catch (Exception e) {
            log.error("获取重点项目列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<BiddingProject> getUrgentProjects() {
        try {
            log.info("获取紧急项目列表");
            return biddingProjectMapper.selectUrgentProjects();
        } catch (Exception e) {
            log.error("获取紧急项目列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<BiddingProject> getMyParticipateProjects(Long userId) {
        try {
            log.info("获取我参与的项目列表，用户ID：{}", userId);
            return biddingProjectMapper.selectMyParticipateProjects(userId);
        } catch (Exception e) {
            log.error("获取我参与的项目列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<BiddingProject> getWinningProjects(Long userId) {
        try {
            log.info("获取中标项目列表，用户ID：{}", userId);
            return biddingProjectMapper.selectWinningProjects(userId);
        } catch (Exception e) {
            log.error("获取中标项目列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<BiddingProject> getBiddingProjectByTendererId(Long tendererId) {
        try {
            log.info("根据招标方ID查询项目列表，招标方ID：{}", tendererId);
            return biddingProjectMapper.selectByTendererId(tendererId);
        } catch (Exception e) {
            log.error("根据招标方ID查询项目列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getBiddingProjectStatistics(BiddingProjectQueryParam param) {
        try {
            log.info("统计招投标项目数据，参数：{}", param);
            return biddingProjectMapper.selectBiddingProjectStatistics(param);
        } catch (Exception e) {
            log.error("统计招投标项目数据失败", e);
            throw new RuntimeException("统计失败：" + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getProjectTypeDistribution(BiddingProjectQueryParam param) {
        try {
            log.info("获取项目类型分布统计，参数：{}", param);
            return biddingProjectMapper.selectProjectTypeDistribution(param);
        } catch (Exception e) {
            log.error("获取项目类型分布统计失败", e);
            throw new RuntimeException("统计失败：" + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getProjectStatusDistribution(BiddingProjectQueryParam param) {
        try {
            log.info("获取项目状态分布统计，参数：{}", param);
            return biddingProjectMapper.selectProjectStatusDistribution(param);
        } catch (Exception e) {
            log.error("获取项目状态分布统计失败", e);
            throw new RuntimeException("统计失败：" + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getParticipateStatusDistribution(BiddingProjectQueryParam param) {
        try {
            log.info("获取参与状态分布统计，参数：{}", param);
            return biddingProjectMapper.selectParticipateStatusDistribution(param);
        } catch (Exception e) {
            log.error("获取参与状态分布统计失败", e);
            throw new RuntimeException("统计失败：" + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getMonthlyProjectTrend(BiddingProjectQueryParam param) {
        try {
            log.info("获取月度项目趋势，参数：{}", param);
            return biddingProjectMapper.selectMonthlyProjectTrend(param);
        } catch (Exception e) {
            log.error("获取月度项目趋势失败", e);
            throw new RuntimeException("统计失败：" + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getBudgetAmountDistribution(BiddingProjectQueryParam param) {
        try {
            log.info("获取预算金额分布统计，参数：{}", param);
            return biddingProjectMapper.selectBudgetAmountDistribution(param);
        } catch (Exception e) {
            log.error("获取预算金额分布统计失败", e);
            throw new RuntimeException("统计失败：" + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getTendererDistribution(BiddingProjectQueryParam param) {
        try {
            log.info("获取招标方分布统计，参数：{}", param);
            return biddingProjectMapper.selectTendererDistribution(param);
        } catch (Exception e) {
            log.error("获取招标方分布统计失败", e);
            throw new RuntimeException("统计失败：" + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getRegionDistribution(BiddingProjectQueryParam param) {
        try {
            log.info("获取地区分布统计，参数：{}", param);
            return biddingProjectMapper.selectRegionDistribution(param);
        } catch (Exception e) {
            log.error("获取地区分布统计失败", e);
            throw new RuntimeException("统计失败：" + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getIndustryDistribution(BiddingProjectQueryParam param) {
        try {
            log.info("获取行业分布统计，参数：{}", param);
            return biddingProjectMapper.selectIndustryDistribution(param);
        } catch (Exception e) {
            log.error("获取行业分布统计失败", e);
            throw new RuntimeException("统计失败：" + e.getMessage());
        }
    }

    @Override
    public List<BiddingProject> searchBiddingProjects(String keyword, Integer limit) {
        try {
            log.info("模糊搜索招投标项目，关键词：{}，限制数量：{}", keyword, limit);
            if (limit == null || limit <= 0) {
                limit = 10;
            }
            return biddingProjectMapper.searchBiddingProjects(keyword, limit);
        } catch (Exception e) {
            log.error("模糊搜索招投标项目失败", e);
            throw new RuntimeException("搜索失败：" + e.getMessage());
        }
    }

    @Override
    public List<BiddingProject> getProjectsExpiringSoon(Integer days) {
        try {
            log.info("获取即将截止的项目列表，天数：{}", days);
            if (days == null || days <= 0) {
                days = 7; // 默认7天
            }
            return biddingProjectMapper.selectProjectsExpiringSoon(days);
        } catch (Exception e) {
            log.error("获取即将截止的项目列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<BiddingProject> getTodayBidOpeningProjects() {
        try {
            log.info("获取今日开标的项目列表");
            return biddingProjectMapper.selectTodayBidOpeningProjects();
        } catch (Exception e) {
            log.error("获取今日开标的项目列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<BiddingProject> getThisWeekBidOpeningProjects() {
        try {
            log.info("获取本周开标的项目列表");
            return biddingProjectMapper.selectThisWeekBidOpeningProjects();
        } catch (Exception e) {
            log.error("获取本周开标的项目列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean participateBid(Long id) {
        try {
            log.info("参与投标，项目ID：{}", id);
            return updateParticipateStatus(id, 1, 1); // 参与投标，准备中
        } catch (Exception e) {
            log.error("参与投标失败", e);
            throw new RuntimeException("参与失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelParticipate(Long id) {
        try {
            log.info("取消参与投标，项目ID：{}", id);
            return updateParticipateStatus(id, 0, null); // 不参与投标
        } catch (Exception e) {
            log.error("取消参与投标失败", e);
            throw new RuntimeException("取消失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean markAsWinning(Long id) {
        try {
            log.info("标记为中标，项目ID：{}", id);
            return updateParticipateStatus(id, 1, 3); // 参与投标，中标
        } catch (Exception e) {
            log.error("标记为中标失败", e);
            throw new RuntimeException("标记失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean markAsNotWinning(Long id) {
        try {
            log.info("标记为未中标，项目ID：{}", id);
            return updateParticipateStatus(id, 1, 4); // 参与投标，未中标
        } catch (Exception e) {
            log.error("标记为未中标失败", e);
            throw new RuntimeException("标记失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importBiddingProjects(List<BiddingProject> projectList) {
        try {
            log.info("导入招投标项目，数量：{}", projectList.size());

            Map<String, Object> result = new HashMap<>();
            int successCount = 0;
            int failCount = 0;
            List<String> errorMessages = new ArrayList<>();

            for (BiddingProject project : projectList) {
                try {
                    // 设置默认值
                    if (!StringUtils.hasText(project.getBiddingNo())) {
                        project.setBiddingNo(generateBiddingNo());
                    }

                    // 检查招标编号是否重复
                    if (existsBiddingNo(project.getBiddingNo(), null)) {
                        failCount++;
                        errorMessages.add("招标编号 " + project.getBiddingNo() + " 已存在");
                        continue;
                    }

                    project.setCreateTime(new Date());
                    project.setUpdateTime(new Date());

                    if (this.save(project)) {
                        successCount++;
                    } else {
                        failCount++;
                        errorMessages.add("保存项目 " + project.getProjectName() + " 失败");
                    }
                } catch (Exception e) {
                    failCount++;
                    errorMessages.add("导入项目 " + project.getProjectName() + " 异常：" + e.getMessage());
                }
            }

            result.put("total", projectList.size());
            result.put("successCount", successCount);
            result.put("failCount", failCount);
            result.put("errorMessages", errorMessages);

            return result;
        } catch (Exception e) {
            log.error("导入招投标项目失败", e);
            throw new RuntimeException("导入失败：" + e.getMessage());
        }
    }

    @Override
    public List<BiddingProject> exportBiddingProjects(BiddingProjectQueryParam param) {
        try {
            log.info("导出招投标项目，参数：{}", param);
            return biddingProjectMapper.selectBiddingProjectList(param);
        } catch (Exception e) {
            log.error("导出招投标项目失败", e);
            throw new RuntimeException("导出失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int autoUpdateExpiredProjects() {
        try {
            log.info("自动更新过期项目状态");

            // 获取已过期但状态未更新的项目
            List<BiddingProject> expiredProjects = getExpiredProjects();
            if (CollectionUtils.isEmpty(expiredProjects)) {
                return 0;
            }

            List<Long> ids = new ArrayList<>();
            for (BiddingProject project : expiredProjects) {
                if (project.getProjectStatus() != null && project.getProjectStatus() < 6) {
                    ids.add(project.getId());
                }
            }

            if (CollectionUtils.isEmpty(ids)) {
                return 0;
            }

            // 批量更新为流标状态
            batchUpdateProjectStatus(ids, 7);
            return ids.size();
        } catch (Exception e) {
            log.error("自动更新过期项目状态失败", e);
            throw new RuntimeException("更新失败：" + e.getMessage());
        }
    }

    @Override
    public int sendDeadlineReminders(Integer days) {
        try {
            log.info("发送截止提醒，提前天数：{}", days);

            List<BiddingProject> projects = getProjectsExpiringSoon(days);
            if (CollectionUtils.isEmpty(projects)) {
                return 0;
            }

            // 这里可以添加发送提醒的逻辑，比如发送邮件、短信等
            // 暂时只记录日志
            for (BiddingProject project : projects) {
                log.info("项目 {} 即将截止，截止时间：{}", project.getProjectName(), project.getBidDeadline());
            }

            return projects.size();
        } catch (Exception e) {
            log.error("发送截止提醒失败", e);
            throw new RuntimeException("发送提醒失败：" + e.getMessage());
        }
    }
}
