package com.huabo.finance.service.impl;

import com.huabo.finance.entity.caiji.FaAccbookinfo;
import com.huabo.finance.entity.caiji.OrgSetofbook;
import com.huabo.finance.mapper.BaseCommonMapper;
import com.huabo.finance.mapper.FaAccbookinfoMapper;
import com.huabo.finance.service.FaAccbookinfoService;
import com.huabo.finance.vo.FaAccbookinfoVo;
import com.huabo.finance.vr.FaAccbookinfoVr;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 账簿信息 服务实现类
 * </p>
 *
 * @author L
 * @since 2025-04-01
 */
@Service
public class FaAccbookinfoServiceImpl extends ServiceImpl<FaAccbookinfoMapper, FaAccbookinfo> implements FaAccbookinfoService {

	@Resource
	private FaAccbookinfoMapper faAccbookinfoMapper;
	
	@Resource
	private BaseCommonMapper baseCommonMapper;
	
	@Override
	public JsonBean getList(HttpServletRequest request, TblStaffUtil staff, FaAccbookinfoVo vo) throws Exception {
		Page<FaAccbookinfoVr> page = new Page<FaAccbookinfoVr>(vo.getPageNumber(), vo.getPageSize());
		page.setOptimizeCountSql(false); // 禁用自动优化
		IPage<FaAccbookinfoVr> pageList = this.faAccbookinfoMapper.selectPageInfo(page, vo);
		return ResponseFormat.retParam(1, 200, pageList);
	}

	@Override
	public JsonBean save(TblStaffUtil staff, FaAccbookinfo fab) throws Exception {
		if(StringUtils.isNotBlank(fab.getPkAccbookinfo())) {
			fab.setModifiedtime(new Date());
			fab.setModifier(staff.getStaffid().toString());
			this.faAccbookinfoMapper.updateById(fab);
		}else {
			fab.setPkAccbookinfo(RandomUtil.uuStringId());
			fab.setCreator(staff.getStaffid().toString());
			fab.setCreationtime(new Date());
			fab.setConvertDate(fab.getConvertDate()!=null?fab.getConvertDate():0);
			fab.setPkOrg(staff.getCurrentOrg().getOrgid().toString());
			fab.setPkGroup(staff.getGroupOrg().getOrgid().toString());
			this.faAccbookinfoMapper.insert(fab);
		}
		return ResponseFormat.retParam(1, 200, fab);
	}

	@Override
	public JsonBean del(TblStaffUtil staff, String pkAccbookinfo) throws Exception {
		this.faAccbookinfoMapper.deleteById(pkAccbookinfo);
		return ResponseFormat.retParam(1, 200, null);
	}

	@Override
	public JsonBean detail(TblStaffUtil staff, String pkAccbookinfo) throws Exception {
		FaAccbookinfoVr vr = this.faAccbookinfoMapper.selectEntityById(pkAccbookinfo);
		
		if(StringUtils.isNotBlank(vr.getCreator())) {
			vr.setCreatorName(this.baseCommonMapper.selectUserNameById(vr.getCreator()));
		}
		
		if(StringUtils.isNotBlank(vr.getModifier())) {
			vr.setModifierName(this.baseCommonMapper.selectUserNameById(vr.getModifier()));
		}
		
		if(StringUtils.isNotBlank(vr.getPkOrg())) {
			vr.setOrgName(this.baseCommonMapper.selectOrgNameById(vr.getPkOrg()));
		}
		
		if(StringUtils.isNotBlank(vr.getPkGroup())) {
			vr.setGroupName(this.baseCommonMapper.selectOrgNameById(vr.getPkGroup()));
		}
		
		return ResponseFormat.retParam(1, 200, vr);
	}

}
