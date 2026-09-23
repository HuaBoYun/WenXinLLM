package com.huabo.audit.service.business;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblAuditModelDataSourceOracle;
import com.huabo.audit.oracle.entity.TblAuditModelExcelExtOracle;
import com.huabo.audit.oracle.entity.TblAuditModelExcelOracle;
import com.huabo.audit.oracle.entity.TblAuditModelExcelTableOracle;
import com.huabo.audit.vo.param.*;
import com.huabo.audit.vo.result.UserInfoParam;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

public interface AuditModelService {

	/**
	 * 数据源管理 列表查询
	 * @param param
	 * @return
	 */
	JsonBean getTblAuditModelDataSourceList(TblAuditModelDataSourceQueryParam param);

	/**
	 * 数据源管理 新增/更新
	 * @param param
	 * @return
	 */
	JsonBean saveOrUpdateTblAuditModelDataSource(TblAuditModelDataSourceOracle param) throws Exception;

	/**
	 * 数据源管理 刪除
	 * @param id
	 * @return
	 */
	JsonBean deleteTblAuditModelDataSource(BigDecimal id);

	/**
	 * 数据源管理 查询
	 * @param id
	 * @return
	 */
	JsonBean getTblAuditModelDataSource(BigDecimal id);

	/**
	 * excel导入记录 列表查询
	 * @param param
	 * @return
	 */
	JsonBean getTblAuditModelExcelList(TblAuditModelExcelQueryParam param) throws Exception;

	/**
	 * excel导入记录 新增/更新
	 * @param param
	 * @return
	 */
	JsonBean saveOrUpdateTblAuditModelExcel(TblAuditModelExcelOracle param);

	/**
	 * excel导入记录 刪除
	 * @param id
	 * @return
	 */
	JsonBean deleteTblAuditModelExcel(BigDecimal id);

	/**
	 * excel导入记录 查询
	 * @param id
	 * @return
	 */
	JsonBean getTblAuditModelExcel(Long id) throws Exception;

	/**
	 * excel表数据 列表查询
	 * @param param
	 * @return
	 */
	JsonBean getTblAuditModelExcelExtList(TblAuditModelExcelExtQueryParam param);

	/**
	 * excel表数据 刪除
	 * @param id
	 * @return
	 */
	JsonBean deleteTblAuditModelExcelExt(Long id);

	/**
	 * excel表数据 查询
	 * @param id
	 * @return
	 */
	JsonBean getTblAuditModelExcelExt(Long id);

	/**
	 * excel分析预览表数据 列表查询
	 * @param param
	 * @return
	 */
	JsonBean getTblAuditModelExcelTableList(TblAuditModelExcelTableQueryParam param);

	/**
	 * excel分析预览表数据 新增/更新
	 * @param param
	 * @return
	 */
	JsonBean saveOrUpdateTblAuditModelExcelTable(List<TblAuditModelExcelTableOracle> param);

	/**
	 * excel分析预览表数据 刪除
	 * @param id
	 * @return
	 */
	JsonBean deleteTblAuditModelExcelTable(Long id);

	/**
	 * excel分析预览表数据 查询
	 * @param id
	 * @return
	 */
	JsonBean getTblAuditModelExcelTable(Long id);

	/**
	 * excel文件解析
	 * @param multipartFile
	 * @param id
	 * @param userInfo
	 * @return
	 */
	JsonBean fileUpload(MultipartFile multipartFile, BigDecimal id, UserInfoParam userInfo);

	/**
	 * excel分析预览表数据-生成表
	 * @param param
	 * @return
	 */
	JsonBean generatingTable(GeneratingTableParam param) throws Exception;

	/**
	 * excel工作副本名校验
	 * @param param
	 * @return
	 */
	JsonBean excelCheck(ExcelCheckParam param);
}
