package com.financial.sharing.util;

import com.financial.sharing.service.CostCenterService;
import com.financial.sharing.vo.result.CostCenterVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 中心验证工具类
 *
 * @author Financial Sharing System
 * @since 2024-12-03
 */
@Slf4j
@Component
public class CenterValidationUtil {

    @Resource
    private CostCenterService costCenterService;

    /**
     * 验证成本中心是否存在且可用
     *
     * @param centerId 中心ID
     * @param bookId 账簿ID
     * @return 如果中心存在且可用返回true，否则返回false
     */
    public boolean validateCostCenter(String centerId, String bookId) {
        try {
            if (centerId == null || bookId == null) {
                log.warn("中心ID或账簿ID为空，centerId: {}, bookId: {}", centerId, bookId);
                return false;
            }

            MyJsonBean<CostCenterVO> result = costCenterService.getCostCenterById(centerId);
            if (result.getCode() != 200 || result.getData() == null) {
                log.warn("成本中心不存在，centerId: {}", centerId);
                return false;
            }

            CostCenterVO center = result.getData();
            boolean isValid = center.getIsEnabled() == 1 && center.getBookId().equals(bookId);

            if (!isValid) {
                log.warn("成本中心不可用，centerId: {}, isEnabled: {}, centerBookId: {}, paramBookId: {}",
                    centerId, center.getIsEnabled(), center.getBookId(), bookId);
            }

            return isValid;
        } catch (Exception e) {
            log.error("验证成本中心时发生异常，centerId: {}, bookId: {}", centerId, bookId, e);
            return false;
        }
    }

    /**
     * 批量验证成本中心
     *
     * @param centerIds 中心ID集合
     * @param bookId 账簿ID
     * @return 验证结果映射，key为中心ID，value为验证结果
     */
    public Map<String, Boolean> batchValidateCostCenters(Set<String> centerIds, String bookId) {
        Map<String, Boolean> result = new HashMap<>();

        if (centerIds == null || centerIds.isEmpty() || bookId == null) {
            log.warn("批量验证参数不合法，centerIds: {}, bookId: {}", centerIds, bookId);
            return result;
        }

        log.info("开始批量验证成本中心，数量: {}, 账簿: {}", centerIds.size(), bookId);

        for (String centerId : centerIds) {
            boolean isValid = validateCostCenter(centerId, bookId);
            result.put(centerId, isValid);

            if (!isValid) {
                log.warn("成本中心验证失败，centerId: {}, bookId: {}", centerId, bookId);
            }
        }

        long successCount = result.values().stream().mapToLong(valid -> valid ? 1 : 0).sum();
        log.info("批量验证完成，成功: {}/{}, 失败: {}",
            successCount, centerIds.size(), centerIds.size() - successCount);

        return result;
    }

    /**
     * 验证成本中心并获取详细信息
     *
     * @param centerId 中心ID
     * @param bookId 账簿ID
     * @return 验证结果对象，包含验证状态和中心信息
     */
    public CenterValidationResult validateCostCenterWithDetails(String centerId, String bookId) {
        CenterValidationResult result = new CenterValidationResult();
        result.setCenterId(centerId);
        result.setBookId(bookId);

        try {
            if (centerId == null || bookId == null) {
                result.setValid(false);
                result.setMessage("中心ID或账簿ID为空");
                return result;
            }

            MyJsonBean<CostCenterVO> serviceResult = costCenterService.getCostCenterById(centerId);

            if (serviceResult.getCode() != 200) {
                result.setValid(false);
                result.setMessage("服务调用失败: " + serviceResult.getMsg());
                return result;
            }

            CostCenterVO center = serviceResult.getData();
            if (center == null) {
                result.setValid(false);
                result.setMessage("成本中心不存在");
                return result;
            }

            result.setCenter(center);

            // 删除状态检查（因为CostCenterVO没有isDeleted字段，我们假设已删除的数据不会返回）
            // 如果业务需要检查删除状态，需要在服务层实现

            // 检查启用状态
            if (center.getIsEnabled() != 1) {
                result.setValid(false);
                result.setMessage("成本中心已禁用");
                return result;
            }

            // 检查账簿匹配
            if (!center.getBookId().equals(bookId)) {
                result.setValid(false);
                result.setMessage(String.format("账簿不匹配，中心账簿: %s，参数账簿: %s",
                    center.getBookId(), bookId));
                return result;
            }

            result.setValid(true);
            result.setMessage("验证通过");
            return result;

        } catch (Exception e) {
            log.error("验证成本中心时发生异常，centerId: {}, bookId: {}", centerId, bookId, e);
            result.setValid(false);
            result.setMessage("验证过程中发生异常: " + e.getMessage());
            return result;
        }
    }

