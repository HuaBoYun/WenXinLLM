package com.huabo.finance.service;

import com.huabo.finance.entity.TblConfigTableInfo;
import com.huabo.finance.entity.caiji.BdAvgrate;
import com.huabo.finance.vo.BusinessDataVo;
import com.huabo.finance.vo.TblConfigTableInfoVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.util.JsonBean;

public interface TblConfigTableInfoService extends IService<TblConfigTableInfo> {

	JsonBean getTablInfoList(TblConfigTableInfoVo vo) throws Exception;

	JsonBean addTableInfo(TblConfigTableInfo tableInfo) throws Exception;

	JsonBean modifyTableInfo(TblConfigTableInfo tableInfo) throws Exception;

	JsonBean removeTableInfo(String fid) throws Exception;

	JsonBean getTableInfo(String fid) throws Exception;

	JsonBean tableCreate(String fid) throws Exception;

	JsonBean cwzbshowTableList() throws Exception;

	JsonBean cwzbshowBusinessDataList(BusinessDataVo vo) throws Exception;

	JsonBean cwzbshowBusinessDataDetail(BusinessDataVo vo) throws Exception;

	JsonBean tableFstatus(String fid, Integer fstatus) throws Exception;

	JsonBean getExportTemplate(String tableId, HttpServletResponse response) throws Exception;

	JsonBean executeSql(TblConfigTableInfo tableInfo) throws Exception;

}
