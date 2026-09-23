package com.huabo.audit.service.business.impl;

import com.github.pagehelper.PageInfo;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.audit.config.DateBaseConfig;
import com.huabo.audit.constant.YesNo;
import com.huabo.audit.exception.ServiceException;
import com.huabo.audit.oracle.entity.*;
import com.huabo.audit.oracle.service.*;
import com.huabo.audit.service.TblAttachmentService;
import com.huabo.audit.service.business.AuditModelService;
import com.huabo.audit.util.ExcelUtil;
import com.huabo.audit.util.OracleSqlProperties;
import com.huabo.audit.util.PageResult;
import com.huabo.audit.vo.param.*;
import com.huabo.audit.vo.result.UserInfoParam;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import com.vip.vjtools.vjkit.mapper.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AuditModelServiceImpl implements AuditModelService {

	@Resource
	private TblAuditModelDataSourceOracleService tblAuditModelDataSourceOracleService;
	@Resource
	private TblAuditModelExcelOracleService tblAuditModelExcelOracleService;
	@Resource
	private TblAuditModelExcelExtOracleService tblAuditModelExcelExtOracleService;
	@Resource
	private TblAuditModelExcelTableOracleService tblAuditModelExcelTableOracleService;
	@Resource
	private TblAuditModelExcelContentOracleService tblAuditModelExcelContentOracleService;
	@Resource
	private TblAttachmentService tblAttachmentService;

	@Resource
	private OracleSqlProperties oracleSqlProperties;

	/**
	 * 数据源管理 列表查询
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean getTblAuditModelDataSourceList(TblAuditModelDataSourceQueryParam param) {

		if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
			PageInfo<TblAuditModelDataSourceOracle> pageInfo = tblAuditModelDataSourceOracleService.getList(param);
			if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
				PageResult<TblAuditModelDataSourceOracle> build = new PageResult<TblAuditModelDataSourceOracle>().build(pageInfo);
				return ResponseFormat.retParam(200, 200, build);
			}
		} else {

		}
		return ResponseFormat.retParam(200, 200, PageResult.buildNoData());

	}

	/**
	 * 数据源管理 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public JsonBean saveOrUpdateTblAuditModelDataSource(TblAuditModelDataSourceOracle param) throws Exception {

		if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
			BigDecimal id = param.getId();
			TblAuditModelDataSourceOracle model = tblAuditModelDataSourceOracleService.saveOrUpdate(param);
			if (id == null && model.getCreateType() == 2) {
				//查询用户名是否在数据库已存在
				String getUserSql = "select * from all_users where username='" + model.getDataBaseUsers() + "'";
				JsonBean jsonBean = oracleSqlProperties.execSql(getUserSql);
				List<Map<String, Integer>> data = (List<Map<String, Integer>>) jsonBean.getData();
				if (CollectionUtil.isNotEmpty(data)) {
					throw new ServiceException(200, "数据库用户已存在数据库中");
				}
				// 创建表用户
				String userSql = "CREATE USER " + model.getDataBaseUsers() + " IDENTIFIED BY " + "\"" + model.getDataBasePassWord() + "\"";
				oracleSqlProperties.execSql(userSql);
				//赋予ABC用户创建新表的权限
				String createSql = "GRANT CREATE TABLE TO " + model.getDataBaseUsers();
				oracleSqlProperties.execSql(createSql);
				//给ABC用户赋予权限
				String sessionSql = "GRANT create session to " + model.getDataBaseUsers();
				oracleSqlProperties.execSql(sessionSql);
				//给ABC用户表空间授权 grant connect,resource,dba to crs;
				String sql = "grant connect,resource to " + model.getDataBaseUsers();
				oracleSqlProperties.execSql(sql);
			}
			return ResponseFormat.retParam(200, 200, model);
		} else {

		}
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 数据源管理 刪除
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean deleteTblAuditModelDataSource(BigDecimal id) {

		if (DateBaseConfig.DATABASETYPE.equals("Oracle"))
			tblAuditModelDataSourceOracleService.delete(id);
		else {

		}
		return ResponseFormat.retParam(200, 200, null);

	}

	/**
	 * 数据源管理 查询
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean getTblAuditModelDataSource(BigDecimal id) {

		if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
			TblAuditModelDataSourceOracle auditModelDataSource = tblAuditModelDataSourceOracleService.findById(id);
			return ResponseFormat.retParam(200, 200, auditModelDataSource);
		} else {

		}
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * excel导入记录 列表查询
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean getTblAuditModelExcelList(TblAuditModelExcelQueryParam param) {
		if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
			PageInfo<TblAuditModelExcelOracle> pageInfo = tblAuditModelExcelOracleService.getList(param);
			if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
				//获取数据库数据map
				Map<BigDecimal, TblAuditModelDataSourceOracle> allMap = tblAuditModelDataSourceOracleService.getAllMap();
				//组装数据
				pageInfo.getList().forEach(x -> {
					if (x.getExcelId() != null) {
						try {
							TblAttachment tblAttachment = tblAttachmentService.selectEntityById(x.getExcelId().toString());
							x.setExcelName(tblAttachment.getAttname());
						} catch (Exception e) {
							log.error("异常：", e);
						}
					}
					if (allMap.containsKey(x.getDataBaseId())) {
						TblAuditModelDataSourceOracle auditModelDataSource = allMap.get(x.getDataBaseId());
						x.setDataBaseConnectionAddress(auditModelDataSource.getDataBaseConnectionAddress());
						x.setDataBaseUsers(auditModelDataSource.getDataBaseUsers());
					}
				});
				PageResult<TblAuditModelExcelOracle> build = new PageResult<TblAuditModelExcelOracle>().build(pageInfo);
				return ResponseFormat.retParam(200, 200, build);
			}
		} else {

		}
		return ResponseFormat.retParam(200, 200, PageResult.buildNoData());
	}

	/**
	 * excel导入记录 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean saveOrUpdateTblAuditModelExcel(TblAuditModelExcelOracle param) {

		if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
			TblAuditModelExcelOracle auditModelDataSource = tblAuditModelExcelOracleService.saveOrUpdate(param);
			return ResponseFormat.retParam(200, 200, auditModelDataSource);
		} else {

		}
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * excel导入记录 刪除
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean deleteTblAuditModelExcel(BigDecimal id) {

		if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
			tblAuditModelExcelOracleService.delete(id);
		} else {

		}
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * excel导入记录 查询
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean getTblAuditModelExcel(BigDecimal id) throws Exception {

		HashMap<String, Object> map = new HashMap<>();
		if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
			TblAuditModelExcelOracle auditModelExcel = tblAuditModelExcelOracleService.findById(id);
			if (auditModelExcel.getDataBaseId() != null) {
				//获取数据源数据
				TblAuditModelDataSourceOracle auditModelDataSource = tblAuditModelDataSourceOracleService.findById(auditModelExcel.getDataBaseId());
				if (auditModelDataSource != null) {
					auditModelExcel.setDataBaseConnectionAddress(auditModelDataSource.getDataBaseConnectionAddress());
					auditModelExcel.setDataBaseUsers(auditModelDataSource.getDataBaseUsers());
				}
			}
			if (auditModelExcel.getExcelId() != null) {
				TblAttachment tblAttachmentEntity = tblAttachmentService.selectEntityById(String.valueOf(auditModelExcel.getExcelId()));
				map.put("tblAttachmentEntity", tblAttachmentEntity);
			}
			map.put("auditModelExcel", auditModelExcel);
			return ResponseFormat.retParam(200, 200, map);
		} else {

		}
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * excel表数据 列表查询
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean getTblAuditModelExcelExtList(TblAuditModelExcelExtQueryParam param) {

		if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
			PageInfo<TblAuditModelExcelExtOracle> pageInfo = tblAuditModelExcelExtOracleService.getList(param);
			if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
				PageResult<TblAuditModelExcelExtOracle> build = new PageResult<TblAuditModelExcelExtOracle>().build(pageInfo);
				return ResponseFormat.retParam(200, 200, build);
			}
		} else {

		}
		return ResponseFormat.retParam(200, 200, PageResult.buildNoData());
	}

	/**
	 * excel表数据 刪除
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean deleteTblAuditModelExcelExt(BigDecimal id) {

		if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
			tblAuditModelExcelExtOracleService.delete(id);
		} else {

		}
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * excel表数据 查询
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean getTblAuditModelExcelExt(BigDecimal id) {

		if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
			TblAuditModelExcelExtOracle auditModelExcelExt = tblAuditModelExcelExtOracleService.findById(id);
			return ResponseFormat.retParam(200, 200, auditModelExcelExt);
		} else {

		}
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * excel分析预览表数据 列表查询
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean getTblAuditModelExcelTableList(TblAuditModelExcelTableQueryParam param) {

		if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
			PageInfo<TblAuditModelExcelTableOracle> pageInfo = tblAuditModelExcelTableOracleService.getList(param);
			if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
				PageResult<TblAuditModelExcelTableOracle> build = new PageResult<TblAuditModelExcelTableOracle>().build(pageInfo);
				return ResponseFormat.retParam(200, 200, build);
			}
		} else {

		}
		return ResponseFormat.retParam(200, 200, PageResult.buildNoData());
	}

	/**
	 * excel分析预览表数据 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean saveOrUpdateTblAuditModelExcelTable(List<TblAuditModelExcelTableOracle> param) {

		if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
			tblAuditModelExcelTableOracleService.saveOrUpdates(param);
		} else {

		}
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * excel分析预览表数据 刪除
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean deleteTblAuditModelExcelTable(BigDecimal id) {

		if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
			tblAuditModelExcelTableOracleService.delete(id);
		} else {

		}
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * excel分析预览表数据 查询
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean getTblAuditModelExcelTable(BigDecimal id) {

		if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
			TblAuditModelExcelTableOracle auditModelExcelTable = tblAuditModelExcelTableOracleService.findById(id);
			return ResponseFormat.retParam(200, 200, auditModelExcelTable);
		} else {

		}
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * excel文件解析
	 * @param multipartFile
	 * @param id
	 * @param userInfo
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public JsonBean fileUpload(MultipartFile multipartFile, BigDecimal id, UserInfoParam userInfo) {

		if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
			boolean flag = false;
			String errorMsg = null;
			try {
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
					//循环插入数据
					int subscript = 0;
					for (String name : result) {
						//新增表基础数据
						TblAuditModelExcelExtOracle model = new TblAuditModelExcelExtOracle();
						model.setColumnName(name);
						model.setExcelId(id);
						model.setTableNameEn(sheetName);
						BeanUtils.copyProperties(userInfo, model);
						tblAuditModelExcelExtOracleService.saveOrUpdate(model);
						//新增表字段基础数据
						TblAuditModelExcelTableOracle auditModelExcelTableOracle = new TblAuditModelExcelTableOracle();
						auditModelExcelTableOracle.setSubscript(subscript);
						auditModelExcelTableOracle.setColumnName(name);
						auditModelExcelTableOracle.setExcelId(id);
						auditModelExcelTableOracle.setIsSelected(YesNo.NO);
						auditModelExcelTableOracle.setTableNameEn(sheetName);
						BeanUtils.copyProperties(userInfo, auditModelExcelTableOracle);
						tblAuditModelExcelTableOracleService.saveOrUpdate(auditModelExcelTableOracle);
						subscript++;
					}
					String meterHeader = JsonMapper.INSTANCE.toJson(result);
					//获取工作副本第二行后面的数据  获取物理行数
					int physicalNumberOfRows = sheetAt.getPhysicalNumberOfRows();
					for (int j = 1; j < physicalNumberOfRows; j++) {
						Row lieRow = sheetAt.getRow(j);
						LinkedHashSet<String> lieRowResult = new LinkedHashSet<>();
						for (int jj = 0; jj < lieRow.getLastCellNum(); jj++) {
							String cellData = ExcelUtil.getCellValue(lieRow.getCell(jj));
							lieRowResult.add(cellData.replaceAll(" ", ""));
						}
						String lieRows = JsonMapper.INSTANCE.toJson(lieRowResult);
						//excel数据保存数据库结构为 "["aaa":"111","bbb":"222"]"
						TblAuditModelExcelContentOracle model = new TblAuditModelExcelContentOracle();
						BeanUtils.copyProperties(userInfo, model);
						model.setTableNameEn(sheetName);
						model.setExcelId(id);
						model.setTableList(meterHeader);
						model.setTableValueList(lieRows);
						tblAuditModelExcelContentOracleService.saveOrUpdate(model);
					}
				}
				flag = true;
			} catch (ServiceException ex) {
				errorMsg = ex.getMsg();
			} catch (Exception e) {
				errorMsg = e.getMessage();
				log.error("excel文件解析异常：", e);
			} finally {
				//更新导入的excel状态
				int state;
				if (flag) { //解析成功
					state = 2;
				} else { //解析失败
					state = 3;
				}
				saveOrUpdateTblAuditModelExcel(TblAuditModelExcelOracle.ofState(id, state, errorMsg));
			}
		} else {

		}
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * excel分析预览表数据-生成表
	 * @param param
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public JsonBean generatingTable(GeneratingTableParam param) {

		if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
			//获取所需要生成表的基础数据
			List<TblAuditModelExcelTableOracle> list = tblAuditModelExcelTableOracleService.getGeneratingTable(param);
			if (CollectionUtil.isNotEmpty(list)) {
				//map key-columnName value-columnType
				Map<String, String> columnMap = list.stream()
						.collect(Collectors.toMap(TblAuditModelExcelTableOracle::getColumnName, TblAuditModelExcelTableOracle::getColumnType));
				String table = "";
				String passWord = "";
				String username = "";
				BigDecimal excelId = null;
				Optional<TblAuditModelExcelTableOracle> first = list.stream().findFirst();
				if (first.isPresent()) {
					TblAuditModelExcelTableOracle excelTable = first.get();
					excelId = excelTable.getExcelId();
					TblAuditModelDataSourceOracle res = tblAuditModelDataSourceOracleService.getExcelIdDataSource(excelId);
					passWord = res.getDataBasePassWord();
					username = res.getDataBaseUsers();
					table = excelTable.getTableNameEn();
				}
				try {
					//创建表结构
					createTable(list, table, passWord, username);
					//从excel中内容中获取对应的值
					List<TblAuditModelExcelContentOracle> excelContentList = getTblAuditModelExcelContentOracles(table, excelId);
					//过滤下标
					List<Integer> subscript = list.stream().map(TblAuditModelExcelTableOracle::getSubscript).collect(Collectors.toList());
					List<String> columnNameNe = list.stream().sorted(Comparator.comparing(TblAuditModelExcelTableOracle::getSubscript))
							.map(TblAuditModelExcelTableOracle::getColumnNameNe).collect(Collectors.toList());
					List<String> result = new ArrayList<>();
					for (TblAuditModelExcelContentOracle excelContent : excelContentList) {
						//循环拼接插入语句
						loopAssembleInsertSql(columnMap, table, subscript, columnNameNe, result, excelContent);
					}
					//循环插入数据库
					for (String sql : result) {
						oracleSqlProperties.insertSql(username, passWord, sql);
					}
					//生成表成功 更改状态
					tblAuditModelExcelTableOracleService.updateState(param, 1);
					tblAuditModelExcelExtOracleService.updateState(param, 1);
				} catch (Exception e) {
					log.error("excel分析预览表数据-生成表 删除表 异常：", e);
					String sql = "drop table " + table;
					try {
						oracleSqlProperties.execSql(username, passWord, sql);
					} catch (Exception exception) {
						log.error("excel分析预览表数据-生成表 删库sql异常：", e);
					}
					//生成表失败 更改状态
					param.setErrorMsg(e.getMessage());
					tblAuditModelExcelTableOracleService.updateState(param, 2);
					tblAuditModelExcelExtOracleService.updateState(param, 2);
				}
			}
		} else {

		}
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 循环拼接插入语句
	 * @param columnMap
	 * @param table
	 * @param subscript
	 * @param columnNameNe
	 * @param result
	 * @param excelContent
	 * @throws Exception
	 */
	private void loopAssembleInsertSql(Map<String, String> columnMap, String table, List<Integer> subscript, List<String> columnNameNe,
			List<String> result, TblAuditModelExcelContentOracle excelContent) throws Exception {
		String tableList = excelContent.getTableList();
		String tableValueList = excelContent.getTableValueList();
		JsonMapper jsonMapper = JsonMapper.INSTANCE;
		List<String> tableValueLists = jsonMapper.fromJson(tableValueList, jsonMapper.buildCollectionType(List.class, String.class));
		List<String> tableLists = jsonMapper.fromJson(tableList, jsonMapper.buildCollectionType(List.class, String.class));
		//循环只勾选的字段进行数据库表新增 值
		int j = 0;
		List<String> values = new ArrayList<>();
		for (String str : tableValueLists) {
			for (Integer s : subscript) {
				if (Objects.equals(s, j)) {
					//类型替换
					String columnValues = doColumnReplace(tableLists.get(j), str, columnMap);
					values.add(columnValues);
				}
			}
			j++;
		}
		/**
		 * insert into 表名（列名1,列名2,列名3.....）values(值1,值2,值3.....);
		 */
		JsonBean oracleSqlProperties = this.oracleSqlProperties.execSql("select HIBERNATE_SEQUENCE.nextval from dual");
		List<Map<String, Integer>> data = (List<Map<String, Integer>>) oracleSqlProperties.getData();
		StringBuffer insert = new StringBuffer();
		insert.append("insert into " + table + "(ID," + StringUtils.join(columnNameNe, ",") + ")");
		insert.append(" values");
		insert.append(" (" + data.get(0).get("NEXTVAL") + " ," + StringUtils.join(values, ",") + ")");
		result.add(insert.toString());
	}

	/**
	 * 字段类型替换
	 * @param str
	 * @param columnMap
	 */
	private String doColumnReplace(String column, String str, Map<String, String> columnMap) throws Exception {
		String varchar2 = "VARCHAR2(500)";
		String varchar24000 = "VARCHAR2(4000)";
		String number = "NUMBER";
		String date = "DATE";
		String numberMin = "NUMBER(10,2)";
		String clob = "CLOB";
		if (columnMap.containsKey(column)) {
			String columnType = columnMap.get(column);
			//date类型特殊处理
			if (Objects.equals(date, columnType)) {
				str = doDateVail(str);
			}
			if (Objects.equals(varchar2, columnType) || Objects.equals(clob, columnType) || Objects.equals(varchar24000, columnType)) {
				str = "'" + str + "'";
			}
		}
		return str;
	}

	/**
	 * 时间格式处理
	 * @param date
	 * @return
	 */
	private String doDateVail(String date) throws ParseException {
		//用于指定 日期/时间 模式
		/**
		 * TO_DATE('2023-05-14 13:43:52', 'YYYY-MM-DD HH24:MI:SS')
		 */
		try {
			Date date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
			String date2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date1);
			date = "TO_DATE('" + date2 + "','YYYY-MM-DD HH24:MI:SS')";
		} catch (Exception e) {
			// yyyy-MM-dd 转换成 yyyy-MM-dd HH:mm:ss
			Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(date);
			String date2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date1);
			date = "TO_DATE('" + date2 + "','YYYY-MM-DD HH24:MI:SS')";
		}
		return date;
	}

	/**
	 * 从excel中内容中获取对应的值
	 * @param table
	 * @param excelId
	 * @return
	 */
	private List<TblAuditModelExcelContentOracle> getTblAuditModelExcelContentOracles(String table, BigDecimal excelId) {
		TblAuditModelExcelContentQueryParam model = new TblAuditModelExcelContentQueryParam();
		model.setExcelId(excelId);
		model.setTableNameEn(table);
		return tblAuditModelExcelContentOracleService.getList(model);
	}

	/**
	 * 创建表结构
	 * @param list
	 * @param table
	 * @param passWord
	 * @param username
	 * @throws Exception
	 */
	private void createTable(List<TblAuditModelExcelTableOracle> list, String table, String passWord, String username) throws Exception {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("CREATE TABLE " + table + "( ID INT NOT NULL, ");
		//组装创建参数
		for (TblAuditModelExcelTableOracle model : list) {
			stringBuffer.append(model.getColumnNameNe() + " " + model.getColumnType() + ",");
		}
		stringBuffer.append("PRIMARY KEY (ID))");
		log.info("创建表结构 最终sql：{}", stringBuffer.toString());
		oracleSqlProperties.execSql(username, passWord, stringBuffer.toString());
	}

	/**
	 * excel工作副本名校验
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean excelCheck(ExcelCheckParam param) {

		if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
			String msg = "成功";
			if (!param.getTableNameEn().matches("[_a-zA-Z]+")) {
				throw new ServiceException(400, "工作副本名称仅支持英文以及下划线");
			}
			boolean flag = tblAuditModelDataSourceOracleService.excelCheck(param);
			if (Boolean.FALSE.equals(flag)) {
				msg = "表名称已存，请更换另个名字（工作副本名称仅支持英文以及下划线 如：tbl_audit_model_test）";
			}
			return ResponseFormat.retParam(200, 200, msg);
		} else {

		}
		return ResponseFormat.retParam(200, 200, null);
	}
}
