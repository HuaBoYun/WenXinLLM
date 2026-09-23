package com.global.treasurer.financialProductDefinition.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.global.treasurer.financialProductDefinition.entity.TblFinancialCategory;
import com.global.treasurer.financialProductDefinition.entity.TblFinancialProduct;
import com.global.treasurer.financialProductDefinition.mapper.TblFinancialCategoryMapper;
import com.global.treasurer.financialProductDefinition.mapper.TblFinancialProductMapper;
import com.global.treasurer.financialProductDefinition.service.TblFinancialProductService;
import com.hbfk.util.SnowflakeIdWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * 理财产品管理Service实现类
 *
 * @author 华博云开发团队
 * @since 2026-01-22
 */
@Service
public class TblFinancialProductServiceImpl extends ServiceImpl<TblFinancialProductMapper, TblFinancialProduct>
        implements TblFinancialProductService {
    private static final Logger log = LoggerFactory.getLogger(TblFinancialProductServiceImpl.class);

    @Resource
    private SnowflakeIdWorker snowflakeIdWorker;

    @Resource
    private TblFinancialCategoryMapper categoryMapper;

    @Override
    public IPage<TblFinancialProduct> getPage(Integer pageNo, Integer pageSize, Map<String, Object> params) {
        log.info("=== 金融产品分页查询开始 ===");
        log.info("pageNo={}, pageSize={}", pageNo, pageSize);
        log.info("params={}", params);

        // 先查询表总数
        long totalCount = this.count();
        log.info("=== TBL_FINANCIAL_PRODUCT 表总记录数 === {}", totalCount);

        Page<TblFinancialProduct> page = new Page<>(pageNo, pageSize);
        QueryWrapper<TblFinancialProduct> wrapper = new QueryWrapper<>();

        // 构建查询条件
        if (params.get("orgId") != null) {
            wrapper.eq("ORG_ID", params.get("orgId"));
            log.info("添加查询条件: ORG_ID = {}", params.get("orgId"));
        } else {
            log.warn("=== orgId为null,不限制ORG_ID查询条件 ===");
        }
        if (params.get("productCode") != null && !"".equals(params.get("productCode"))) {
            wrapper.like("PRODUCT_CODE", params.get("productCode"));
            log.info("添加查询条件: PRODUCT_CODE LIKE {}", params.get("productCode"));
        }
        if (params.get("productName") != null && !"".equals(params.get("productName"))) {
            wrapper.like("PRODUCT_NAME", params.get("productName"));
            log.info("添加查询条件: PRODUCT_NAME LIKE {}", params.get("productName"));
        }
        if (params.get("productType") != null && !"".equals(params.get("productType"))) {
            wrapper.eq("PRODUCT_TYPE", params.get("productType"));
            log.info("添加查询条件: PRODUCT_TYPE = {}", params.get("productType"));
        }
        if (params.get("categoryId") != null) {
            wrapper.eq("CATEGORY_ID", params.get("categoryId"));
            log.info("添加查询条件: CATEGORY_ID = {}", params.get("categoryId"));
        }
        if (params.get("issuer") != null && !"".equals(params.get("issuer"))) {
            wrapper.like("ISSUER", params.get("issuer"));
            log.info("添加查询条件: ISSUER LIKE {}", params.get("issuer"));
        }
        if (params.get("currencyCode") != null && !"".equals(params.get("currencyCode"))) {
            wrapper.eq("CURRENCY_CODE", params.get("currencyCode"));
            log.info("添加查询条件: CURRENCY_CODE = {}", params.get("currencyCode"));
        }
        if (params.get("riskLevel") != null && !"".equals(params.get("riskLevel"))) {
            wrapper.eq("RISK_LEVEL", params.get("riskLevel"));
            log.info("添加查询条件: RISK_LEVEL = {}", params.get("riskLevel"));
        }
        if (params.get("productStatus") != null && !"".equals(params.get("productStatus"))) {
            wrapper.eq("PRODUCT_STATUS", params.get("productStatus"));
            log.info("添加查询条件: PRODUCT_STATUS = {}", params.get("productStatus"));
        }
        if (params.get("shelfStatus") != null && !"".equals(params.get("shelfStatus"))) {
            wrapper.eq("SHELF_STATUS", params.get("shelfStatus"));
            log.info("添加查询条件: SHELF_STATUS = {}", params.get("shelfStatus"));
        }

        wrapper.orderByDesc("CREATE_TIME");

        log.info("=== 开始执行分页查询 ===");
        IPage<TblFinancialProduct> result = this.page(page, wrapper);
        log.info("=== 查询结果 === total={}, records.size={}", result.getTotal(), result.getRecords().size());

        // 手动设置分类名称
        if (!result.getRecords().isEmpty()) {
            Set<Long> categoryIds = new HashSet<>();
            for (TblFinancialProduct product : result.getRecords()) {
                if (product.getCategoryId() != null) {
                    categoryIds.add(product.getCategoryId());
                }
            }

            if (!categoryIds.isEmpty()) {
                QueryWrapper<TblFinancialCategory> categoryWrapper = new QueryWrapper<>();
                categoryWrapper.in("CATEGORY_ID", categoryIds);
                List<TblFinancialCategory> categories = categoryMapper.selectList(categoryWrapper);

                Map<Long, String> categoryMap = new HashMap<>();
                for (TblFinancialCategory category : categories) {
                    categoryMap.put(category.getCategoryId(), category.getCategoryName());
                }

                for (TblFinancialProduct product : result.getRecords()) {
                    if (product.getCategoryId() != null) {
                        product.setCategoryName(categoryMap.get(product.getCategoryId()));
                    }
                }
            }
        }

        return result;
    }

    @Override
    public TblFinancialProduct getDetail(Long id) {
        return this.getById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblFinancialProduct create(TblFinancialProduct entity, String createBy) {
        entity.setProductId(snowflakeIdWorker.nextId());
        entity.setCreateBy(createBy);
        entity.setCreateTime(new Date());
        entity.setUpdateBy(createBy);
        entity.setUpdateTime(new Date());
        if (entity.getProductStatus() == null) {
            entity.setProductStatus("DRAFT");
        }
        if (entity.getShelfStatus() == null) {
            entity.setShelfStatus("OFF");
        }
        this.save(entity);
        return entity;
    }

    @Override
    public boolean update(TblFinancialProduct entity, String updateBy) {
        entity.setUpdateBy(updateBy);
        return this.updateById(entity);
    }

    @Override
    public boolean delete(Long id) {
        return this.removeById(id);
    }

    @Override
    public boolean batchDelete(List<Long> ids) {
        return this.removeByIds(ids);
    }

    @Override
    public boolean updateStatus(Long id, String productStatus, String updateBy) {
        TblFinancialProduct entity = new TblFinancialProduct();
        entity.setProductId(id);
        entity.setProductStatus(productStatus);
        entity.setUpdateBy(updateBy);
        entity.setUpdateTime(new Date());
        return this.updateById(entity);
    }

    @Override
    public boolean batchUpdateStatus(List<Long> ids, String productStatus, String updateBy) {
        return baseMapper.batchUpdateStatus(ids, productStatus, updateBy) > 0;
    }

    @Override
    public boolean updateShelfStatus(Long id, String shelfStatus, String updateBy) {
        TblFinancialProduct entity = new TblFinancialProduct();
        entity.setProductId(id);
        entity.setShelfStatus(shelfStatus);
        entity.setUpdateBy(updateBy);
        entity.setUpdateTime(new Date());
        return this.updateById(entity);
    }

    @Override
    public boolean batchUpdateShelfStatus(List<Long> ids, String shelfStatus, String updateBy) {
        return baseMapper.batchUpdateShelfStatus(ids, shelfStatus, updateBy) > 0;
    }

    @Override
    public List<TblFinancialProduct> getEnabledList(Long orgId) {
        QueryWrapper<TblFinancialProduct> wrapper = new QueryWrapper<>();
        wrapper.eq("PRODUCT_STATUS", "ACTIVE");
        wrapper.eq("SHELF_STATUS", "ON");
        if (orgId != null) {
            wrapper.eq("ORG_ID", orgId);
        }
        return this.list(wrapper);
    }

    @Override
    public boolean checkCodeUnique(String productCode, Long excludeId) {
        return baseMapper.checkCodeUnique(productCode, excludeId) == 0;
    }

    @Override
    public List<TblFinancialProduct> getByCategoryId(Long categoryId) {
        return baseMapper.selectByCategoryId(categoryId);
    }

    @Override
    public TblFinancialProduct copy(Long id, String newCode, String newName, String createBy) {
        TblFinancialProduct source = this.getById(id);
        if (source == null) {
            return null;
        }
        TblFinancialProduct target = new TblFinancialProduct();
        BeanUtils.copyProperties(source, target);
        target.setProductId(snowflakeIdWorker.nextId());
        target.setProductCode(newCode);
        target.setProductName(newName);
        target.setProductStatus("DRAFT");
        target.setShelfStatus("OFF");
        target.setCreateBy(createBy);
        target.setCreateTime(new Date());
        target.setUpdateBy(createBy);
        target.setUpdateTime(new Date());
        this.save(target);
        return target;
    }

    @Override
    public Map<String, Object> getStatistics(Long orgId) {
        return baseMapper.getStatistics(orgId);
    }

    @Override
    public Map<String, Object> validateDelete(Long id) {
        Map<String, Object> result = new HashMap<>();
        TblFinancialProduct product = this.getById(id);
        if (product != null && "ON".equals(product.getShelfStatus())) {
            result.put("canDelete", false);
            result.put("message", "产品已上架,无法删除");
        } else {
            result.put("canDelete", true);
        }
        return result;
    }

    @Override
    public Map<String, Object> getUsage(Long id) {
        Map<String, Object> result = new HashMap<>();
        result.put("usageCount", 0);
        result.put("isUsed", false);
        return result;
    }
}
