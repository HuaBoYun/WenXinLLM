package com.huabo.finance.service.impl;

import com.huabo.finance.entity.caiji.GlAssBalane;
import com.huabo.finance.entity.caiji.GlVoucher;
import com.huabo.finance.mapper.GlAssBalaneMapper;
import com.huabo.finance.mapper.OrgOrgsMapper;
import com.huabo.finance.service.GlAssBalaneService;
import com.huabo.finance.vo.BdFinanceAccassVo;
import com.huabo.finance.vo.GlAssBalaneVo;
import com.huabo.finance.vr.GlAssBalaneVr;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.entity.FaAccbookinfoUtil;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 辅助账余额 服务实现类
 * </p>
 *
 * @author L
 * @since 2025-04-20
 */
@Service
public class GlAssBalaneServiceImpl extends ServiceImpl<GlAssBalaneMapper, GlAssBalane> implements GlAssBalaneService {
	
	@Resource
	private GlAssBalaneMapper glAssBalaneMapper;
	
	@Resource
	private OrgOrgsMapper orgOrgsMapper;

	@Override
	public JsonBean getFinanceAccAssBalanceList(TblStaffUtil staff, GlAssBalaneVo vo)
			throws Exception {
		
		//获取用户所选择的账簿
		FaAccbookinfoUtil bookInfo = staff.getAccbook();
						
		if(bookInfo == null) {
			return ResponseFormat.retParam(0, 204, null);
		}
		
		if(StringUtils.isBlank(vo.getPkAccassitem())) {
			return ResponseFormat.retParam(0, "请选择辅助类型！", null);
		}
		
		if(StringUtils.isBlank(vo.getPkOrg())) {
			String org = this.orgOrgsMapper.selectFinanceOrgIdByOrgId(staff.getCurrentOrg().getOrgid());
			if(StringUtils.isBlank(org)) {
				return ResponseFormat.retParam(0, "请选择财务组织！", null);
			}
			vo.setPkOrg(org);
		}
		Page<GlAssBalaneVr> page = new Page<GlAssBalaneVr>(vo.getPageNumber(), vo.getPageSize());
		page.setOptimizeCountSql(false); // 禁用自动优化
		IPage<GlAssBalaneVr> pageList = this.glAssBalaneMapper.selectFinanceAccAssBalanceList(page, vo, bookInfo);
		
		GlAssBalane yearBalance = null;//年初数
		
		GlAssBalane preBalance = null;//上月余额信息
		
		QueryWrapper<GlAssBalane> wrapper = new QueryWrapper<GlAssBalane>();
		
		BigDecimal balance = null;
		
		BigDecimal beginBalance = new BigDecimal("0");
		
		String minPeriod = null;//最小会计期间
		
		
		for (GlAssBalaneVr vr : pageList.getRecords()) {
			
			
			minPeriod = this.glAssBalaneMapper.selectMinPeriod(vr);
			
			//查询年初数
			wrapper.clear();
			wrapper.eq("PK_ORG", vr.getPkOrg());
			wrapper.eq("PK_ACCASOA", vr.getPkAccasoa());
			wrapper.eq("PK_ACCASS", vr.getPkAccass());
			wrapper.eq("YEAR", vr.getYear());
			wrapper.eq("PERIOD", minPeriod);
			wrapper.eq("DATAORIGINFLAG", -2);
			wrapper.eq("FPLANID", vr.getFplanid());
			yearBalance = this.glAssBalaneMapper.selectOne(wrapper);
			
			if(yearBalance == null) {
				yearBalance = new GlAssBalane();
				yearBalance.setBeginbalancemount(BigDecimal.valueOf(0));
			}
			
			
			//查询当前记录上一个月的余额信息，可能为null 
			wrapper.clear();
			wrapper.eq("PK_ORG", vr.getPkOrg());
			wrapper.eq("PK_ACCASOA", vr.getPkAccass());
			wrapper.eq("PK_ACCASS", vr.getPkAccass());
			wrapper.eq("YEAR", vr.getYear());
			wrapper.eq("PERIOD", StringUtils.isNotBlank(vr.getPeriod())?Integer.parseInt(vr.getPeriod())-1:0);
			wrapper.eq("DATAORIGINFLAG", -2);
			wrapper.eq("FPLANID", vr.getFplanid());
			preBalance = this.glAssBalaneMapper.selectOne(wrapper);
			
			if(preBalance == null) {
				preBalance = new GlAssBalane();
				preBalance.setYearcreditmount(BigDecimal.valueOf(0));
				preBalance.setYeardebitamount(BigDecimal.valueOf(0));
			}
			
			
			//计算期末方向 根据科目借贷方向  以及辅助余额本年累计 计算
			if(vr.getBalanorient() == 0) {
				//借方
				balance = yearBalance.getBeginbalancemount().add(vr.getYeardebitamount()).subtract(vr.getYearcreditmount());
				if(balance.compareTo(BigDecimal.ZERO) < 0) {
					vr.setEndBalanorient(1);
				}else {
					vr.setEndBalanorient(0);
				}
				vr.setEndbalancemount(balance);
				//计算期初方向
				beginBalance = yearBalance.getBeginbalancemount().add(preBalance.getYeardebitamount()).subtract(preBalance.getYearcreditmount());
				if(balance.compareTo(BigDecimal.ZERO) < 0) {
					vr.setBeginBalanorient(1);
				}else {
					vr.setBeginBalanorient(0);
				}
				vr.setBeginbalancemount(beginBalance);
			}
			
			if(vr.getBalanorient() == 1) {
				//贷方
				balance = yearBalance.getBeginbalancemount().add(vr.getYearcreditmount()).subtract(vr.getYeardebitamount());
				if(balance.compareTo(BigDecimal.ZERO) < 0) {
					vr.setEndBalanorient(0);
				}else {
					vr.setEndBalanorient(1);
				}
				vr.setEndbalancemount(balance);
				//计算期初方向
				beginBalance = yearBalance.getBeginbalancemount().add(preBalance.getYearcreditmount()).subtract(preBalance.getYeardebitamount());
				if(balance.compareTo(BigDecimal.ZERO) < 0) {
					vr.setBeginBalanorient(0);
				}else {
					vr.setBeginBalanorient(1);
				}
				vr.setBeginbalancemount(beginBalance);
			}
		}
		
		return ResponseFormat.retParam(1, 200, pageList);
	}

