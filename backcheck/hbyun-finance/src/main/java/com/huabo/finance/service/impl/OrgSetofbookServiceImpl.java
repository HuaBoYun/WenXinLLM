package com.huabo.finance.service.impl;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.finance.entity.caiji.OrgSetofbook;
import com.huabo.finance.mapper.BaseCommonMapper;
import com.huabo.finance.mapper.OrgSetofbookMapper;
import com.huabo.finance.service.OrgSetofbookService;
import com.huabo.finance.vo.OrgSetofbookVo;
import com.huabo.finance.vr.BdPlanSqlconfigVr;
import com.huabo.finance.vr.OrgSetofbookVr;

/**
 * <p>
 * 账簿类型 服务实现类
 * </p>
 *
 * @author L
 * @since 2025-03-31
 */
@Service
public class OrgSetofbookServiceImpl extends ServiceImpl<OrgSetofbookMapper, OrgSetofbook> implements OrgSetofbookService {

	@Resource
	private OrgSetofbookMapper orgSetofbookMapper;
	
	@Resource
	private BaseCommonMapper baseCommonMapper;
	
	@Override
	public JsonBean getList(HttpServletRequest request, TblStaffUtil staff, OrgSetofbookVo vo) throws Exception {
		QueryWrapper<OrgSetofbook> wrapper = new QueryWrapper<OrgSetofbook>();
		
		if(StringUtils.isNotBlank(vo.getCode())) {
			wrapper.eq("CODE", vo.getCode());
		}
		
		if(vo.getDataoriginflag() != null) {
			wrapper.eq("DATAORIGINFLAG", vo.getDataoriginflag());
		}
		
		if(StringUtils.isNotBlank(vo.getMnecode())) {
			wrapper.eq("MNECODE", vo.getMnecode());
		}
		
		if(StringUtils.isNotBlank(vo.getName())) {
			wrapper.eq("NAME", vo.getName());
		}
		
		if(StringUtils.isNotBlank(vo.getShortname())) {
			wrapper.eq("SHORTNAME", vo.getShortname());
		}
		Page<OrgSetofbook> page = new Page<OrgSetofbook>(vo.getPageNumber(), vo.getPageSize());
		page.setOptimizeCountSql(false); // 禁用自动优化
		IPage<OrgSetofbook> pageList = this.orgSetofbookMapper.selectPage(page, wrapper);
		return ResponseFormat.retParam(1, 200, pageList);
	}

	@Override
	public JsonBean save(TblStaffUtil staff, OrgSetofbook sob) throws Exception {
		if(StringUtils.isNotBlank(sob.getPkSetofbook())) {
			sob.setModifier(staff.getStaffid().toString());
			sob.setModifiedtime(new Date());
			this.orgSetofbookMapper.updateById(sob);
		}else {
			sob.setPkSetofbook(RandomUtil.uuStringId());
			sob.setCreator(staff.getStaffid().toString());
			sob.setCreationtime(new Date());
			sob.setPkGroup(staff.getGroupOrg().getOrgid().toString());
			sob.setPkOrg(staff.getCurrentOrg().getOrgid().toString());
			this.orgSetofbookMapper.insert(sob);
		}
		return ResponseFormat.retParam(1, 200, sob);
	}

	@Override
	public JsonBean del(TblStaffUtil staff, String pkSetofbook) throws Exception {
		this.orgSetofbookMapper.deleteById(pkSetofbook);
		return ResponseFormat.retParam(1, 200, null);
	}

	@Override
	public JsonBean detail(TblStaffUtil staff, String pkSetofbook) throws Exception {
		
		OrgSetofbookVr book = this.orgSetofbookMapper.selectEntityById(pkSetofbook);
		
		if(StringUtils.isNotBlank(book.getCreator())) {
			book.setCreatorName(this.baseCommonMapper.selectUserNameById(book.getCreator()));
		}
		
		if(StringUtils.isNotBlank(book.getModifier())) {
			book.setModifierName(this.baseCommonMapper.selectUserNameById(book.getModifier()));
		}
		
		if(StringUtils.isNotBlank(book.getPkOrg())) {
			book.setPkOrgName(this.baseCommonMapper.selectOrgNameById(book.getPkOrg()));
		}
		
		if(StringUtils.isNotBlank(book.getPkGroup())) {
			book.setPkGroupName(this.baseCommonMapper.selectOrgNameById(book.getPkGroup()));
		}
		return ResponseFormat.retParam(1, 200, book);
	}

}
