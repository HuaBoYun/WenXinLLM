package com.huabo.legal.service;

import com.hbfk.util.JsonBean;
import com.huabo.legal.mysql.entity.TblFwglConferenceManagementMySql;
import com.huabo.legal.mysql.entity.TblFwglOtherFileMessageMySql;
import com.huabo.legal.util.PageResult;
import com.huabo.legal.vo.param.TblFwglConferenceManagementQueryParam;
import com.huabo.legal.vo.param.TblFwglOtherFileMessageQueryParam;
import com.huabo.legal.vo.result.TblFwglConferenceManagement;
import com.huabo.legal.vo.result.TblFwglOtherFileMessage;

public interface DailyManagementService {

	/**
	 * 会议管理列表 查询
	 * @param param
	 * @return
	 */
	JsonBean getTblFwglConferenceManagementList(TblFwglConferenceManagementQueryParam param);

	/**
	 * 会议管理 新增/更新
	 * @param param
	 * @return
	 */
	JsonBean saveOrUpdateTblFwglConferenceManagement(TblFwglConferenceManagement param);

	/**
	 * 会议管理 刪除
	 * @param id
	 * @return
	 */
	JsonBean deleteTblFwglConferenceManagement(Long id);

	/**
	 * 会议管理详情 查询
	 * @param id
	 * @return
	 */
	JsonBean getTblFwglConferenceManagement(Long id);

	/**
	 * 其他文件报文列表 查询
	 * @param param
	 * @return
	 */
	JsonBean getTblFwglOtherFileMessageList(TblFwglOtherFileMessageQueryParam param);

	/**
	 * 其他文件报文 新增/更新
	 * @param param
	 * @return
	 */
	JsonBean saveOrUpdateTblFwglOtherFileMessage(TblFwglOtherFileMessage param);

	/**
	 * 其他文件报文 刪除
	 * @param id
	 * @return
	 */
	JsonBean deleteTblFwglOtherFileMessage(Long id);

	/**
	 * 其他文件报文详情 查询
	 * @param id
	 * @return
	 */
	JsonBean getTblFwglOtherFileMessage(Long id);
}
