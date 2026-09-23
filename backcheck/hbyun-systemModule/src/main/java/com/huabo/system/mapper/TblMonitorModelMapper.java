package com.huabo.system.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.system.entity.TblMonitorModel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2022-04-28
 */
public interface TblMonitorModelMapper extends BaseMapper<TblMonitorModel> {

    @SelectProvider(type=TblMonitorModelMapperSqlConfig.class,method="findByModelJKZX")
    IPage<TblMonitorModel> findByModelJKZX(String solutionid, IPage<TblMonitorModel> page);
}
