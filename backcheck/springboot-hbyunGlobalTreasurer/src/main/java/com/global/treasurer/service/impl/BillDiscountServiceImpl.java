package com.global.treasurer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.BillDiscountDTO;
import com.global.treasurer.dto.BillDiscountQueryDTO;
import com.global.treasurer.entity.TblBillDiscount;
import com.global.treasurer.mapper.BillDiscountMapper;
import com.global.treasurer.service.IBillDiscountService;
import com.global.treasurer.vo.BillDiscountVO;
import com.hbfk.util.BizException;
import com.hbfk.util.user.UserProvider;
import com.hbfk.entity.TblStaffUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 票据贴现Service实现类
 *
 * @author 华博云开发团队
 * @since 2025-12-29
 */
@Service
public class BillDiscountServiceImpl extends ServiceImpl<BillDiscountMapper, TblBillDiscount>
        implements IBillDiscountService {
    private static final Logger log = LoggerFactory.getLogger(BillDiscountServiceImpl.class);

    @Autowired
    private UserProvider userProvider;

    /**
     * 获取当前登录用户名
     */
    private String getLoginUserName() {
        try {
            TblStaffUtil user = userProvider.get();
            return user != null ? user.getUsername() : "system";
        } catch (Exception e) {
            return "system";
        }
    }

    @Override
    public PageInfo<BillDiscountVO> selectBillDiscountList(BillDiscountQueryDTO queryDTO) {
        if (queryDTO == null) {
            throw new BizException("查询参数不能为空");
        }
        PageHelper.startPage(queryDTO.getPageNum() != null ? queryDTO.getPageNum() : 1,
                            queryDTO.getPageSize() != null ? queryDTO.getPageSize() : 10);
        List<BillDiscountVO> list = baseMapper.selectBillDiscountList(queryDTO);
        return new PageInfo<>(list);
    }

    @Override
    public BillDiscountVO selectBillDiscountById(Long discountId) {
        if (discountId == null) {
            throw new BizException("贴现ID不能为空");
        }
        return baseMapper.selectBillDiscountById(discountId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblBillDiscount insertBillDiscount(BillDiscountDTO dto) {
        TblBillDiscount discount = new TblBillDiscount();
        BeanUtils.copyProperties(dto, discount);
        
        // 生成贴现编号
        discount.setDiscountNumber(generateDiscountNumber());
        
        // 计算贴现利息和贴现金额
        if (dto.getBillAmount() != null && dto.getDiscountRate() != null && dto.getDiscountPeriod() != null) {
            BigDecimal interest = dto.getBillAmount()
                    .multiply(dto.getDiscountRate())
                    .multiply(new BigDecimal(dto.getDiscountPeriod()))
                    .divide(new BigDecimal(36000), 2, RoundingMode.HALF_UP);
            discount.setDiscountInterest(interest);
            discount.setDiscountAmount(dto.getBillAmount().subtract(interest));
        }
        
        discount.setDiscountStatus("PENDING");
        discount.setDeleteFlag(0);
        discount.setCreateTime(new Date());
        discount.setUpdateTime(new Date());
        discount.setApplicationDate(new Date());
        discount.setDiscountId(System.currentTimeMillis());

        // 设置申请人信息
        if (discount.getApplicantName() == null || discount.getApplicantName().isEmpty()) {
            discount.setApplicantName(getLoginUserName());
        }
        if (discount.getCreateBy() == null || discount.getCreateBy().isEmpty()) {
            discount.setCreateBy(getLoginUserName());
        }

        baseMapper.insert(discount);
        return discount;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblBillDiscount updateBillDiscount(BillDiscountDTO dto) {
        if (dto.getDiscountId() == null) {
            throw new BizException("贴现ID不能为空");
        }
        TblBillDiscount exist = baseMapper.selectById(dto.getDiscountId());
        if (exist == null || exist.getDeleteFlag() == 1) {
            throw new BizException("贴现记录不存在或已删除");
        }
        if (!"PENDING".equals(exist.getDiscountStatus())) {
            throw new BizException("只能修改待审批状态的贴现记录");
        }
        TblBillDiscount discount = new TblBillDiscount();
        BeanUtils.copyProperties(dto, discount);
        discount.setUpdateTime(new Date());
        baseMapper.updateById(discount);
        return discount;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBillDiscountByIds(Long[] discountIds) {
        if (discountIds == null || discountIds.length == 0) {
            throw new BizException("请选择要删除的贴现记录");
        }
        return baseMapper.deleteBillDiscountByIds(discountIds) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean approveBillDiscount(Long discountId, Map<String, Object> approvalData) {
        TblBillDiscount discount = baseMapper.selectById(discountId);
        if (discount == null) {
            throw new BizException("贴现记录不存在");
        }
        String approvalResult = (String) approvalData.get("approvalResult");
        if ("APPROVED".equals(approvalResult)) {
            discount.setDiscountStatus("APPROVED");
        } else if ("REJECTED".equals(approvalResult)) {
            discount.setDiscountStatus("REJECTED");
        } else {
            throw new BizException("无效的审批结果: " + approvalResult);
        }
        discount.setApprovalComment((String) approvalData.get("approvalComment"));
        discount.setApprovalDate(new Date());
        discount.setUpdateTime(new Date());
        return baseMapper.updateById(discount) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean executeBillDiscount(Long discountId) {
        TblBillDiscount discount = baseMapper.selectById(discountId);
        if (discount == null) {
            throw new BizException("贴现记录不存在");
        }
        if (!"APPROVED".equals(discount.getDiscountStatus())) {
            throw new BizException("只能执行已审批通过的贴现");
        }
        discount.setDiscountStatus("COMPLETED");
        discount.setActualDiscountDate(new Date());
        discount.setUpdateTime(new Date());
        return baseMapper.updateById(discount) > 0;
    }

    @Override
    public List<Map<String, Object>> getAvailableBillsForDiscount(Map<String, Object> params) {
        if (params == null) {
            params = new java.util.HashMap<>();
        }
        List<Map<String, Object>> result = baseMapper.selectAvailableBillsForDiscount(params);
        // 达梦数据库返回的Map key可能是大写，需要转换为驼峰命名以匹配前端期望的格式
        if (result != null && !result.isEmpty()) {
            return result.stream().map(this::convertKeysToCamelCase).collect(java.util.stream.Collectors.toList());
        }
        return result;
    }

    /**
     * 将Map的key从大写转换为驼峰命名
     */
    private Map<String, Object> convertKeysToCamelCase(Map<String, Object> map) {
        Map<String, Object> result = new java.util.LinkedHashMap<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            // 如果key已经是驼峰命名（包含小写字母），直接使用
            if (key.chars().anyMatch(Character::isLowerCase)) {
                result.put(key, entry.getValue());
            } else {
                // 将大写下划线格式转换为驼峰命名
                result.put(toCamelCase(key), entry.getValue());
            }
        }
        return result;
    }

    /**
     * 将大写下划线格式转换为驼峰命名
     * 例如: BILL_NUMBER -> billNumber, INSTRUMENT_ID -> instrumentId
     */
    private String toCamelCase(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        StringBuilder result = new StringBuilder();
        boolean nextUpper = false;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '_') {
                nextUpper = true;
            } else {
                if (nextUpper) {
                    result.append(Character.toUpperCase(c));
                    nextUpper = false;
                } else {
                    result.append(Character.toLowerCase(c));
                }
            }
        }
        return result.toString();
    }

    private String generateDiscountNumber() {
        return "TX" + System.currentTimeMillis();
    }

    @Override
    public Map<String, Object> getBillDiscountStatistics(BillDiscountQueryDTO queryDTO) {
        Map<String, Object> result = new java.util.HashMap<>();
        try {
            Map<String, Object> stats = baseMapper.selectDiscountStatistics(queryDTO);
            if (stats != null && !stats.isEmpty()) {
                // SQL 别名已用引号保持大小写，直接按驼峰 key 取值
                // 同时兼容达梦可能返回大写 key 的情况
                result.put("totalDiscounts", getIntValue(getIgnoreCase(stats, "totalDiscounts")));
                result.put("pendingDiscounts", getIntValue(getIgnoreCase(stats, "pendingDiscounts")));
                result.put("completedDiscounts", getIntValue(getIgnoreCase(stats, "completedDiscounts")));
                result.put("totalAmount", getBigDecimalValue(getIgnoreCase(stats, "totalAmount")));
            } else {
                log.warn("贴现统计查询返回空结果, queryDTO={}", queryDTO);
                result.put("totalDiscounts", 0);
                result.put("pendingDiscounts", 0);
                result.put("completedDiscounts", 0);
                result.put("totalAmount", BigDecimal.ZERO);
            }
        } catch (Exception e) {
            log.error("查询贴现统计数据异常", e);
            result.put("totalDiscounts", 0);
            result.put("pendingDiscounts", 0);
            result.put("completedDiscounts", 0);
            result.put("totalAmount", BigDecimal.ZERO);
        }
        return result;
    }

    /**
     * 从 Map 中按 key 忽略大小写取值，兼容达梦数据库返回大写 key 的情况
     */
    private Object getIgnoreCase(Map<String, Object> map, String key) {
        Object val = map.get(key);
        if (val != null) {
            return val;
        }
        // 尝试全小写
        val = map.get(key.toLowerCase());
        if (val != null) {
            return val;
        }
        // 尝试全大写
        val = map.get(key.toUpperCase());
        if (val != null) {
            return val;
        }
        // 遍历查找
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(key)) {
                return entry.getValue();
            }
        }
        return null;
    }

    /**
     * 安全获取整数值
     */
    private int getIntValue(Object obj) {
        if (obj == null) {
            return 0;
        }
        if (obj instanceof Number) {
            return ((Number) obj).intValue();
        }
        try {
            return Integer.parseInt(obj.toString());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * 安全获取 BigDecimal 值
     */
    private BigDecimal getBigDecimalValue(Object obj) {
        if (obj == null) {
            return BigDecimal.ZERO;
        }
        if (obj instanceof BigDecimal) {
            return (BigDecimal) obj;
        }
        if (obj instanceof Number) {
            return new BigDecimal(obj.toString());
        }
        try {
            return new BigDecimal(obj.toString());
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }
}

