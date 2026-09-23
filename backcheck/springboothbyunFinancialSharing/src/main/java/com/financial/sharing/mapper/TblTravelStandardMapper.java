package com.financial.sharing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.entity.TblTravelStandard;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 差旅标准Mapper
 */
public interface TblTravelStandardMapper extends BaseMapper<TblTravelStandard> {
    
    /**
     * 根据标准编码查询
     */
    TblTravelStandard selectByStandardCode(@Param("standardCode") String standardCode);
    
    /**
     * 根据城市级别和职位级别查询
     */
    List<TblTravelStandard> selectByCityAndPosition(@Param("cityLevel") String cityLevel, 
                                                    @Param("positionLevel") String positionLevel);
    
    /**
     * 根据城市级别查询
     */
    List<TblTravelStandard> selectByCityLevel(@Param("cityLevel") String cityLevel);
    
    /**
     * 根据职位级别查询
     */
    List<TblTravelStandard> selectByPositionLevel(@Param("positionLevel") String positionLevel);
}

