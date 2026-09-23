package com.huabo.finance.service.impl;

import com.huabo.finance.entity.caiji.GlBalance;
import com.huabo.finance.entity.caiji.GlDetail;
import com.huabo.finance.entity.caiji.GlVoucher;
import com.huabo.finance.mapper.GlBalanceMapper;
import com.huabo.finance.mapper.GlDetailMapper;
import com.huabo.finance.mapper.OrgOrgsMapper;
import com.huabo.finance.service.IGlDetailService;
import com.huabo.finance.vo.GlDetailVo;
import com.huabo.finance.vr.GlBalanceVr;
import com.huabo.finance.vr.GlDetailVr;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.entity.FaAccbookinfoUtil;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.DateUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 凭证明细 服务实现类
 * </p>
 *
 * @author L
 * @since 2025-03-26
 */
@Service
public class GlDetailServiceImpl extends ServiceImpl<GlDetailMapper, GlDetail> implements IGlDetailService {
	
	@Resource
	private GlDetailMapper glDetailMapper;
	
	@Resource
	private GlBalanceMapper glBalanceMapper;
	
	@Resource
	private OrgOrgsMapper orgOrgsMapper;

	@Override
	public JsonBean getFinanceDataList(TblStaffUtil staff, GlDetailVo vo) throws Exception {
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
		
		Page<GlDetailVr> page = new Page<GlDetailVr>(vo.getPageNumber(), vo.getPageSize());
		page.setOptimizeCountSql(false); // 禁用自动优化
		IPage<GlDetailVr> pageList = this.glDetailMapper.selectFinanceDataPage(page, vo, bookInfo);
		
		return ResponseFormat.retParam(1, 200, pageList);
	}

