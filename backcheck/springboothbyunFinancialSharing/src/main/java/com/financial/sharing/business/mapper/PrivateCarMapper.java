package com.financial.sharing.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.business.entity.TblPrivateCar;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * 私车公用 Mapper
 */
@Component("bizPrivateCarMapper")
public interface PrivateCarMapper extends BaseMapper<TblPrivateCar> {

    TblPrivateCar selectByCarNumber(@Param("carNumber") String carNumber);
}
