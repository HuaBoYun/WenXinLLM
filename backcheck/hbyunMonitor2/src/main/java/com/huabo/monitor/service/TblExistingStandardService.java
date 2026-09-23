package com.huabo.monitor.service;

import com.alibaba.fastjson.JSON;
import com.hbfk.util.JsonBean;
import com.huabo.monitor.entity.TblExistingStandard;
import com.huabo.monitor.entity.TblExistingStandardVo;

import java.math.BigDecimal;

public interface TblExistingStandardService {
    JsonBean insertOrUpdate(TblExistingStandardVo entity) throws Exception;

    JsonBean deleteById(BigDecimal id) throws Exception;

    JsonBean selectList(String ruleNumber,String ruleName,String summaryInfo,Integer pageNumber,Integer pageSize) throws Exception;

    JsonBean selectById(BigDecimal id) throws Exception;

    JsonBean previewById(BigDecimal id) throws Exception;

    /**
     * 删除现行标准附件关联
     * @param attid 附件ID（必填）
     * @param tesId 现行标准ID（必填）
     * @param token 用户token（必填）
     * @return JsonBean
     * @throws Exception
     */
    JsonBean deleteAttachment(BigDecimal attid, BigDecimal tesId, String token) throws Exception;

//    JsonBean 导出
}
