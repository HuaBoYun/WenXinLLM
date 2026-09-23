package com.huabo.financialdata.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.financialdata.config.mvn.ApiResponse;
import com.huabo.financialdata.entity.base.LoginUserInfoDO;
import com.huabo.financialdata.entity.entity.Account;
import com.huabo.financialdata.entity.entity.AccountQuery;
import com.huabo.financialdata.entity.vo.accBook.AccBookVO;
import com.huabo.financialdata.entity.vo.diaryBook.DiaryBookRequestVo;
import com.huabo.financialdata.entity.vo.diaryBook.DiaryBookResponseVo;
import com.huabo.financialdata.mapper.AccBsegMapper;
import com.huabo.financialdata.mapper.AccSumMapper;
import com.huabo.financialdata.mapper.AccountMapper;
import com.huabo.financialdata.service.DiaryBookSerivce;
import com.huabo.financialdata.service.IAccBookService;
import com.huabo.financialdata.util.DateUtils;
import com.huabo.financialdata.util.LoginTokenUtil;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * <p>
 * 日记账  接口实现类
 * </p>
 *
 * @author Mr.xiang
 * @since 2022-11-20
 */
@Service
public class DiaryBookSerivceImpl implements DiaryBookSerivce {

    private Logger logger = LoggerFactory.getLogger(DiaryBookSerivceImpl.class);

    @Resource
    AccSumMapper accSumMapper;
    @Resource
    AccBsegMapper accBsegMapper;
    @Resource
    AccountMapper accountMapper;
    @Resource
    private IAccBookService accBookService;
    
    @Resource
    private UserProvider userProvider;

