package com.huabo.finance.service.impl;

import com.huabo.finance.entity.caiji.GlBalance;
import com.huabo.finance.entity.caiji.GlVoucher;
import com.huabo.finance.mapper.GlBalanceMapper;
import com.huabo.finance.mapper.OrgOrgsMapper;
import com.huabo.finance.service.IGlBalanceService;
import com.huabo.finance.vo.GlBalanceVo;
import com.huabo.finance.vr.GlBalanceVr;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.entity.FaAccbookinfoUtil;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriUtils;

/**
 * <p>
 * 凭证余额 服务实现类
 * </p>
 *
 * @author L
 * @since 2025-03-28
 */
@Service
public class GlBalanceServiceImpl extends ServiceImpl<GlBalanceMapper, GlBalance> implements IGlBalanceService {

	@Resource
	private GlBalanceMapper glBalanceMapper;
	
	@Resource
	private OrgOrgsMapper orgOrgsMapper;
	
	@Override
	public JsonBean getFinanceDataList(TblStaffUtil staff, GlBalanceVo vo) throws Exception {
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
		
		Page<GlBalanceVr> page = new Page<GlBalanceVr>(vo.getPageNumber(), vo.getPageSize());
		page.setOptimizeCountSql(false); // 禁用自动优化
		IPage<GlBalanceVr> pageList = this.glBalanceMapper.selectFinanceDataSumTotalList(page, vo, bookInfo);
		
		GlBalance preMonthB = null;//上个月的余额 用来获取本年累计发生额  计算当前月份的年初方向
		QueryWrapper<GlBalance> ycw = new QueryWrapper<GlBalance>();
		GlBalance yearb = null;//每个科目每年的期初余额
		BigDecimal yearmoney = null;//年初借方金额
		
		BigDecimal monthMOney = null;//本月月初金额
		BigDecimal endMonthMoney = null;//本月月末金额
		List<GlBalanceVr> list = new ArrayList<GlBalanceVr>(0);
		String minPeriod = null;//最小会计期间
		for (GlBalanceVr vr : pageList.getRecords()) {
			minPeriod = this.glBalanceMapper.selectMinPeriod(vr);
			ycw.clear();
			ycw.eq("DATAORIGINFLAG", -2 );
			ycw.eq("PK_ORG", vr.getPkOrg());
			ycw.eq("PK_ACCASOA", vr.getPkAccasoa());
			ycw.eq("YEAR", vr.getYear());
			ycw.eq("PERIOD", minPeriod);
			yearb = this.glBalanceMapper.selectOne(ycw);
			if(yearb == null) {
				yearmoney = new BigDecimal(0);
			}else {
				yearmoney = yearb.getFbeginBalanceLocal();
			}
			
			ycw.clear();
			ycw.eq("DATAORIGINFLAG", -2 );
			ycw.eq("PK_ORG", vr.getPkOrg());
			ycw.eq("PK_ACCASOA", vr.getPkAccasoa());
			ycw.eq("YEAR", vr.getYear());
			ycw.eq("PERIOD", Integer.parseInt(vr.getPeriod())-1);
			preMonthB = this.glBalanceMapper.selectOne(ycw);
			
			if(preMonthB == null) {
				preMonthB = new GlBalance();
				preMonthB.setFyearCreditLocal(new BigDecimal(0));
				preMonthB.setFyearDeditLocal(BigDecimal.valueOf(0));
			}
			
			
			//根据科目方向  判定年初数方向，并计算当前月份期初方向
			if(vr.getBalanorient() == 0 ){
				//借方 ; 年初数加借方-贷方  根据结构判断 正数  月初方向为借方，负数月初方向为贷方
				monthMOney = yearmoney.add(preMonthB.getFyearDeditLocal()).subtract(preMonthB.getFyearCreditLocal());
				if(monthMOney.compareTo(BigDecimal.ZERO) < 0) {
					vr.setBeginBalanorient(1);
				}else {
					vr.setBeginBalanorient(0);
				}
			}
			
			if(vr.getBalanorient() == 1) {
				//贷方 ; 年初数加贷方 - 借方方  根据结构判断 正数  月初方向为贷方，负数月初方向为借方
				monthMOney = yearmoney.add(preMonthB.getFyearCreditLocal()).subtract(preMonthB.getFyearDeditLocal());
				if(monthMOney.compareTo(BigDecimal.ZERO) < 0) {
					vr.setBeginBalanorient(0);
				}else {
					vr.setBeginBalanorient(1);
				}
			}
			
			//计算本地的余额 根据月初方向 判断逻辑为借方+借-待 或 待+待-借  得到余额 根据余额正负判断余额方向
			if(vr.getBeginBalanorient().compareTo(0) == 0) {
				//借方
				endMonthMoney = monthMOney.add(vr.getLocaldebitamount()).subtract(vr.getLocalcreditamount());
				if(endMonthMoney.compareTo(BigDecimal.ZERO) < 0) {
					vr.setEndBalanorient(1);
				}else {
					vr.setEndBalanorient(0);
				}
			}
			if(vr.getBeginBalanorient().compareTo(1) == 0) {
				//贷方
				endMonthMoney = monthMOney.add(vr.getLocalcreditamount()).subtract(vr.getLocaldebitamount());
				if(endMonthMoney.compareTo(BigDecimal.ZERO) < 0) {
					vr.setEndBalanorient(0);
				}else {
					vr.setEndBalanorient(1);
				}
			}
			vr.setFbeginBalanceLocal(monthMOney);
			vr.setFendBalanceLocal(endMonthMoney);
			list.add(vr);
		}
		pageList.setRecords(list);
		return ResponseFormat.retParam(1, 200, pageList);
	}

