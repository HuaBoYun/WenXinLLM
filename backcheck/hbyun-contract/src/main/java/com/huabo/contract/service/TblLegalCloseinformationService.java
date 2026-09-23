package com.huabo.contract.service;

import java.math.BigDecimal;

import com.hbfk.util.PageInfo;
import com.huabo.contract.entity.TblLegalCloseinformation;

public interface TblLegalCloseinformationService {
    String findListByPageInfo(PageInfo<TblLegalCloseinformation> pageInfo, TblLegalCloseinformation closeInfo,BigDecimal disputeid);

    TblLegalCloseinformation findById(BigDecimal closeId);

    void addDisputeSettlement(TblLegalCloseinformation closeInfo);

    void updateModifyDisputeSettlementModify(TblLegalCloseinformation oldCloseInfo);

    void removeDisputeSettlementRemove(BigDecimal closeid);
}
