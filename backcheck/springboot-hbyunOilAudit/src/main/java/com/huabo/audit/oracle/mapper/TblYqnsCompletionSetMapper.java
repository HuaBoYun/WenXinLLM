package com.huabo.audit.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblYqnsCompletionSet;
import com.huabo.audit.oracle.entity.TblYqnsGcjsSettlement;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import java.math.BigDecimal;
import java.util.List;

public interface TblYqnsCompletionSetMapper extends BaseMapper<TblYqnsCompletionSet> {


    @SelectProvider(method = "selectListByPageInfo", type = TblYqnsCompletionSetMapperSqlConfig.class)
    @Results({
    })
    List<TblYqnsCompletionSet> selectListByPageInfo(PageInfo<TblYqnsCompletionSet> pageInfo, TblYqnsCompletionSet vo);

    @Select("SELECT MAX(SORTINDEX) FROM TBL_YQNS_COMPLETION_SET ")
	BigDecimal selectMaxNo();
}