	@Override
	public JsonBean getFinanceAccAssGeneralLedgerList(TblStaffUtil staff, GlAssBalaneVo vo) throws Exception {
		//获取用户所选择的账簿
		FaAccbookinfoUtil bookInfo = staff.getAccbook();
						
		if(bookInfo == null) {
			return ResponseFormat.retParam(0, 204, null);
		}
		
		if(StringUtils.isBlank(vo.getPkAccassitem())) {
			return ResponseFormat.retParam(0, "请选择辅助类型！", null);
		}
		
		if(StringUtils.isBlank(vo.getPkOrg())) {
			String org = this.orgOrgsMapper.selectFinanceOrgIdByOrgId(staff.getCurrentOrg().getOrgid());
			if(StringUtils.isBlank(org)) {
				return ResponseFormat.retParam(0, "请选择财务组织！", null);
			}
			vo.setPkOrg(org);
		}		
		
		Page<GlAssBalaneVr> page = new Page<GlAssBalaneVr>(vo.getPageNumber(), vo.getPageSize());
		page.setOptimizeCountSql(false); // 禁用自动优化
		IPage<GlAssBalaneVr> pageList = this.glAssBalaneMapper.selectFinanceAccAssGeneralLedgerList(page, vo, bookInfo);
		
		GlAssBalane yearBalance = null;//年初数
		
		GlAssBalane preBalance = null;//上月余额信息
		
		QueryWrapper<GlAssBalane> wrapper = new QueryWrapper<GlAssBalane>();
		
		BigDecimal balance = null;
		
		BigDecimal beginBalance = null;
		
		String minPeriod = null;//最小会计期间
		
		
		for (GlAssBalaneVr vr : pageList.getRecords()) {
			
			
			minPeriod = this.glAssBalaneMapper.selectMinPeriod(vr);
			
			//查询年初数
			wrapper.clear();
			wrapper.eq("PK_ORG", vr.getPkOrg());
			wrapper.eq("PK_ACCASOA", vr.getPkAccasoa());
			wrapper.eq("PK_ACCASS", vr.getPkAccass());
			wrapper.eq("YEAR", vr.getYear());
			wrapper.eq("PERIOD", minPeriod);
			wrapper.eq("DATAORIGINFLAG", -2);
			wrapper.eq("FPLANID", vr.getFplanid());
			yearBalance = this.glAssBalaneMapper.selectOne(wrapper);
			
			if(yearBalance == null) {
				yearBalance = new GlAssBalane();
				yearBalance.setBeginbalancemount(BigDecimal.valueOf(0));
			}
			
			
			//查询当前记录上一个月的余额信息，可能为null 
			wrapper.clear();
			wrapper.eq("PK_ORG", vr.getPkOrg());
			wrapper.eq("PK_ACCASOA", vr.getPkAccass());
			wrapper.eq("PK_ACCASS", vr.getPkAccass());
			wrapper.eq("YEAR", vr.getYear());
			wrapper.eq("PERIOD", StringUtils.isNotBlank(vr.getPeriod())?Integer.parseInt(vr.getPeriod())-1:0);
			wrapper.eq("DATAORIGINFLAG", -2);
			wrapper.eq("FPLANID", vr.getFplanid());
			preBalance = this.glAssBalaneMapper.selectOne(wrapper);
			
			if(preBalance == null) {
				preBalance = new GlAssBalane();
				preBalance.setYearcreditmount(BigDecimal.valueOf(0));
				preBalance.setYeardebitamount(BigDecimal.valueOf(0));
			}
			
			
			//计算期末方向 根据科目借贷方向  以及辅助余额本年累计 计算
			if(vr.getBalanorient() == 0) {
				//借方
				balance = yearBalance.getBeginbalancemount().add(vr.getYeardebitamount()).subtract(vr.getYearcreditmount());
				if(balance.compareTo(BigDecimal.ZERO) < 0) {
					vr.setEndBalanorient(1);
				}else {
					vr.setEndBalanorient(0);
				}
				//计算期初方向
				beginBalance = yearBalance.getBeginbalancemount().add(preBalance.getYeardebitamount()).subtract(preBalance.getYearcreditmount());
				if(balance.compareTo(BigDecimal.ZERO) < 0) {
					vr.setBeginBalanorient(1);
				}else {
					vr.setBeginBalanorient(0);
				}
			}
			
			if(vr.getBalanorient() == 1) {
				//贷方
				balance = yearBalance.getBeginbalancemount().add(vr.getYearcreditmount()).subtract(vr.getYeardebitamount());
				if(balance.compareTo(BigDecimal.ZERO) < 0) {
					vr.setEndBalanorient(0);
				}else {
					vr.setEndBalanorient(1);
				}
				//计算期初方向
				beginBalance = yearBalance.getBeginbalancemount().add(preBalance.getYearcreditmount()).subtract(preBalance.getYeardebitamount());
				if(balance.compareTo(BigDecimal.ZERO) < 0) {
					vr.setBeginBalanorient(0);
				}else {
					vr.setBeginBalanorient(1);
				}
			}
		}
		return ResponseFormat.retParam(1, 200, pageList);
	}

}
