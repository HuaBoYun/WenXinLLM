package com.huabo.contract.service;

import java.math.BigDecimal;
import java.util.List;

import com.hbfk.util.PageInfo;
import com.huabo.contract.entity.TblLegalArbitrationrecord;

public interface TblLegalArbitrationrecordService {
    List<TblLegalArbitrationrecord> findListBynegotiaId(BigDecimal arbitraId) throws Exception;

    void saveNegotiateRecord(TblLegalArbitrationrecord record) throws Exception;

    TblLegalArbitrationrecord findById(BigDecimal arrecordId) throws Exception;

    void modifyNegotiateRecord(TblLegalArbitrationrecord oldRecord) throws Exception;

    void removeNegitiateRecord(BigDecimal arrecordId);

    void findListByPageInfo(PageInfo<TblLegalArbitrationrecord> pageInfo, BigDecimal arrecordid) throws Exception;
}
