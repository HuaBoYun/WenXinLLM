package com.huabo.contract.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.util.PageInfo;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.contract.entity.TblLegalCloseinformation;
import com.huabo.contract.mapper.TblLegalCloseinformationMapper;
import com.huabo.contract.service.TblLegalCloseinformationService;

@Service
public class TblLegalCloseinformationServiceImpl implements TblLegalCloseinformationService {

    @Resource
    private TblLegalCloseinformationMapper tblLegalCloseinformationMapper;

    @Override
    public String findListByPageInfo(PageInfo<TblLegalCloseinformation> pageInfo, TblLegalCloseinformation closeInfo,BigDecimal disputeid) {
    	String result = null;
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        IPage<TblLegalCloseinformation> page = new Page<TblLegalCloseinformation>(pageInfo.getCurrentPage(),pageInfo.getPageSize());
        IPage<TblLegalCloseinformation> pageList = tblLegalCloseinformationMapper.findListByPageInfo(page, closeInfo, disputeid);
        pageInfo.setTlist(pageList.getRecords());
        pageInfo.setTotalRecord((int)pageList.getTotal());
        resultMap.put("code", "1");
        resultMap.put("msg", "访问接口成功");
        resultMap.put("date", pageInfo);
        JSONObject jsonObject = new JSONObject(resultMap);
        result = jsonObject.toString();
        return result;
    }

    @Override
    public TblLegalCloseinformation findById(BigDecimal closeId) {
        return tblLegalCloseinformationMapper.findByCloseId(closeId);
    }

    @Override
    public void addDisputeSettlement(TblLegalCloseinformation closeInfo) {
    	closeInfo.setCloseid(RandomUtil.uuBigDecimalId());
    	tblLegalCloseinformationMapper.addDisputeSettlement(closeInfo);
    }

    @Override
    public void updateModifyDisputeSettlementModify(TblLegalCloseinformation oldCloseInfo) {
        tblLegalCloseinformationMapper.updateModifyDisputeSettlementModify(oldCloseInfo);
    }

    @Override
    public void removeDisputeSettlementRemove(BigDecimal closeid) {
    	tblLegalCloseinformationMapper.removeDisputeSettlementRemove(closeid);
    }
}
