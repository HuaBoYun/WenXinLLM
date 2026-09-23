package com.huabo.audit.controller;


import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.FtpUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.exception.ServiceException;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblAuditModelDataSourceOracle;
import com.huabo.audit.oracle.entity.TblAuditModelExcelOracle;
import com.huabo.audit.oracle.entity.TblAuditModelExcelTableOracle;
import com.huabo.audit.oracle.service.TblAuditModelExcelOracleService;
import com.huabo.audit.service.TblAttachmentService;
import com.huabo.audit.service.business.AuditModelService;
import com.huabo.audit.util.ExcelUtil;
import com.huabo.audit.util.SnowflakeIdWorker;
import com.huabo.audit.util.TokenUtil;
import com.huabo.audit.vo.param.ExcelCheckParam;
import com.huabo.audit.vo.param.GeneratingTableParam;
import com.huabo.audit.vo.param.TblAuditModelDataSourceQueryParam;
import com.huabo.audit.vo.param.TblAuditModelExcelExtQueryParam;
import com.huabo.audit.vo.param.TblAuditModelExcelQueryParam;
import com.huabo.audit.vo.param.TblAuditModelExcelTableQueryParam;
import com.huabo.audit.vo.result.UserInfoParam;
import com.vip.vjtools.vjkit.mapper.JsonMapper;

import cn.hutool.core.util.StrUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 审计模型控制器
 * <p>提供审计模型的创建、配置、数据源管理、Excel模板管理等接口</p>
 *
 * @author hbyun
 */
@RestController
@Tag(name="审计-审计模型接口",description="审计-审计模型接口")
@Slf4j
public class AuditModelController {

	@Resource
	private AuditModelService auditModelService;
	@Resource
	private TblAttachmentService tblAttachmentService;
	@Resource
	private TblAuditModelExcelOracleService tblAuditModelExcelOracleService;

	private SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(5, 5);
	
	@Resource
    private UserProvider userProvider;
	
	
	@Resource(name = "synExecutor")
	private ThreadPoolTaskExecutor threadPoolExecutor;

	@OperationLog(
			success = "审计模型-审计模型数据源管理接口",
			busType = "智能审计—审计模型",
			fail = "数据源管理 列表查询失败",
			operationType = OperationType.SELECT,
			subType = "审计模型—数据源管理，查询数据源列表"
	)
	@Operation(summary = "数据源管理 列表查询")
	@PostMapping("/model/data/source/getList")
	public JsonBean getTblAuditModelDataSourceList(@RequestHeader("token") String token,
			@RequestBody @Validated TblAuditModelDataSourceQueryParam param) {
		JsonBean jsonBean = null;
		try {
			TblStaffUtil staff = userProvider.get();
			if (staff == null) {
				throw new ServiceException(401, 20006);
			}
			UserInfoParam userInfo = TokenUtil.getUserInfo(staff);
			BeanUtils.copyProperties(userInfo, param);
			jsonBean = auditModelService.getTblAuditModelDataSourceList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("数据源管理 列表查询 ...接口 异常", e);
		}
		return jsonBean;
	}

