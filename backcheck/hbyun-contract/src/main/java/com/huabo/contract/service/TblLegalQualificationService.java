package com.huabo.contract.service;

import java.math.BigDecimal;

import com.hbfk.util.PageInfo;
import com.huabo.contract.entity.TblLegalQualification;

public interface TblLegalQualificationService {
    void findListByPageInfo(PageInfo<TblLegalQualification> pageInfo, TblLegalQualification qualification,BigDecimal disputeid);

    TblLegalQualification findById(BigDecimal qualId);

    void saveQualification(TblLegalQualification qualification);

    void updateModifyQualification(TblLegalQualification old);

    void removeQualification(BigDecimal qualId);
}
