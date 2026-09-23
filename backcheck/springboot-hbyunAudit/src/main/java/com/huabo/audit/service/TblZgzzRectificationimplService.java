package com.huabo.audit.service;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblNbsjAdvicenoteEntity;
import com.huabo.audit.oracle.entity.TblZgzzRectificationimpl;
import com.huabo.audit.oracle.vo.TblNbsjAdvicenoteVo;
import com.huabo.audit.oracle.vo.TblRectificationIssuesVo;
import com.huabo.audit.util.R;

public interface TblZgzzRectificationimplService {

	/**
	 * 我的整改，落实人保存填写的落实信息
	 * @param token			用户登录令牌
	 * @param impl			保存的落实信息实体
	 * @param attIds		附件主键数组
	 * @return 保存后的实体
	 * @throws Exception
	 */
	JsonBean saveRectificationImpl(String token, TblZgzzRectificationimpl impl, String[] attIds) throws Exception;

	/**
	 * 我的整改  填写落实信息页面 删除附件关系
	 * @param token		用户登录令牌
	 * @param implId	落实信息主键
	 * @param attId		附件主键
	 * @return
	 */
	JsonBean removeRectificationImplAtt(String token, String implId, String attId) throws Exception;

	/**
	 * 整改报告 -- 选择方案后获取整改落实信息
	 * @param loginStaff
	 * @param riv
	 * @param reporttype
	 * @param planIdStrs 
	 * @return
	 * @throws Exception
	 */
	JsonBean getRectificationIssuesListByReportType(TblStaffUtil loginStaff, TblRectificationIssuesVo riv,
			Integer reporttype, String planIdStrs) throws Exception;
	
	JsonBean getRectificationImplDetailInfo(String token, String implId) throws Exception;
	
}
