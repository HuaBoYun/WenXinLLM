package com.management.accountant.oracle.service;

import com.management.accountant.oracle.entity.TblAttachment;

import java.util.List;

public interface TblAttachmentService {

	/**
	 * 批量查询 法务管理-文件存储详情
	 * @param ids
	 * @return
	 */
	List<TblAttachment> findByIds(String ids);
}
