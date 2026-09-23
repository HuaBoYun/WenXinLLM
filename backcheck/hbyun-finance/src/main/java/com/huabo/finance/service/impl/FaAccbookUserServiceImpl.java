package com.huabo.finance.service.impl;

import com.huabo.finance.entity.caiji.FaAccbookUser;
import com.huabo.finance.entity.caiji.FaAccbookinfo;
import com.huabo.finance.mapper.FaAccbookUserMapper;
import com.huabo.finance.mapper.FaAccbookinfoMapper;
import com.huabo.finance.service.FaAccbookUserService;
import com.huabo.finance.vo.FaAccbookinfoVo;
import com.huabo.finance.vr.FaAccbookinfoVr;

import redis.clients.jedis.Jedis;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.entity.FaAccbookinfoUtil;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JedisUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户选择默认账簿 服务实现类
 * </p>
 *
 * @author L
 * @since 2025-04-01
 */
@Service
public class FaAccbookUserServiceImpl extends ServiceImpl<FaAccbookUserMapper, FaAccbookUser> implements FaAccbookUserService {

	@Resource
	private FaAccbookinfoMapper faAccbookinfoMapper;
	
	@Resource
	private FaAccbookUserMapper faAccbookUserMapper;
	
	@Resource
	private UserProvider userProvider;
	
	
	@Override
	public JsonBean getBookList(HttpServletRequest request, TblStaffUtil staff, FaAccbookinfoVo vo) throws Exception {
		Page<FaAccbookinfoVr> page = new Page<FaAccbookinfoVr>(vo.getPageNumber(), vo.getPageSize());
		page.setOptimizeCountSql(false); // 禁用自动优化
		IPage<FaAccbookinfoVr> pageList = this.faAccbookinfoMapper.selectPageInfoByUserRole(page, vo, staff.getRoleIdStrs(),staff.getStaffid());
		return ResponseFormat.retParam(1, 200, pageList);
	}

	@Override
	public JsonBean selected(TblStaffUtil staff, String pkAccbookinfo) throws Exception {
		
			//先清除掉之前已选择的账簿数据，在新增保存现在的
			QueryWrapper<FaAccbookUser> wrapper = new QueryWrapper<FaAccbookUser>();
			//wrapper.eq("ACCBOOKID", pkAccbookinfo);
			wrapper.eq("STAFFID", staff.getStaffid());
			this.faAccbookUserMapper.delete(wrapper);
			
			FaAccbookUser abu = new FaAccbookUser();
			abu.setFid(RandomUtil.uuStringId());
			abu.setAccbookid(pkAccbookinfo);
			abu.setStaffid(staff.getStaffid().toString());
			this.faAccbookUserMapper.insert(abu);
			
			FaAccbookinfo book = this.faAccbookinfoMapper.selectById(abu.getAccbookid());
			
			FaAccbookinfoUtil bookUtil = new FaAccbookinfoUtil();
			bookUtil.setPkAccbookinfo(book.getPkAccbookinfo());
			bookUtil.setPkSetofbook(book.getPkSetofbook());
			bookUtil.setConvertDate(book.getConvertDate());
			bookUtil.setLocaloriginvalue(book.getLocaloriginvalue());
			bookUtil.setBodyvos(book.getBodyvos());
			bookUtil.setBookName(book.getBookName());
			bookUtil.setPkFinanplanid(book.getPkFinanplanid());
			bookUtil.setAccbooktypecode(book.getAccbooktypecode());
			bookUtil.setAccbooktypename(book.getAccbooktypename());
			staff.setAccbook(bookUtil);
			userProvider.add(staff);
		return ResponseFormat.retParam(1, 200, null);
	}

}
