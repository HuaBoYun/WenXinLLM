package com.huabo.system.service;


import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.huabo.system.entity.TblAuthorizationRecord;
import com.huabo.system.vo.TblAuthorizationRecordVo;

public interface TblAuthorizationRecordService {

	void addEntity(TblAuthorizationRecord confirm) throws Exception;

	TblAuthorizationRecord findSpzRecordInfoByTargetId(String string) throws Exception;

	void modifyEntity(TblAuthorizationRecord confirm) throws Exception;

	JsonBean modifyFlowEndModifyOperationData(String recordId, TblStaffUtil loginStaff) throws Exception;

	JsonBean findById(String recordId) throws Exception;

	JsonBean findPageList(TblAuthorizationRecordVo vo) throws Exception;

	JsonBean verifyOperation(String targetId, String targetType, int operationType) throws Exception;


}
