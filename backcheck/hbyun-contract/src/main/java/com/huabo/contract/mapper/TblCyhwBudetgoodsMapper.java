package com.huabo.contract.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.contract.entity.TblCyhwBudetgoods;

import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2022-04-07
 */
public interface TblCyhwBudetgoodsMapper extends BaseMapper<TblCyhwBudetgoods> {

    @Select("SELECT * FROM TBL_CYHW_BUDETGOODS WHERE parentId IS NULL")
    List<TblCyhwBudetgoods> findkFirstLevel();

    @Select("SELECT * FROM TBL_CYHW_BUDETGOODS TCB LEFT JOIN TBL_CYHW_PROJECTBUDGET TCP ON TCB.PARENTID = TCP.GOODSTYPE WHERE TCB.PARENTID = #{goodstype}")
    List<TblCyhwBudetgoods> findkListByParentId(Integer goodstype);
}
