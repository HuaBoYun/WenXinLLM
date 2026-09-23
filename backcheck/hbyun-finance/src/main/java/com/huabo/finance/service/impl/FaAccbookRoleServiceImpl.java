package com.huabo.finance.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.finance.entity.caiji.FaAccbookRole;
import com.huabo.finance.entity.caiji.FaAccbookUser;
import com.huabo.finance.mapper.FaAccbookRoleMapper;
import com.huabo.finance.mapper.FaAccbookUserMapper;
import com.huabo.finance.service.FaAccbookRoleService;

/**
 * <p>
 * 账簿角色授权 服务实现类
 * </p>
 *
 * @author L
 * @since 2025-04-01
 */
@Service
public class FaAccbookRoleServiceImpl extends ServiceImpl<FaAccbookRoleMapper, FaAccbookRole> implements FaAccbookRoleService {

	@Resource
	private FaAccbookRoleMapper faAccbookRoleMapper;
	
	@Override
	public JsonBean grant(TblStaffUtil staff, String pkAccbookinfo, String[] roleIds) throws Exception {
		//查找这个账簿之前的授权信息 如果已经授权的角色 则跳过 不进行授权
		
		QueryWrapper<FaAccbookRole> wrapper = new QueryWrapper<FaAccbookRole>();
		wrapper.eq("ACCBOOKID", pkAccbookinfo);
		
		wrapper.select("ROLEID");
		
		List<String> roleIdList = this.faAccbookRoleMapper.selectList(wrapper).stream().map(FaAccbookRole::getRoleid).collect(Collectors.toList());
		
		FaAccbookRole ar = null;
		if(roleIdList != null && roleIdList.size() > 0) {
			for (String roleId : roleIds) {
				if(roleIdList.contains(roleId)) {
					continue;
				}
				ar = new FaAccbookRole();
				ar.setFid(RandomUtil.uuStringId());
				ar.setAccbookid(pkAccbookinfo);
				ar.setRoleid(roleId);
				this.faAccbookRoleMapper.insert(ar);
			}
		}else {
			for (String roleId : roleIds) {
				ar = new FaAccbookRole();
				ar.setFid(RandomUtil.uuStringId());
				ar.setAccbookid(pkAccbookinfo);
				ar.setRoleid(roleId);
				this.faAccbookRoleMapper.insert(ar);
			}
		}
		
		return ResponseFormat.retParam(1, 200, null);
	}

	@Override
	public JsonBean cancel(TblStaffUtil staff, String pkAccbookinfo, String[] roleIds) throws Exception {
		//之间删除；
		
		QueryWrapper<FaAccbookRole> wrapper = new QueryWrapper<FaAccbookRole>();
		for (String roleId : roleIds) {
			wrapper.clear();
			wrapper.eq("ACCBOOKID", pkAccbookinfo);
			wrapper.eq("ROLEID", roleId);
			this.faAccbookRoleMapper.delete(wrapper);
		}
		return ResponseFormat.retParam(1, 200, null);
	}

}
