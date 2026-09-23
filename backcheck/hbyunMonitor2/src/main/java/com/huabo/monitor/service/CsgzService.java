package com.huabo.monitor.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.pagehelper.PageInfo;
import com.hbfk.entity.TblStaffUtil;
import com.huabo.monitor.entity.TblTestplan;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author：yhr
 * @date:2022-09-13 11:34
 * @description:
 */
public interface CsgzService {



    IPage<TblTestplan> findAllTrack(TblTestplan plan, Integer pageNumber, String starttime_min, String starttime_max, BigDecimal orgid);

    IPage<TblTestplan> findAllnoSjTrack(TblTestplan plan, Integer pageNumber, String starttime_min, String starttime_max, String toString);

    IPage<Map<String, Object>> findAllnoSjTrack2(BigDecimal selectProjectid, Integer pageNumber);
    
    PageInfo<Map<String, Object>> findAllnoSjTrack2New(BigDecimal selectProjectid, Integer pageNumber);

    
    //new 方法为数据库兼容之后的接口
    PageInfo<TblTestplan> findAllTrackNew(TblTestplan plan, Integer pageNumber, String starttime_min, String starttime_max, BigDecimal orgid,TblStaffUtil user) throws Exception;

    PageInfo<TblTestplan> findAllnoSjTrackNew(TblTestplan plan, Integer pageNumber, String starttime_min, String starttime_max, String toString,TblStaffUtil user) throws Exception;

}
