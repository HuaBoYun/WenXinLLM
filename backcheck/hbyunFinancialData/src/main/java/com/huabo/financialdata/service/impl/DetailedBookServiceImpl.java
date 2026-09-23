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
import com.huabo.financialdata.entity.vo.detailedBook.DetailedBookRequestVo;
import com.huabo.financialdata.entity.vo.detailedBook.DetailedBookResponseVo;
import com.huabo.financialdata.entity.vo.diaryBook.DiaryBookRequestVo;
import com.huabo.financialdata.mapper.AccBsegMapper;
import com.huabo.financialdata.mapper.AccSumMapper;
import com.huabo.financialdata.mapper.AccountMapper;
import com.huabo.financialdata.service.DetailedBookService;
import com.huabo.financialdata.service.IAccBookService;
import com.huabo.financialdata.util.LoginTokenUtil;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * 明细账  接口实现类
 * </p>
 *
 * @author Mr.xiang
 * @since 2022-11-19
 */
@Service
public class DetailedBookServiceImpl implements DetailedBookService {
    private Logger logger = LoggerFactory.getLogger(DetailedBookServiceImpl.class);

    @Resource
    AccSumMapper accSumMapper;
    @Resource
    AccBsegMapper accBsegMapper;
    @Resource
    private IAccBookService accBookService;
    
    @Resource
    private AccountMapper accountMapper;
    
    @Resource
    private UserProvider userProvider;


