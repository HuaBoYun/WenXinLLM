package com.financial.sharing.consolidationReport.service.impl;

import com.financial.sharing.util.UserUtils;

import com.financial.sharing.consolidationReport.entity.TblEquityInfo;
import com.financial.sharing.consolidationReport.entity.TblEquityStructure;
import com.financial.sharing.consolidationReport.mapper.EquityInfoMapper;
import com.financial.sharing.consolidationReport.mapper.EquityStructureMapper;
import com.financial.sharing.consolidationReport.service.EquityStructureService;
import com.hbfk.util.user.UserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 股权结构Service实现类
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Service
public class EquityStructureServiceImpl implements EquityStructureService {

    @Autowired
    private EquityStructureMapper equityStructureMapper;

    @Autowired
    private EquityInfoMapper equityInfoMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void calculateEquityStructure(String modelId) {
        String tenantId = UserUtils.getUser().getOrgid().toString();
        String userId = UserUtils.getUser().getStaffid().toString();
        Date now = new Date();

        // 1. 删除旧的股权结构数据
        equityStructureMapper.deleteByModelId(modelId);

        // 2. 获取所有股权信息
        List<TblEquityInfo> equityList = equityInfoMapper.selectByModelId(modelId);
        if (equityList == null || equityList.isEmpty()) {
            throw new RuntimeException("该模型没有股权信息数据");
        }

        // 3. 构建股权关系图
        Map<String, List<TblEquityInfo>> parentChildMap = new HashMap<>();
        Set<String> allOrgs = new HashSet<>();
        Set<String> childOrgs = new HashSet<>();

        for (TblEquityInfo equity : equityList) {
            String parentId = equity.getParentOrgId();
            String childId = equity.getSubsidiaryOrgId();

            allOrgs.add(parentId);
            allOrgs.add(childId);
            childOrgs.add(childId);

            parentChildMap.computeIfAbsent(parentId, k -> new ArrayList<>()).add(equity);
        }

        // 4. 找到顶层母公司(没有作为子公司出现的组织)
        Set<String> rootOrgs = new HashSet<>(allOrgs);
        rootOrgs.removeAll(childOrgs);

        if (rootOrgs.isEmpty()) {
            throw new RuntimeException("未找到顶层母公司,可能存在循环持股");
        }

        // 5. 计算股权结构
        List<TblEquityStructure> structureList = new ArrayList<>();
        for (String rootOrg : rootOrgs) {
            // 添加顶层母公司
            TblEquityStructure rootStructure = new TblEquityStructure();
            rootStructure.setStructureId(UUID.randomUUID().toString().replace("-", ""));
            rootStructure.setModelId(modelId);
            rootStructure.setOrgId(rootOrg);
            rootStructure.setOrgName(getOrgName(equityList, rootOrg));
            rootStructure.setParentOrgId(null);
            rootStructure.setDirectHoldingRatio(new BigDecimal("100.0000"));
            rootStructure.setIndirectHoldingRatio(BigDecimal.ZERO);
            rootStructure.setTotalHoldingRatio(new BigDecimal("100.0000"));
            rootStructure.setLevel(1);
            rootStructure.setPath(rootOrg);
            rootStructure.setCalculationDate(now);
            rootStructure.setTenantId(tenantId);
            rootStructure.setCreateUser(userId);
            rootStructure.setCreateTime(now);
            rootStructure.setUpdateUser(userId);
            rootStructure.setUpdateTime(now);
            structureList.add(rootStructure);

            // 递归计算子公司
            calculateChildren(rootOrg, rootOrg, new BigDecimal("100.0000"), 2, 
                parentChildMap, structureList, modelId, tenantId, userId, now);
        }

        // 6. 批量插入股权结构数据
        if (!structureList.isEmpty()) {
            equityStructureMapper.batchInsert(structureList);
        }
    }