	@Override
	public JsonBean getFinanceDataDetailBookList(TblStaffUtil staff, GlDetailVo vo) throws Exception {
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
		
		Page<GlDetailVr> page = new Page<GlDetailVr>(vo.getPageNumber(), vo.getPageSize());
		page.setOptimizeCountSql(false); // 禁用自动优化
		IPage<GlDetailVr> pageList = this.glDetailMapper.selectFinanceDetailBookPage(page, vo, bookInfo);
		
		GlBalance yearb = null;//每个科目每年的期初余额
		
		GlBalance preMonthB = null;//上个月的余额 用来获取本年累计发生额  计算当前月份的年初方向
		
		GlDetail sumPreDetail = null;//当前凭证之前的数据
		
		String pkAccount = "";//当前循环的科目编号
		
		GlBalanceVr currentBr = null;//当前余额信息
		BigDecimal sumDmoney = null;//本期借方累计发生额
		BigDecimal sumCmoney = null;//本期贷方累计发生额
		
		BigDecimal yearmoney = null;//年初借方金额
		
		BigDecimal monthMOney = null;//本月月初金额
		BigDecimal endMonthMoney = null;//本月月末金额
		QueryWrapper<GlBalance> ycw = new QueryWrapper<GlBalance>();
		
		
		for (GlDetailVr vr : pageList.getRecords()) {
			
			if(StringUtils.isBlank(pkAccount) || !vr.getPkAccasoa().equals(pkAccount)) {
				pkAccount = vr.getPkAccasoa();
				ycw.clear();
				ycw.eq("DATAORIGINFLAG", -2 );
				ycw.eq("PK_ORG", vr.getPkOrg());
				ycw.eq("PK_ACCASOA", vr.getPkAccasoa());
				ycw.eq("YEAR", vr.getYearv());
				ycw.eq("PERIOD", "1");
				yearb = this.glBalanceMapper.selectOne(ycw);
				if(yearb == null) {
					yearmoney = new BigDecimal(0);
				}else {
					yearmoney = yearb.getFbeginBalanceLocal();
				}
			}
				
			//查找当前科目在  本月份之前   的   明细   发 生额总额
				sumPreDetail = this.glDetailMapper.selectPreDetailSum(vr);
				sumDmoney = sumPreDetail.getDebitamount();
				sumCmoney = sumPreDetail.getCreditamount();
				
			currentBr = vr.getGlBalanceVr();
			ycw.clear();
			ycw.eq("DATAORIGINFLAG", -2 );
			ycw.eq("PK_ORG", vr.getPkOrg());
			ycw.eq("PK_ACCASOA", vr.getPkAccasoa());
			ycw.eq("YEAR", vr.getYearv());
			ycw.eq("PERIOD", Integer.parseInt(vr.getPeriodv())-1);
			preMonthB = this.glBalanceMapper.selectOne(ycw);
			
			if(preMonthB == null) {
				preMonthB = new GlBalance();
				preMonthB.setFyearCreditLocal(new BigDecimal(0));
				preMonthB.setFyearDeditLocal(BigDecimal.valueOf(0));
			}
			
			//根据科目方向  判定年初数方向，并计算当前月份期初方向
			if(currentBr.getBalanorient() == 0 ){
				//借方 ; 年初数加借方-贷方  根据结构判断 正数  月初方向为借方，负数月初方向为贷方
				monthMOney = yearmoney.add(preMonthB.getFyearDeditLocal()).subtract(preMonthB.getFyearCreditLocal());
				if(monthMOney.compareTo(BigDecimal.ZERO) < 0) {
					currentBr.setBeginBalanorient(1);
				}else {
					currentBr.setBeginBalanorient(0);
				}
			}
			
			if(currentBr.getBalanorient() == 1) {
				//贷方 ; 年初数加贷方 - 借方方  根据结构判断 正数  月初方向为贷方，负数月初方向为借方
				monthMOney = yearmoney.add(preMonthB.getFyearCreditLocal()).subtract(preMonthB.getFyearDeditLocal());
				if(monthMOney.compareTo(BigDecimal.ZERO) < 0) {
					currentBr.setBeginBalanorient(0);
				}else {
					currentBr.setBeginBalanorient(1);
				}
			}
			
			//计算本地的余额 根据月初方向 判断逻辑为借方+借-待 或 待+待-借  得到余额 根据余额正负判断余额方向
			if(currentBr.getBeginBalanorient().compareTo(0) == 0) {
				//借方
				endMonthMoney = monthMOney.add(sumDmoney).subtract(sumCmoney);
				if(endMonthMoney.compareTo(BigDecimal.ZERO) < 0) {
					currentBr.setEndBalanorient(1);
				}else {
					currentBr.setEndBalanorient(0);
				}
			}
			if(currentBr.getBeginBalanorient().compareTo(1) == 0) {
				//贷方
				endMonthMoney = monthMOney.add(sumCmoney).subtract(sumDmoney);
				if(endMonthMoney.compareTo(BigDecimal.ZERO) < 0) {
					currentBr.setEndBalanorient(0);
				}else {
					currentBr.setEndBalanorient(1);
				}
			}
			currentBr.setFbeginBalanceLocal(monthMOney);
			currentBr.setFendBalanceLocal(endMonthMoney);
			vr.setGlBalanceVr(currentBr);
		}
		return ResponseFormat.retParam(1, 200, pageList);
	}