    /**
     * 查询明细账列表信息
     * @param token
     * @param detailedBookRequestVo 明细账
     * @return
     */
    @Override
    public ApiResponse<Map<String, Object>> getList(String token, DetailedBookRequestVo detailedBookRequestVo) throws Exception {
        logger.info("进入查询明细账列表接口：：");
        Integer pageNumber = detailedBookRequestVo.getPageNumber();
        String status = detailedBookRequestVo.getStatus();
        String accName = detailedBookRequestVo.getAccName();
        String type = detailedBookRequestVo.getType();
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
        detailedBookRequestVo.setDbSource(accBookVO.getAcctId());
        if (pageNumber <= 0) {
            pageNumber = 1;
        }
        detailedBookRequestVo.setStatus(status);
       
        if(StringUtils.isNotBlank(accName)) {
        	if("PZ_DATE".equals(type)) {
            	detailedBookRequestVo.setPzDate(accName);
            }else if("LINETEXT".equals(type)) {
            	detailedBookRequestVo.setLineText(accName);
            }else if("ACCID".equals(type)) {
            	detailedBookRequestVo.setAccid(accName);
            }else if("ACCNAME1".equals(type)) {
            	detailedBookRequestVo.setAccNameOne(accName);
            }else if("MD".equals(type)) {
            	detailedBookRequestVo.setMd(new BigDecimal(accName));
            }else if("MC".equals(type)) {
            	detailedBookRequestVo.setMc(new BigDecimal(accName));
            }
        }
        
        detailedBookRequestVo.setBookYear(Integer.parseInt(accBookVO.getBookYear()));
        if(detailedBookRequestVo.getMaxMonth() == null) {
        	detailedBookRequestVo.setMaxMonth(12);
        }
        if(detailedBookRequestVo.getMinMonth() == null) {
        	detailedBookRequestVo.setMinMonth(1);
        }
        
        if(StringUtils.isBlank(detailedBookRequestVo.getAccid())) {
        	detailedBookRequestVo.setAccid(this.accBsegMapper.selectDefaultAccid(detailedBookRequestVo));
        }
        
        AccountQuery accountVo = new AccountQuery();
        accountVo.setDbSource(accBookVO.getAcctId());
        accountVo.setAYEAR(Integer.parseInt(accBookVO.getBookYear()));
        accountVo.setACCID(detailedBookRequestVo.getAccid());
        accountVo.setStatus("等于");
        Account	account = accountMapper.selectByCondition(accountVo).get(0);
        
        
        
        PageHelper.startPage(detailedBookRequestVo.getPageNumber(), detailedBookRequestVo.getPageSize());
        List<DetailedBookResponseVo> detailedBookResponseVoList = accBsegMapper.selectAccBseg(detailedBookRequestVo);
        
        DetailedBookRequestVo detailVO = null;//查询本笔之前明细的条件
        DetailedBookResponseVo calDetail = null;//存储本笔明细之前累计发生的借贷金额
        BigDecimal bbqcmoney = null;//本笔明细 期初的金额
        String bbqcdc = null;//本笔明细的期初借贷方向
        BigDecimal bbqmmoney = null;//本笔明细 期末的金额
        String bbqmdc = null;//本笔明细的期末借贷方向
        for (DetailedBookResponseVo detail : detailedBookResponseVoList) {
        	detailVO = new DetailedBookRequestVo();
        	detailVO.setDbSource(detailedBookRequestVo.getDbSource());
        	detailVO.setBookYear(detailedBookRequestVo.getBookYear());
        	detailVO.setAccid(detail.getAccid());
        	detailVO.setMonth(detail.getAmonth());
        	detailVO.setPzDate(detail.getPzDate());
        	detailVO.setPzh(detail.getPzh());
        	detailVO.setEntryId(detail.getEntryId());
        	//获取本笔明细放发生之前的累计的借贷方的金额
        	calDetail = this.accBsegMapper.selectCurrentMonthyMdc(detailVO);
        	
        	//判断本月期初方向是借方（D）,还是贷方（C）
        	if("D".equals(detail.getQcdc())) {
        		//1.如果是借方，本笔期初金额  = 本期期初借方+之前累计借方金额 - 之前累计的贷方金额 ；如果是正数则借贷方向不变，如果是负数需将金额调正，并改变借贷方向、
        		bbqcmoney = detail.getQcmd().add(calDetail.getMd()).subtract(calDetail.getMc());
        		if(bbqcmoney.compareTo(BigDecimal.ZERO) > 0) {
        			bbqcdc = "D";
        		}else {
        			bbqcmoney = bbqcmoney.abs();
        			bbqcdc = "C";
        		}
        	}else {
        		//2.如果是贷方，本笔期初金额  = 本期期初贷方+之前累计贷方金额 - 之前累计的借方金额 ；如果是正数则借贷方向不变，如果是负数需将金额调正，并改变借贷方向、
        		bbqcmoney = detail.getQcmc().add(calDetail.getMc()).subtract(calDetail.getMd());
        		if(bbqcmoney.compareTo(BigDecimal.ZERO) > 0) {
        			bbqcdc = "C";
        		}else {
        			bbqcmoney = bbqcmoney.abs();
        			bbqcdc = "D";
        		}
        	}
        	
        	//判断本笔明细期初借贷方向 并进行计算
        	if("D".equals(bbqcdc)) {
        		bbqmmoney = bbqcmoney.add(detail.getMd()).subtract(detail.getMc());
        		if(bbqmmoney.compareTo(BigDecimal.ZERO) > 0) {
        			bbqmdc = "D";
        		}else {
        			bbqmmoney = bbqmmoney.abs();
        			bbqmdc = "C";
        		}
        	}else {
        		bbqmmoney = bbqcmoney.add(detail.getMc()).subtract(detail.getMd());
        		if(bbqmmoney.compareTo(BigDecimal.ZERO) > 0) {
        			bbqmdc = "C";
        		}else {
        			bbqmmoney = bbqmmoney.abs();
        			bbqmdc = "D";
        		}
        	}
        	detail.setBqqcdc(bbqcdc);
        	detail.setBqqmdc(bbqmdc);
        	detail.setQcye(bbqcmoney);
        	detail.setBalance(bbqmmoney);
		}
        
        
        Integer count = accBsegMapper.selectAccBsegCount(detailedBookRequestVo);

        PageInfo<DetailedBookResponseVo> detailedBookResponseVoPageInfo = new PageInfo<DetailedBookResponseVo>();
        detailedBookResponseVoPageInfo.setList(detailedBookResponseVoList);
        detailedBookResponseVoPageInfo.setTotal(count);
        
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("pageInfo", detailedBookResponseVoPageInfo);
        resultMap.put("account", account);
        
        return ApiResponse.success(resultMap);
    }
}
