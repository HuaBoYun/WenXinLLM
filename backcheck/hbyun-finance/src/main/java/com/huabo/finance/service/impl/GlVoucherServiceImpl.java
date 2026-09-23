package com.huabo.finance.service.impl;

import com.huabo.finance.entity.caiji.GlVoucher;
import com.huabo.finance.mapper.GlVoucherMapper;
import com.huabo.finance.mapper.OrgOrgsMapper;
import com.huabo.finance.service.IGlVoucherService;
import com.huabo.finance.vo.GlVoucherVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.entity.FaAccbookinfoUtil;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 凭证库表 服务实现类
 * </p>
 *
 * @author L
 * @since 2025-03-19
 */
@Service
public class GlVoucherServiceImpl extends ServiceImpl<GlVoucherMapper, GlVoucher> implements IGlVoucherService {

	@Resource
	private GlVoucherMapper glVoucherMapper;
	
	@Resource
	private OrgOrgsMapper orgOrgsMapper;
	
	@Override
	public JsonBean getFinanceDataList(TblStaffUtil staff, GlVoucherVo vo) throws Exception {
		//获取用户所选择的账簿
		FaAccbookinfoUtil bookInfo = staff.getAccbook();
				
		if(bookInfo == null) {
			return ResponseFormat.retParam(0, 204, null);
		}
		
		if(StringUtils.isBlank(vo.getPkOrg())) {
			String org = this.orgOrgsMapper.selectFinanceOrgIdByOrgId(staff.getCurrentOrg().getOrgid());
			if(StringUtils.isBlank(org)) {
				return ResponseFormat.retParam(0, "请选择财务组织！", null);
			}
			vo.setPkOrg(org);
		}
		
		Page<GlVoucher> page = new Page<GlVoucher>(vo.getPageNumber(), vo.getPageSize());
		page.setOptimizeCountSql(false); // 禁用自动优化
		IPage<GlVoucher> pageList = this.glVoucherMapper.selectFinanceDataPage(page, vo, bookInfo);
		
		return ResponseFormat.retParam(1, 200, pageList);
	}

}
