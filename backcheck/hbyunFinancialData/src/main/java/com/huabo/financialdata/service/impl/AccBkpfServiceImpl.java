package com.huabo.financialdata.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.financialdata.config.mvn.ApiResponse;
import com.huabo.financialdata.entity.base.LoginUserInfoDO;
import com.huabo.financialdata.entity.entity.TblAccBkpf;
import com.huabo.financialdata.entity.vo.accBkpf.AccBkpfRequestVo;
import com.huabo.financialdata.entity.vo.accBkpf.AccBkpfResponseVo;
import com.huabo.financialdata.entity.vo.accBook.AccBookVO;
import com.huabo.financialdata.mapper.AccBkpfMapper;
import com.huabo.financialdata.mapper.AccSumMapper;
import com.huabo.financialdata.service.AccBkpfService;
import com.huabo.financialdata.service.IAccBookService;
import com.huabo.financialdata.util.LoginTokenUtil;

import net.sf.jsqlparser.statement.select.SelectBody;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 凭证库  接口实现类
 * </p>
 *
 * @author Mr.xiang
 * @since 2022-11-22
 */
@Service
public class AccBkpfServiceImpl implements AccBkpfService {

    private Logger logger = LoggerFactory.getLogger(AccBkpfServiceImpl.class);

    @Resource
    AccBkpfMapper accBkpfMapper;
    @Resource
    AccSumMapper accSumMapper;
    @Resource
    private IAccBookService accBookService;
    
    @Resource
    private UserProvider userProvider;

    /**
     * 凭证库 列表分页查询
     *
     * @param token            用户登录token
     * @param accBkpfRequestVo 请求参数封装公共类
     * @return
     */
    @Override
    public ApiResponse<PageInfo<AccBkpfResponseVo>> getList(String token, AccBkpfRequestVo accBkpfRequestVo) {
        logger.info("进入获取凭证库信息接口：：");
        try {
            String type = accBkpfRequestVo.getType();
//            String status =  accBkpfRequestVo.getStatus();
            String accName = accBkpfRequestVo.getAccName();

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
            accBkpfRequestVo.setDbSource(accBookVO.getAcctId());
            accBkpfRequestVo.setBookYear(Integer.parseInt(accBookVO.getBookYear()));
            //前端传入的查询条件  例如前端查询科目名称包含银行 则      type='ACCNAME1' ,status ='包含'  accName = '银行'  ，以此类推
            //科目名称-ACCNAME1，科目编号-ACCID，凭证日期-PZ_DATE，抬头文本-LINETEXT，借方金额-MD，贷方金额-MC，凭证号-PZH，凭证类型-PZTYPE，附件数-FJ，期间-AMONTH，财务主管-CWZG，记账人-JZR，出纳人-CNR，审核人-SHR，制单人-ZDR")
            if (type != null) {
                if (!"".equals(accName) && accName != null) {
                    if (type.equals("ACCID")) {
                        accBkpfRequestVo.setAccid(accName);
                    }
                    // 凭证日期
                    if (type.equals("PZ_DATE")) {
                        accBkpfRequestVo.setPzDate(accName);
                    }
                    // 抬头文本
                    if (type.equals("LINETEXT")) {
                        accBkpfRequestVo.setLineText(accName);
                    }
                    if (type.equals("ACCNAME1")) {
                        accBkpfRequestVo.setAccNameOne(accName);
                    }
                    if (type.equals("MD")) {
                        accBkpfRequestVo.setMd(accName);
                    }
                    if (type.equals("MC")) {
                        accBkpfRequestVo.setMc(accName);
                    }
                    if (type.equals("PZH")) {
                        accBkpfRequestVo.setPzh(accName);
                    }
                    if (type.equals("PZTYPE")) {
                        accBkpfRequestVo.setPzType(accName);
                    }
                    if (type.equals("FJ")) {
                        accBkpfRequestVo.setFj(accName);
                    }
                    if (type.equals("CWZG")) {
                        accBkpfRequestVo.setCwzg(accName);
                    }
                    if (type.equals("JZR")) {
                        accBkpfRequestVo.setJzrl(accName);
                    }
                    if (type.equals("CNR")) {
                        accBkpfRequestVo.setCnr(accName);
                    }
                    if (type.equals("SHR")) {
                        accBkpfRequestVo.setShrs(accName);
                    }
                    if (type.equals("ZDR")) {
                        accBkpfRequestVo.setZdr(accName);
                    }
                }
            }
            String wnss = accBkpfRequestVo.getWnss();//万能搜索id对接
            if (StringUtils.isNotBlank(wnss)) {
                accBkpfRequestVo.setMaxMonth("13");
                accBkpfRequestVo.setMinMonth("1");
                if (wnss.indexOf("2014") >= 0) {
                    accBkpfRequestVo.setBookYear(2014);
                }
                if (wnss.indexOf("2015") >= 0) {
                    accBkpfRequestVo.setBookYear(2015);
                }
                if (wnss.indexOf("2016") >= 0) {
                    accBkpfRequestVo.setBookYear(2016);
                }
                if (wnss.indexOf("2017") >= 0) {
                    accBkpfRequestVo.setBookYear(2017);
                }
                if (wnss.indexOf("2018") >= 0) {
                    accBkpfRequestVo.setBookYear(2018);
                }
                if (wnss.indexOf("2019") >= 0) {
                    accBkpfRequestVo.setBookYear(2019);
                }
                if (wnss.indexOf("2020") >= 0) {
                    accBkpfRequestVo.setBookYear(2020);
                }
            }
            PageHelper.startPage(accBkpfRequestVo.getPageNumber(), accBkpfRequestVo.getPageSize());
            List<AccBkpfResponseVo> accBkpfResponseVoList = accBkpfMapper.selectAuxiliaryBookList(accBkpfRequestVo);
            Integer count = accBkpfMapper.selectAuxiliaryBookCount(accBkpfRequestVo);
            PageInfo<AccBkpfResponseVo> pageInfo = new PageInfo<AccBkpfResponseVo>();
            pageInfo.setTotal(count);
            pageInfo.setList(accBkpfResponseVoList);
            return ApiResponse.success(pageInfo);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取记录凭证
     * @param token
     * @param pzh 凭证号
     * @param pzDate 凭证日期
     * @param book 账套
     * @param year 年
     * @return
     */
    @Override
    public ApiResponse<List<TblAccBkpf>> findAccBkpfAmonth(String token, String pzh, String pzDate, String book, String year) throws Exception {
    	//查询当前财务账套有效的最大月份
        TblStaffUtil staff = userProvider.get();
        
        if(staff == null) {
        	return ApiResponse.fail("用户已失效");
        }
        List<TblAccBkpf> list = accBkpfMapper.findAccBkpfAmonth(pzh, pzDate, book, year);
        return ApiResponse.success(list);
    }

}
