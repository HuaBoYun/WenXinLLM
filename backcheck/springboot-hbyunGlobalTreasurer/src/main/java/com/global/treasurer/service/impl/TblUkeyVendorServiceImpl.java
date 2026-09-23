package com.global.treasurer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.global.treasurer.entity.TblUkeyVendor;
import com.global.treasurer.mapper.TblUkeyVendorMapper;
import com.global.treasurer.service.TblUkeyVendorService;
import com.global.treasurer.vo.param.TblUkeyVendorQueryParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Ukey厂商管理Service实现类
 *
 * @author 华博云开发团队
 * @since 2024-12-23
 */
@Service
public class TblUkeyVendorServiceImpl extends ServiceImpl<TblUkeyVendorMapper, TblUkeyVendor> implements TblUkeyVendorService {
    private static final Logger log = LoggerFactory.getLogger(TblUkeyVendorServiceImpl.class);

    @Autowired
    private TblUkeyVendorMapper tblUkeyVendorMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public IPage<TblUkeyVendor> selectPage(TblUkeyVendorQueryParam param) {
        // 使用PageHelper分页
        com.github.pagehelper.PageHelper.startPage(param.getPageNo(), param.getPageSize());

        // 构建查询条件
        QueryWrapper<TblUkeyVendor> queryWrapper = new QueryWrapper<>();
        if (StringUtils.hasText(param.getVendorCode())) {
            queryWrapper.like("VENDOR_CODE", param.getVendorCode());
        }
        if (StringUtils.hasText(param.getVendorName())) {
            queryWrapper.like("VENDOR_NAME", param.getVendorName());
        }
        if (StringUtils.hasText(param.getVendorType())) {
            queryWrapper.eq("VENDOR_TYPE", param.getVendorType());
        }
        if (StringUtils.hasText(param.getCooperationStatus())) {
            queryWrapper.eq("COOPERATION_STATUS", param.getCooperationStatus());
        }
        queryWrapper.orderByDesc("CREATE_TIME");

        // 查询列表(PageHelper会自动处理分页)
        List<TblUkeyVendor> list = this.list(queryWrapper);

        // 获取分页信息
        com.github.pagehelper.PageInfo<TblUkeyVendor> pageInfo =
            new com.github.pagehelper.PageInfo<>(list);

        // 转换为MyBatis-Plus的IPage格式
        Page<TblUkeyVendor> pageResult = new Page<>(param.getPageNo(), param.getPageSize());
        pageResult.setRecords(list);
        pageResult.setTotal(pageInfo.getTotal());
        pageResult.setCurrent(pageInfo.getPageNum());
        pageResult.setSize(pageInfo.getPageSize());

        return pageResult;
    }

    @Override
    public TblUkeyVendor selectById(String id) {
        return this.getById(Long.parseLong(id));
    }

    @Override
    public List<TblUkeyVendor> selectList(TblUkeyVendorQueryParam param) {
        QueryWrapper<TblUkeyVendor> queryWrapper = new QueryWrapper<>();

        // 添加查询条件
        if (StringUtils.hasText(param.getVendorCode())) {
            queryWrapper.like("VENDOR_CODE", param.getVendorCode());
        }
        if (StringUtils.hasText(param.getVendorName())) {
            queryWrapper.like("VENDOR_NAME", param.getVendorName());
        }
        if (StringUtils.hasText(param.getVendorType())) {
            queryWrapper.eq("VENDOR_TYPE", param.getVendorType());
        }
        if (StringUtils.hasText(param.getCooperationStatus())) {
            queryWrapper.eq("COOPERATION_STATUS", param.getCooperationStatus());
        }

        // 按创建时间倒序
        queryWrapper.orderByDesc("CREATE_TIME");

        return this.list(queryWrapper);
    }

    @Override
    public int insert(TblUkeyVendor vendor) {
        vendor.setId(System.currentTimeMillis() + new java.util.Random().nextInt(1000));
        vendor.setCreateTime(new java.util.Date());
        vendor.setUpdateTime(new java.util.Date());
        return this.save(vendor) ? 1 : 0;
    }

    @Override
    public int update(TblUkeyVendor vendor) {
        vendor.setUpdateTime(new java.util.Date());
        return this.updateById(vendor) ? 1 : 0;
    }

    @Override
    public int delete(String id) {
        return this.removeById(Long.parseLong(id)) ? 1 : 0;
    }

