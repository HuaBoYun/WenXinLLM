package com.huabo.monitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.monitor.entity.TblAttachment;
import com.huabo.monitor.entity.TblBug;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yhr
 * @since 2022-08-26
 */
public interface TblBugMapper extends BaseMapper<TblBug> {

	@Select("${sql}")
	    <p extends IPage<TblBug>> p getSqlPage(p page, @Param("sql") String sql);
	
	@Select("${sql}")
    List<TblBug> findBySql(@Param("sql") String sql);

	@Select("select * from TBL_BUG where bugid = #{bugid}")
	TblBug findById(@Param("bugid") BigDecimal bugid);
	
	@Select("${sql}")
    TblBug excuteFunReturnUniqueBug(@Param("sql") String sql);
}
