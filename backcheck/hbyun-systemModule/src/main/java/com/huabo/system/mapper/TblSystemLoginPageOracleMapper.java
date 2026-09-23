package com.huabo.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.system.entity.TblRole;
import com.huabo.system.entity.TblSystemHomePageOracle;
import com.huabo.system.entity.TblSystemLoginPageOracle;
import com.huabo.system.vo.param.TblSystemLoginPageQueryParam;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.math.BigDecimal;
import java.util.List;

public interface TblSystemLoginPageOracleMapper extends BaseMapper<TblSystemLoginPageOracle> {

    List<TblSystemLoginPageOracle> selectAll(@Param("param") TblSystemLoginPageOracle param);
}