    @Override
    public Map<String, Object> getStatistics() {
        Map<String, Object> statistics = new HashMap<>();

        try {
            // 使用原生SQL查询避免MyBatis-Plus的count()方法类型转换问题
            // 获取总厂商数
            Integer totalVendors = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM TBL_UKEY_VENDOR", Integer.class);
            statistics.put("totalVendors", totalVendors != null ? totalVendors : 0);

            // 获取已认证厂商数 (CERTIFIED)
            Integer certifiedVendors = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM TBL_UKEY_VENDOR WHERE CERTIFICATION_STATUS = 'CERTIFIED'", Integer.class);
            statistics.put("certifiedVendors", certifiedVendors != null ? certifiedVendors : 0);

            // 获取活跃厂商数 (ACTIVE)
            Integer activeVendors = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM TBL_UKEY_VENDOR WHERE COOPERATION_STATUS = 'ACTIVE'", Integer.class);
            statistics.put("activeVendors", activeVendors != null ? activeVendors : 0);

            // 获取产品型号总数 - 从数据库查询所有产品型号
            List<String> productModelsList = jdbcTemplate.queryForList(
                "SELECT DISTINCT PRODUCT_MODELS FROM TBL_UKEY_VENDOR WHERE PRODUCT_MODELS IS NOT NULL", String.class);

            // 统计产品型号数量(逗号分隔的型号)
            Set<String> uniqueModels = new HashSet<>();
            for (String models : productModelsList) {
                if (models != null && !models.trim().isEmpty()) {
                    String[] modelArray = models.split(",");
                    for (String model : modelArray) {
                        String trimmedModel = model.trim();
                        if (!trimmedModel.isEmpty()) {
                            uniqueModels.add(trimmedModel);
                        }
                    }
                }
            }
            statistics.put("productModels", uniqueModels.size());

            log.info("统计查询成功: totalVendors={}, certifiedVendors={}, activeVendors={}, productModels={}",
                totalVendors, certifiedVendors, activeVendors, uniqueModels.size());

        } catch (Exception e) {
            log.error("查询统计数据失败", e);
            // 返回默认值避免前端报错
            statistics.put("totalVendors", 0);
            statistics.put("certifiedVendors", 0);
            statistics.put("activeVendors", 0);
            statistics.put("productModels", 0);
        }

        return statistics;
    }

    @Override
    public List<Map<String, String>> getVendorTypes() {
        List<Map<String, String>> vendorTypes = new ArrayList<>();

        Map<String, String> hardware = new HashMap<>();
        hardware.put("value", "HARDWARE");
        hardware.put("label", "硬件厂商");
        vendorTypes.add(hardware);

        Map<String, String> software = new HashMap<>();
        software.put("value", "SOFTWARE");
        software.put("label", "软件厂商");
        vendorTypes.add(software);

        Map<String, String> service = new HashMap<>();
        service.put("value", "SERVICE");
        service.put("label", "服务厂商");
        vendorTypes.add(service);

        return vendorTypes;
    }

    @Override
    public List<Map<String, String>> getVendorLevels() {
        List<Map<String, String>> vendorLevels = new ArrayList<>();

        Map<String, String> level1 = new HashMap<>();
        level1.put("value", "LEVEL1");
        level1.put("label", "一级厂商");
        vendorLevels.add(level1);

        Map<String, String> level2 = new HashMap<>();
        level2.put("value", "LEVEL2");
        level2.put("label", "二级厂商");
        vendorLevels.add(level2);

        Map<String, String> level3 = new HashMap<>();
        level3.put("value", "LEVEL3");
        level3.put("label", "三级厂商");
        vendorLevels.add(level3);

        return vendorLevels;
    }

    @Override
    public List<Map<String, String>> getCooperationStatuses() {
        List<Map<String, String>> cooperationStatuses = new ArrayList<>();

        Map<String, String> cooperation = new HashMap<>();
        cooperation.put("value", "COOPERATION");
        cooperation.put("label", "合作中");
        cooperationStatuses.add(cooperation);

        Map<String, String> terminated = new HashMap<>();
        terminated.put("value", "TERMINATED");
        terminated.put("label", "已终止");
        cooperationStatuses.add(terminated);

        Map<String, String> pending = new HashMap<>();
        pending.put("value", "PENDING");
        pending.put("label", "待合作");
        cooperationStatuses.add(pending);

        return cooperationStatuses;
    }

    @Override
    public List<Map<String, String>> getEvaluationLevels() {
        List<Map<String, String>> evaluationLevels = new ArrayList<>();

        Map<String, String> excellent = new HashMap<>();
        excellent.put("value", "EXCELLENT");
        excellent.put("label", "优秀");
        evaluationLevels.add(excellent);

        Map<String, String> good = new HashMap<>();
        good.put("value", "GOOD");
        good.put("label", "良好");
        evaluationLevels.add(good);

        Map<String, String> general = new HashMap<>();
        general.put("value", "GENERAL");
        general.put("label", "一般");
        evaluationLevels.add(general);

        Map<String, String> poor = new HashMap<>();
        poor.put("value", "POOR");
        poor.put("label", "较差");
        evaluationLevels.add(poor);

        return evaluationLevels;
    }
}