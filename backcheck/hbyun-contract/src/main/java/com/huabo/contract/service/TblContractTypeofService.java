package com.huabo.contract.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.hbfk.entity.TblStaffUtil;
import com.huabo.contract.entity.TblContractTypeof;
import com.huabo.contract.util.Tree;

public interface TblContractTypeofService {


    Map<String, Object> findAllListToCreateContract(String token, String staffId);

    Map<String, Object> findPageInfoList(Integer pageNumber, Integer pageSize, String typeId,String choiceTypeName, String token, String staffId);

    List<Tree> getFatherTree();//BigDecimal orgid

    Map<String, Object> saveContractType(String[] typeNameArr, BigDecimal parentid, String token, String staffId);

    Map<String, Object> modifyContractType(String token, String staffId, String typeId, String typeName);

    Map<String, Object> removeContractType(String typeId, String token, String staffId);

    Map<String, Object> toAddContract(BigDecimal flowId, BigDecimal typeId, String token, String fatherNo);

    List<TblContractTypeof> findAllList(BigDecimal orgid, int i);
    
    String getAutoContractNo(BigDecimal flowId,String fatherNo, TblStaffUtil user) throws Exception;
}
