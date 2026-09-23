package com.huabo.system.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 审计问题整改验证完成率DTO
 */
@Data
public class AuditIssueVerificationRateDTO {

    /**
     * 状态列表
     */
    private List<StatusItem> list;

    @Data
    public static class StatusItem {
        /**
         * 状态
         */
        private String s;

        /**
         * 数量
         */
        private BigDecimal c;
    }
}
