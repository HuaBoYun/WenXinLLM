package com.regulatory.penetration.oracle.service;

import com.regulatory.penetration.oracle.entity.TblAttachment;

import java.util.List;

public interface TblAttachmentService {

	/**
	 * 批量查询 法务管理-文件存储详情
	 * @param ids
	 * @return
	 */
	List<TblAttachment> findByIds(String ids);
}
