package com.huabo.finance.service.impl;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.entity.FaAccbookinfoUtil;
import com.hbfk.entity.TblAttachment;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.FinancialDataExport;
import com.hbfk.util.ImportOrExportExcelUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.finance.config.SystemStaticConfig;
import com.huabo.finance.entity.BdFinancedate;
import com.huabo.finance.entity.BdFinanceplan;
import com.huabo.finance.entity.TblConfigColumnInfo;
import com.huabo.finance.entity.TblConfigTableInfo;
import com.huabo.finance.mapper.BdFinancedateMapper;
import com.huabo.finance.mapper.BdFinanceplanMapper;
import com.huabo.finance.mapper.TblConfigColumnInfoMapper;
import com.huabo.finance.mapper.TblConfigTableInfoMapper;
import com.huabo.finance.service.TblConfigTableInfoService;
import com.huabo.finance.unit.BaseDao;
import com.huabo.finance.unit.ExcelExporter;
import com.huabo.finance.unit.SqlValidatorUtil;
import com.huabo.finance.vo.BusinessDataVo;
import com.huabo.finance.vo.TblConfigTableInfoVo;
import com.huabo.finance.vr.TblConfigTableInfoVr;

/**
 * <p>
 * 账簿角色授权 服务实现类
 * </p>
 *
 * @author L
 * @since 2025-04-01
 */
@Service
public class TblConfigTableInfoServiceServiceImpl extends ServiceImpl<TblConfigTableInfoMapper, TblConfigTableInfo> implements TblConfigTableInfoService {
	
	@Resource
	private UserProvider userProvider;
	
	@Resource
	private TblConfigTableInfoMapper tblConfigTableInfoMapper;
	
	@Resource
	private TblConfigColumnInfoMapper tblConfigColumnInfoMapper;
	
	@Resource
	private BdFinanceplanMapper bdFinanceplanMapper;
	
	@Resource
	private BdFinancedateMapper bdFinancedateMapper;
	
	@Override
	public JsonBean getTablInfoList(TblConfigTableInfoVo vo) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		
		vo.setOrgId(staff.getCurrentOrg().getOrgid());
		Page<TblConfigTableInfoVr> page = new Page<TblConfigTableInfoVr>(vo.getPageNumber(), vo.getPageSize());
		page.setOptimizeCountSql(false); // 禁用自动优化
	    IPage<TblConfigTableInfoVr> pageList  = this.tblConfigTableInfoMapper.selectPageInfo(page, vo);
		
