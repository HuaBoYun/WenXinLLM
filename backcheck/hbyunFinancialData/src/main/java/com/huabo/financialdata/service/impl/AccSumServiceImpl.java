package com.huabo.financialdata.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.huabo.financialdata.entity.dto.AccSumListPageQuery;
import com.huabo.financialdata.entity.entity.AccSum;
import com.huabo.financialdata.entity.entity.AccSumQuery;
import com.huabo.financialdata.entity.enums.AccountDcEnum;
import com.huabo.financialdata.entity.vo.AccSumListPageInfoVO;
import com.huabo.financialdata.entity.vo.accBook.AccBookVO;
import com.huabo.financialdata.mapper.AccSumMapper;
import com.huabo.financialdata.service.IAccSumService;
import org.assertj.core.util.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @author lee
 * @version 1.0.0
 * @description 科目余额-接口服务
 * @date 2022/8/29 10:58 下午
 **/
@Service
public class AccSumServiceImpl extends ServiceImpl<AccSumMapper, AccSum> implements IAccSumService {

    @Resource
    private AccSumMapper accSumMapper;

    /**
     * 分页查询
     * 科目余额-接口服务
     * @param accBookVO 当前登录用户选中的账套
     * @param query     查询请求参数
     * @return 返回结果
     */
    @Override
    public PageInfo<AccSumListPageInfoVO> getListByPage(AccBookVO accBookVO, AccSumListPageQuery query) {
        AccSumQuery accSumQuery = new AccSumQuery();
        //数据源+年份设置
        accSumQuery.setDbSource(accBookVO.getAcctId());
        accSumQuery.setAYEAR(Integer.valueOf(accBookVO.getBookYear()));
        //请求参数赋值
        accSumQuery.setACCID(query.getAccId());
        accSumQuery.setAccName(query.getAccName());
        accSumQuery.setMinMonth(query.getMinMonth());
        accSumQuery.setMaxMonth(query.getMaxMonth());

        accSumQuery.setBQMC(query.getBqmc());
        accSumQuery.setBQMD(query.getBqmd());
        accSumQuery.setQMMD(query.getQmmd());
        accSumQuery.setQMMC(query.getQmmc());
        accSumQuery.setQCMC(query.getQcmc());
        accSumQuery.setQCMD(query.getQcmd());
        accSumQuery.setStatus(query.getStatus() == null ? "等于" : query.getStatus());
        if (Objects.nonNull(query.getBookYear())) {
            accSumQuery.setAYEAR(query.getBookYear());
        }
        //设置排序
        accSumQuery.setOrderByClause("s.ACCID,s.AMONTH");
        PageHelper.startPage(query.getPageNo(), query.getPageSize());
        // 条件查询
        List<AccSum> list = accSumMapper.selectByCondition(accSumQuery);
        PageInfo pageInfo = new PageInfo<>(list);
        if (CollectionUtil.isNotEmpty(list)) {
            List<AccSumListPageInfoVO> voList = Lists.newArrayList();
            list.forEach(item -> voList.add(doInfoVoMapper(item)));
            pageInfo.setList(voList);
        }
        return pageInfo;
    }

    /**
     * vo实体对象映射
     *
     * @param info 待转换对象
     * @return 返回结果
     */
    private AccSumListPageInfoVO doInfoVoMapper(AccSum info) {
        AccSumListPageInfoVO vo = new AccSumListPageInfoVO();
        vo.setAccId(info.getACCID());
        vo.setAccName(info.getAccName());
        vo.setQcdcName(AccountDcEnum.getNameByCode(vo.getQCDC()));
        vo.setQmdcName(AccountDcEnum.getNameByCode(vo.getQMDC()));
        BeanUtils.copyProperties(info, vo);
        return vo;
    }
}
