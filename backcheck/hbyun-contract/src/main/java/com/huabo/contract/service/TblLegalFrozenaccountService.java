package com.huabo.contract.service;

import java.math.BigDecimal;

import com.hbfk.util.PageInfo;
import com.huabo.contract.entity.TblLegalFrozenaccount;

public interface TblLegalFrozenaccountService {
    void findListByPageInfo(PageInfo<TblLegalFrozenaccount> pageInfo, TblLegalFrozenaccount frozenAccount,BigDecimal disputeid);

    TblLegalFrozenaccount findById(BigDecimal inforId);

    void saveFrozenAccount(TblLegalFrozenaccount frozen);

    void updateModifyFrozenAccount(TblLegalFrozenaccount oldFrozen);

    void removeFrozenAccount(BigDecimal inforId);

    TblLegalFrozenaccount findInforid(BigDecimal inforId);
}
