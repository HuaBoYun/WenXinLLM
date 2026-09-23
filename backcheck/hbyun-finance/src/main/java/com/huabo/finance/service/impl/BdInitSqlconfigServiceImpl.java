package com.huabo.finance.service.impl;

import com.huabo.finance.entity.BdInitSqlconfig;
import com.huabo.finance.mapper.BdInitSqlconfigMapper;
import com.huabo.finance.service.BdInitSqlconfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;

import org.springframework.stereotype.Service;

/**
 * <p>
 * 财务采方案sql初始化表 服务实现类
 * </p>
 *
 * @author L
 * @since 2025-03-13
 */
@Service
public class BdInitSqlconfigServiceImpl extends ServiceImpl<BdInitSqlconfigMapper, BdInitSqlconfig> implements BdInitSqlconfigService {

	@Override
	public JsonBean findAllList(TblStaffUtil staff, String fid) throws Exception {
		BdInitSqlconfig config = this.baseMapper.selectById(fid);
		config.setFinitcol(config.getFinitcol().replace("\\n", "\n"));
		return ResponseFormat.retParam(1, 200, config);
	}

}
