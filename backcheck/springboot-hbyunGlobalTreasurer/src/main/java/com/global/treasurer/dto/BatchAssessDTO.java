package com.global.treasurer.dto;

import java.util.List;

/**
 * 批量评估DTO
 *
 * @author 华博云开发团队
 * @since 2026-03-25
 */
public class BatchAssessDTO {
    /** 要评估的记录ID列表 */
    private List<Long> ids;

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }
}
