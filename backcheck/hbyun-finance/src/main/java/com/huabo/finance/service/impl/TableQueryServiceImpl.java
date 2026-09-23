package com.huabo.finance.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.finance.dto.ColumnInfoDto;
import com.huabo.finance.dto.TableExistsDto;
import com.huabo.finance.dto.TableInfoDto;
import com.huabo.finance.mapper.TableQueryMapper;
import com.huabo.finance.service.TableQueryService;
import com.huabo.finance.vo.TableDataQueryVo;

/**
 * 数据库表查询工具 Service实现类
 * 
 * @author system
 * @since 2025-11-22
 */
@Service
public class TableQueryServiceImpl implements TableQueryService {

	@Resource
	private TableQueryMapper tableQueryMapper;
	
	@Override
	public JsonBean checkTableExists(TblStaffUtil staff, String tableName) throws Exception {
		if (StringUtils.isBlank(tableName)) {
			return ResponseFormat.retParam(0, "表名不能为空", null);
		}
		
		// 转换为大写
		String upperTableName = tableName.trim().toUpperCase();
		
		// 查询表是否存在
		TableExistsDto result = this.tableQueryMapper.checkTableExists(upperTableName);
		
		if (result == null) {
			TableExistsDto notExists = new TableExistsDto();
			notExists.setTableName(upperTableName);
			notExists.setExists(false);
			return ResponseFormat.retParam(1, 200, notExists);
		}
		
		result.setExists(true);
		
		// 保存查询历史
		try {
			String userId = staff.getStaffid() != null ? staff.getStaffid().toString() : "SYSTEM";
			this.tableQueryMapper.saveQueryHistory(userId, upperTableName, result.getTableComment());
		} catch (Exception e) {
			// 保存历史失败不影响主流程
			e.printStackTrace();
		}
		
		return ResponseFormat.retParam(1, 200, result);
	}

	@Override
	public JsonBean getTableStructure(TblStaffUtil staff, String tableName) throws Exception {
		if (StringUtils.isBlank(tableName)) {
			return ResponseFormat.retParam(0, "表名不能为空", null);
		}
		
		// 转换为大写
		String upperTableName = tableName.trim().toUpperCase();
		
		// 检查表是否存在
		TableExistsDto existsDto = this.tableQueryMapper.checkTableExists(upperTableName);
		if (existsDto == null) {
			return ResponseFormat.retParam(0, "表不存在", null);
		}
		
		// 构建表信息
		TableInfoDto tableInfo = new TableInfoDto();
		tableInfo.setTableName(upperTableName);
		tableInfo.setTableComment(existsDto.getTableComment());
		
		// 获取字段信息
		List<ColumnInfoDto> columns = this.tableQueryMapper.getTableColumns(upperTableName);
		tableInfo.setColumns(columns);
		
		// 保存查询历史
		try {
			String userId = staff.getStaffid() != null ? staff.getStaffid().toString() : "SYSTEM";
			this.tableQueryMapper.saveQueryHistory(userId, upperTableName, existsDto.getTableComment());
		} catch (Exception e) {
			// 保存历史失败不影响主流程
			e.printStackTrace();
		}
		
		return ResponseFormat.retParam(1, 200, tableInfo);
	}

	@Override
	public JsonBean getTableData(TblStaffUtil staff, TableDataQueryVo vo) throws Exception {
		if (StringUtils.isBlank(vo.getTableName())) {
			return ResponseFormat.retParam(0, "表名不能为空", null);
		}

		// 转换为大写
		String upperTableName = vo.getTableName().trim().toUpperCase();

		// 检查表是否存在
		TableExistsDto existsDto = this.tableQueryMapper.checkTableExists(upperTableName);
		if (existsDto == null) {
			return ResponseFormat.retParam(0, "表不存在", null);
		}

		// 分页参数
		Integer pageNum = vo.getPageNum() != null ? vo.getPageNum() : 1;
		Integer pageSize = vo.getPageSize() != null ? vo.getPageSize() : 20;

		// 计算偏移量
		int offset = (pageNum - 1) * pageSize;

		// 构建WHERE条件
		StringBuilder whereClause = new StringBuilder();
		if (vo.getFieldConditions() != null && !vo.getFieldConditions().isEmpty()) {
			whereClause.append(" WHERE ");
			int index = 0;
			for (Map.Entry<String, String> entry : vo.getFieldConditions().entrySet()) {
				if (index > 0) {
					whereClause.append(" AND ");
				}
				String fieldName = entry.getKey().toUpperCase();
				String fieldValue = entry.getValue();
				// 使用LIKE进行模糊查询
				whereClause.append(fieldName).append(" LIKE '%").append(fieldValue.replace("'", "''")).append("%'");
				index++;
			}
		}

		// 构建查询SQL (Oracle分页)
		String dataSql = String.format(
			"SELECT * FROM ( " +
			"  SELECT t.*, ROWNUM rn FROM ( " +
			"    SELECT * FROM %s%s " +
			"  ) t WHERE ROWNUM <= %d " +
			") WHERE rn > %d",
			upperTableName, whereClause.toString(), offset + pageSize, offset
		);

		// 构建总数SQL
		String countSql = String.format("SELECT COUNT(*) FROM %s%s", upperTableName, whereClause.toString());

		// 查询数据
		List<Map<String, Object>> dataList = this.tableQueryMapper.queryTableData(dataSql);

		// 查询总数
		Long total = this.tableQueryMapper.queryTableDataCount(countSql);

		// 构建分页结果
		Map<String, Object> result = new HashMap<>();
		result.put("records", dataList);
		result.put("list", dataList);
		result.put("total", total);
		result.put("pageNum", pageNum);
		result.put("pageSize", pageSize);
		result.put("pages", (total + pageSize - 1) / pageSize);

		return ResponseFormat.retParam(1, 200, result);
	}

	@Override
	public JsonBean getQueriedTableList(TblStaffUtil staff) throws Exception {
		String userId = staff.getStaffid() != null ? staff.getStaffid().toString() : "SYSTEM";

		List<TableInfoDto> tableList = this.tableQueryMapper.getQueriedTableList(userId);

		if (tableList == null) {
			tableList = new ArrayList<>();
		}

		return ResponseFormat.retParam(1, 200, tableList);
	}

	@Override
	public JsonBean updateTableCategory(TblStaffUtil staff, String tableName, String category, Integer sortOrder) throws Exception {
		if (StringUtils.isBlank(tableName)) {
			return ResponseFormat.retParam(0, "表名不能为空", null);
		}

		if (StringUtils.isBlank(category)) {
			return ResponseFormat.retParam(0, "分类名称不能为空", null);
		}

		String userId = staff.getStaffid() != null ? staff.getStaffid().toString() : "SYSTEM";
		String upperTableName = tableName.trim().toUpperCase();

		if (sortOrder == null) {
			sortOrder = 0;
		}

		try {
			this.tableQueryMapper.updateTableCategory(userId, upperTableName, category, sortOrder);
			return ResponseFormat.retParam(1, "更新成功", null);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFormat.retParam(0, "更新失败: " + e.getMessage(), null);
		}
	}

}

