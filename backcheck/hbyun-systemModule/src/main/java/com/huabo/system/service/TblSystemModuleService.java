package com.huabo.system.service;


import com.huabo.system.entity.TblSystemModule;

import java.math.BigDecimal;
import java.util.Map;

public interface TblSystemModuleService {


    Map<String, Object> selectPageInfoByOrgId(Integer pageNumber, Integer pageSize, String token, String staffId, TblSystemModule module);

    Integer checkReplay(TblSystemModule module, int type, String token, String staffId);

    Map<String, Object> saveEntity(String token, String staffId, TblSystemModule module, BigDecimal[] flowid,
                                   Integer[] flowOrderNo);

    TblSystemModule findAllInfoById(BigDecimal moduleId);

    Map<String, Object> modifyEntity(String token, String staffId, TblSystemModule module, BigDecimal[] flowid, Integer[] flowOrderNo);

    void modifyFlowModifyStatus(Integer moduleStatus, BigDecimal moduleId);

    String findModelTypeByModuleId(BigDecimal moduleId);

    Integer selectOrgManageRight(String id, String rightId);

    void saveOrgRightRelation(String id, String rightId, int type);

    void dealModuleFlowOrganizationRealtion(BigDecimal moduleId, String orgId);

    void removeStaffModuleRelation(BigDecimal moduleId, String orgId);

    void removeModelFlowRelation(String[] flowIds, Integer modelId);
}
