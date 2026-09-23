package com.huabo.financialdata.service.impl;

import cn.hutool.core.convert.Convert;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.financialdata.config.mvn.ApiResponse;
import com.huabo.financialdata.entity.base.LoginUserInfoDO;
import com.huabo.financialdata.entity.vo.accBook.AccBookVO;
import com.huabo.financialdata.entity.vo.accSumTotal.AccSumTotalRequestVo;
import com.huabo.financialdata.entity.vo.accSumTotal.AccSumTotalResponseVo;
import com.huabo.financialdata.mapper.AccSumMapper;
import com.huabo.financialdata.service.AccSumTotalService;
import com.huabo.financialdata.service.IAccBookService;
import com.huabo.financialdata.util.LoginTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * <p>
 * 总分类账   服务实现类
 * </p>
 *
 * @author Mr.xiang
 * @since 2022-10-18
 */
@Service
public class AccSumTotalServiceImpl implements AccSumTotalService {

    private Logger logger = LoggerFactory.getLogger(AccSumTotalServiceImpl.class);

    @Resource
    AccSumMapper accSumMapper;
    @Resource
    private IAccBookService accBookService;
    
    @Resource
    private UserProvider userProvider;

    /**
     * 总分类账 列表分页查询
     *
     * @param token                用户登录token
     * @param accSumTotalRequestVo 请求参数封装公共类
     * @return
     */
    @Override
    public ApiResponse<PageInfo<AccSumTotalResponseVo>> getList(String token, AccSumTotalRequestVo accSumTotalRequestVo) {
        try {
            logger.info("进入获取总分类账数据接口：：：");
//            Integer maxMonth = accSumTotalRequestVo.getMaxMonth();
//            Integer minMonth = accSumTotalRequestVo.getMinMonth();
//            Integer amonth = accSumTotalRequestVo.getAmonth();
//            String book = accSumTotalRequestVo.getBook();
            Integer year = accSumTotalRequestVo.getBookyear();
            String accName = accSumTotalRequestVo.getAccName();
            String type = accSumTotalRequestVo.getType();
            String status = accSumTotalRequestVo.getStatus();
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

//            AccBkpfRequestVo accBkpfRequestVo = new AccBkpfRequestVo();
//            accBkpfRequestVo.setBookYear(year);
//            accBkpfRequestVo.setDbSource(accBookVO.getAcctId());
            //判断年份
//            if(Objects.isNull(maxMonth)){
//                Integer years = accSumMapper.selectMaxMonth(accBkpfRequestVo);
//                amonth  = years > 12 ==true ? 12 : years;
//            }else {
//                maxMonth = amonth;
//            }
//            minMonth = minMonth == null ? maxMonth -1 : minMonth;
//            maxMonth = (maxMonth == minMonth) == true ? maxMonth++ : maxMonth;
//
//            if(accName != null && accName != ""){
//                accName = accName.trim().replaceAll(",","");
//            }

//            AccSumTotalRequestVo accSum = new AccSumTotalRequestVo();
            if (type != null && type != "") {
                if (accName != null && accName != "") {
                    if (type.equals("ACCID")) {
                        accSumTotalRequestVo.setAccid(accName);
                    } else if (type.equals("ACCNAME1")) {
                        accSumTotalRequestVo.setAccNameOne(accName);
                    }
                }

                if ("等于".equals(status)) {
                    status = "=";
                }
                if ("包含".equals(status)) {
                    status = "包含";
                }
                if ("不等于".equals(status)) {
                    status = "!=";
                }
                if ("大于".equals(status)) {
                    status = ">";
                }
                if ("小于".equals(status)) {
                    status = "<";
                }
                if ("小于等于".equals(status)) {
                    status = "<=";
                }
                if ("大于等于".equals(status)) {
                    status = ">=";
                }
            }
            accSumTotalRequestVo.setStatus(status);
            accSumTotalRequestVo.setAyear(Convert.toInt(accBookVO.getBookYear()));
//            accSum.setAyear(year);
//            accSum.setMinMonth(minMonth);
//            accSum.setMaxMonth(maxMonth);
            accSumTotalRequestVo.setDbSource(accBookVO.getAcctId());
            PageHelper.startPage(accSumTotalRequestVo.getPageNumber(), accSumTotalRequestVo.getPageSize());

            PageInfo<AccSumTotalResponseVo> pageInfo = new PageInfo<AccSumTotalResponseVo>();
            pageInfo.setList(accSumMapper.findAccSumTotal(accSumTotalRequestVo));
            pageInfo.setTotal(accSumMapper.findTotalCount(accSumTotalRequestVo));

            return ApiResponse.success(pageInfo);
        } catch (Exception e) {
            logger.info("总分类账请求错误！" + e.getMessage());
            e.printStackTrace();
        }
        return ApiResponse.fail("查询数据失败，请重试！");

    }
}
