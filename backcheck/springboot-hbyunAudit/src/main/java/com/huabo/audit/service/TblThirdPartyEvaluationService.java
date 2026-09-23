package com.huabo.audit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.audit.oracle.entity.TblThirdPartyEvaluationEntity;
import com.huabo.audit.util.PageResult;

import java.util.List;

public interface TblThirdPartyEvaluationService extends IService<TblThirdPartyEvaluationEntity> {
    PageResult<TblThirdPartyEvaluationEntity> page(Integer pageNumber, Integer pageSize, TblThirdPartyEvaluationEntity entity);

    List<TblThirdPartyEvaluationEntity> list(TblThirdPartyEvaluationEntity entity);
}
