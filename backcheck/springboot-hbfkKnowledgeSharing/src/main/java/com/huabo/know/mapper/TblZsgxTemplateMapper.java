package com.huabo.know.mapper;

import com.huabo.know.entity.TblZsgxTemplate;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 
 * @since 2024-04-03
 */
@Mapper
public interface TblZsgxTemplateMapper extends BaseMapper<TblZsgxTemplate> {

    TblZsgxTemplate selectByNumber(@Param("templateNumber")Integer templateNumber);

}
