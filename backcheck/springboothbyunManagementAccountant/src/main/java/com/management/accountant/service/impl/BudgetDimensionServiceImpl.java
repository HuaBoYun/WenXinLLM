package com.management.accountant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.budget.BudgetDimension;
import com.management.accountant.oracle.entity.budget.BudgetDimensionAttribute;
import com.management.accountant.oracle.entity.budget.BudgetDimensionMemberImportDTO;
import com.management.accountant.oracle.entity.budget.BudgetDimensionRelation;
import com.management.accountant.oracle.mapper.budget.BudgetDimensionAttributeMapper;
import com.management.accountant.oracle.mapper.budget.BudgetDimensionMapper;
import com.management.accountant.oracle.mapper.budget.BudgetDimensionRelationMapper;
import com.management.accountant.service.BudgetDimensionService;
import com.management.accountant.util.ExcelUtil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;

/**
 * 预算维度配置管理Service实现类
 *
 * @description 预算维度配置管理业务实现
 * @author AI Assistant
 * @date 2025-01-04
 */
@Service
@Slf4j
public class BudgetDimensionServiceImpl implements BudgetDimensionService {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());


    @Resource
    private BudgetDimensionMapper dimensionMapper;

    @Resource
    private BudgetDimensionAttributeMapper attributeMapper;

    @Resource
    private BudgetDimensionRelationMapper relationMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BudgetDimension create(BudgetDimension dimension) {
        // 1. 参数校验
        if (dimension == null) {
            throw new ServiceException("维度信息不能为空");
        }
        if (!StringUtils.hasText(dimension.getDimensionCode())) {
            throw new ServiceException("维度编码不能为空");
        }
        if (!StringUtils.hasText(dimension.getDimensionName())) {
            throw new ServiceException("维度名称不能为空");
        }

        // 2. 检查编码是否重复
        if (checkCodeExists(dimension.getDimensionCode())) {
            throw new ServiceException("维度编码已存在");
        }

        // 3. 设置默认值
        if (dimension.getIsDeleted() == null) {
            dimension.setIsDeleted(0);
        }
        if (dimension.getIsRequired() == null) {
            dimension.setIsRequired(0);
        }
        if (dimension.getSortOrder() == null) {
            dimension.setSortOrder(0);
        }
        dimension.setCreateTime(new Date());
        dimension.setUpdateTime(new Date());

        // 4. 插入数据库
        int result = dimensionMapper.insert(dimension);
        if (result <= 0) {
            throw new ServiceException("创建维度失败");
        }

        log.info("创建维度成功，ID: {}, 编码: {}", dimension.getDimensionId(), dimension.getDimensionCode());
        return dimension;
    }

    @Override
    public BudgetDimension getById(String dimensionId) {
        if (!StringUtils.hasText(dimensionId)) {
            throw new ServiceException("维度ID不能为空");
        }

        QueryWrapper<BudgetDimension> wrapper = new QueryWrapper<>();
        wrapper.eq("DIMENSION_ID", dimensionId)
               .eq("IS_DELETED", 0);

        return dimensionMapper.selectOne(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BudgetDimension update(BudgetDimension dimension) {
        // 1. 参数校验
        if (dimension == null || !StringUtils.hasText(dimension.getDimensionId())) {
            throw new ServiceException("维度ID不能为空");
        }

        // 2. 检查是否存在
        BudgetDimension existing = getById(dimension.getDimensionId());
        if (existing == null) {
            throw new ServiceException("维度不存在");
        }

        // 3. 如果修改了编码，检查新编码是否重复
        if (StringUtils.hasText(dimension.getDimensionCode())
                && !dimension.getDimensionCode().equals(existing.getDimensionCode())) {
            if (checkCodeExists(dimension.getDimensionCode())) {
                throw new ServiceException("维度编码已存在");
            }
        }

        // 4. 更新时间
        dimension.setUpdateTime(new Date());

        // 5. 更新数据库
        int result = dimensionMapper.updateById(dimension);
        if (result <= 0) {
            throw new ServiceException("更新维度失败");
        }

        log.info("更新维度成功，ID: {}", dimension.getDimensionId());
        return getById(dimension.getDimensionId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String dimensionId) {
        if (!StringUtils.hasText(dimensionId)) {
            throw new ServiceException("维度ID不能为空");
        }

        // 检查是否存在
        BudgetDimension existing = getById(dimensionId);
        if (existing == null) {
            throw new ServiceException("维度不存在");
        }

        // 检查是否有子维度
        QueryWrapper<BudgetDimension> childWrapper = new QueryWrapper<>();
        childWrapper.eq("PARENT_ID", dimensionId)
                   .eq("IS_DELETED", 0);
        Integer childCount = dimensionMapper.selectCount(childWrapper).intValue();
        if (childCount > 0) {
            throw new ServiceException("该维度下存在子维度，无法删除");
        }

        // 逻辑删除
        BudgetDimension dimension = new BudgetDimension();
        dimension.setDimensionId(dimensionId);
        dimension.setIsDeleted(1);
        dimension.setUpdateTime(new Date());

        int result = dimensionMapper.updateById(dimension);
        if (result <= 0) {
            throw new ServiceException("删除维度失败");
        }

        log.info("删除维度成功，ID: {}", dimensionId);
    }

    @Override
    public Map<String, Object> getPage(Map<String, Object> params) {
        // 1. 获取分页参数
        int pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 20;

        // 2. 构建查询条件
        QueryWrapper<BudgetDimension> wrapper = new QueryWrapper<>();
        wrapper.eq("IS_DELETED", 0);

        // 维度名称模糊查询 - 必须有值且不为空字符串
        String dimensionName = params.get("dimensionName") != null ? params.get("dimensionName").toString().trim() : "";
        if (!dimensionName.isEmpty()) {
            wrapper.like("DIMENSION_NAME", dimensionName);
        }

        // 维度编码模糊查询 - 必须有值且不为空字符串
        String dimensionCode = params.get("dimensionCode") != null ? params.get("dimensionCode").toString().trim() : "";
        if (!dimensionCode.isEmpty()) {
            wrapper.like("DIMENSION_CODE", dimensionCode);
        }

        // 维度类型 - 必须有值且不为空字符串
        String dimensionType = params.get("dimensionType") != null ? params.get("dimensionType").toString().trim() : "";
        if (!dimensionType.isEmpty()) {
            wrapper.eq("DIMENSION_TYPE", dimensionType);
        }

        // 维度状态 - 必须有值且不为空字符串
        String dimensionStatus = params.get("dimensionStatus") != null ? params.get("dimensionStatus").toString().trim() : "";
        if (!dimensionStatus.isEmpty()) {
            if ("ACTIVE".equals(dimensionStatus)) {
                wrapper.eq("IS_ACTIVE", 1);
            } else if ("INACTIVE".equals(dimensionStatus)) {
                wrapper.eq("IS_ACTIVE", 0);
            }
        }

        // 父维度ID - 支持 parentId 和 parentDimensionId 两种参数名
        Object parentIdParam = params.get("parentId");
        if (parentIdParam == null) {
            parentIdParam = params.get("parentDimensionId");
        }

        // 只有当明确指定了父维度ID时才添加条件
        if (parentIdParam != null) {
            String parentIdStr = parentIdParam.toString().trim();
            if (!parentIdStr.isEmpty()) {
                // 判断是否需要包含所有子级（递归筛选）
                boolean includeChildren = false;
                Object includeChildrenParam = params.get("includeChildren");
                if (includeChildrenParam != null) {
                    includeChildren = Boolean.parseBoolean(includeChildrenParam.toString());
                }

                if (includeChildren) {
                    // 递归收集该节点及其所有后代维度ID
                    List<String> allDescendantIds = collectDescendantIds(parentIdStr);
                    allDescendantIds.add(parentIdStr); // 包含自身
                    // 查询 PARENT_ID 在这些ID中的维度（即这些节点的直接子级都会被查出来）
                    // 或者查询 DIMENSION_ID 在后代ID中（展示该分支下所有维度）
                    wrapper.in("DIMENSION_ID", allDescendantIds);
                } else {
                    wrapper.eq("PARENT_ID", parentIdStr);
                }
            }
        }

        // 排序
        wrapper.orderByAsc("SORT_ORDER").orderByDesc("CREATE_TIME");

        // 3. 执行分页查询
        Page<BudgetDimension> page = new Page<>(pageNum, pageSize);
        IPage<BudgetDimension> pageResult = dimensionMapper.selectPage(page, wrapper);

        // 4. 增强返回数据，添加计算字段
        List<Map<String, Object>> enrichedRecords = new ArrayList<>();
        for (BudgetDimension dimension : pageResult.getRecords()) {
            Map<String, Object> record = convertToMap(dimension);

            // 计算维度级次（通过递归查找父维度）
            int level = calculateDimensionLevel(dimension.getDimensionId());
            record.put("dimensionLevel", level);

            // 判断是否为层级维度（是否有子维度）
            boolean isHierarchy = hasChildren(dimension.getDimensionId());
            record.put("isHierarchy", isHierarchy);

            // 统计成员数量（子维度数量）
            int memberCount = countChildren(dimension.getDimensionId());
            record.put("memberCount", memberCount);

            enrichedRecords.add(record);
        }

        // 5. 封装返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("records", enrichedRecords);
        result.put("total", pageResult.getTotal());
        result.put("pageNum", pageNum);
        result.put("pageSize", pageSize);

        log.info("分页查询维度列表，pageNum: {}, pageSize: {}, total: {}", pageNum, pageSize, pageResult.getTotal());
        return result;
    }

    /**
     * 将实体转换为Map
     */
    private Map<String, Object> convertToMap(BudgetDimension dimension) {
        Map<String, Object> map = new HashMap<>();
        map.put("dimensionId", dimension.getDimensionId());
        map.put("dimensionName", dimension.getDimensionName());
        map.put("dimensionCode", dimension.getDimensionCode());
        map.put("dimensionType", dimension.getDimensionType());
        map.put("parentId", dimension.getParentId());
        map.put("sortOrder", dimension.getSortOrder());
        map.put("isRequired", dimension.getIsRequired());
        map.put("dimensionDescription", dimension.getDimensionDescription());
        map.put("creatorId", dimension.getCreatorId());
        map.put("creatorName", dimension.getCreatorName());
        map.put("createTime", dimension.getCreateTime());
        map.put("updaterId", dimension.getUpdaterId());
        map.put("updaterName", dimension.getUpdaterName());
        map.put("updateTime", dimension.getUpdateTime());
        map.put("isActive", dimension.getIsActive());
        map.put("isDeleted", dimension.getIsDeleted());
        map.put("companyId", dimension.getCompanyId());
        map.put("companyName", dimension.getCompanyName());
        return map;
    }

    /**
     * 计算维度级次
     */
    private int calculateDimensionLevel(String dimensionId) {
        BudgetDimension dimension = dimensionMapper.selectById(dimensionId);
        if (dimension == null) {
            return 0;
        }

        int level = 1;
        String parentId = dimension.getParentId();

        // 递归查找父维度，计算级次
        while (parentId != null && !parentId.isEmpty()) {
            BudgetDimension parent = dimensionMapper.selectById(parentId);
            if (parent == null) {
                break;
            }
            level++;
            parentId = parent.getParentId();
        }

        return level;
    }

    /**
     * 判断是否有子维度
     */
    private boolean hasChildren(String dimensionId) {
        QueryWrapper<BudgetDimension> wrapper = new QueryWrapper<>();
        wrapper.eq("PARENT_ID", dimensionId).eq("IS_DELETED", 0);
        return dimensionMapper.selectCount(wrapper) > 0;
    }

    /**
     * 统计子维度数量
     */
    private int countChildren(String dimensionId) {
        QueryWrapper<BudgetDimension> wrapper = new QueryWrapper<>();
        wrapper.eq("PARENT_ID", dimensionId).eq("IS_DELETED", 0);
        return dimensionMapper.selectCount(wrapper).intValue();
    }

    /**
     * 递归收集指定维度的所有后代维度ID
     */
    private List<String> collectDescendantIds(String parentId) {
        List<String> result = new ArrayList<>();
        QueryWrapper<BudgetDimension> wrapper = new QueryWrapper<>();
        wrapper.eq("PARENT_ID", parentId).eq("IS_DELETED", 0);
        List<BudgetDimension> children = dimensionMapper.selectList(wrapper);
        for (BudgetDimension child : children) {
            result.add(child.getDimensionId());
            // 递归收集子级的后代
            result.addAll(collectDescendantIds(child.getDimensionId()));
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> getTree() {
        // 查询所有未删除的维度
        QueryWrapper<BudgetDimension> wrapper = new QueryWrapper<>();
        wrapper.eq("IS_DELETED", 0)
               .orderByAsc("SORT_ORDER");
        List<BudgetDimension> allDimensions = dimensionMapper.selectList(wrapper);

        // 构建树结构
        return buildTree(allDimensions, "0");
    }

    /**
     * 递归构建树结构
     */
    private List<Map<String, Object>> buildTree(List<BudgetDimension> allDimensions, String parentId) {
        List<Map<String, Object>> tree = new ArrayList<>();

        for (BudgetDimension dimension : allDimensions) {
            String pid = dimension.getParentId();
            if (pid == null) {
                pid = "0";
            }

            if (pid.equals(parentId)) {
                Map<String, Object> node = new HashMap<>();
                node.put("dimensionId", dimension.getDimensionId());
                node.put("dimensionCode", dimension.getDimensionCode());
                node.put("dimensionName", dimension.getDimensionName());
                node.put("dimensionType", dimension.getDimensionType());
                node.put("parentId", dimension.getParentId());
                node.put("sortOrder", dimension.getSortOrder());
                node.put("isRequired", dimension.getIsRequired());

                // 递归查找子节点
                List<Map<String, Object>> children = buildTree(allDimensions, dimension.getDimensionId());
                node.put("children", children);

                tree.add(node);
            }
        }

        return tree;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new ServiceException("请选择要删除的数据");
        }

        for (String id : ids) {
            delete(id);
        }

        log.info("批量删除维度成功，数量: {}", ids.size());
    }

    @Override
    public List<BudgetDimension> getChildren(String parentId) {
        if (!StringUtils.hasText(parentId)) {
            throw new ServiceException("父维度ID不能为空");
        }

        QueryWrapper<BudgetDimension> wrapper = new QueryWrapper<>();
        wrapper.eq("PARENT_ID", parentId)
               .eq("IS_DELETED", 0)
               .orderByAsc("SORT_ORDER");

        return dimensionMapper.selectList(wrapper);
    }

    @Override
    public List<Map<String, Object>> getValues(String dimensionId) {
        if (!StringUtils.hasText(dimensionId)) {
            throw new ServiceException("维度ID不能为空");
        }

        // 这里返回维度的子维度作为维度值
        List<BudgetDimension> children = getChildren(dimensionId);
        List<Map<String, Object>> values = new ArrayList<>();

        for (BudgetDimension child : children) {
            Map<String, Object> value = new HashMap<>();
            value.put("valueId", child.getDimensionId());
            value.put("valueCode", child.getDimensionCode());
            value.put("valueName", child.getDimensionName());
            values.add(value);
        }

        return values;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchCreateValues(String dimensionId, List<Map<String, Object>> values) {
        if (!StringUtils.hasText(dimensionId)) {
            throw new ServiceException("维度ID不能为空");
        }
        if (values == null || values.isEmpty()) {
            throw new ServiceException("维度值列表不能为空");
        }

        // 检查父维度是否存在
        BudgetDimension parent = getById(dimensionId);
        if (parent == null) {
            throw new ServiceException("父维度不存在");
        }

        // 批量创建子维度作为维度值
        for (Map<String, Object> valueMap : values) {
            BudgetDimension dimension = new BudgetDimension();
            dimension.setDimensionCode((String) valueMap.get("valueCode"));
            dimension.setDimensionName((String) valueMap.get("valueName"));
            dimension.setDimensionType(parent.getDimensionType());
            dimension.setParentId(dimensionId);
            dimension.setIsRequired(0);
            dimension.setIsDeleted(0);
            dimension.setCreateTime(new Date());
            dimension.setUpdateTime(new Date());

            dimensionMapper.insert(dimension);
        }

        log.info("批量创建维度值成功，父维度ID: {}, 数量: {}", dimensionId, values.size());
    }

    @Override
    public boolean checkCodeExists(String code) {
        if (!StringUtils.hasText(code)) {
            return false;
        }

        QueryWrapper<BudgetDimension> wrapper = new QueryWrapper<>();
        wrapper.eq("DIMENSION_CODE", code)
               .eq("IS_DELETED", 0);

        return dimensionMapper.selectCount(wrapper) > 0;
    }

    @Override
    public List<BudgetDimensionAttribute> getAttributes(String dimensionId) {
        if (!StringUtils.hasText(dimensionId)) {
            throw new ServiceException("维度ID不能为空");
        }
        return attributeMapper.selectByDimensionId(dimensionId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BudgetDimensionAttribute createAttribute(BudgetDimensionAttribute attribute) {
        if (attribute == null || !StringUtils.hasText(attribute.getDimensionId())) {
            throw new ServiceException("维度ID不能为空");
        }
        if (!StringUtils.hasText(attribute.getAttributeName())) {
            throw new ServiceException("属性名称不能为空");
        }
        attribute.setCreateTime(new Date());
        attribute.setIsDeleted(0);
        attributeMapper.insert(attribute);
        return attribute;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BudgetDimensionAttribute updateAttribute(String attributeId, BudgetDimensionAttribute attribute) {
        if (!StringUtils.hasText(attributeId)) {
            throw new ServiceException("属性ID不能为空");
        }
        BudgetDimensionAttribute existing = attributeMapper.selectById(attributeId);
        if (existing == null) {
            throw new ServiceException("属性不存在");
        }
        existing.setAttributeName(attribute.getAttributeName());
        existing.setAttributeType(attribute.getAttributeType());
        existing.setDefaultValue(attribute.getDefaultValue());
        existing.setIsRequired(attribute.getIsRequired());
        existing.setAttributeDescription(attribute.getAttributeDescription());
        attributeMapper.updateById(existing);
        return existing;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAttribute(String attributeId) {
        if (!StringUtils.hasText(attributeId)) {
            throw new ServiceException("属性ID不能为空");
        }
        BudgetDimensionAttribute attr = attributeMapper.selectById(attributeId);
        if (attr == null) {
            throw new ServiceException("属性不存在");
        }
        attr.setIsDeleted(1);
        attributeMapper.updateById(attr);
    }

    @Override
    public List<BudgetDimensionRelation> getRelations(String dimensionId) {
        if (!StringUtils.hasText(dimensionId)) {
            throw new ServiceException("维度ID不能为空");
        }
        return relationMapper.selectByDimensionId(dimensionId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BudgetDimensionRelation createRelation(BudgetDimensionRelation relation) {
        if (relation == null || !StringUtils.hasText(relation.getSourceDimensionId())) {
            throw new ServiceException("源维度ID不能为空");
        }
        if (!StringUtils.hasText(relation.getTargetDimensionId())) {
            throw new ServiceException("目标维度ID不能为空");
        }
        if (!StringUtils.hasText(relation.getRelationType())) {
            throw new ServiceException("关联类型不能为空");
        }
        relation.setCreateTime(new Date());
        relation.setIsDeleted(0);
        relationMapper.insert(relation);
        return relation;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BudgetDimensionRelation updateRelation(String relationId, BudgetDimensionRelation relation) {
        if (!StringUtils.hasText(relationId)) {
            throw new ServiceException("关联ID不能为空");
        }
        BudgetDimensionRelation existing = relationMapper.selectById(relationId);
        if (existing == null) {
            throw new ServiceException("关联关系不存在");
        }
        existing.setTargetDimensionId(relation.getTargetDimensionId());
        existing.setRelationType(relation.getRelationType());
        existing.setRelationRule(relation.getRelationRule());
        relationMapper.updateById(existing);
        return existing;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRelation(String relationId) {
        if (!StringUtils.hasText(relationId)) {
            throw new ServiceException("关联ID不能为空");
        }
        BudgetDimensionRelation rel = relationMapper.selectById(relationId);
        if (rel == null) {
            throw new ServiceException("关联关系不存在");
        }
        rel.setIsDeleted(1);
        relationMapper.updateById(rel);
    }

    @Override
    public List<BudgetDimension> getParents() {
        // 返回所有启用的维度,供选择上级维度时使用
        QueryWrapper<BudgetDimension> wrapper = new QueryWrapper<>();
        wrapper.eq("IS_DELETED", 0)
               .eq("IS_ACTIVE", 1)
               .orderByAsc("SORT_ORDER")
               .orderByDesc("CREATE_TIME");
        return dimensionMapper.selectList(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(String dimensionId, Boolean isActive) {
        if (!StringUtils.hasText(dimensionId)) {
            throw new ServiceException("维度ID不能为空");
        }
        BudgetDimension update = new BudgetDimension();
        update.setDimensionId(dimensionId);
        update.setIsActive(isActive);
        update.setUpdateTime(new Date());
        dimensionMapper.updateById(update);
        log.info("更新维度状态，维度ID: {}, isActive: {}", dimensionId, isActive);
    }

    @Override
    public Map<String, Object> batchValidate(List<String> ids) {
        Map<String, Object> result = new HashMap<>();
        result.put("total", ids.size());
        result.put("valid", ids.size());
        result.put("invalid", 0);
        return result;
    }

    @Override
    public List<Map<String, Object>> getMembers(String dimensionId) {
        if (!StringUtils.hasText(dimensionId)) {
            throw new ServiceException("维度ID不能为空");
        }
        BudgetDimension parentDimension = getById(dimensionId);
        QueryWrapper<BudgetDimension> wrapper = new QueryWrapper<>();
        wrapper.eq("PARENT_ID", dimensionId)
               .eq("IS_DELETED", 0)
               .orderByAsc("SORT_ORDER");
        List<BudgetDimension> children = dimensionMapper.selectList(wrapper);
        List<Map<String, Object>> members = new ArrayList<>();
        for (BudgetDimension child : children) {
            Map<String, Object> m = new HashMap<>();
            m.put("memberId", child.getDimensionId());
            m.put("memberCode", child.getDimensionCode());
            m.put("memberName", child.getDimensionName());
            m.put("parentId", child.getParentId());
            m.put("parentMemberName", parentDimension != null ? parentDimension.getDimensionName() : "-");
            m.put("memberLevel", 1);
            m.put("sortOrder", child.getSortOrder());
            m.put("isActive", child.getIsActive());
            m.put("dimensionDescription", child.getDimensionDescription());
            members.add(m);
        }
        return members;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveMembers(String dimensionId, List<Map<String, Object>> members) {
        if (!StringUtils.hasText(dimensionId)) {
            throw new ServiceException("维度ID不能为空");
        }
        BudgetDimension parent = getById(dimensionId);
        if (parent == null) {
            throw new ServiceException("维度不存在");
        }
        if (members == null || members.isEmpty()) {
            return;
        }
        for (Map<String, Object> m : members) {
            String memberId = m.get("memberId") != null ? m.get("memberId").toString() : null;
            String memberCode = m.get("memberCode") != null ? m.get("memberCode").toString().trim() : "";
            String memberName = m.get("memberName") != null ? m.get("memberName").toString().trim() : "";
            if (!StringUtils.hasText(memberCode) || !StringUtils.hasText(memberName)) {
                continue;
            }
            if (StringUtils.hasText(memberId)) {
                BudgetDimension update = new BudgetDimension();
                update.setDimensionId(memberId);
                update.setDimensionName(memberName);
                update.setDimensionCode(memberCode);
                update.setUpdateTime(new Date());
                if (m.get("isActive") != null) {
                    update.setIsActive(Boolean.parseBoolean(m.get("isActive").toString()));
                }
                if (m.get("sortOrder") != null && StringUtils.hasText(m.get("sortOrder").toString())) {
                    update.setSortOrder(Integer.parseInt(m.get("sortOrder").toString()));
                }
                if (m.get("dimensionDescription") != null) {
                    update.setDimensionDescription(m.get("dimensionDescription").toString());
                }
                dimensionMapper.updateById(update);
            } else {
                if (checkCodeExists(memberCode)) {
                    throw new ServiceException("成员编码[" + memberCode + "]已存在");
                }
                BudgetDimension newMember = new BudgetDimension();
                newMember.setDimensionCode(memberCode);
                newMember.setDimensionName(memberName);
                newMember.setDimensionType(parent.getDimensionType());
                newMember.setParentId(dimensionId);
                newMember.setIsRequired(0);
                newMember.setIsDeleted(0);
                newMember.setIsActive(m.get("isActive") == null || Boolean.parseBoolean(m.get("isActive").toString()));
                newMember.setCompanyId(parent.getCompanyId());
                newMember.setCompanyName(parent.getCompanyName());
                newMember.setCreateTime(new Date());
                newMember.setUpdateTime(new Date());
                if (m.get("sortOrder") != null && StringUtils.hasText(m.get("sortOrder").toString())) {
                    newMember.setSortOrder(Integer.parseInt(m.get("sortOrder").toString()));
                }
                if (m.get("dimensionDescription") != null) {
                    newMember.setDimensionDescription(m.get("dimensionDescription").toString());
                }
                dimensionMapper.insert(newMember);
            }
        }
        log.info("保存维度成员成功，维度ID: {}, 数量: {}", dimensionId, members.size());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importMembers(String dimensionId, org.springframework.web.multipart.MultipartFile file) {
        if (!StringUtils.hasText(dimensionId)) {
            throw new ServiceException("维度ID不能为空");
        }
        if (!ExcelUtil.validateExcelFile(file)) {
            throw new ServiceException("请上传 .xlsx 或 .xls 格式的Excel文件");
        }
        BudgetDimension parent = getById(dimensionId);
        if (parent == null) {
            throw new ServiceException("维度不存在");
        }

        List<BudgetDimensionMemberImportDTO> rows = ExcelUtil.importExcel(file, BudgetDimensionMemberImportDTO.class);
        if (rows == null || rows.isEmpty()) {
            throw new ServiceException("Excel文件中没有可导入的数据");
        }

        int success = 0;
        int fail = 0;
        List<Map<String, Object>> errors = new ArrayList<>();

        for (int i = 0; i < rows.size(); i++) {
            BudgetDimensionMemberImportDTO row = rows.get(i);
            int rowNum = i + 2;
            try {
                String memberCode = row.getMemberCode() != null ? row.getMemberCode().trim() : "";
                String memberName = row.getMemberName() != null ? row.getMemberName().trim() : "";
                if (!StringUtils.hasText(memberCode)) {
                    throw new ServiceException("第" + rowNum + "行成员编码不能为空");
                }
                if (!StringUtils.hasText(memberName)) {
                    throw new ServiceException("第" + rowNum + "行成员名称不能为空");
                }

                QueryWrapper<BudgetDimension> wrapper = new QueryWrapper<>();
                wrapper.eq("PARENT_ID", dimensionId)
                       .eq("DIMENSION_CODE", memberCode)
                       .eq("IS_DELETED", 0)
                       .last("FETCH FIRST 1 ROWS ONLY");
                BudgetDimension existed = dimensionMapper.selectOne(wrapper);

                if (existed == null && checkCodeExists(memberCode)) {
                    throw new ServiceException("第" + rowNum + "行成员编码[" + memberCode + "]已存在");
                }

                BudgetDimension entity = existed != null ? existed : new BudgetDimension();
                entity.setDimensionCode(memberCode);
                entity.setDimensionName(memberName);
                entity.setDimensionType(parent.getDimensionType());
                entity.setParentId(dimensionId);
                entity.setIsRequired(0);
                entity.setIsDeleted(0);
                entity.setIsActive(parseImportBoolean(row.getIsActive()));
                entity.setCompanyId(parent.getCompanyId());
                entity.setCompanyName(parent.getCompanyName());
                entity.setDimensionDescription(row.getDimensionDescription());
                entity.setUpdateTime(new Date());
                if (row.getSortOrder() != null) {
                    entity.setSortOrder(row.getSortOrder());
                }

                if (existed != null) {
                    dimensionMapper.updateById(entity);
                } else {
                    entity.setCreateTime(new Date());
                    dimensionMapper.insert(entity);
                }
                success++;
            } catch (Exception ex) {
                fail++;
                Map<String, Object> error = new HashMap<>();
                error.put("row", rowNum);
                error.put("message", ex.getMessage());
                errors.add(error);
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("total", rows.size());
        result.put("success", success);
        result.put("fail", fail);
        result.put("errors", errors);
        return result;
    }

    private Boolean parseImportBoolean(String text) {
        if (!StringUtils.hasText(text)) {
            return true;
        }
        String value = text.trim();
        return "启用".equals(value) || "是".equals(value) || "true".equalsIgnoreCase(value) || "1".equals(value);
    }





    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMember(String memberId) {
        if (!StringUtils.hasText(memberId)) {
            throw new ServiceException("成员ID不能为空");
        }
        BudgetDimension member = dimensionMapper.selectById(memberId);
        if (member == null || member.getIsDeleted() == 1) {
            throw new ServiceException("成员不存在");
        }
        BudgetDimension update = new BudgetDimension();
        update.setDimensionId(memberId);
        update.setIsDeleted(1);
        update.setUpdateTime(new Date());
        dimensionMapper.updateById(update);
        log.info("删除维度成员成功，成员ID: {}", memberId);
    }

    @Override
    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();

        // 查询总数
        QueryWrapper<BudgetDimension> totalWrapper = new QueryWrapper<>();
        totalWrapper.eq("IS_DELETED", 0);
        long totalDimensions = dimensionMapper.selectCount(totalWrapper);
        stats.put("totalDimensions", totalDimensions);

        // 查询启用维度数
        QueryWrapper<BudgetDimension> activeWrapper = new QueryWrapper<>();
        activeWrapper.eq("IS_DELETED", 0).eq("IS_ACTIVE", 1);
        long activeDimensions = dimensionMapper.selectCount(activeWrapper);
        stats.put("activeDimensions", activeDimensions);

        // 查询层级维度数（有子维度的维度就是层级维度）
        // 通过查询有多少个不同的 PARENT_ID 来统计层级维度数量
        QueryWrapper<BudgetDimension> childWrapper = new QueryWrapper<>();
        childWrapper.eq("IS_DELETED", 0)
                   .isNotNull("PARENT_ID")
                   .ne("PARENT_ID", "");
        List<BudgetDimension> childDimensions = dimensionMapper.selectList(childWrapper);

        // 统计有多少个不同的父维度（即层级维度）
        long hierarchyDimensions = childDimensions.stream()
                .map(BudgetDimension::getParentId)
                .distinct()
                .count();
        stats.put("hierarchyDimensions", hierarchyDimensions);

        // 计算启用率
        double activeRate = totalDimensions > 0 ? (activeDimensions * 100.0 / totalDimensions) : 0;
        stats.put("activeRate", Math.round(activeRate * 10) / 10.0);

        // 计算层级率
        double hierarchyRate = totalDimensions > 0 ? (hierarchyDimensions * 100.0 / totalDimensions) : 0;
        stats.put("hierarchyRate", Math.round(hierarchyRate * 10) / 10.0);

        // 查询维度成员总数（子维度数量）
        long totalMembers = childDimensions.size();
        stats.put("totalMembers", totalMembers);

        // 计算成员率
        double memberRate = totalDimensions > 0 ? (totalMembers * 100.0 / totalDimensions) : 0;
        stats.put("memberRate", Math.round(memberRate * 10) / 10.0);

        log.info("获取维度统计数据: {}", stats);
        return stats;
    }

    @Override
    public void exportDimension(Map<String, Object> params, javax.servlet.http.HttpServletResponse response) {
        try {
            // 查询维度列表
            QueryWrapper<BudgetDimension> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("IS_DELETED", 0);

            // 添加查询条件
            if (params != null) {
                String dimensionName = params.get("dimensionName") != null ? params.get("dimensionName").toString() : null;
                if (dimensionName != null && !dimensionName.trim().isEmpty()) {
                    queryWrapper.like("DIMENSION_NAME", dimensionName);
                }
                String dimensionCode = params.get("dimensionCode") != null ? params.get("dimensionCode").toString() : null;
                if (dimensionCode != null && !dimensionCode.trim().isEmpty()) {
                    queryWrapper.like("DIMENSION_CODE", dimensionCode);
                }
                String dimensionType = params.get("dimensionType") != null ? params.get("dimensionType").toString() : null;
                if (dimensionType != null && !dimensionType.trim().isEmpty()) {
                    queryWrapper.eq("DIMENSION_TYPE", dimensionType);
                }
                String dimensionStatus = params.get("dimensionStatus") != null ? params.get("dimensionStatus").toString() : null;
                if (dimensionStatus != null && !dimensionStatus.trim().isEmpty()) {
                    queryWrapper.eq("IS_ACTIVE", "ACTIVE".equals(dimensionStatus));
                }
                // 如果有选中的ID，只导出选中的维度
                Object selectedIdsObj = params.get("selectedIds");
                if (selectedIdsObj != null) {
                    @SuppressWarnings("unchecked")
                    List<String> selectedIds = (List<String>) selectedIdsObj;
                    if (!selectedIds.isEmpty()) {
                        queryWrapper.in("DIMENSION_ID", selectedIds);
                    }
                }
            }

            // 按排序号和创建时间排序
            queryWrapper.orderByAsc("SORT_ORDER", "CREATE_TIME");

            List<BudgetDimension> dimensions = dimensionMapper.selectList(queryWrapper);

            // 设置响应头
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("UTF-8");
            String fileName = java.net.URLEncoder.encode("预算维度数据", "UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename*=utf-8''" + fileName + ".xlsx");

            // 构建导出数据
            List<Map<String, Object>> exportData = new ArrayList<>();
            for (BudgetDimension dim : dimensions) {
                Map<String, Object> row = new HashMap<>();
                row.put("dimensionCode", dim.getDimensionCode());
                row.put("dimensionName", dim.getDimensionName());
                row.put("dimensionType", dim.getDimensionType());
                row.put("parentId", dim.getParentId());
                row.put("sortOrder", dim.getSortOrder());
                row.put("isRequired", dim.getIsRequired() != null && dim.getIsRequired() == 1 ? "是" : "否");
                row.put("isActive", dim.getIsActive() != null && dim.getIsActive() ? "是" : "否");
                row.put("dimensionDescription", dim.getDimensionDescription());
                row.put("companyName", dim.getCompanyName());
                row.put("createTime", dim.getCreateTime());
                row.put("updateTime", dim.getUpdateTime());
                exportData.add(row);
            }

            // 使用 EasyExcel 导出
            com.alibaba.excel.EasyExcel.write(response.getOutputStream())
                    .head(createExportHead())
                    .sheet("预算维度")
                    .doWrite(exportData);

            log.info("导出维度数据成功，共 {} 条", dimensions.size());
        } catch (Exception e) {
            log.error("导出维度数据异常", e);
            throw new ServiceException("导出失败：" + e.getMessage());
        }
    }

    @Override
    public void exportSingleDimension(String dimensionId, javax.servlet.http.HttpServletResponse response) {
        try {
            // 查询单个维度及其子维度
            QueryWrapper<BudgetDimension> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("IS_DELETED", 0)
                       .and(wrapper -> wrapper.eq("DIMENSION_ID", dimensionId)
                               .or()
                               .eq("PARENT_ID", dimensionId));

            List<BudgetDimension> dimensions = dimensionMapper.selectList(queryWrapper);

            if (dimensions.isEmpty()) {
                throw new ServiceException("维度不存在");
            }

            BudgetDimension mainDimension = dimensions.stream()
                    .filter(d -> dimensionId.equals(d.getDimensionId()))
                    .findFirst()
                    .orElse(dimensions.get(0));

            // 设置响应头
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("UTF-8");
            String fileName = java.net.URLEncoder.encode(
                    "维度_" + mainDimension.getDimensionName() + "_" + mainDimension.getDimensionCode(),
                    "UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename*=utf-8''" + fileName + ".xlsx");

            // 构建导出数据
            List<Map<String, Object>> exportData = new ArrayList<>();
            for (BudgetDimension dim : dimensions) {
                Map<String, Object> row = new HashMap<>();
                row.put("dimensionCode", dim.getDimensionCode());
                row.put("dimensionName", dim.getDimensionName());
                row.put("dimensionType", dim.getDimensionType());
                row.put("parentId", dim.getParentId());
                row.put("sortOrder", dim.getSortOrder());
                row.put("isRequired", dim.getIsRequired() != null && dim.getIsRequired() == 1 ? "是" : "否");
                row.put("isActive", dim.getIsActive() != null && dim.getIsActive() ? "是" : "否");
                row.put("dimensionDescription", dim.getDimensionDescription());
                exportData.add(row);
            }

            // 使用 EasyExcel 导出
            com.alibaba.excel.EasyExcel.write(response.getOutputStream())
                    .head(createExportHead())
                    .sheet("维度数据")
                    .doWrite(exportData);

            log.info("导出单个维度数据成功，维度ID: {}, 共 {} 条", dimensionId, dimensions.size());
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("导出单个维度数据异常", e);
            throw new ServiceException("导出失败：" + e.getMessage());
        }
    }

    /**
     * 创建导出表头
     */
    private List<List<String>> createExportHead() {
        List<List<String>> head = new ArrayList<>();

        // 创建单行表头
        List<String> headRow = new ArrayList<>();
        headRow.add("维度编码");
        headRow.add("维度名称");
        headRow.add("维度类型");
        headRow.add("上级维度ID");
        headRow.add("排序号");
        headRow.add("必填维度");
        headRow.add("状态");
        headRow.add("维度描述");
        head.add(headRow);

        return head;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importDimensions(MultipartFile file) {
        Map<String, Object> result = new HashMap<>();

        try {
            // 1. 验证文件
            if (file == null || file.isEmpty()) {
                throw new ServiceException("上传文件不能为空");
            }

            String fileName = file.getOriginalFilename();
            if (fileName == null || (!fileName.endsWith(".xls") && !fileName.endsWith(".xlsx"))) {
                throw new ServiceException("文件格式不正确，请上传Excel文件");
            }

            // 2. 解析Excel文件 - 读取为Map格式
            List<Map<String, Object>> excelData = ExcelUtil.importExcelToMap(file);
            if (excelData == null || excelData.isEmpty()) {
                throw new ServiceException("Excel文件中没有数据");
            }

            // 提取第一行数据（跳过表头）
            List<Map<String, Object>> actualData = new ArrayList<>();
            for (int i = 0; i < excelData.size(); i++) {
                if (i > 0) {  // 跳过第一行（表头）
                    actualData.add(excelData.get(i));
                }
            }
            if (actualData == null || actualData.isEmpty()) {
                throw new ServiceException("Excel文件中没有数据");
            }

            // 3. 转换并验证数据
            List<BudgetDimension> dimensions = new ArrayList<>();
            List<String> errors = new ArrayList<>();
            int successCount = 0;
            int failCount = 0;

            for (int i = 0; i < actualData.size(); i++) {
                Map<String, Object> row = actualData.get(i);
                try {
                    BudgetDimension dim = new BudgetDimension();

                    // 维度编码
                    Object dimensionCodeObj = row.get("dimensionCode");
                    String dimensionCode = dimensionCodeObj != null ? dimensionCodeObj.toString().trim() : "";
                    if (StringUtils.isEmpty(dimensionCode)) {
                        throw new ServiceException("第" + (i + 1) + "行：维度编码不能为空");
                    }
                    dim.setDimensionCode(dimensionCode);

                    // 验证编码是否存在
                    if (checkCodeExists(dim.getDimensionCode())) {
                        throw new ServiceException("第" + (i + 1) + "行：维度编码[" + dimensionCode + "]已存在");
                    }

                    // 维度名称
                    Object dimensionNameObj = row.get("dimensionName");
                    String dimensionName = dimensionNameObj != null ? dimensionNameObj.toString().trim() : "";
                    if (StringUtils.isEmpty(dimensionName)) {
                        throw new ServiceException("第" + (i + 1) + "行：维度名称不能为空");
                    }
                    dim.setDimensionName(dimensionName);

                    // 维度类型
                    Object dimensionTypeObj = row.get("dimensionType");
                    String dimensionType = dimensionTypeObj != null ? dimensionTypeObj.toString().trim() : "";
                    if (StringUtils.isEmpty(dimensionType)) {
                        throw new ServiceException("第" + (i + 1) + "行：维度类型不能为空");
                    }

                    // 验证维度类型是否有效
                    String[] validTypes = {"ORGANIZATION", "ACCOUNT", "PROJECT", "PRODUCT", "CUSTOMER", "REGION", "TIME", "CUSTOM"};
                    boolean isValidType = false;
                    for (String type : validTypes) {
                        if (type.equals(dimensionType)) {
                            isValidType = true;
                            break;
                        }
                    }
                    if (!isValidType) {
                        throw new ServiceException("第" + (i + 1) + "行：维度类型无效");
                    }
                    dim.setDimensionType(dimensionType);

                    // 上级维度ID
                    Object parentIdObj = row.get("parentId");
                    if (parentIdObj != null && StringUtils.hasText(parentIdObj.toString())) {
                        dim.setParentId(parentIdObj.toString().trim());
                    }

                    // 排序号
                    Object sortOrderObj = row.get("sortOrder");
                    if (sortOrderObj != null) {
                        try {
                            dim.setSortOrder(Integer.parseInt(sortOrderObj.toString()));
                        } catch (NumberFormatException e) {
                            dim.setSortOrder(0);
                        }
                    } else {
                        dim.setSortOrder(0);
                    }

                    // 必填维度
                    Object isRequiredObj = row.get("isRequired");
                    String isRequiredStr = isRequiredObj != null ? isRequiredObj.toString().trim() : "";
                    if ("是".equals(isRequiredStr) || "1".equals(isRequiredStr)) {
                        dim.setIsRequired(1);
                    } else {
                        dim.setIsRequired(0);
                    }

                    // 状态
                    Object isActiveObj = row.get("isActive");
                    String isActiveStr = isActiveObj != null ? isActiveObj.toString().trim() : "";
                    if ("是".equals(isActiveStr) || "1".equals(isActiveStr)) {
                        dim.setIsActive(true);
                    } else {
                        dim.setIsActive(false);
                    }

                    // 描述
                    Object descriptionObj = row.get("dimensionDescription");
                    if (descriptionObj != null && StringUtils.hasText(descriptionObj.toString())) {
                        dim.setDimensionDescription(descriptionObj.toString().trim());
                    }

                    // 设置默认值
                    dim.setIsDeleted(0);
                    dim.setCreateTime(new Date());
                    dim.setUpdateTime(new Date());

                    dimensions.add(dim);
                    successCount++;
                } catch (Exception e) {
                    errors.add("第" + (i + 1) + "行：" + e.getMessage());
                    failCount++;
                }
            }

            // 4. 批量保存
            if (!dimensions.isEmpty()) {
                for (BudgetDimension dim : dimensions) {
                    dimensionMapper.insert(dim);
                }
            }

            // 5. 返回结果
            result.put("total", excelData.size());
            result.put("successCount", successCount);
            result.put("failCount", failCount);
            result.put("errors", errors);

            log.info("导入维度成功，总数: {}, 成功: {}, 失败: {}", excelData.size(), successCount, failCount);

        } catch (ServiceException ex) {
            log.error("导入维度失败：{}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("导入维度异常", ex);
            throw new ServiceException("导入失败：" + ex.getMessage());
        }

        return result;
    }

    @Override
    public void downloadDimensionTemplate(javax.servlet.http.HttpServletResponse response) {
        try {
            // 设置响应头
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("UTF-8");
            String fileName = java.net.URLEncoder.encode("预算维度导入模板", "UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename*=utf-8''" + fileName + ".xlsx");

            // 使用 EasyExcel 写入空模板
            com.alibaba.excel.EasyExcel.write(response.getOutputStream())
                    .head(createExportHead())
                    .sheet("模板")
                    .doWrite(new ArrayList<>());

            log.info("下载维度导入模板成功");
        } catch (Exception e) {
            log.error("下载维度导入模板失败", e);
            throw new ServiceException("下载模板失败：" + e.getMessage());
        }
    }
}