    /**
     * 递归计算子公司股权结构
     */
    private void calculateChildren(String rootOrg, String parentOrg, BigDecimal parentRatio,
                                   int level, Map<String, List<TblEquityInfo>> parentChildMap,
                                   List<TblEquityStructure> structureList, String modelId,
                                   String tenantId, String userId, Date now) {
        List<TblEquityInfo> children = parentChildMap.get(parentOrg);
        if (children == null || children.isEmpty()) {
            return;
        }

        for (TblEquityInfo equity : children) {
            String childOrg = equity.getSubsidiaryOrgId();
            BigDecimal holdingRatio = equity.getHoldingRatio();

            // 计算从顶层母公司到当前子公司的综合持股比例
            BigDecimal totalRatio = parentRatio.multiply(holdingRatio)
                .divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);

            TblEquityStructure structure = new TblEquityStructure();
            structure.setStructureId(UUID.randomUUID().toString().replace("-", ""));
            structure.setModelId(modelId);
            structure.setOrgId(childOrg);
            structure.setOrgName(equity.getSubsidiaryOrgName());
            structure.setParentOrgId(parentOrg);

            // 判断是直接持股还是间接持股
            if (parentOrg.equals(rootOrg)) {
                // 直接持股
                structure.setDirectHoldingRatio(holdingRatio);
                structure.setIndirectHoldingRatio(BigDecimal.ZERO);
            } else {
                // 间接持股
                structure.setDirectHoldingRatio(BigDecimal.ZERO);
                structure.setIndirectHoldingRatio(holdingRatio);
            }

            structure.setTotalHoldingRatio(totalRatio);
            structure.setLevel(level);
            structure.setPath(getPath(structureList, parentOrg) + "/" + childOrg);
            structure.setCalculationDate(now);
            structure.setTenantId(tenantId);
            structure.setCreateUser(userId);
            structure.setCreateTime(now);
            structure.setUpdateUser(userId);
            structure.setUpdateTime(now);

            structureList.add(structure);

            // 递归计算下一层
            calculateChildren(rootOrg, childOrg, totalRatio, level + 1,
                parentChildMap, structureList, modelId, tenantId, userId, now);
        }
    }

    /**
     * 获取组织名称
     */
    private String getOrgName(List<TblEquityInfo> equityList, String orgId) {
        for (TblEquityInfo equity : equityList) {
            if (equity.getParentOrgId().equals(orgId)) {
                return equity.getParentOrgName();
            }
            if (equity.getSubsidiaryOrgId().equals(orgId)) {
                return equity.getSubsidiaryOrgName();
            }
        }
        return orgId;
    }

    /**
     * 获取路径
     */
    private String getPath(List<TblEquityStructure> structureList, String orgId) {
        for (TblEquityStructure structure : structureList) {
            if (structure.getOrgId().equals(orgId)) {
                return structure.getPath();
            }
        }
        return orgId;
    }

    @Override
    public List<TblEquityStructure> getEquityStructureList(String modelId) {
        return equityStructureMapper.selectByModelId(modelId);
    }

    @Override
    public List<TblEquityStructure> getEquityStructureTree(String modelId) {
        List<TblEquityStructure> allList = equityStructureMapper.selectByModelId(modelId);
        if (allList == null || allList.isEmpty()) {
            return new ArrayList<>();
        }

        // 构建树形结构
        Map<String, TblEquityStructure> map = allList.stream()
            .collect(Collectors.toMap(TblEquityStructure::getOrgId, s -> s));

        List<TblEquityStructure> treeList = new ArrayList<>();
        for (TblEquityStructure structure : allList) {
            if (structure.getParentOrgId() == null || structure.getParentOrgId().isEmpty()) {
                // 顶层节点
                treeList.add(structure);
            } else {
                // 子节点,添加到父节点的children中
                TblEquityStructure parent = map.get(structure.getParentOrgId());
                if (parent != null) {
                    if (parent.getChildren() == null) {
                        parent.setChildren(new ArrayList<>());
                    }
                    parent.getChildren().add(structure);
                }
            }
        }

        return treeList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteEquityStructure(String modelId) {
        equityStructureMapper.deleteByModelId(modelId);
    }
}


