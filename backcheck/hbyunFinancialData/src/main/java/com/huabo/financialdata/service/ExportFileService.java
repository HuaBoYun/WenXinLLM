package com.huabo.financialdata.service;

import com.github.pagehelper.PageInfo;
import com.hbfk.entity.TblAttachment;
import com.huabo.financialdata.config.mvn.ApiResponse;
import com.huabo.financialdata.entity.entity.TblAccBkpf;
import com.huabo.financialdata.entity.vo.accBkpf.AccBkpfRequestVo;
import com.huabo.financialdata.entity.vo.accBkpf.AccBkpfResponseVo;
import com.huabo.financialdata.entity.vo.export.ExportRequestVo;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

public interface ExportFileService {

	/**
	 * 导出选中信息为Excel
	 * @param token
	 * @param exportRequestVo
	 * @return
	 */
	ApiResponse<TblAttachment> exportFileFunc(String token, ExportRequestVo exportRequestVo) throws Exception;

    void fileDownLoad(HttpServletResponse response, String fileId, Boolean isCa)throws Exception ;

}