	@Override
	public JsonBean getFinanceDataSumTotalList(TblStaffUtil staff, GlBalanceVo vo) throws Exception {
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
		
		Page<GlBalanceVr> page = new Page<GlBalanceVr>(vo.getPageNumber(), vo.getPageSize());
		page.setOptimizeCountSql(false); // 禁用自动优化
		IPage<GlBalanceVr> pageList = this.glBalanceMapper.selectFinanceDataSumTotalList(page, vo, bookInfo);
		
		GlBalance preMonthB = null;//上个月的余额 用来获取本年累计发生额  计算当前月份的年初方向
		QueryWrapper<GlBalance> ycw = new QueryWrapper<GlBalance>();
		GlBalance yearb = null;//每个科目每年的期初余额
		BigDecimal yearmoney = null;//年初借方金额
		
		BigDecimal monthMOney = null;//本月月初金额
		BigDecimal endMonthMoney = null;//本月月末金额
		List<GlBalanceVr> list = new ArrayList<GlBalanceVr>(0);
		
		
		String minPeriod = null;//最小会计期间
		for (GlBalanceVr vr : page.getRecords()) {
			minPeriod = this.glBalanceMapper.selectMinPeriod(vr);
			ycw.clear();
			ycw.eq("DATAORIGINFLAG", -2 );
			ycw.eq("PK_ORG", vr.getPkOrg());
			ycw.eq("PK_ACCASOA", vr.getPkAccasoa());
			ycw.eq("YEAR", vr.getYear());
			ycw.eq("PERIOD", minPeriod);
			yearb = this.glBalanceMapper.selectOne(ycw);
			if(yearb == null) {
				yearmoney = new BigDecimal(0);
			}else {
				yearmoney = yearb.getFbeginBalanceLocal();
			}
			
			ycw.clear();
			ycw.eq("DATAORIGINFLAG", -2 );
			ycw.eq("PK_ORG", vr.getPkOrg());
			ycw.eq("PK_ACCASOA", vr.getPkAccasoa());
			ycw.eq("YEAR", vr.getYear());
			ycw.eq("PERIOD", Integer.parseInt(vr.getPeriod())-1);
			preMonthB = this.glBalanceMapper.selectOne(ycw);
			
			if(preMonthB == null) {
				preMonthB = new GlBalance();
				preMonthB.setFyearCreditLocal(new BigDecimal(0));
				preMonthB.setFyearDeditLocal(BigDecimal.valueOf(0));
			}
			
			
			//根据科目方向  判定年初数方向，并计算当前月份期初方向
			if(vr.getBalanorient() == 0 ){
				//借方 ; 年初数加借方-贷方  根据结构判断 正数  月初方向为借方，负数月初方向为贷方
				monthMOney = yearmoney.add(preMonthB.getFyearDeditLocal()).subtract(preMonthB.getFyearCreditLocal());
				if(monthMOney.compareTo(BigDecimal.ZERO) < 0) {
					vr.setBeginBalanorient(1);
				}else {
					vr.setBeginBalanorient(0);
				}
			}
			
			if(vr.getBalanorient() == 1) {
				//贷方 ; 年初数加贷方 - 借方方  根据结构判断 正数  月初方向为贷方，负数月初方向为借方
				monthMOney = yearmoney.add(preMonthB.getFyearCreditLocal()).subtract(preMonthB.getFyearDeditLocal());
				if(monthMOney.compareTo(BigDecimal.ZERO) < 0) {
					vr.setBeginBalanorient(0);
				}else {
					vr.setBeginBalanorient(1);
				}
			}
			
			//计算本地的余额 根据月初方向 判断逻辑为借方+借-待 或 待+待-借  得到余额 根据余额正负判断余额方向
			if(vr.getBeginBalanorient().compareTo(0) == 0) {
				//借方
				endMonthMoney = monthMOney.add(vr.getLocaldebitamount()).subtract(vr.getLocalcreditamount());
				if(endMonthMoney.compareTo(BigDecimal.ZERO) < 0) {
					vr.setEndBalanorient(1);
				}else {
					vr.setEndBalanorient(0);
				}
			}
			if(vr.getBeginBalanorient().compareTo(1) == 0) {
				//贷方
				endMonthMoney = monthMOney.add(vr.getLocalcreditamount()).subtract(vr.getLocaldebitamount());
				if(endMonthMoney.compareTo(BigDecimal.ZERO) < 0) {
					vr.setEndBalanorient(0);
				}else {
					vr.setEndBalanorient(1);
				}
			}
			vr.setFbeginBalanceLocal(monthMOney);
			vr.setFendBalanceLocal(endMonthMoney);
			list.add(vr);
		}
		pageList.setRecords(list);
		return ResponseFormat.retParam(1, 200, pageList);
	}

}