	    return ResponseFormat.retParam(1, 200, pageList);
	}

	@Override
	public JsonBean addTableInfo(TblConfigTableInfo tableInfo) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		
		String sql ;
		
		if("DM".equals(SystemStaticConfig.dbtype)) {
			sql = "SELECT COUNT(*) FROM ALL_TABLES WHERE TABLE_NAME = '"+tableInfo.getOursTableName().toUpperCase()+"' AND OWNER = '"+SystemStaticConfig.method+"'";
		}else if("Oracle".equals(SystemStaticConfig.dbtype)) {
			sql = "SELECT COUNT(*) FROM ALL_TABLES WHERE TABLE_NAME = '"+tableInfo.getOursTableName().toUpperCase()+"' AND OWNER = '"+SystemStaticConfig.method+"'";
		}else {
			sql = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = '"+SystemStaticConfig.method+"' AND TABLE_NAME = '"+tableInfo.getOursTableName().toUpperCase()+"'";
		}
		
		Integer tableCount = this.tblConfigTableInfoMapper.selectTableCount(sql);
		
		if(tableCount > 0) {
			return ResponseFormat.retParam(0, "表已存在，请更换表名。", null);
		}
		
		tableInfo.setFid(RandomUtil.uuStringId());
		tableInfo.setLinkdeptid(staff.getLinkDetp().getOrgid());
		tableInfo.setLinkOrgId(staff.getCurrentOrg().getOrgid());
		tableInfo.setCreateTime(new Date());
		tableInfo.setCreateUserId(staff.getStaffid());
		tableInfo.setFstatus(0);
		
		this.tblConfigTableInfoMapper.insert(tableInfo);
		
		if(tableInfo.getColList() != null) {
			for (TblConfigColumnInfo col : tableInfo.getColList()) {
				col.setFid(RandomUtil.uuStringId());
				col.setFtableId(tableInfo.getFid());
				this.tblConfigColumnInfoMapper.insert(col);
			}
			
			//保存后创建数据库表结构信息
			//this.createBussinessTableInfo(tableInfo);
		}
		return ResponseFormat.retParam(1, 200, null);
	}

	@Override
	public JsonBean modifyTableInfo(TblConfigTableInfo tableInfo) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		
		tableInfo.setModifyTime(new Date());
		tableInfo.setModifyUserId(staff.getStaffid());
		this.tblConfigTableInfoMapper.updateById(tableInfo);
		String sql ;
		if(tableInfo.getColList() != null) {
			for (TblConfigColumnInfo col : tableInfo.getColList()) {
				if(StringUtils.isNotBlank(col.getFid())) {
					//sql = this.getAlterTableModifyCol(col,tableInfo);
					this.tblConfigColumnInfoMapper.updateById(col);
				}else {
					col.setFid(RandomUtil.uuStringId());
					col.setFtableId(tableInfo.getFid());
					this.tblConfigColumnInfoMapper.insert(col);
					//数据库表增加列
					//sql = this.getAlterTableAddCol(col,tableInfo);
					//this.tblConfigTableInfoMapper.executeSql(sql);
				}
				
			}
		}
		
		return ResponseFormat.retParam(1, 200, null);
	}
	
	@Override
	public JsonBean executeSql(TblConfigTableInfo tableInfo) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		
		String sql = tableInfo.getSqlText();
		
		if(StringUtils.isBlank(sql)) {
			return ResponseFormat.retParam(0, "sql语句为空", null);
		}
		
		sql = sql.trim().toUpperCase();
		
		if(!SqlValidatorUtil.validateSql(sql)) {
			return ResponseFormat.retParam(0, "sql语句参数不符合规范", null);
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		//获取数据源
		BdFinancedate dataConfig = this.bdFinancedateMapper.selectById(tableInfo.getDataConfig());
		try {
			con = BaseDao.getConnection(dataConfig.getFinancedbtype(), dataConfig.getFinanceconn(), dataConfig.getFinanceport(), dataConfig.getFinanceuser(), dataConfig.getFinancepwd(), dataConfig.getFinancedbexpm());
			ps = con.prepareStatement(tableInfo.getSqlText());
			rs = ps.executeQuery();
			
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			
			List<String> columnNameList = new ArrayList<String>(0);
			for (int i = 1 ; i <= columnCount ; i++) {
				columnNameList.add(rsmd.getColumnName(i));
			}
			resultMap.put("colName", columnNameList);
			
			List<Object[]> dataList = new ArrayList<Object[]>(0);
			Object[] objs = null;
			
			while (rs.next()) {
				objs = new Object[columnCount];
				
				for (int i = 0 ; i < columnCount ; i++) {
					objs[i] = rs.getObject(i+1)==null?null:rs.getObject(i+1);
				}
				dataList.add(objs);
			}
			
			resultMap.put("dataList", dataList);
		}finally {
			BaseDao.close(dataConfig.getFinancedbtype(), con, rs, ps);
		}
		return ResponseFormat.retParam(1, 200, resultMap);
	}
	
	@Override
	public JsonBean removeTableInfo(String fid) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		
		TblConfigTableInfo tableInfo = this.tblConfigTableInfoMapper.selectById(fid);
		
		String sql;
		if("DM".equals(SystemStaticConfig.dbtype)) {
			sql = "SELECT COUNT(*) FROM ALL_TABLES WHERE TABLE_NAME = '"+tableInfo.getOursTableName().toUpperCase()+"' AND OWNER = '"+SystemStaticConfig.method+"'";
		}else if("Oracle".equals(SystemStaticConfig.dbtype)) {
			sql = "SELECT COUNT(*) FROM ALL_TABLES WHERE TABLE_NAME = '"+tableInfo.getOursTableName().toUpperCase()+"' AND OWNER = '"+SystemStaticConfig.method+"'";
		}else {
			sql = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = '"+SystemStaticConfig.method+"' AND TABLE_NAME = '"+tableInfo.getOursTableName().toUpperCase()+"'";
		}
		
		Integer tableCount = this.tblConfigTableInfoMapper.selectTableCount(sql);
		
		if(tableCount > 0) {
			sql = "DROP TABLE "+ tableInfo.getOursTableName().toUpperCase();
			this.tblConfigTableInfoMapper.executeSql(sql);
		}
		
		this.tblConfigColumnInfoMapper.deleteByTableId(fid);
		this.tblConfigTableInfoMapper.deleteById(tableInfo.getFid());
		
		return ResponseFormat.retParam(1, 200, null);
	}
	
	@Override
	public JsonBean getTableInfo(String fid) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		
		TblConfigTableInfoVr info = this.tblConfigTableInfoMapper.selectEntityById(fid);
		List<TblConfigColumnInfo> colList = this.tblConfigColumnInfoMapper.selectListByTableId(fid);
		
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		resultMap.put("tableInfo", info);
		resultMap.put("colList", colList);
		
		return ResponseFormat.retParam(1, 200, resultMap);
	}
	
	@Override
	public JsonBean tableCreate(String fid) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		
		TblConfigTableInfo tableInfo = this.tblConfigTableInfoMapper.selectById(fid);
		
		if(tableInfo.getFstatus() == 2) {
			return ResponseFormat.retParam(0, "表单已弃用!", null);
		}
		
		if(StringUtils.isBlank(tableInfo.getDataConfig())){
			return ResponseFormat.retParam(0, "未配置数据源，无法发布", null);
		}
		
		if(StringUtils.isBlank(tableInfo.getOursTableName())){
			return ResponseFormat.retParam(0, "缺少目标端表名配置，无法发布", null);
		}
		
		if(tableInfo.getFinanceType() != 2 && StringUtils.isBlank(tableInfo.getOutsTableName())){
			return ResponseFormat.retParam(0, "缺少源端表名配置，无法发布", null);
		}
		
		String sql;
		if("DM".equals(SystemStaticConfig.dbtype)) {
			sql = "SELECT COUNT(*) FROM ALL_TABLES WHERE TABLE_NAME = '"+tableInfo.getOursTableName().toUpperCase()+"' AND OWNER = '"+SystemStaticConfig.method+"'";
		}else if("Oracle".equals(SystemStaticConfig.dbtype)) {
			sql = "SELECT COUNT(*) FROM ALL_TABLES WHERE TABLE_NAME = '"+tableInfo.getOursTableName().toUpperCase()+"' AND OWNER = '"+SystemStaticConfig.method+"'";
		}else {
			sql = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = '"+SystemStaticConfig.method+"' AND TABLE_NAME = '"+tableInfo.getOursTableName().toUpperCase()+"'";
		}
		Integer tableCount = this.tblConfigTableInfoMapper.selectTableCount(sql);
		
		if(tableCount > 0) {
			return ResponseFormat.retParam(0, "表已存在，请更换表名。", null);
		}
		List<TblConfigColumnInfo> colList = this.tblConfigColumnInfoMapper.selectListByTableId(fid);
		
		tableInfo.setColList(colList);
		
		this.createBussinessTableInfo(tableInfo);
		
		this.tblConfigTableInfoMapper.updateTableStatus(fid,3);
		
		return ResponseFormat.retParam(1, 200, null);
	}
	
	
	
	private String getAlterTableModifyCol(TblConfigColumnInfo col, TblConfigTableInfo tableInfo) throws Exception {
		String sql = "";
		if("DM".equals(SystemStaticConfig.dbtype)) {
			sql = "ALTER TABLE "+tableInfo.getOursTableName().toUpperCase()+" ALTER COLUMN "+col.getOursColname().toUpperCase()+" SET DATA TYPE "+col.getColType()+"("+col.getColLength()+")";
		}else if("Oracle".equals(SystemStaticConfig.dbtype)) {
			sql = "ALTER TABLE "+tableInfo.getOursTableName().toUpperCase()+" MODIFY ( "+col.getOursColname().toUpperCase()+" "+col.getColType()+"("+col.getColLength()+"))";
		}else {
			sql = "ALTER TABLE "+tableInfo.getOursTableName().toUpperCase()+" MODIFY COLUMN "+col.getOursColname().toUpperCase()+" "+col.getColType()+"("+col.getColLength()+")";
		}
		return sql;
	}

	private String getAlterTableAddCol(TblConfigColumnInfo col, TblConfigTableInfo tableInfo) throws Exception {
		String sql = "";
		if("DM".equals(SystemStaticConfig.dbtype)) {
			sql = "ALTER TABLE "+tableInfo.getOursTableName().toUpperCase()+" ADD COLUMN "+col.getOursColname().toUpperCase()+" "+col.getColType()+"("+col.getColLength()+")";
		}else if("Oracle".equals(SystemStaticConfig.dbtype)) {
			sql = "ALTER TABLE "+tableInfo.getOursTableName().toUpperCase()+" ADD ( "+col.getOursColname().toUpperCase()+" "+col.getColType()+"("+col.getColLength()+"))";
		}else {
			sql = "ALTER TABLE "+tableInfo.getOursTableName().toUpperCase()+" ADD COLUMN "+col.getOursColname().toUpperCase()+" "+col.getColType()+"("+col.getColLength()+")";
		}
		return sql;
	}

	private void createBussinessTableInfo(TblConfigTableInfo tableInfo) throws Exception {
		StringBuffer sqlSb = new StringBuffer("CREATE TABLE ").append(tableInfo.getOursTableName().toUpperCase())
			.append("( ");
		for (TblConfigColumnInfo col : tableInfo.getColList()) {
			sqlSb.append(col.getOursColname().toUpperCase()).append(" ").append(col.getColType());
			if(StringUtils.isNotBlank(col.getColLength()) &&  !col.getColType().equals("TEXT") && !col.getColType().equals("CLOB")) {
				sqlSb.append("(").append(col.getColLength()).append(")");
			}
			sqlSb.append(",");
		}
		
		//数据来源 F_DATASOURCETYPE 1-采集  2-导入，F_COLLECTBATCHES 导入记录批次主键
		if("DM".equals(SystemStaticConfig.dbtype) || "Oracle".equals(SystemStaticConfig.dbtype)) {
			sqlSb.append(" F_DATASOURCETYPE NUMBER,F_IMPORTBATCHES VARCHAR2(50) ");
		}else {
			sqlSb.append(" F_DATASOURCETYPE INT,F_IMPORTBATCHES VARCHAR(50) ");
		}
		sqlSb.append(")");
		
		String sql = sqlSb.toString();
		this.tblConfigTableInfoMapper.executeSql(sql);
	}

	@Override
	public JsonBean cwzbshowTableList() throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		
		//获取用户所选择的账簿
		FaAccbookinfoUtil bookInfo = staff.getAccbook();
		if(bookInfo == null) {
			return ResponseFormat.retParam(0, 204, null);
		}
		
		//通过数据采集方案获取所有的业务数据表单
		List<TblConfigTableInfo> tbList = this.tblConfigTableInfoMapper.selectTreeListByPlanId(bookInfo.getPkFinanplanid());
		
		return ResponseFormat.retParam(1, 200, tbList);
	}

	@Override
	public JsonBean cwzbshowBusinessDataList(BusinessDataVo vo) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		
		if(StringUtils.isBlank(vo.getTableId())) {
			return ResponseFormat.retParam(0, 10004, null);
		}
		TblConfigTableInfoVr info = this.tblConfigTableInfoMapper.selectEntityById(vo.getTableId());
		
		List<TblConfigColumnInfo> colList = this.tblConfigColumnInfoMapper.selectListByTableId(vo.getTableId());
		List<TblConfigColumnInfo> pageColList = colList.stream().filter(item -> 0== item.getIsShowList()).collect(Collectors.toList());
		List<TblConfigColumnInfo> filterList = colList.stream().filter(item -> 0 == item.getIsFilter()).collect(Collectors.toList());
		
		TblConfigColumnInfo primaryCol = colList.stream().filter(item -> 0== item.getIsPrimaryKey()).collect(Collectors.toList()).get(0);
		
		Page<Map<String,Object>> page = new Page<Map<String,Object>>(vo.getPageNumber(), vo.getPageSize());
		page.setOptimizeCountSql(false); // 禁用自动优化
	    IPage<Map<String,Object>> pageList  = this.tblConfigTableInfoMapper.selectBusinessDatePage(page, vo,info,pageColList,primaryCol);
		
	    Map<String,Object> resultMap = new HashMap<String,Object>(0);
	    
	    resultMap.put("page", pageList);
	    resultMap.put("filter", filterList);
	    resultMap.put("filterColList", vo.getFilterColList());
	    resultMap.put("pageColList", pageColList);
	    return ResponseFormat.retParam(1, 200, resultMap);
	}

	@Override
	public JsonBean cwzbshowBusinessDataDetail(BusinessDataVo vo) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		TblConfigTableInfoVr info = this.tblConfigTableInfoMapper.selectEntityById(vo.getTableId());
		List<TblConfigColumnInfo> colList = this.tblConfigColumnInfoMapper.selectListByTableId(vo.getTableId());
		
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		Map<String,Object> dataMap = this.tblConfigTableInfoMapper.selectBusinessDateEntity(vo,info,colList);
		
		resultMap.put("dataMap", dataMap);
		resultMap.put("colList", colList);
		
		return ResponseFormat.retParam(1, 200, resultMap);
	}

	@Override
	public JsonBean tableFstatus(String fid, Integer fstatus) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		
		if(fstatus == 1) {
			TblConfigTableInfoVr info = this.tblConfigTableInfoMapper.selectEntityById(fid);
			if(StringUtils.isBlank(info.getDataConfig())){
				return ResponseFormat.retParam(0, "未配置数据源，无法启用", null);
			}
		}
		
		this.tblConfigTableInfoMapper.updateTableStatus(fid,fstatus);
		return ResponseFormat.retParam(1, 200, null);
	}

	@Override
	public JsonBean getExportTemplate(String tableId, HttpServletResponse response) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		
		TblConfigTableInfoVr info = this.tblConfigTableInfoMapper.selectEntityById(tableId);
		List<TblConfigColumnInfo> colList = this.tblConfigColumnInfoMapper.selectListByTableId(tableId);
		if(colList == null || colList.size() == 0) {
			return ResponseFormat.retParam(0, "未配置列信息，无法导出模板", null);
		}
		
		List<String> cNames = new ArrayList<String>(0);
		
		List<TblConfigColumnInfo> primaryCol = colList.stream().filter(item -> 0 == item.getIsPrimaryKey()).collect(Collectors.toList());
		List<TblConfigColumnInfo> commonCol = colList.stream().filter(item -> 0 != item.getIsPrimaryKey()).collect(Collectors.toList());
		for (TblConfigColumnInfo col : primaryCol) {
			cNames.add(col.getFname()+"（唯一标识列，为空自动生成）");
		}
		for (TblConfigColumnInfo col : commonCol) {
			if(col.getColType().toUpperCase().equals("DATE")) {
				cNames.add(col.getFname()+"（示例：YYYY-MM-DD/年-月-日）");
			}else if(col.getColType().toUpperCase().equals("TIME") || col.getColType().toUpperCase().equals("TIMESTAMP")) {
				cNames.add(col.getFname()+"（示例：YYYY-MM-DD HH24:mm:ss/年-月-日 时:分:秒）");
			}else {
				cNames.add(col.getFname());
			}
		}
		response.setHeader("Content-Disposition", "attachment;filename=" + info.getFname() + new String("导入模板".getBytes(),"UTF-8") + ".xlsx");
		HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        responseHeaders.set(HttpHeaders.CONTENT_DISPOSITION, 
                           "attachment; filename="+ info.getFname() + new String("导入模板".getBytes(),"UTF-8") + ".xlsx");
		ByteArrayOutputStream stream = ExcelExporter.exportToExcel(cNames, new ArrayList<List<Object>>(0));
		
		return ResponseFormat.retParam(1, 200, ResponseEntity.ok()
                .headers(responseHeaders)
                .body(stream.toByteArray()));
	}

}
