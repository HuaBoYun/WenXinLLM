package com.huabo.cybermonitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.cybermonitor.entity.TblPropertyRight;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.vo.TblPropertyRightQueryVO;

import java.util.Map;

public interface ITblPropertyRightService extends IService<TblPropertyRight> {
    PageResult<TblPropertyRight> selectByPage(TblPropertyRightQueryVO queryVO);
    boolean addRecord(TblPropertyRight record);
    boolean updateRecord(TblPropertyRight record);
    boolean deleteRecord(String id);

    /** 兼容旧调用：仅按 companyId 过滤 */
    default Map<String, Object> getStatistics(String companyId) {
        return getStatistics(companyId, null);
    }

    /**
     * 按 companyId 或 companyName 过滤统计（两者都为空时返回全集团）
     * 说明：DB 中 companyId 可能存在脏数据（多家公司共享同一 ID），
     *      此时业务方可改用 companyName 作过滤主键
     */
    Map<String, Object> getStatistics(String companyId, String companyName);
}

