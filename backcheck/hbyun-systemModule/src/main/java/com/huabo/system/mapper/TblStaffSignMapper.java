package com.huabo.system.mapper;

import org.apache.ibatis.annotations.SelectProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.system.entity.TblStaffSign;

/**
 * <p>
 * 电子签名存储表 Mapper 接口
 * </p>
 *
 * @author lhp
 * @since 2024-12-02
 */
public interface TblStaffSignMapper extends BaseMapper<TblStaffSign> {

	@SelectProvider(type = TblStaffSignMapperSqlConfig.class,method = "selectListPage")
	IPage<TblStaffSign> selectListPage(TblStaffSign sign, IPage<TblStaffSign> page) throws Exception;

}