	@Override
	public JsonBean getFinanceDataDiaryBookList(TblStaffUtil staff, GlDetailVo vo) throws Exception {
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
		
		Page<GlDetailVr> page = new Page<GlDetailVr>(vo.getPageNumber(), vo.getPageSize());
		page.setOptimizeCountSql(false); // 禁用自动优化
		IPage<GlDetailVr> pageList = this.glDetailMapper.selectFinanceDetailBookPage(page, vo, bookInfo);
		
		if(pageList.getRecords() == null || pageList.getRecords().size() == 0){
			return ResponseFormat.retParam(1, 200, pageList);
		}
		List<GlDetailVr> vrList = new ArrayList<GlDetailVr>();
		GlDetailVr returnVr = null;
		
		
		GlBalanceVr currentBr = null;//当前余额信息
		
		String prePkAccasoa = null;//记录上次循环的科目
		String preMonth = null;//记录上次循环的月份
		
		List<String> detailPkList = null;//记录科目当前年度 当前年份 每个月份第一日的 凭证明细主键 集合 
		String ycPkDetail = null;
		
		BigDecimal yearmoney = null;//年初借方金额
		QueryWrapper<GlBalance> ycw = new QueryWrapper<GlBalance>();
		GlBalance yearb = null;//每个科目每年的期初余额
		GlBalance preMonthB = null;//上个月的余额 用来获取本年累计发生额  计算当前月份的年初方向
		
		GlDetail sumPreDetail = null;//当前凭证之前的数据
		
		GlDetail sumDayDetail = null;//本日累计数据
		
		BigDecimal sumDmoney = null;//本期借方累计发生额
		BigDecimal sumCmoney = null;//本期贷方累计发生额
		
		Date nextDate = null;//下一笔明细信息
		
		BigDecimal calAmount = null;//计算后的得数
		
		for (GlDetailVr vr : pageList.getRecords()) {
			currentBr = vr.getGlBalanceVr();
			//查找上一个月的 余额  1月份的为空
			ycw.clear();
			ycw.eq("DATAORIGINFLAG", -2 );
			ycw.eq("PK_ORG", vr.getPkOrg());
			ycw.eq("PK_ACCASOA", vr.getPkAccasoa());
			ycw.eq("YEAR", vr.getYearv());
			ycw.eq("PERIOD", Integer.parseInt(vr.getPeriodv())-1);
			preMonthB = this.glBalanceMapper.selectOne(ycw);
			if(preMonthB == null) {
				preMonthB = new GlBalance();
				preMonthB.setFyearCreditLocal(new BigDecimal(0));
				preMonthB.setFyearDeditLocal(BigDecimal.valueOf(0));
			}
			
			//查找当前科目在  本月份之前   的   明细   发 生额总额
			sumPreDetail = this.glDetailMapper.selectPreDetailSum(vr);
			sumDmoney = sumPreDetail.getDebitamount();
			sumCmoney = sumPreDetail.getCreditamount();
			
			
			//查找本科目本年度 第一个月份的年初数
			ycw.clear();
			ycw.eq("DATAORIGINFLAG", -2 );
			ycw.eq("PK_ORG", vr.getPkOrg());
			ycw.eq("PK_ACCASOA", vr.getPkAccasoa());
			ycw.eq("YEAR", vr.getYearv());
			ycw.eq("PERIOD", "1");
			yearb = this.glBalanceMapper.selectOne(ycw);
			if(yearb == null) {
				yearmoney = new BigDecimal(0);
			}else {
				yearmoney = yearb.getFbeginBalanceLocal();
			}
			
			//根据科目方向  判定年初数方向，并计算当前月份期初方向
			if(currentBr.getBalanorient() == 0 ){
				//借方 ; 年初数加借方-贷方  根据结构判断 正数  月初方向为借方，负数月初方向为贷方
				calAmount = yearmoney.add(preMonthB.getFyearDeditLocal()).subtract(preMonthB.getFyearCreditLocal());
				if(calAmount.compareTo(BigDecimal.ZERO) < 0) {
					currentBr.setEndBalanorient(1);
					currentBr.setBeginBalanorient(1);
				}else {
					currentBr.setEndBalanorient(0);
					currentBr.setBeginBalanorient(0);
				}
			}
			if(currentBr.getBalanorient() == 1) {
				//贷方 ; 年初数加贷方 - 借方方  根据结构判断 正数  月初方向为贷方，负数月初方向为借方
				calAmount = yearmoney.add(preMonthB.getFyearCreditLocal()).subtract(preMonthB.getFyearDeditLocal());
				if(calAmount.compareTo(BigDecimal.ZERO) < 0) {
					currentBr.setEndBalanorient(0);
					currentBr.setBeginBalanorient(0);
				}else {
					currentBr.setEndBalanorient(1);
					currentBr.setBeginBalanorient(1);
				}
			}
			
			//如果是第一次循环 或者 切换科目查询相关信息 或者 月份发生改变
			if(StringUtils.isBlank(prePkAccasoa) || !vr.getPkAccasoa().equals(prePkAccasoa) || !vr.getPeriodv().equals(preMonth)) {
				prePkAccasoa = vr.getPkAccasoa();
				preMonth = vr.getPeriodv();
				//判断当前项是不是此科目本年本月第一条记录
				detailPkList = this.glDetailMapper.selectFirstDayYear(vo, bookInfo,prePkAccasoa,vr.getYearv(),vr.getPeriodv());
				ycPkDetail = detailPkList==null||detailPkList.size()==0?"":detailPkList.get(0);
				if(ycPkDetail.equals(vr.getPkDetail())) {
					//当前循环是本年第一个月第一参数 输入期初余额
					returnVr = new GlDetailVr();
					returnVr.setExplanation("期初余额");
					returnVr.setPrepareddatev(DateUtil.formatDate(vr.getYearv()+"-"+vr.getPeriodv()+"-01", DateUtil.DATE_SMALL_STR));
					returnVr.setCreditamount(BigDecimal.valueOf(0));
					returnVr.setDebitamount(BigDecimal.valueOf(0));
					currentBr.setFendBalanceLocal(calAmount);
					returnVr.setGlBalanceVr(currentBr);
					vrList.add(returnVr);
				}
			}
			
			//计算本笔明细的余额 和 方向
			//计算本地的余额 根据月初方向 判断逻辑为借方+借-待 或 待+待-借  得到余额 根据余额正负判断余额方向
			if(currentBr.getBeginBalanorient().compareTo(0) == 0) {
				//借方
				calAmount = calAmount.add(sumDmoney).subtract(sumCmoney);
				if(calAmount.compareTo(BigDecimal.ZERO) < 0) {
					currentBr.setEndBalanorient(1);
				}else {
					currentBr.setEndBalanorient(0);
				}
			}
			if(currentBr.getBeginBalanorient().compareTo(1) == 0) {
				//贷方
				calAmount = calAmount.add(sumCmoney).subtract(sumDmoney);
				if(calAmount.compareTo(BigDecimal.ZERO) < 0) {
					currentBr.setEndBalanorient(0);
				}else {
					currentBr.setEndBalanorient(1);
				}
			}
			currentBr.setFendBalanceLocal(calAmount);
			vr.setGlBalanceVr(currentBr);
			vrList.add(vr);
			//获取下一笔的明细信息日期 如果下一笔明细为null 则直接输出本日累计、本月累计和本年累计
			
			nextDate = this.glDetailMapper.selectNextDetailDate(vr,bookInfo);
			
			if(nextDate == null) {
				//直接输出本日累计、本月累计，和本年累计
				
				//查找本日累计
				sumDayDetail = this.glDetailMapper.selectCurrentDaySumDetail(vr);
				
				returnVr = new GlDetailVr();
				returnVr.setExplanation("本日累计");
				returnVr.setPrepareddatev(vr.getPrepareddatev());
				returnVr.setCreditamount(sumDayDetail.getCreditamount());
				returnVr.setDebitamount(sumDayDetail.getDebitamount());
				currentBr.setFendBalanceLocal(calAmount);
				returnVr.setGlBalanceVr(currentBr);
				vrList.add(returnVr);
				
				returnVr = new GlDetailVr();
				returnVr.setExplanation("本月累计");
				returnVr.setCreditamount(currentBr.getLocalcreditamount());
				returnVr.setDebitamount(currentBr.getLocaldebitamount());
				currentBr.setFendBalanceLocal(calAmount);
				returnVr.setGlBalanceVr(currentBr);
				vrList.add(returnVr);
				
				returnVr = new GlDetailVr();
				returnVr.setExplanation("本年累计");
				returnVr.setCreditamount(currentBr.getFyearCreditLocal());
				returnVr.setDebitamount(currentBr.getFyearDeditLocal());
				currentBr.setFendBalanceLocal(calAmount);
				returnVr.setGlBalanceVr(currentBr);
				vrList.add(returnVr);
				
			}else {
				//如果不为空 证明此月明细未全部展示，判断日期是否相等，如果不相等则输出本日累计
				if(DateUtil.compare_date(DateUtil.parseDate(vr.getPrepareddatev(), DateUtil.DATE_SMALL_STR), DateUtil.parseDate(nextDate, DateUtil.DATE_SMALL_STR)) != 0) {
					sumDayDetail = this.glDetailMapper.selectCurrentDaySumDetail(vr);
					returnVr = new GlDetailVr();
					returnVr.setExplanation("本日累计");
					returnVr.setPrepareddatev(vr.getPrepareddatev());
					returnVr.setCreditamount(sumDayDetail.getCreditamount());
					returnVr.setDebitamount(sumDayDetail.getDebitamount());
					currentBr.setFendBalanceLocal(calAmount);
					returnVr.setGlBalanceVr(currentBr);
					vrList.add(returnVr);
				}
			}
		}
		
		pageList.setRecords(vrList);
		
		return ResponseFormat.retParam(1, 200, pageList);
	}

}
