package com.financial.sharing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.entity.TblTravelStandardDetail;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 差旅标准明细Mapper
 */
public interface TblTravelStandardDetailMapper extends BaseMapper<TblTravelStandardDetail> {
    
    /**
     * 根据标准ID查询明细
     */
    List<TblTravelStandardDetail> selectByStandardId(@Param("standardId") String standardId);
    
    /**
     * 根据标准ID和费用类型查询
     */
    TblTravelStandardDetail selectByStandardAndExpenseType(@Param("standardId") String standardId, 
                                                          @Param("expenseType") String expenseType);
    
    /**
     * 删除标准的所有明细
     */
    int deleteByStandardId(@Param("standardId") String standardId);
}

