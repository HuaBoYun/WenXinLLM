package com.huabo.audit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.audit.oracle.entity.TblAuditPersonnelInfoEntity;
import com.huabo.audit.util.PageResult;

import java.util.List;

public interface TblAuditPersonnelInfoService extends IService<TblAuditPersonnelInfoEntity> {
    PageResult<TblAuditPersonnelInfoEntity> page(Integer pageNumber, Integer pageSize, TblAuditPersonnelInfoEntity entity);

    List<TblAuditPersonnelInfoEntity> list(TblAuditPersonnelInfoEntity entity);
}