    /**
     * 验证源成本中心和目标成本中心的关系
     *
     * @param sourceCenterId 源中心ID
     * @param targetCenterId 目标中心ID
     * @param bookId 账簿ID
     * @return 验证结果
     */
    public CenterRelationshipValidationResult validateCenterRelationship(
            String sourceCenterId, String targetCenterId, String bookId) {

        CenterRelationshipValidationResult result = new CenterRelationshipValidationResult();
        result.setSourceCenterId(sourceCenterId);
        result.setTargetCenterId(targetCenterId);
        result.setBookId(bookId);

        try {
            // 检查基本参数
            if (sourceCenterId == null || targetCenterId == null || bookId == null) {
                result.setValid(false);
                result.setMessage("参数不完整，源中心、目标中心或账簿ID为空");
                return result;
            }

            // 检查源中心不能等于目标中心
            if (sourceCenterId.equals(targetCenterId)) {
                result.setValid(false);
                result.setMessage("源成本中心不能等于目标成本中心");
                return result;
            }

            // 验证源中心
            CenterValidationResult sourceValidation = validateCostCenterWithDetails(sourceCenterId, bookId);
            result.setSourceValidation(sourceValidation);

            if (!sourceValidation.isValid()) {
                result.setValid(false);
                result.setMessage("源中心验证失败: " + sourceValidation.getMessage());
                return result;
            }

            // 验证目标中心
            CenterValidationResult targetValidation = validateCostCenterWithDetails(targetCenterId, bookId);
            result.setTargetValidation(targetValidation);

            if (!targetValidation.isValid()) {
                result.setValid(false);
                result.setMessage("目标中心验证失败: " + targetValidation.getMessage());
                return result;
            }

            result.setValid(true);
            result.setMessage("中心关系验证通过");
            return result;

        } catch (Exception e) {
            log.error("验证中心关系时发生异常，sourceId: {}, targetId: {}, bookId: {}",
                sourceCenterId, targetCenterId, bookId, e);
            result.setValid(false);
            result.setMessage("验证过程中发生异常: " + e.getMessage());
            return result;
        }
    }

    /**
     * 中心验证结果内部类
     */
    public static class CenterValidationResult {
        private String centerId;
        private String bookId;
        private boolean valid;
        private String message;
        private CostCenterVO center;

        // getters and setters
        public String getCenterId() { return centerId; }
        public void setCenterId(String centerId) { this.centerId = centerId; }
        public String getBookId() { return bookId; }
        public void setBookId(String bookId) { this.bookId = bookId; }
        public boolean isValid() { return valid; }
        public void setValid(boolean valid) { this.valid = valid; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public CostCenterVO getCenter() { return center; }
        public void setCenter(CostCenterVO center) { this.center = center; }
    }

    /**
     * 中心关系验证结果内部类
     */
    public static class CenterRelationshipValidationResult {
        private String sourceCenterId;
        private String targetCenterId;
        private String bookId;
        private boolean valid;
        private String message;
        private CenterValidationResult sourceValidation;
        private CenterValidationResult targetValidation;

        // getters and setters
        public String getSourceCenterId() { return sourceCenterId; }
        public void setSourceCenterId(String sourceCenterId) { this.sourceCenterId = sourceCenterId; }
        public String getTargetCenterId() { return targetCenterId; }
        public void setTargetCenterId(String targetCenterId) { this.targetCenterId = targetCenterId; }
        public String getBookId() { return bookId; }
        public void setBookId(String bookId) { this.bookId = bookId; }
        public boolean isValid() { return valid; }
        public void setValid(boolean valid) { this.valid = valid; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public CenterValidationResult getSourceValidation() { return sourceValidation; }
        public void setSourceValidation(CenterValidationResult sourceValidation) { this.sourceValidation = sourceValidation; }
        public CenterValidationResult getTargetValidation() { return targetValidation; }
        public void setTargetValidation(CenterValidationResult targetValidation) { this.targetValidation = targetValidation; }
    }
}