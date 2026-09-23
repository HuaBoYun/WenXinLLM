package com.huabo.compliance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.compliance.entity.TblTestplan;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author：yhr
 * @date:2022-09-13 11:34
 * @description:
 */
public interface CsgzService {



    IPage<TblTestplan> findAllTrack(TblTestplan plan, Integer pageNumber, String starttime_min, String starttime_max, BigDecimal orgid,Integer pageSize);

    IPage<TblTestplan> findAllnoSjTrack(TblTestplan plan, Integer pageNumber, String starttime_min, String starttime_max, String toString,Integer pageSize);

    IPage<Map<String, Object>> findAllnoSjTrack2(BigDecimal selectProjectid, Integer pageNumber);
}
