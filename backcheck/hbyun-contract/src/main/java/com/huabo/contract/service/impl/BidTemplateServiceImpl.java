package com.huabo.contract.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.huabo.contract.entity.BidTemplate;
import com.huabo.contract.mapper.BidTemplateMapper;
import com.huabo.contract.service.BidTemplateService;
import com.huabo.contract.vo.BidTemplateQueryParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 标书模板表 服务实现类
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Slf4j
@Service
public class BidTemplateServiceImpl extends ServiceImpl<BidTemplateMapper, BidTemplate> implements BidTemplateService {

    @Autowired
    private BidTemplateMapper bidTemplateMapper;

    @Override
    public PageInfo<BidTemplate> getBidTemplateList(BidTemplateQueryParam param) {
        try {
            log.info("分页查询标书模板列表，参数：{}", param);
            
            // 设置分页参数
            PageHelper.startPage(param.getPageNumber(), param.getPageSize());
            
            // 查询数据
            List<BidTemplate> list = bidTemplateMapper.selectBidTemplateList(param);
            
            return new PageInfo<>(list);
        } catch (Exception e) {
            log.error("分页查询标书模板列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveBidTemplate(BidTemplate bidTemplate) {
        try {
            log.info("保存标书模板，模板：{}", bidTemplate);
            
            if (bidTemplate.getId() == null) {
                // 新增
                return this.save(bidTemplate);
            } else {
                // 修改
                return this.updateById(bidTemplate);
            }
        } catch (Exception e) {
            log.error("保存标书模板失败", e);
            throw new RuntimeException("保存失败：" + e.getMessage());
        }
    }

    @Override
    public BidTemplate getBidTemplateById(Long id) {
        try {
            log.info("根据ID获取标书模板详情，ID：{}", id);
            return this.getById(id);
        } catch (Exception e) {
            log.error("根据ID获取标书模板详情失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public BidTemplate getBidTemplateByTemplateNo(String templateNo) {
        try {
            log.info("根据模板编号获取标书模板，编号：{}", templateNo);
            return bidTemplateMapper.selectByTemplateNo(templateNo);
        } catch (Exception e) {
            log.error("根据模板编号获取标书模板失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBidTemplate(Long id) {
        try {
            log.info("删除标书模板，ID：{}", id);
            return this.removeById(id);
        } catch (Exception e) {
            log.error("删除标书模板失败", e);
            throw new RuntimeException("删除失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteBidTemplate(List<Long> ids) {
        try {
            log.info("批量删除标书模板，IDs：{}", ids);
            return this.removeByIds(ids);
        } catch (Exception e) {
            log.error("批量删除标书模板失败", e);
            throw new RuntimeException("批量删除失败：" + e.getMessage());
        }
    }

    @Override
    public boolean existsTemplateNo(String templateNo, Long excludeId) {
        try {
            log.info("检查模板编号是否存在，编号：{}，排除ID：{}", templateNo, excludeId);
            return bidTemplateMapper.existsTemplateNo(templateNo, excludeId);
        } catch (Exception e) {
            log.error("检查模板编号是否存在失败", e);
            throw new RuntimeException("检查失败：" + e.getMessage());
        }
    }

    @Override
    public List<BidTemplate> getBidTemplatesByType(Integer templateType) {
        try {
            log.info("根据模板类型获取模板列表，类型：{}", templateType);
            return bidTemplateMapper.selectByTemplateType(templateType);
        } catch (Exception e) {
            log.error("根据模板类型获取模板列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateTemplateStatus(Long id, Integer enabled) {
        try {
            log.info("更新模板启用状态，ID：{}，启用状态：{}", id, enabled);
            
            BidTemplate template = new BidTemplate();
            template.setId(id);
            template.setIsEnabled(enabled);
            template.setUpdateTime(new Date());
            
            return this.updateById(template);
        } catch (Exception e) {
            log.error("更新模板启用状态失败", e);
            throw new RuntimeException("更新失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BidTemplate copyBidTemplate(Long sourceId) {
        try {
            log.info("复制标书模板，源ID：{}", sourceId);
            
            BidTemplate sourceTemplate = this.getById(sourceId);
            if (sourceTemplate == null) {
                throw new RuntimeException("源模板不存在");
            }
            
            BidTemplate newTemplate = new BidTemplate();
            BeanUtils.copyProperties(sourceTemplate, newTemplate);
            
            // 重置关键字段
            newTemplate.setId(null);
            newTemplate.setTemplateName(sourceTemplate.getTemplateName() + "_副本");
            newTemplate.setTemplateNo(generateTemplateNo());
            newTemplate.setCreateTime(new Date());
            newTemplate.setUpdateTime(new Date());
            newTemplate.setUsageCount(0);
            newTemplate.setDownloadCount(0);
            newTemplate.setIsDefault(0);
            
            boolean result = this.save(newTemplate);
            return result ? newTemplate : null;
        } catch (Exception e) {
            log.error("复制标书模板失败", e);
            throw new RuntimeException("复制失败：" + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getTemplateStatistics() {
        try {
            log.info("获取模板统计信息");
            
            Map<String, Object> statistics = new HashMap<>();
            
            // 总模板数
            long totalCount = this.count();
            statistics.put("totalCount", totalCount);
            
            // 启用模板数
            long enabledCount = this.lambdaQuery()
                .eq(BidTemplate::getIsEnabled, 1)
                .count();
            statistics.put("enabledCount", enabledCount);
            
            // 各类型模板数量
            Map<String, Object> typeDistribution = getTemplateTypeDistribution();
            statistics.put("typeDistribution", typeDistribution);
            
            return statistics;
        } catch (Exception e) {
            log.error("获取模板统计信息失败", e);
            throw new RuntimeException("获取统计信息失败：" + e.getMessage());
        }
    }

    @Override
    public String generateTemplateNo() {
        try {
            log.info("生成模板编号");
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String dateStr = sdf.format(new Date());
            
            // 查询当天已有的模板数量
            String prefix = "TPL" + dateStr;
            long count = this.lambdaQuery()
                .likeRight(BidTemplate::getTemplateNo, prefix)
                .count();
            
            return prefix + String.format("%03d", count + 1);
        } catch (Exception e) {
            log.error("生成模板编号失败", e);
            throw new RuntimeException("生成编号失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUsageCount(Long id) {
        try {
            log.info("更新模板使用次数，ID：{}", id);
            
            BidTemplate template = this.getById(id);
            if (template != null) {
                template.setUsageCount(template.getUsageCount() + 1);
                template.setLastUsedDate(new Date());
                return this.updateById(template);
            }
            return false;
        } catch (Exception e) {
            log.error("更新模板使用次数失败", e);
            throw new RuntimeException("更新失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateDownloadCount(Long id) {
        try {
            log.info("更新模板下载次数，ID：{}", id);
            
            BidTemplate template = this.getById(id);
            if (template != null) {
                template.setDownloadCount(template.getDownloadCount() + 1);
                return this.updateById(template);
            }
            return false;
        } catch (Exception e) {
            log.error("更新模板下载次数失败", e);
            throw new RuntimeException("更新失败：" + e.getMessage());
        }
    }

    @Override
    public List<BidTemplate> getPopularTemplates(Integer limit) {
        try {
            log.info("获取热门模板列表，限制数量：{}", limit);
            
            return this.lambdaQuery()
                .eq(BidTemplate::getIsEnabled, 1)
                .orderByDesc(BidTemplate::getUsageCount)
                .orderByDesc(BidTemplate::getDownloadCount)
                .last("LIMIT " + (limit != null ? limit : 10))
                .list();
        } catch (Exception e) {
            log.error("获取热门模板列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<BidTemplate> getLatestTemplates(Integer limit) {
        try {
            log.info("获取最新模板列表，限制数量：{}", limit);
            
            return this.lambdaQuery()
                .eq(BidTemplate::getIsEnabled, 1)
                .orderByDesc(BidTemplate::getCreateTime)
                .last("LIMIT " + (limit != null ? limit : 10))
                .list();
        } catch (Exception e) {
            log.error("获取最新模板列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<BidTemplate> searchBidTemplates(String keyword, Integer limit) {
        try {
            log.info("搜索标书模板，关键词：{}，限制数量：{}", keyword, limit);
            
            return this.lambdaQuery()
                .eq(BidTemplate::getIsEnabled, 1)
                .and(wrapper -> wrapper
                    .like(BidTemplate::getTemplateName, keyword)
                    .or()
                    .like(BidTemplate::getTemplateDescription, keyword)
                    .or()
                    .like(BidTemplate::getKeywords, keyword))
                .orderByDesc(BidTemplate::getUsageCount)
                .last("LIMIT " + (limit != null ? limit : 20))
                .list();
        } catch (Exception e) {
            log.error("搜索标书模板失败", e);
            throw new RuntimeException("搜索失败：" + e.getMessage());
        }
    }

    @Override
    public List<BidTemplate> getRecommendedTemplates(Integer templateType, Integer limit) {
        try {
            log.info("获取推荐模板列表，类型：{}，限制数量：{}", templateType, limit);
            
            return this.lambdaQuery()
                .eq(BidTemplate::getIsEnabled, 1)
                .eq(templateType != null, BidTemplate::getTemplateType, templateType)
                .orderByDesc(BidTemplate::getRatingScore)
                .orderByDesc(BidTemplate::getUsageCount)
                .last("LIMIT " + (limit != null ? limit : 10))
                .list();
        } catch (Exception e) {
            log.error("获取推荐模板列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateTemplateRating(Long id, Double rating) {
        try {
            log.info("更新模板评分，ID：{}，评分：{}", id, rating);
            
            BidTemplate template = this.getById(id);
            if (template != null) {
                // 计算新的平均评分
                double currentScore = template.getRatingScore() != null ? template.getRatingScore() : 0.0;
                int currentCount = template.getRatingCount() != null ? template.getRatingCount() : 0;
                
                double newScore = (currentScore * currentCount + rating) / (currentCount + 1);
                
                template.setRatingScore(newScore);
                template.setRatingCount(currentCount + 1);
                
                return this.updateById(template);
            }
            return false;
        } catch (Exception e) {
            log.error("更新模板评分失败", e);
            throw new RuntimeException("更新失败：" + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getTemplateTypeDistribution() {
        try {
            log.info("获取模板类型分布统计");
            
            Map<String, Object> distribution = new HashMap<>();
            
            // 技术标
            long techCount = this.lambdaQuery()
                .eq(BidTemplate::getTemplateType, 1)
                .count();
            distribution.put("techCount", techCount);
            
            // 商务标
            long businessCount = this.lambdaQuery()
                .eq(BidTemplate::getTemplateType, 2)
                .count();
            distribution.put("businessCount", businessCount);
            
            // 资格标
            long qualificationCount = this.lambdaQuery()
                .eq(BidTemplate::getTemplateType, 3)
                .count();
            distribution.put("qualificationCount", qualificationCount);
            
            // 综合标
            long comprehensiveCount = this.lambdaQuery()
                .eq(BidTemplate::getTemplateType, 4)
                .count();
            distribution.put("comprehensiveCount", comprehensiveCount);
            
            return distribution;
        } catch (Exception e) {
            log.error("获取模板类型分布统计失败", e);
            throw new RuntimeException("获取统计失败：" + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getTemplateUsageStatistics() {
        try {
            log.info("获取模板使用情况统计");
            
            Map<String, Object> statistics = new HashMap<>();
            
            // 总使用次数
            Integer totalUsage = this.lambdaQuery()
                .select(BidTemplate::getUsageCount)
                .list()
                .stream()
                .mapToInt(template -> template.getUsageCount() != null ? template.getUsageCount() : 0)
                .sum();
            statistics.put("totalUsage", totalUsage);
            
            // 总下载次数
            Integer totalDownloads = this.lambdaQuery()
                .select(BidTemplate::getDownloadCount)
                .list()
                .stream()
                .mapToInt(template -> template.getDownloadCount() != null ? template.getDownloadCount() : 0)
                .sum();
            statistics.put("totalDownloads", totalDownloads);
            
            return statistics;
        } catch (Exception e) {
            log.error("获取模板使用情况统计失败", e);
            throw new RuntimeException("获取统计失败：" + e.getMessage());
        }
    }

    @Override
    public boolean canDeleteTemplate(Long id) {
        try {
            log.info("检查模板是否可以删除，ID：{}", id);
            
            BidTemplate template = this.getById(id);
            if (template == null) {
                return false;
            }
            
            // 如果是默认模板，不能删除
            if (template.getIsDefault() != null && template.getIsDefault() == 1) {
                return false;
            }
            
            // 如果使用次数过多，建议不删除
            if (template.getUsageCount() != null && template.getUsageCount() > 100) {
                return false;
            }
            
            return true;
        } catch (Exception e) {
            log.error("检查模板是否可以删除失败", e);
            return false;
        }
    }

    // 其他方法的实现将在下一个文件中继续...
    
    @Override
    public List<BidTemplate> getTemplateVersionHistory(String templateNo) {
        // TODO: 实现版本历史查询
        return new ArrayList<>();
    }

    @Override
    public boolean setDefaultTemplate(Long id, Integer templateType) {
        // TODO: 实现设置默认模板
        return false;
    }

    @Override
    public BidTemplate getDefaultTemplate(Integer templateType) {
        // TODO: 实现获取默认模板
        return null;
    }

    @Override
    public Map<String, Object> exportTemplate(Long id) {
        // TODO: 实现模板导出
        return new HashMap<>();
    }

    @Override
    public boolean importTemplate(Map<String, Object> templateData) {
        // TODO: 实现模板导入
        return false;
    }

    @Override
    public Map<String, Object> validateTemplate(BidTemplate bidTemplate) {
        // TODO: 实现模板验证
        return new HashMap<>();
    }

    @Override
    public Map<String, Object> getTemplatePreview(Long id) {
        // TODO: 实现模板预览
        return new HashMap<>();
    }

    @Override
    public boolean updateLastUsedDate(Long id) {
        try {
            BidTemplate template = new BidTemplate();
            template.setId(id);
            template.setLastUsedDate(new Date());
            return this.updateById(template);
        } catch (Exception e) {
            log.error("更新模板最后使用时间失败", e);
            return false;
        }
    }
}
