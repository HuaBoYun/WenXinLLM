package com.huabo.audit.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.audit.oracle.entity.TblUruleTask;
import io.lettuce.core.dynamic.annotation.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * <p>
 * 定时任务 Mapper 接口
 * </p>
 *
 * @author L
 * @since 2025-03-24
 */
public interface TblUruleTaskMapper extends BaseMapper<TblUruleTask> {

//	@SelectProvider(type = TblSysScheduledTaskMapperSqlConfig.class , method = "selectFinanceDataPage")
//	IPage<TblSysScheduledTaskVr> selectFinanceDataPage(Page<TblSysScheduledTaskVr> page, TblSysScheduledTaskVo vo) throws Exception;

	@Select("SELECT * FROM TBL_URULE_TASK WHERE STATUS = #{status}")
	List<TblUruleTask> findByStatus(@Param("status")Integer status);

	@Select("SELECT * FROM TBL_URULE_TASK WHERE TASKNAME LIKE CONCAT('%', #{taskName}, '%')")
	List<TblUruleTask> findByNameList(@Param("status")String taskName);

}
