package com.huabo.fxgl.mapper;

import com.huabo.fxgl.entity.YyPrice;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LiYe
 * @since 2022-08-02
 */
@Repository
public interface YyPriceMapper extends BaseMapper<YyPrice> {

    /**
     * 根据逗号分隔的 priceid 字符串查询价格列表。
     * 注意：调用方需保证传入的 ids 已经是带单引号的格式，例如 "'2','19'" 或 "'cjbdi_03','cjbdi_08'"
     * 兼容纯数字 ID 和 CJBDI 字符串 ID。
     */
    @Select("select * from TBL_YY_PRICE where PRICEID in( ${ids} )")
    List<YyPrice> selectListByIds(@Param("ids") String ids);

}