	/**
	 * 分页查询日记账信息
	 * @param token
	 * @param diaryBookRequestVo
	 * @return
	 */
    @Override
    public ApiResponse<Map<String, Object>> getList(String token, DiaryBookRequestVo diaryBookRequestVo) throws Exception {
        logger.info("获取日记账列表信息：：：token=" + token);
        Integer pageNumber = diaryBookRequestVo.getPageNumber();
        String type = diaryBookRequestVo.getType();
        String status = diaryBookRequestVo.getStatus();
        String accName = diaryBookRequestVo.getAccName();
        String accid = diaryBookRequestVo.getAccid();
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        if (pageNumber == null || pageNumber < 1) {
            pageNumber = 1;
        }

      //查询当前财务账套有效的最大月份
        TblStaffUtil staff = userProvider.get();
        if(staff == null) {
        	return ApiResponse.fail("用户已失效");
        }
        LoginUserInfoDO userInfoDO = LoginTokenUtil.tokenAnalysis(staff);

        AccBookVO accBookVO = accBookService.getSelectedBookByStaffId(userInfoDO.getStaffId(), userInfoDO.getCurrentOrgId());
        if (Objects.isNull(accBookVO)) {
            return ApiResponse.fail("当前登录用户未选中财务账套");
        }

        diaryBookRequestVo.setDbSource(accBookVO.getAcctId());
        diaryBookRequestVo.setBookYear(Integer.parseInt(accBookVO.getBookYear()));
        Account account = null;
        if(StringUtils.isBlank(accid)) {
        	AccountQuery accountVo = new AccountQuery();
            accountVo.setDbSource(accBookVO.getAcctId());
            accountVo.setAYEAR(Integer.parseInt(accBookVO.getBookYear()));
            accountVo.setACCID(diaryBookRequestVo.getAccid());
            accountVo.setStatus("等于");
            account = accountMapper.selectByCondition(accountVo).get(0);
        	diaryBookRequestVo.setAccid(account.getACCID());
        }else {
        	diaryBookRequestVo.setAccid(accid);
        	AccountQuery accountVo = new AccountQuery();
            accountVo.setDbSource(accBookVO.getAcctId());
            accountVo.setAYEAR(Integer.parseInt(accBookVO.getBookYear()));
            accountVo.setACCID(diaryBookRequestVo.getAccid());
            accountVo.setStatus("等于");
            account = accountMapper.selectByCondition(accountVo).get(0);
        }

		// 科目名称-ACCNAME1，科目编号-ACCID，凭证日期-PZ_DATE，抬头文本-LINETEXT，借方金额-MD，贷方金额-MC，凭证号-PZH，凭证类型-PZTYPE，附件数-FJ，期间-AMONTH，财务主管-CWZG，记账人-JZR，出纳人-CNR，审核人-SHR，制单人-ZDR"
		if (type != null && !"".equals(type)) {
            if (accName != null && !"".equals(accName)) {
                if (type.equals("PZ_DATE")) {
                    diaryBookRequestVo.setPzDate(DateUtils.parse(accName, "yyyy-MM-dd"));
                }
                if (type.equals("LINETEXT")) {
                    diaryBookRequestVo.setLineText(accName);
                }
                if (type.equals("ACCNAME1")) {
                    diaryBookRequestVo.setAccNameOne(accName);
                }
                if (type.equals("ACCID")) {
                    diaryBookRequestVo.setAccid(accName);
                }
                if (type.equals("MD")) {
                    diaryBookRequestVo.setMd(accName);
                }
                if (type.equals("MC")) {
                    diaryBookRequestVo.setMc(accName);
                }
                if("PZH".equals(type)) {
                	diaryBookRequestVo.setPzh(accName);
                }
            }
        }
        diaryBookRequestVo.setStatus(status);
        //String accNames = accountMapper.selectAccName(diaryBookRequestVo);
        PageHelper.startPage(diaryBookRequestVo.getPageNumber(), diaryBookRequestVo.getPageSize());
        List<DiaryBookResponseVo> diaryBookResponseVoList = accBsegMapper.selectDiaryBookList(diaryBookRequestVo);
        
        Integer count = accBsegMapper.selectDiaryBookCount(diaryBookRequestVo);
        if(count == 0) {
       	 	PageInfo<DiaryBookResponseVo> pageInfo = new PageInfo<DiaryBookResponseVo>();
            pageInfo.setList(diaryBookResponseVoList);
            pageInfo.setTotal(count);
            
            resultMap.put("pageInfo", pageInfo);
            resultMap.put("account", account);
            return ApiResponse.success(resultMap);
       }
        
        List<DiaryBookResponseVo> dealBsegList = new ArrayList<DiaryBookResponseVo>(0);
        DiaryBookResponseVo dealBseg = null;
        
        Integer preMonth = null; //上一次循环的月份，判断是否显示期初余额
        Integer preDay = null;//上一次循环的日期 ，判断是否显示本日累计；
        
        DiaryBookRequestVo preBookRequestVo = null;
        DiaryBookResponseVo preBookBesgVo = null;
        DiaryBookResponseVo sumBseg = null;
        String pzDate = null;
        String qmdc = null;
        if(diaryBookRequestVo.getPageNumber() == 1 && diaryBookResponseVoList != null && diaryBookResponseVoList.size() != 0) {
        	//当出在第一页的时候输出 期初余额
        	dealBseg = new DiaryBookResponseVo();
        	pzDate = diaryBookResponseVoList.get(0).getPzDate();
        	qmdc = diaryBookResponseVoList.get(0).getQcdc();
        	dealBseg.setPzDate(pzDate.substring(0,pzDate.lastIndexOf("-")+1)+"01");
        	dealBseg.setLineText("期初余额");
        	dealBseg.setMd(new BigDecimal("0"));
        	dealBseg.setMc(new BigDecimal("0"));
        	dealBseg.setQmdc(qmdc);
        	if("D".equals(qmdc)) {
        		dealBseg.setBalance(diaryBookResponseVoList.get(0).getQcmd());
        	}else {
        		dealBseg.setBalance(diaryBookResponseVoList.get(0).getQcmc());
        	}
        	dealBsegList.add(dealBseg);
        	preMonth = diaryBookResponseVoList.get(0).getAmonth();
			preDay = diaryBookResponseVoList.get(0).getDayNo();
        }else if(diaryBookRequestVo.getPageNumber() != 1 && diaryBookResponseVoList != null && diaryBookResponseVoList.size() != 0) {
        	//获取分页前一个
        	preBookRequestVo = new DiaryBookRequestVo();
        	preBookRequestVo.setBookYear(diaryBookRequestVo.getBookYear());
        	preBookRequestVo.setDbSource(diaryBookRequestVo.getDbSource());
        	preBookRequestVo.setPageSize((diaryBookRequestVo.getPageNumber()-1)*diaryBookRequestVo.getPageSize());
        	preBookRequestVo.setAccid(diaryBookRequestVo.getAccid());
        	preBookBesgVo = this.accBsegMapper.selectPreDiaryBookRequestPreVo(preBookRequestVo);
        	preMonth = preBookBesgVo.getAmonth();
        	preDay = preBookBesgVo.getDayNo();
        }
        int index = 1 ;
        String qcdc = null;
        String qmDc = null;
        BigDecimal qmmoney = null;
        DiaryBookResponseVo prebseg = null;
        for (DiaryBookResponseVo bseg : diaryBookResponseVoList) {
        	if(bseg.getAmonth() != preMonth && index == 1 && diaryBookRequestVo.getPageNumber() != 1) {
        		//月份已变更
        		//首页默认第一个 输出 期初余额，如果不是第一页 并且是第一条记录 也分不等 由于上一页最后一个是结束已输出本月 本年累计 ，则只输出期初余额
        		dealBseg = new DiaryBookResponseVo();
    			pzDate = bseg.getPzDate();
            	qmdc = bseg.getQcdc();
            	dealBseg.setPzDate(pzDate.substring(0,pzDate.lastIndexOf("-")+1)+"01");
            	dealBseg.setLineText("期初余额");
            	dealBseg.setQmdc(qmdc);
            	if("D".equals(qmdc)) {
            		dealBseg.setBalance(bseg.getQcmd());
            	}else {
            		dealBseg.setBalance(bseg.getQcmc());
            	}
            	dealBsegList.add(dealBseg);
            	preMonth = bseg.getAmonth();
    			preDay = bseg.getDayNo();
        	}
        	
        	//获取本笔明细发生之前本月累计的借贷方发生额总和
        	preBookRequestVo = new DiaryBookRequestVo();
        	preBookRequestVo.setBookYear(diaryBookRequestVo.getBookYear());
        	preBookRequestVo.setDbSource(diaryBookRequestVo.getDbSource());
        	preBookRequestVo.setAccid(bseg.getAccid());
        	preBookRequestVo.setPzDate(DateUtils.parse(bseg.getPzDate(), "yyyy-MM-dd"));
        	preBookRequestVo.setMonth(bseg.getAmonth());
        	preBookRequestVo.setPzh(bseg.getPzh());
        	preBookRequestVo.setEntryId(bseg.getEntryId());
        	sumBseg = this.accBsegMapper.selectSumMonthyMdc(preBookRequestVo);
        	
        	
        	qcdc = bseg.getQcdc();
        	if("D".equals(qcdc)) {
        		qmmoney = bseg.getQcmd().add(sumBseg.getMd()).subtract(sumBseg.getMc());
        		if(qmmoney.compareTo(BigDecimal.ZERO) > 0) {
        			qmDc = "D";
        		}else {
        			qmmoney = qmmoney.abs();
        			qmDc = "C";
        		}
        	}else {
        		qmmoney = bseg.getQmmc().add(sumBseg.getMc()).subtract(sumBseg.getMd());
        		if(qmmoney.compareTo(BigDecimal.ZERO) > 0) {
        			qmDc = "C";
        		}else {
        			qmmoney = qmmoney.abs();
        			qmDc = "D";
        		}
        	}
        	bseg.setQmdc(qmDc);
        	bseg.setBalance(qmmoney);
        	
        	if(bseg.getDayNo() != preDay && index != 1) {
        		//日期已变更，输出本日累计
        		preBookRequestVo.setAccid(prebseg.getAccid());
            	preBookRequestVo.setPzDate(DateUtils.parse(prebseg.getPzDate(), "yyyy-MM-dd"));
            	preBookRequestVo.setMonth(prebseg.getAmonth());
            	preBookRequestVo.setPzh(prebseg.getPzh());
            	preBookRequestVo.setEntryId(prebseg.getEntryId());
        		sumBseg = this.accBsegMapper.selectSumMdcByDayNo(preBookRequestVo);
            	dealBseg = new DiaryBookResponseVo();
            	dealBseg.setLineText("本日累计");
            	dealBseg.setMd(sumBseg.getMd());
            	dealBseg.setMc(sumBseg.getMc());
            	dealBseg.setQmdc(qmDc);
            	dealBseg.setBalance(prebseg.getBalance());
            	dealBsegList.add(dealBseg);
            	preDay = bseg.getDayNo();
        	}
        	if(bseg.getAmonth() != preMonth) {
        		//月份已变更，输出本月累计和本年累计，并输出下月期初余额
        			//如果不是第一页，并且也不是第一个发生月份变化，则输出上月累计额结余与下月期初数据信息
        			//输出本月累计
        			dealBseg = new DiaryBookResponseVo();
                	qmdc = bseg.getQmdc();
                	dealBseg.setLineText("本月累计");
                	dealBseg.setMd(prebseg.getBqmd());
                	dealBseg.setMc(prebseg.getBqmc());
                	dealBseg.setQmdc(qmdc);
                	if("D".equals(qmdc)) {
                		dealBseg.setBalance(prebseg.getQmmd());
                	}else {
                		dealBseg.setBalance(prebseg.getQmmc());
                	}
                	dealBsegList.add(dealBseg);
        			//输出本年累计
                	dealBseg = new DiaryBookResponseVo();
                	qmdc = prebseg.getQmdc();
                	dealBseg.setLineText("本年累计");
                	dealBseg.setMd(prebseg.getLjmd());
                	dealBseg.setMc(prebseg.getLjmc());
                	dealBseg.setQmdc(qmdc);
                	if("D".equals(qmdc)) {
                		dealBseg.setBalance(prebseg.getQmmd());
                	}else {
                		dealBseg.setBalance(prebseg.getQmmc());
                	}
                	dealBsegList.add(dealBseg);
        			
        			//判断是否是最后一个 如果是最后一个则不输出下月期初数据，如果不是则输出
        			if(index != diaryBookResponseVoList.size()) {
        				dealBseg = new DiaryBookResponseVo();
            			pzDate = bseg.getPzDate();
                    	qmdc = bseg.getQcdc();
                    	dealBseg.setPzDate(pzDate.substring(0,pzDate.lastIndexOf("-")+1)+"01");
                    	dealBseg.setLineText("期初余额");
                    	dealBseg.setQmdc(qmdc);
                    	if("D".equals(qmdc)) {
                    		dealBseg.setBalance(bseg.getQcmd());
                    	}else {
                    		dealBseg.setBalance(bseg.getQcmc());
                    	}
                    	dealBsegList.add(dealBseg);
        			}
        		preMonth = bseg.getAmonth();
    			preDay = bseg.getDayNo();
        	}
        	dealBsegList.add(bseg);
        	prebseg = bseg;
        	index++;
		}
        
        //计算总页数
        Integer totalPage = count/diaryBookRequestVo.getPageSize();
        if(count%diaryBookRequestVo.getPageSize() != 0) {
        	totalPage++;
        }
        
        DiaryBookResponseVo lastBseg = diaryBookResponseVoList.get(index-2);
        if(diaryBookRequestVo.getPageNumber().compareTo(totalPage) == 0) {
        	//如果是最后一页则输出 本日累计 本月累计与本年累计
        	preBookRequestVo.setAccid(lastBseg.getAccid());
        	preBookRequestVo.setPzDate(DateUtils.parse(lastBseg.getPzDate(), "yyyy-MM-dd"));
        	preBookRequestVo.setMonth(lastBseg.getAmonth());
        	preBookRequestVo.setPzh(lastBseg.getPzh());
        	preBookRequestVo.setEntryId(lastBseg.getEntryId());
        	sumBseg = this.accBsegMapper.selectSumMdcByDayNo(preBookRequestVo);
        	dealBseg = new DiaryBookResponseVo();
        	dealBseg.setLineText("本日累计");
        	dealBseg.setMd(sumBseg.getMd());
        	dealBseg.setMc(sumBseg.getMc());
        	dealBseg.setQmdc(qmDc);
        	dealBseg.setBalance(qmmoney);
        	dealBsegList.add(dealBseg);
        	dealBseg = new DiaryBookResponseVo();
        	qmdc = lastBseg.getQmdc();
        	dealBseg.setLineText("本月累计");
        	dealBseg.setMd(lastBseg.getBqmd());
        	dealBseg.setMc(lastBseg.getBqmc());
        	dealBseg.setQmdc(qmdc);
        	if("D".equals(qmdc)) {
        		dealBseg.setBalance(lastBseg.getQmmd());
        	}else {
        		dealBseg.setBalance(lastBseg.getQmmc());
        	}
        	dealBsegList.add(dealBseg);
			//输出本年累计
        	dealBseg = new DiaryBookResponseVo();
        	qmdc = lastBseg.getQmdc();
        	dealBseg.setLineText("本年累计");
        	dealBseg.setMd(lastBseg.getLjmd());
        	dealBseg.setMc(lastBseg.getLjmc());
        	dealBseg.setQmdc(qmdc);
        	if("D".equals(qmdc)) {
        		dealBseg.setBalance(lastBseg.getQmmd());
        	}else {
        		dealBseg.setBalance(lastBseg.getQmmc());
        	}
        	dealBsegList.add(dealBseg);
        }else {
        	//不是最后一页 ，则获取下一个凭证明细进行逻辑判断是否需要先本日累计，本月累计和本年累计
        	preBookRequestVo.setPageSize(diaryBookRequestVo.getPageNumber()*diaryBookRequestVo.getPageSize()+1);
        	preBookRequestVo.setAccid(diaryBookRequestVo.getAccid());
        	DiaryBookResponseVo nextBseg = this.accBsegMapper.selectPreDiaryBookRequestPreVo(preBookRequestVo);
        	if(lastBseg.getDayNo().compareTo(nextBseg.getDayNo()) != 0) {
        		//下个凭证明细日期已发生改变，则输出本日累计
        		preBookRequestVo.setAccid(lastBseg.getAccid());
            	preBookRequestVo.setPzDate(DateUtils.parse(lastBseg.getPzDate(), "yyyy-MM-dd"));
            	preBookRequestVo.setMonth(lastBseg.getAmonth());
            	preBookRequestVo.setPzh(lastBseg.getPzh());
            	preBookRequestVo.setEntryId(lastBseg.getEntryId());
            	sumBseg = this.accBsegMapper.selectSumMdcByDayNo(preBookRequestVo);
            	dealBseg = new DiaryBookResponseVo();
            	dealBseg.setLineText("本日累计");
            	dealBseg.setMd(sumBseg.getMd());
            	dealBseg.setMc(sumBseg.getMc());
            	dealBseg.setQmdc(qmDc);
            	dealBseg.setBalance(qmmoney);
            	dealBsegList.add(dealBseg);
        	}
        	if(lastBseg.getAmonth().compareTo(nextBseg.getAmonth()) != 0) {
            	dealBseg = new DiaryBookResponseVo();
            	qmdc = lastBseg.getQmdc();
            	dealBseg.setLineText("本月累计");
            	dealBseg.setMd(lastBseg.getBqmd());
            	dealBseg.setMc(lastBseg.getBqmc());
            	dealBseg.setQmdc(qmdc);
            	if("D".equals(qmdc)) {
            		dealBseg.setBalance(lastBseg.getQmmd());
            	}else {
            		dealBseg.setBalance(lastBseg.getQmmc());
            	}
            	dealBsegList.add(dealBseg);
    			//输出本年累计
            	dealBseg = new DiaryBookResponseVo();
            	qmdc = lastBseg.getQmdc();
            	dealBseg.setLineText("本年累计");
            	dealBseg.setMd(lastBseg.getLjmd());
            	dealBseg.setMc(lastBseg.getLjmc());
            	dealBseg.setQmdc(qmdc);
            	if("D".equals(qmdc)) {
            		dealBseg.setBalance(lastBseg.getQmmd());
            	}else {
            		dealBseg.setBalance(lastBseg.getQmmc());
            	}
            	dealBsegList.add(dealBseg);
        	}
        }
        
        
        PageInfo<DiaryBookResponseVo> pageInfo = new PageInfo<DiaryBookResponseVo>();
        pageInfo.setList(dealBsegList);
        pageInfo.setTotal(count);
        
        
        resultMap.put("pageInfo", pageInfo);
        resultMap.put("account", account);
        
        return ApiResponse.success(resultMap);
    }

}

