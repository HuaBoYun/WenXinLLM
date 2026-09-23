package com.huabo.contract.service.impl;


import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.contract.entity.TblContractInformation;
import com.huabo.contract.entity.TblCyhwUnit;
import com.huabo.contract.mapper.TblContractInformationMapper;
import com.huabo.contract.mapper.TblCyhwUnitMapper;
import com.huabo.contract.service.TblContractInformationService;

@Service
public class TblContractInformationServiceImpl implements TblContractInformationService {

    @Resource
    private TblCyhwUnitMapper tblCyhwUnitMapper;

    @Resource
    private TblContractInformationMapper tblContractInformationMapper;


    @Override
    public Map<String, Object> saveContractInfoMation(BigDecimal contractId,TblContractInformation information) {
            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            try {
                TblCyhwUnit unit = this.tblCyhwUnitMapper.findContractById(contractId);
                information.setProjectid(unit.getContractid());
                if (information.getInfoid() != null) {
                    this.tblContractInformationMapper.updateContractInfoMation(information);
                } else {
                	information.setInfoid(RandomUtil.uuBigDecimalId());
                    this.tblContractInformationMapper.saveContractInfoMation(information);
                }

            } catch (Exception e) {
                resultMap.put("code", "-1");
                resultMap.put("msg", "保存失败");
                return resultMap;
            }
            resultMap.put("code", "1");
            resultMap.put("msg", "保存成功");
            resultMap.put("data", information);
            return resultMap;
    }

    @Override
    public Map<String, Object> removeContractInfoMation(BigDecimal infoId) {
            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            try {
                this.tblContractInformationMapper.removeContractInfoMation(infoId);
            } catch (Exception e) {
                resultMap.put("code", "-1");
                resultMap.put("msg", "删除失败");
                return resultMap;
            }
            resultMap.put("code", "1");
            resultMap.put("msg", "删除成功");
            return resultMap;
    }

    @Override
    public Map<String, Object> findInformationListById(BigDecimal contractId) {
            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            try {
                List<TblContractInformation> informationList = this.tblContractInformationMapper.findInformationListById(contractId);
                resultMap.put("code", "1");
                resultMap.put("msg", "成功");
                resultMap.put("data", informationList);
            } catch (Exception e) {
                resultMap.put("code", "0");
                resultMap.put("msg", "失败");
                return resultMap;
            }
            return resultMap;
    }

}
