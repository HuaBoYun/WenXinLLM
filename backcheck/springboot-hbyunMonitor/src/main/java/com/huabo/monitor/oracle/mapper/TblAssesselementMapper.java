package com.huabo.monitor.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.huabo.monitor.oracle.entity.TblAssesselement;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;


/**
 * 要素 dao
 *
 * @author SongXiangYing
 */
public interface TblAssesselementMapper extends BaseMapper<TblAssesselement> {
    @SelectProvider(type = TblAssesselementMapperSqlConfig.class, method = "findByPageBean")
    List<String> findByPageBean(PageInfo<TblAssesselement> pageInfo);
}