package com.huabo.finance.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.finance.entity.BdFinancePlanDataConfig;
import com.huabo.finance.vr.BdFinancePlanDataConfigVr;

import io.lettuce.core.dynamic.annotation.Param;

/**
 * <p>
 * 公司采集配置方案信息表 Mapper 接口
 * </p>
 *
 * @author L
 * @since 2025-03-12
 */
public interface BdFinancePlanDataConfigMapper extends BaseMapper<BdFinancePlanDataConfig> {

	@Select("SELECT BFDS.*,BFD.FINTEXT AS DATACONFIGNAME FROM BD_FINANCEPLAN_DATACONFIG BFDS LEFT JOIN BD_FINANCEDATE BFD ON BFDS.DATACONFIG = BFD.FID WHERE BFDS.PLANID = #{planid}")
	List<BdFinancePlanDataConfigVr> selectListByPlanId(@Param("planid")String planid) throws Exception;

	@Delete("DELETE FROM BD_FINANCEPLAN_DATACONFIG WHERE PLANID = #{planid}")
	void deleteByPlanId(@Param("planid")String planid) throws Exception;

}
