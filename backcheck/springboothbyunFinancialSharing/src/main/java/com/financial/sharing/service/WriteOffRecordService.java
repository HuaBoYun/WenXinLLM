package com.financial.sharing.service;

import com.financial.sharing.dto.param.WriteOffRecordQueryParam;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.result.WriteOffRecordVO;

/**
 * 核销记录Service
 * @author system
 * @since 2025-01-13
 */
public interface WriteOffRecordService {

    /**
     * 分页查询核销记录
     * @param param 查询参数
     * @return 分页结果
     */
    PageResult<WriteOffRecordVO> queryPage(WriteOffRecordQueryParam param);

    /**
     * 查询核销记录详情
     * @param writeOffId 核销ID
     * @return 核销记录详情
     */
    WriteOffRecordVO getDetail(String writeOffId);

    /**
     * 撤销核销
     * @param writeOffId 核销ID
     */
    void reverseWriteOff(String writeOffId);
}
