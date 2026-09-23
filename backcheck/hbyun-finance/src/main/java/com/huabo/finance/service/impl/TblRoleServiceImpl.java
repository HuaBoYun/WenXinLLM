package com.huabo.finance.service.impl;

import com.huabo.finance.entity.TblRole;
import com.huabo.finance.mapper.TblRoleMapper;
import com.huabo.finance.service.TblRoleService;
import com.huabo.finance.vr.FaAccbookinfoVr;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色表管理
rid:主键ID,自动增长；
rname:角色名称；
rdesc:角色描述；
rstatus:角色状态，是否启用 服务实现类
 * </p>
 *
 * @author L
 * @since 2025-04-11
 */
@Service
public class TblRoleServiceImpl extends ServiceImpl<TblRoleMapper, TblRole> implements TblRoleService {

	@Resource
	private TblRoleMapper tblRoleMapper;
	
	@Override
	public JsonBean getRoleListByBook(TblStaffUtil staff, String pkAccbookinfo, String roleName, Integer pageNumber, Integer pageSize) throws Exception {
		QueryWrapper<TblRole> wrapper = new QueryWrapper<TblRole>();
		if(StringUtils.isBlank(pkAccbookinfo)) {
			return ResponseFormat.retParam(0, "参数缺失", null);
		}
		if(StringUtils.isNotBlank(roleName)) {
			wrapper.like("RNAME", roleName);
		}
		wrapper.inSql("RID", "SELECT ROLEID FROM FA_ACCBOOK_ROLE WHERE ACCBOOKID = '"+pkAccbookinfo+"'");
		
		wrapper.orderByAsc("RID");
		Page<TblRole> page = new Page<TblRole>(pageNumber, pageSize);
		page.setOptimizeCountSql(false); // 禁用自动优化
		IPage<TblRole> pageList = this.tblRoleMapper.selectPage(page, wrapper);
		return ResponseFormat.retParam(1, 200, pageList);
	}
}