	@OperationLog(
			success = "审计模型-审计模型数据源管理 新增/修改",
			busType = "智能审计—审计模型",
			fail = "数据源管理，新增/修改失败",
			operationType = OperationType.UPDATE,
			subType = "审计模型-审计模型数据源管理"
	)
	@Operation(summary = "数据源管理 新增/更新")
	@PostMapping("/model/data/source/saveOrUpdate")
	public JsonBean saveOrUpdateTblAuditModelDataSource(@RequestHeader("token") String token,
			@RequestBody @Validated TblAuditModelDataSourceOracle param) {
		JsonBean jsonBean = null;
		try {
			TblStaffUtil staff = userProvider.get();
			if (staff == null) {
				throw new ServiceException(401, 20006);
			}
			UserInfoParam userInfo = TokenUtil.getUserInfo(staff);
			BeanUtils.copyProperties(userInfo, param);
			jsonBean = auditModelService.saveOrUpdateTblAuditModelDataSource(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("数据源管理 新增/更新 ...接口 异常", e);
		}
		return jsonBean;
	}
	@OperationLog(
			success = "删除操作——对数据源进删除",
			busType = "智能审计",
			fail = "删除操作",
			operationType = OperationType.DELETE,
			subType = "审计模型删除"
	)
	@Operation(summary = "数据源管理 刪除")
	@DeleteMapping("/model/data/source/{id}")
	public JsonBean deleteTblAuditModelDataSource(@RequestHeader("token") String token, @PathVariable BigDecimal id) {
		JsonBean jsonBean = null;
		try {
			TblStaffUtil staff = userProvider.get();
			if (staff == null) {
				throw new ServiceException(401, 20006);
			}
			UserInfoParam userInfo = TokenUtil.getUserInfo(staff);
			jsonBean = auditModelService.deleteTblAuditModelDataSource(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("数据源管理 刪除 ...接口 异常", e);
		}
		return jsonBean;
	}

	@OperationLog(
			success = "查询操作——对数据源列表进行查询",
			busType = "智能审计",
			fail = "查询操作",
			operationType = OperationType.SELECT,
			subType = "审计模型—数据源详情查询"
	)
	@Operation(summary = "数据源管理 查询")
	@GetMapping("/model/data/source/{id}")
	public JsonBean getTblAuditModelDataSource(@RequestHeader("token") String token, @PathVariable BigDecimal id) {
		JsonBean jsonBean = null;
		try {
			TblStaffUtil staff = userProvider.get();
			if (staff == null) {
				throw new ServiceException(401, 20006);
			}
			UserInfoParam userInfo = TokenUtil.getUserInfo(staff);
			jsonBean = auditModelService.getTblAuditModelDataSource(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("数据源管理 查询  ...接口 异常", e);
		}
		return jsonBean;
	}
	@OperationLog(
			success = "excel导入记录 列表查询",
			busType = "智能审计",
			fail = "查询操作",
			operationType = OperationType.SELECT,
			subType = "审计模型——excel导入记录 列表查询"
	)
	@Operation(summary = "excel导入记录 列表查询")
	@PostMapping("/model/excel/getList")
	public JsonBean getTblAuditModelExcelList(@RequestHeader("token") String token, @RequestBody TblAuditModelExcelQueryParam param) {
		JsonBean jsonBean = null;
		try {
			TblStaffUtil staff = userProvider.get();
			if (staff == null) {
				throw new ServiceException(401, 20006);
			}
			UserInfoParam userInfo = TokenUtil.getUserInfo(staff);
			BeanUtils.copyProperties(userInfo, param);
			jsonBean = auditModelService.getTblAuditModelExcelList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("excel导入记录 列表查询 ...接口 异常", e);
		}
		return jsonBean;
	}

	@OperationLog(
			success = "新增/更新成功",
			busType = "智能审计",
			fail = "新增/更新失败",
			operationType = OperationType.UPDATE,
			subType = "审计模型——excel导入"
	)
	@Operation(summary = "excel导入记录 新增/更新")
	@PostMapping("/model/excel/saveOrUpdate")
	public JsonBean saveOrUpdateTblAuditModelExcel(@RequestHeader("token") String token, @RequestBody @Validated TblAuditModelExcelOracle param) {
		JsonBean jsonBean = null;
		try {
			TblStaffUtil staff = userProvider.get();
			if (staff == null) {
				throw new ServiceException(401, 20006);
			}
			UserInfoParam userInfo = TokenUtil.getUserInfo(staff);
			BeanUtils.copyProperties(userInfo, param);
			jsonBean = auditModelService.saveOrUpdateTblAuditModelExcel(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("excel导入记录 新增/更新 ...接口 异常", e);
		}
		return jsonBean;
	}

	@OperationLog(
			success = "删除成功",
			busType = "智能审计",
			fail = "删除失败",
			operationType = OperationType.DELETE,
			subType = "审计模型——excel导入记录 刪除"
	)
	@Operation(summary = "excel导入记录 刪除")
	@DeleteMapping("/model/excel/{id}")
	public JsonBean deleteTblAuditModelExcel(@RequestHeader("token") String token, @PathVariable BigDecimal id) {
		JsonBean jsonBean = null;
		try {
			TblStaffUtil staff = userProvider.get();
			if (staff == null) {
				throw new ServiceException(401, 20006);
			}
			UserInfoParam userInfo = TokenUtil.getUserInfo(staff);
			jsonBean = auditModelService.deleteTblAuditModelExcel(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("excel导入记录 刪除 ...接口 异常", e);
		}
		return jsonBean;
	}
	@OperationLog(
			success = "查询操作——对excel导入记录查询",
			busType = "智能审计",
			fail = "查询操作",
			operationType = OperationType.SELECT,
			subType = "审计模型——excel导入记录详情查询"
	)
	@Operation(summary = "excel导入记录 查询")
	@GetMapping("/model/excel/{id}")
	public JsonBean getTblAuditModelExcel(@RequestHeader("token") String token, @PathVariable Long id) {
		JsonBean jsonBean = null;
		try {
			TblStaffUtil staff = userProvider.get();
			if (staff == null) {
				throw new ServiceException(401, 20006);
			}
			UserInfoParam userInfo = TokenUtil.getUserInfo(staff);
			jsonBean = auditModelService.getTblAuditModelExcel(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("excel导入记录 查询  ...接口 异常", e);
		}
		return jsonBean;
	}

	//	@Operation(summary = "excel导入记录 上传文件解析接口 id为上传的excelId")
	//	@GetMapping("/model/excel/analysis/{id}")
	//	public JsonBean getTblAuditModelExcelAnalysis(@RequestHeader("token") String token, @PathVariable Integer id) {
	//		JsonBean jsonBean = null;
	//		try {
	//			TokenUtil.getUserInfo(token);
	//			jsonBean = auditModelService.getTblAuditModelExcelAnalysis(id);
	//		} catch (ServiceException ex) {
	//			throw ex;
	//		} catch (Exception e) {
	//			log.error("excel导入记录 上传文件解析接口  ...接口 异常", e);
	//		}
	//		return jsonBean;
	//	}

	@OperationLog(
			success = "列表查询",
			busType = "智能审计",
			fail = "查询操作",
			operationType = OperationType.SELECT,
			subType = "审计模型——excel表数据 列表查询"
	)
	@Operation(summary = "excel表数据 列表查询")
	@PostMapping("/model/excel/ext/getList")
	public JsonBean getTblAuditModelExcelExtList(@RequestHeader("token") String token,
			@RequestBody @Validated TblAuditModelExcelExtQueryParam param) {
		JsonBean jsonBean = null;
		try {
			TblStaffUtil staff = userProvider.get();
			if (staff == null) {
				throw new ServiceException(401, 20006);
			}
			UserInfoParam userInfo = TokenUtil.getUserInfo(staff);
			BeanUtils.copyProperties(userInfo, param);
			jsonBean = auditModelService.getTblAuditModelExcelExtList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("excel表数据 列表查询 ...接口 异常", e);
		}
		return jsonBean;
	}

	//	@Operation(summary = "excel表数据 新增/更新")
	//	@PostMapping("/model/excel/ext/saveOrUpdate")
	//	public JsonBean saveOrUpdateTblAuditModelExcelExt(@RequestHeader("token") String token,
	//			@RequestBody @Validated TblAuditModelExcelExtOracle param) {
	//		JsonBean jsonBean = null;
	//		try {
	//			UserInfoParam userInfo = TokenUtil.getUserInfo(token);
	//			BeanUtils.copyProperties(userInfo, param);
	//			jsonBean = auditModelService.saveOrUpdateTblAuditModelExcelExt(param);
	//		} catch (ServiceException ex) {
	//			throw ex;
	//		} catch (Exception e) {
	//			log.error("数据源管理 新增/更新 ...接口 异常", e);
	//		}
	//		return jsonBean;
	//	}

	//	@Operation(summary = "excel表数据 刪除")
	//	@DeleteMapping("/model/excel/ext/{id}")
	//	public JsonBean deleteTblAuditModelExcelExt(@RequestHeader("token") String token, @PathVariable Integer id) {
	//		JsonBean jsonBean = null;
	//		try {
	//			TokenUtil.getUserInfo(token);
	//			jsonBean = auditModelService.deleteTblAuditModelExcelExt(id);
	//		} catch (ServiceException ex) {
	//			throw ex;
	//		} catch (Exception e) {
	//			log.error("excel表数据 刪除 ...接口 异常", e);
	//		}
	//		return jsonBean;
	//	}

	//	@Operation(summary = "excel表数据 查询")
	//	@GetMapping("/model/excel/ext/{id}")
	//	public JsonBean getTblAuditModelExcelExt(@RequestHeader("token") String token, @PathVariable Integer id) {
	//		JsonBean jsonBean = null;
	//		try {
	//			TokenUtil.getUserInfo(token);
	//			jsonBean = auditModelService.getTblAuditModelExcelExt(id);
	//		} catch (ServiceException ex) {
	//			throw ex;
	//		} catch (Exception e) {
	//			log.error("excel表数据 查询  ...接口 异常", e);
	//		}
	//		return jsonBean;
	//	}
	@OperationLog(
			success = "列表查询",
			busType = "智能审计",
			fail = "查询操作",
			operationType = OperationType.SELECT,
			subType = "审计模型——excel分析预览表数据 列表查询"
	)
	@Operation(summary = "excel分析预览表数据 列表查询")
	@PostMapping("/model/excel/table/getList")
	public JsonBean getTblAuditModelExcelTableList(@RequestHeader("token") String token, @RequestBody TblAuditModelExcelTableQueryParam param) {
		JsonBean jsonBean = null;
		try {
			TblStaffUtil staff = userProvider.get();
			if (staff == null) {
				throw new ServiceException(401, 20006);
			}
			UserInfoParam userInfo = TokenUtil.getUserInfo(staff);
			BeanUtils.copyProperties(userInfo, param);
			jsonBean = auditModelService.getTblAuditModelExcelTableList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("excel分析预览表数据 列表查询 ...接口 异常", e);
		}
		return jsonBean;
	}
	@OperationLog(
			success = "列表查询",
			busType = "智能审计",
			fail = "查询操作",
			operationType = OperationType.UPDATE,
			subType = "审计模型——excel分析预览表数据 列表查询"
	)
	@Operation(summary = "excel分析预览表数据 新增/更新")
	@PostMapping("/model/excel/table/saveOrUpdate")
	public JsonBean saveOrUpdateTblAuditModelExcelTable(@RequestHeader("token") String token,
			@RequestBody List<TblAuditModelExcelTableOracle> param) {
		JsonBean jsonBean = null;
		try {
			TblStaffUtil staff = userProvider.get();
			if (staff == null) {
				throw new ServiceException(401, 20006);
			}
			UserInfoParam userInfo = TokenUtil.getUserInfo(staff);
			param.forEach(x -> BeanUtils.copyProperties(userInfo, x));
			jsonBean = auditModelService.saveOrUpdateTblAuditModelExcelTable(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("excel分析预览表数据 新增/更新 ...接口 异常", e);
		}
		return jsonBean;
	}

	//	@Operation(summary = "excel分析预览表数据 刪除")
	//	@DeleteMapping("/model/excel/table/{id}")
	//	public JsonBean deleteTblAuditModelExcelTable(@RequestHeader("token") String token, @PathVariable Integer id) {
	//		JsonBean jsonBean = null;
	//		try {
	//			TokenUtil.getUserInfo(token);
	//			jsonBean = auditModelService.deleteTblAuditModelExcelTable(id);
	//		} catch (ServiceException ex) {
	//			throw ex;
	//		} catch (Exception e) {
	//			log.error("excel分析预览表数据 刪除 ...接口 异常", e);
	//		}
	//		return jsonBean;
	//	}

	//	@Operation(summary = "excel分析预览表数据 查询")
	//	@GetMapping("/model/excel/table/{id}")
	//	public JsonBean getTblAuditModelExcelTable(@RequestHeader("token") String token, @PathVariable Integer id) {
	//		JsonBean jsonBean = null;
	//		try {
	//			TokenUtil.getUserInfo(token);
	//			jsonBean = auditModelService.getTblAuditModelExcelTable(id);
	//		} catch (ServiceException ex) {
	//			throw ex;
	//		} catch (Exception e) {
	//			log.error("excel分析预览表数据 查询  ...接口 异常", e);
	//		}
	//		return jsonBean;
	//	}
	@OperationLog(
			success = "副本校验",
			busType = "智能审计",
			fail = "副本校验",
			operationType = OperationType.SELECT,
			subType = "审计模型——excel工作副本名校验"
	)
	@PostMapping("/model/excel/check")
	@Operation(summary = "excel工作副本名校验")
	public JsonBean excelCheck(@RequestHeader("token") String token, @RequestBody @Validated ExcelCheckParam param) {
		JsonBean jsonBean = null;
		try {
			TblStaffUtil staff = userProvider.get();
			if (staff == null) {
				throw new ServiceException(401, 20006);
			}
			UserInfoParam userInfo = TokenUtil.getUserInfo(staff);
			jsonBean = auditModelService.excelCheck(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("excel工作副本名校验 ...接口 异常", e);
		}
		return jsonBean;
	}

	@OperationLog(
			success = "文件上传",
			busType = "智能审计",
			fail = "文件上传",
			operationType = OperationType.SELECT,
			subType = "审计模型-excel导入记录 上传文件解析"
	)
	@PostMapping("/model/excel/analysis/upload/{id}")
	@Operation(summary = "审计模型-excel导入记录 上传文件解析接口 id为主键id")
	public JsonBean fileUpload(MultipartFile[] file, @RequestHeader("token") String token, @PathVariable BigDecimal id) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			throw new ServiceException(401, 20006);
		}
		UserInfoParam userInfo = TokenUtil.getUserInfo(staff);
		String attPath = "";
		TblAttachment tblAttachmentEntity = new TblAttachment();
		for (MultipartFile multipartFile : file) {
			String originalFilename = multipartFile.getOriginalFilename();
			String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
			if (!ExcelUtil.checkExtension(extension)) {
				throw new ServiceException(400, "上传文件不是excel文件");
			}
			try {
				InputStream inputStream = multipartFile.getInputStream();
				long imageName = snowflakeIdWorker.nextId(); //雪花算法生成图片名称
				String fileName = new String(multipartFile.getOriginalFilename().getBytes()); //重新编码
				String name = fileName.substring(fileName.lastIndexOf("."), fileName.length());
				attPath = FtpUtil.uploadFilePath(imageName + name, inputStream);
				if (StrUtil.isEmpty(attPath)) {
					throw new ServiceException(400, "文件上传失败");
				}
				tblAttachmentEntity.setAttpath(imageName + name);
				tblAttachmentEntity.setAttsize(multipartFile.getSize() / 1024);
				tblAttachmentEntity.setUploadtime(new Date());
				tblAttachmentEntity.setUploader(userInfo.getCreatorName());
				tblAttachmentEntity.setAttname(fileName);
				tblAttachmentEntity.setAttid(RandomUtil.uuBigDecimalId());
				tblAttachmentService.saveEntity(tblAttachmentEntity);
			} catch (Exception e) {
				throw new ServiceException(400, "上传失败");
			}
			//新增导入记录
			TblAuditModelExcelOracle model = new TblAuditModelExcelOracle();
			BeanUtils.copyProperties(userInfo, model);
			model.setDataBaseId(id);
			model.setExcelId(tblAttachmentEntity.getAttid());
			TblAuditModelExcelOracle auditModelExcelOracle = tblAuditModelExcelOracleService.saveOrUpdate(model);
			//线程处理excel文件
//			threadPoolExecutor.execute(() -> auditModelService.fileUpload(multipartFile, auditModelExcelOracle.getId(), userInfo));
			auditModelService.fileUpload(multipartFile, auditModelExcelOracle.getId(), userInfo);
		}
		return ResponseFormat.retParam(200, 200, tblAttachmentEntity);
	}
	@OperationLog(
			success = "excel分析预览表数据-生成表",
			busType = "智能审计",
			fail = "excel分析预览表数据-生成表，失败",
			operationType = OperationType.SELECT,
			subType = "审计模型——excel分析预览表数据-生成表"
	)
	@Operation(summary = "excel分析预览表数据-生成表")
	@PostMapping("/model/excel/table/generatingTable")
	public JsonBean generatingTable(@RequestHeader("token") String token, @RequestBody @Validated GeneratingTableParam param) {
		JsonBean jsonBean = null;
		try {
			TblStaffUtil staff = userProvider.get();
			if (staff == null) {
				throw new ServiceException(401, 20006);
			}
			UserInfoParam userInfo = TokenUtil.getUserInfo(staff);
			jsonBean = auditModelService.generatingTable(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("excel分析预览表数据-生成表 ...接口 异常", e);
		}
		return jsonBean;
	}
	@OperationLog(
			success = "审计模型测试",
			busType = "智能审计",
			fail = "测试操作",
			operationType = OperationType.SELECT,
			subType = "审计模型-测试"
	)
	@PostMapping("/model/test/upload/{id}")
	@Operation(summary = "审计模型-测试")
	public JsonBean fileUpload(MultipartFile[] file) throws Exception {
		for (MultipartFile multipartFile : file) {
			InputStream inputStream = multipartFile.getInputStream();
			Workbook workbook = new XSSFWorkbook(inputStream);
			//工作副本数
			int numberOfSheets = workbook.getNumberOfSheets();
			for (int i = 0; i < numberOfSheets; i++) {
				//获取工作副本
				Sheet sheetAt = workbook.getSheetAt(i);
				//获取工作副本名称
				String sheetName = sheetAt.getSheetName().toUpperCase();
				if (!sheetName.matches("[_a-zA-Z]+")) {
					throw new ServiceException(400, "工作副本名称仅支持英文以及下划线");
				}
				//获取工作副本第一行数据
				LinkedHashSet<String> result = new LinkedHashSet<>();
				Row row = sheetAt.getRow(0);
				//判断工作副本是否为空 空着不处理
				if (ExcelUtil.isBlankRow(row)) {
					continue;
				}
				for (int j = 0; j < row.getLastCellNum(); j++) {
					String cellData = ExcelUtil.getCellValue(row.getCell(j));
					result.add(cellData.replaceAll(" ", ""));
				}
				//获取工作副本第二行后面的数据  获取物理行数
				int physicalNumberOfRows = sheetAt.getPhysicalNumberOfRows();
				log.info("===================physicalNumberOfRows:{}", physicalNumberOfRows);
				for (int j = 1; j < physicalNumberOfRows; j++) {
					Row lieRow = sheetAt.getRow(j);
					LinkedHashSet<String> lieRowResult = new LinkedHashSet<>();
					for (int jj = 0; jj < lieRow.getLastCellNum(); jj++) {
						String cellData = ExcelUtil.getCellValue(lieRow.getCell(jj));
						lieRowResult.add(cellData.replaceAll(" ", ""));
					}
					String a = JsonMapper.INSTANCE.toJson(result);
					String b = JsonMapper.INSTANCE.toJson(lieRowResult);
					log.info("j:{}===============result:{}", j, a);
					log.info("j:{}===============lieRowResult:{}", j, b); 

					JsonMapper jsonMapper = JsonMapper.INSTANCE;
					List<String> aList = jsonMapper.fromJson(a, jsonMapper.buildCollectionType(List.class, String.class));
					List<String> bList = jsonMapper.fromJson(b, jsonMapper.buildCollectionType(List.class, String.class));
					log.info("==========aList:{}", aList);
					log.info("==========bList:{}", bList);
					/**
					 * insert into 表名（列名1,列名2,列名3.....）values(值1,值2,值3.....);
					 */
					StringBuffer stringBuffer = new StringBuffer();
					String table = "";
					stringBuffer.append("insert into " + table + "(" + StringUtils.join(aList,",") +")");
					stringBuffer.append(" values");
					stringBuffer.append(" ("+ StringUtils.join(bList,",") +")");
					log.info("==========stringBuffer:{}", stringBuffer.toString());
				}
			}
		}
		return ResponseFormat.retParam(200, 200, null);
	}
}
