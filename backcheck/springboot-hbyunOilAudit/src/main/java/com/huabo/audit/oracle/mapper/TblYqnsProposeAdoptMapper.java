package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.entity.TblYqnsProposeAdopt;

/**
 * @ Author: Striker dev@example.com
 * @ Date: 2023-09-07 19:30
 * @ TODO:
 **/
public interface TblYqnsProposeAdoptMapper extends BaseMapper<TblYqnsProposeAdopt> {

	@Select("SELECT * FROM TBL_YQNS_PROPOSE_ADOPT WHERE ID = #{id} AND WTZGID = #{wtzgid}")
	TblYqnsProposeAdopt selectByWtzgPropoose(@Param("wtzgid") BigDecimal wtzgid,@Param("id") BigDecimal id);

	@Select("SELECT * FROM TBL_YQNS_PROPOSE_ADOPT WHERE WTZGID = #{wtzgid}")
	List<TblYqnsProposeAdopt> selectListByCopy(@Param("wtzgid")BigDecimal wtzgid);

}
