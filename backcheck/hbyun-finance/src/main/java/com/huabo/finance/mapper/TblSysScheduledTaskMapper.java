package com.huabo.finance.mapper;

import com.huabo.finance.entity.TblSysScheduledTask;
import com.huabo.finance.entity.caiji.BdAccount;
import com.huabo.finance.mappersql.BdAccountMapperSqlConfig;
import com.huabo.finance.mappersql.TblSysScheduledTaskMapperSqlConfig;
import com.huabo.finance.vo.BdAccountVo;
import com.huabo.finance.vo.ExportRequestVo;
import com.huabo.finance.vo.TblSysScheduledTaskVo;
import com.huabo.finance.vr.BdAccountVr;
import com.huabo.finance.vr.TblSysScheduledTaskVr;

import io.lettuce.core.dynamic.annotation.Param;
import lombok.experimental.PackagePrivate;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.FaAccbookinfoUtil;
import com.hbfk.entity.TblAttachment;

/**
 * <p>
 * 定时任务 Mapper 接口
 * </p>
 *
 * @author L
 * @since 2025-03-24
 */
public interface TblSysScheduledTaskMapper extends BaseMapper<TblSysScheduledTask> {

	@SelectProvider(type = TblSysScheduledTaskMapperSqlConfig.class , method = "selectFinanceDataPage")
	IPage<TblSysScheduledTaskVr> selectFinanceDataPage(Page<TblSysScheduledTaskVr> page, TblSysScheduledTaskVo vo) throws Exception;

	@Select("SELECT * FROM TBL_SYS_SCHEDULEDTASK WHERE STATUS = #{status}")
	List<TblSysScheduledTask> findByStatus(@Param("status")Integer status);

}
