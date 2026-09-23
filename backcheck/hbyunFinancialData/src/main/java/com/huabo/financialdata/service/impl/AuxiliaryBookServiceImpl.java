package com.huabo.financialdata.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.financialdata.config.mvn.ApiResponse;
import com.huabo.financialdata.entity.base.LoginUserInfoDO;
import com.huabo.financialdata.entity.vo.accBook.AccBookVO;
import com.huabo.financialdata.entity.vo.auxiliaryBook.AuxiliaryBookRequestVo;
import com.huabo.financialdata.entity.vo.auxiliaryBook.AuxiliaryBookResponsetVo;
import com.huabo.financialdata.mapper.AssInfoMapper;
import com.huabo.financialdata.service.AuxiliaryBookService;
import com.huabo.financialdata.service.IAccBookService;
import com.huabo.financialdata.util.LoginTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * <p>
 * 辅助账  接口实现类
 * </p>
 *
 * @author Mr.xiang
 * @since 2022-11-21
 */
@Service
public class AuxiliaryBookServiceImpl implements AuxiliaryBookService {

    private Logger logger = LoggerFactory.getLogger(AuxiliaryBookServiceImpl.class);

    @Resource
    AssInfoMapper assInfoMapper;
    @Resource
    private IAccBookService accBookService;
    
    @Resource
    private UserProvider userProvider;

    /**
     * 查询辅助账数据
     * @param token
     * @param auxiliaryBookRequestVo
     * @return
     */
    @Override
    public ApiResponse<PageInfo<AuxiliaryBookResponsetVo>> getList(String token, AuxiliaryBookRequestVo auxiliaryBookRequestVo) throws Exception {
        logger.info("进入获取辅助账数据接口实现类：：：");
//        String strType = auxiliaryBookRequestVo.getType();
        String status = auxiliaryBookRequestVo.getStatus();
        String valueType = auxiliaryBookRequestVo.getValueType();
        String values = auxiliaryBookRequestVo.getValues();
        Integer pageNumber = auxiliaryBookRequestVo.getPageNumber();
        if (pageNumber != null && !"".equals(pageNumber)) {
            pageNumber = 1;
        }

        String strType = auxiliaryBookRequestVo.getTypeName();
        String strTypeName = strType;
        String leftMenu = auxiliaryBookRequestVo.getLeftMenu() == null ? "1" : auxiliaryBookRequestVo.getLeftMenu();
        /*if (strType != null && !"".equals(strType)) {
            if (strType.equals("1")) {
                strTypeName = "待摊费用";
            } else if (strType.equals("2")) {
                strTypeName = "银行账户";
            } else if (strType.equals("3")) {
                strTypeName = "股东";
            } else if (strType.equals("4")) {
                strTypeName = "客户";
            } else if (strType.equals("5")) {
                strTypeName = "供应商";
            } else if (strType.equals("6")) {
                strTypeName = "部门";
            } else if (strType.equals("7")) {
                strTypeName = "金融机构";
            } else if (strType.equals("8")) {
                strTypeName = "职员";
            } else if (strType.equals("9")) {
                strTypeName = "专项费用";
            } else if (strType.equals("10")) {
                strTypeName = "物料";
            } else if (strType.equals("11")) {
                strTypeName = "投资单位";
            }
            
        }*/
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
        auxiliaryBookRequestVo.setDbSource(accBookVO.getAcctId());
        auxiliaryBookRequestVo.setBookYear(accBookVO.getBookYear());
        auxiliaryBookRequestVo.setTypeName(strTypeName);
        List<AuxiliaryBookResponsetVo> auxiliaryBookResponsetVoList = new ArrayList<AuxiliaryBookResponsetVo>();
        Integer count = 0;
        if (status == null || "".equals(status)) {
            status = "=";
        }
        PageHelper.startPage(auxiliaryBookRequestVo.getPageNumber(), auxiliaryBookRequestVo.getPageSize());
        auxiliaryBookRequestVo.setStatus(status);
        if ("1".equals(leftMenu)) {
            // 获取分页辅助账数据
            auxiliaryBookResponsetVoList = assInfoMapper.selectAuxiliaryBookList(auxiliaryBookRequestVo);
            // 查询总共条数 辅助信息表
            count = assInfoMapper.selectAuxiliaryBookCount(auxiliaryBookRequestVo);
        } else if ("2".equals(leftMenu)) {
            // 辅助账 辅助余额表
            auxiliaryBookResponsetVoList = assInfoMapper.findAssInfoByBalanceList(auxiliaryBookRequestVo);
            // 辅助账 辅助余额表
            count = assInfoMapper.findAssInfoByBalanceCount(auxiliaryBookRequestVo);
        } else if ("3".equals(leftMenu)) {
            // 辅助账  总账
            auxiliaryBookResponsetVoList = assInfoMapper.findAssSumByTotalList(auxiliaryBookRequestVo);
            // 辅助账  总账条数
            count = assInfoMapper.findAssSumByTotalCount(auxiliaryBookRequestVo);
        }
        PageInfo<AuxiliaryBookResponsetVo> pageInfo = new PageInfo<AuxiliaryBookResponsetVo>();
        pageInfo.setTotal(count);
        pageInfo.setList(auxiliaryBookResponsetVoList);
        return ApiResponse.success(pageInfo);
    }

    /**
     * 获取辅助账类型数据接口
     * @param token
     * @return
     */
    @Override
    public ApiResponse<List<String>> getAssTypeList(String token) throws Exception {
        logger.info("进入获取辅助账类型数据接口实现类：：：");

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
        AuxiliaryBookRequestVo auxiliaryBookRequestVo = new AuxiliaryBookRequestVo();

        auxiliaryBookRequestVo.setBookYear(accBookVO.getBookYear());
        auxiliaryBookRequestVo.setDbSource(accBookVO.getAcctId());

        List<String> assTypeList = this.assInfoMapper.selectAllAssTypeList(auxiliaryBookRequestVo);

        return ApiResponse.success(assTypeList);
    }
}
