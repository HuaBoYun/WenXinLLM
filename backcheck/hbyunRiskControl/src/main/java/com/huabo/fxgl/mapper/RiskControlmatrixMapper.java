package com.huabo.fxgl.mapper;

import com.huabo.fxgl.entity.RiskControlmatrix;
import com.huabo.fxgl.entity.Riskcategory;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LiYe
 * @since 2022-08-05
 */
@Repository
public interface RiskControlmatrixMapper extends BaseMapper<RiskControlmatrix> {
}
