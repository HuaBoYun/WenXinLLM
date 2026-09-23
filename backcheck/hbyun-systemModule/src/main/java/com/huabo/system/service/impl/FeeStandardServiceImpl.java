package com.huabo.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.system.dto.FeeStandardSaveDTO;
import com.huabo.system.entity.TblFeeStandard;
import com.huabo.system.entity.TblFeeStandardLog;
import com.huabo.system.entity.TblFeeStandardTier;
import com.huabo.system.entity.TblSystemRight;
import com.huabo.system.mapper.TblFeeStandardLogMapper;
import com.huabo.system.mapper.TblFeeStandardMapper;
import com.huabo.system.mapper.TblFeeStandardTierMapper;
import com.huabo.system.mapper.TblSystemRightMapper;
import com.huabo.system.service.FeeStandardService;
import com.hbfk.util.user.UserProvider;
import com.huabo.system.vo.FeeStandardTreeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FeeStandardServiceImpl implements FeeStandardService {

    @Resource
    private TblFeeStandardMapper feeStandardMapper;
    @Resource
    private TblFeeStandardLogMapper feeStandardLogMapper;
    @Resource
    private TblSystemRightMapper systemRightMapper;
    @Resource
    private TblFeeStandardTierMapper feeStandardTierMapper;
    @Resource
    private UserProvider userProvider;

    @Override
    public JsonBean getStandardList(String token, String moduleType) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

        // 1. Query all visible rights (TYPE=0 directory and TYPE=1 page only)
        QueryWrapper<TblSystemRight> rightWrapper = new QueryWrapper<>();
        rightWrapper.eq("VISIBLE", 1);
        rightWrapper.in("TYPE", Arrays.asList(0, 1));
        if (moduleType != null && !moduleType.isEmpty()) {
            rightWrapper.eq("MODULETYPE", moduleType);
        }
        rightWrapper.orderByAsc("SORT");
        List<TblSystemRight> rightList = systemRightMapper.selectList(rightWrapper);

        // 2. Query all fee standards
        List<TblFeeStandard> standardList = feeStandardMapper.selectList(null);
        Map<BigDecimal, TblFeeStandard> standardMap = standardList.stream()
                .collect(Collectors.toMap(TblFeeStandard::getRightId, s -> s, (a, b) -> a));

        // 3. Convert to VO and attach fee info
        List<FeeStandardTreeVO> voList = rightList.stream().map(right -> {
            FeeStandardTreeVO vo = new FeeStandardTreeVO();
            vo.setId(right.getId());
            vo.setName(right.getName());
            vo.setParent(right.getParent());
            vo.setType(right.getType());
            vo.setPerms(right.getPerms());
            vo.setPath(right.getPath());
            vo.setModuletype(right.getModuletype());
            vo.setVisible(right.getVisible());
            // Attach fee standard info for page nodes (TYPE=1)
            if (right.getType() != null && right.getType() == 1) {
                TblFeeStandard std = standardMap.get(right.getId());
                if (std != null) {
                    vo.setFeeStandardId(std.getId());
                    vo.setFeeType(std.getFeeType());
                    vo.setFeeAmount(std.getFeeAmount());
                    vo.setFeeStatus(std.getStatus());
                    vo.setUpdateTime(std.getUpdateTime());
                    vo.setUpdateBy(std.getUpdateBy());
                    // 按调用量时附加阶梯规则
                    if (std.getFeeType() != null && std.getFeeType() == 2) {
                        vo.setTiers(new ArrayList<>(feeStandardTierMapper.findByStandardId(std.getId())));
                    }
                }
            }
            return vo;
        }).collect(Collectors.toList());

        // 4. Build tree structure
        List<FeeStandardTreeVO> tree = buildTree(voList);

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("list", tree);
        return ResponseFormat.retParam(1, 200, resultMap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonBean saveStandard(String token, FeeStandardSaveDTO dto) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

        if (dto.getRightId() == null) {
            return ResponseFormat.retParam(0, 10002, null);
        }

        // 1. Look up the right node
        TblSystemRight right = systemRightMapper.findById(dto.getRightId());
        if (right == null) {
            return ResponseFormat.retParam(0, 10001, null);
        }

        // 2. Check if fee standard already exists
        TblFeeStandard existing = feeStandardMapper.findByRightId(dto.getRightId());
        Date now = new Date();
        String operator = loginStaff.getRealname();

        // feeAmount为null表示"重置为服务默认值"：删除费用标准记录，让BillingAspect回退到各服务yml的default-fee-amount
        if (dto.getFeeAmount() == null) {
            if (existing != null) {
                // 删除阶梯规则
                feeStandardTierMapper.deleteByStandardId(existing.getId());
                // 删除费用标准记录
                feeStandardMapper.deleteById(existing.getId());
                log.info("[费用标准] 重置为服务默认值: rightId={}, rightName={}, operator={}", dto.getRightId(), right.getName(), operator);
            }
            // 记录变更日志（标记为重置）
            TblFeeStandardLog logEntry = new TblFeeStandardLog();
            logEntry.setId(feeStandardLogMapper.getNextId());
            logEntry.setRightId(dto.getRightId());
            logEntry.setRightName(right.getName());
            logEntry.setModuleType(right.getModuletype());
            logEntry.setFeeType(dto.getFeeType());
            logEntry.setFeeAmount(null);
            logEntry.setChangeTime(now);
            logEntry.setChangeBy(operator);
            logEntry.setRemark(dto.getRemark() != null ? dto.getRemark() : "重置为服务默认值");
            feeStandardLogMapper.insert(logEntry);
            return ResponseFormat.retParam(1, 200, null);
        }

        if (existing != null) {
            // Update existing
            existing.setFeeType(dto.getFeeType());
            existing.setFeeAmount(dto.getFeeAmount());
            existing.setUpdateTime(now);
            existing.setUpdateBy(operator);
            feeStandardMapper.updateById(existing);
        } else {
            // Insert new — feeAmount已确认非null，不再兜底为BigDecimal.ONE
            TblFeeStandard newStd = new TblFeeStandard();
            newStd.setId(feeStandardMapper.getNextId());
            newStd.setRightId(dto.getRightId());
            newStd.setRightName(right.getName());
            newStd.setModuleType(right.getModuletype());
            newStd.setFeeType(dto.getFeeType() != null ? dto.getFeeType() : 1);
            newStd.setFeeAmount(dto.getFeeAmount());
            newStd.setStatus(1);
            newStd.setCreateTime(now);
            newStd.setUpdateTime(now);
            newStd.setUpdateBy(operator);
            feeStandardMapper.insert(newStd);
        }

        // 3. Insert price snapshot log
        TblFeeStandardLog logEntry = new TblFeeStandardLog();
        logEntry.setId(feeStandardLogMapper.getNextId());
        logEntry.setRightId(dto.getRightId());
        logEntry.setRightName(right.getName());
        logEntry.setModuleType(right.getModuletype());
        logEntry.setFeeType(dto.getFeeType());
        logEntry.setFeeAmount(dto.getFeeAmount());
        logEntry.setChangeTime(now);
        logEntry.setChangeBy(operator);
        logEntry.setRemark(dto.getRemark());
        feeStandardLogMapper.insert(logEntry);

        // 4. 如果是按调用量(FEE_TYPE=2)，保存阶梯规则
        if (dto.getFeeType() != null && dto.getFeeType() == 2 && dto.getTiers() != null) {
            // 获取当前标准ID
            TblFeeStandard currentStd = feeStandardMapper.findByRightId(dto.getRightId());
            if (currentStd != null) {
                // 先删旧阶梯
                feeStandardTierMapper.deleteByStandardId(currentStd.getId());
                // 插入新阶梯
                int sort = 0;
                for (FeeStandardSaveDTO.TierItem tier : dto.getTiers()) {
                    TblFeeStandardTier t = new TblFeeStandardTier();
                    t.setId(feeStandardTierMapper.getNextId());
                    t.setStandardId(currentStd.getId());
                    t.setMinCount(tier.getMinCount() != null ? tier.getMinCount() : 0);
                    t.setMaxCount(tier.getMaxCount());
                    t.setFeeAmount(tier.getFeeAmount());
                    t.setSort(sort++);
                    feeStandardTierMapper.insert(t);
                }
            }
        }

        return ResponseFormat.retParam(1, 200, null);
    }

    /**
     * Build tree from flat list
     * 规则：上级目录未启用（不在列表中）时，其下的页面也不展示
     */
    private List<FeeStandardTreeVO> buildTree(List<FeeStandardTreeVO> list) {
        Map<BigDecimal, FeeStandardTreeVO> map = new LinkedHashMap<>();
        for (FeeStandardTreeVO vo : list) {
            map.put(vo.getId(), vo);
        }
        List<FeeStandardTreeVO> roots = new ArrayList<>();
        for (FeeStandardTreeVO vo : list) {
            BigDecimal parentId = vo.getParent();
            if (parentId == null || parentId.compareTo(BigDecimal.ZERO) == 0) {
                // 顶级节点，直接加入根
                roots.add(vo);
            } else if (map.containsKey(parentId)) {
                // 父节点存在（已启用），挂到父节点下
                FeeStandardTreeVO parent = map.get(parentId);
                if (parent.getChildren() == null) {
                    parent.setChildren(new ArrayList<>());
                }
                parent.getChildren().add(vo);
            }
            // 父节点不在列表中（未启用）→ 丢弃该节点，不展示
        }
        return roots;
    }
}
