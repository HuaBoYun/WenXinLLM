package com.huabo.finance.thread;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.read.listener.ReadListener;
import com.hbfk.util.DateUtil;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.finance.entity.BdImportBatchsRecord;
import com.huabo.finance.entity.TblConfigColumnInfo;
import com.huabo.finance.mapper.BdImportBatchsRecordMapper;
import com.huabo.finance.service.impl.ExcelImportServiceImpl;
import com.huabo.finance.unit.BussinessDataSqlFunc;
import com.huabo.finance.vr.TblConfigTableInfoVr;

public class DynamicDataImportListener implements ReadListener<Map<Integer,Object>>{
	
	private String insertSql; //新增sql语句
	private String modifySql; //修改sql语句
	private String selectSql; //查询数量sql语句
	private ValidationStatus validationStatus = ValidationStatus.PENDING;; //为ture 则excel所有列没有匹配上数据字段，无法导入；
	private Map<Integer,TblConfigColumnInfo> colMap = new HashMap<Integer,TblConfigColumnInfo>(0); //列信息 按照excel列索引顺序的集合
	private String recordMemo;//采集记录
	private String importId;
	
	private Integer insertCount = 0;
	private Integer updateCount = 0;
	
	
	
	// 终止标志
    private boolean shouldTerminate = false;
	private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private static final DateTimeFormatter DATE_FORMAT_FUll = DateTimeFormatter.ofPattern("`yyyy-MM-dd HH:mm:ss");
	
	private final TblConfigTableInfoVr tableConfig; //业务数据表配置信息
    private final JdbcTemplate jdbcTemplate;		//jdbc
    private final List<TblConfigColumnInfo> colList; //业务数据列配置信息
    private final List<TblConfigColumnInfo> primaryColList; //业务数据主键配置信息
    private final int batchSize;//批处理数量
    private final BdImportBatchsRecordMapper bdImportBatchsRecordMapper;
    
    private List<Object[]> insertData = new ArrayList<Object[]>(0);
    private List<Object[]> modifyData = new ArrayList<Object[]>(0);   

    public DynamicDataImportListener(TblConfigTableInfoVr tableConfig,List<TblConfigColumnInfo> colList, List<TblConfigColumnInfo> primaryCol, String importId, String recordMemo, BdImportBatchsRecordMapper bdImportBatchsRecordMapper, JdbcTemplate jdbcTemplate, int batchSize) {
        this.tableConfig = tableConfig;
        this.colList = colList;
        this.jdbcTemplate = jdbcTemplate;
        this.batchSize = batchSize;
        this.importId = importId;
        this.recordMemo = recordMemo;
        this.bdImportBatchsRecordMapper = bdImportBatchsRecordMapper;
        this.primaryColList = primaryCol;
    }


    
    
    @Override
	public void invokeHead(Map<Integer, ReadCellData<?>> headMap, AnalysisContext context) {
    	 // 1. 处理表头
        int maxIndex = headMap.keySet().stream().max(Integer::compare).orElse(-1);
        String titleStr = "";
        int bracketIndex = 0;
        String column = "";
        String value = "";
        String update = "";
        String updateWhere = "";
        TblConfigColumnInfo colInfo = null;
        int matchCount = 0;//记录匹配数量
        this.recordMemo += "2.解析表头，匹配excel列和数据库字段...\n";
        for (int i = 0; i <= maxIndex; i++) {
            ReadCellData<?> cell = headMap.get(i);
            if (cell == null) continue;
            //循环判断生成列信息
            titleStr = cell.getStringValue();
            bracketIndex = titleStr.lastIndexOf("（");
            final String cleanTitle = (bracketIndex > 0) &&(titleStr.contains("（唯一标识列，为空自动生成）") || titleStr.contains("（示例：YYYY-MM-DD/年-月-日）") || titleStr.contains("（示例：YYYY-MM-DD HH24:mm:ss/年-月-日 时:分:秒）"))
                    ? titleStr.substring(0, bracketIndex)
                    : titleStr;
                    
           Optional<TblConfigColumnInfo> matchedCol = colList.stream()
                            .filter(item -> cleanTitle.equals(item.getFname()))
                            .findFirst();
           
           if (matchedCol.isPresent()) {
        	   colInfo = matchedCol.get();
        	   column += colInfo.getOursColname().toUpperCase()+",";
        	   value += "?,";
        	   update += colInfo.getOursColname().toUpperCase()+" = ?,";
               colMap.put(i, colInfo);
               matchCount++;
           } else {
               // 处理未匹配的情况
        	   recordMemo += "未找到匹配列: "+titleStr+"\n";
           }
        }
        
        if (matchCount == 0) {
            // 零匹配：终止操作
        	validationStatus = ValidationStatus.FAILED;
        	shouldTerminate = true; 
            // 立即停止读取（不使用interrupt）
            recordMemo += "3.excel列与数据库字段全部不匹配，中止导入\n";
        } else {
            // 部分/全部匹配：继续读取
        	validationStatus = ValidationStatus.SUCCESS;
            recordMemo += "3.生成where的主键筛选条件\n";
            for (TblConfigColumnInfo primaryCol : primaryColList) {
            	updateWhere += primaryCol.getOursColname().toUpperCase() +" = ? AND";
    		}
            insertSql = "INSERT INTO "+tableConfig.getOursTableName().toUpperCase()+"("+column+"F_IMPORTBATCHES,F_DATASOURCETYPE ) VALUES("+value+importId+",2 )";
            modifySql = "UPDATE "+tableConfig.getOursTableName().toUpperCase() + " SET "+update+"F_DATASOURCETYPE = 2, F_IMPORTBATCHES = "+importId+" WHERE "+updateWhere.substring(0,updateWhere.length() - 4);
            selectSql = "SELECT COUNT(0) FROM "+tableConfig.getOursTableName().toUpperCase()+" WHERE "+updateWhere.substring(0,updateWhere.length() - 4);
            recordMemo += "4.生成DDL模板语句：\n\t新增语句："+insertSql+"\n\t修改语句："+modifySql+"\n\t查询语句："+selectSql+"\n";
        }
        recordMemo += "5.excel导入数据校验和转换...\n";
	}




	@Override
    public void invoke(Map<Integer,Object> data, AnalysisContext context) {
		// 如果应该终止或校验失败，跳过数据处理
        if (shouldTerminate || validationStatus != ValidationStatus.SUCCESS) {
            return;
        }
		
        // 数据校验和转换
        Object[] convertedData = convertData(data, context.readRowHolder().getRowIndex());
        if (convertedData != null) {
        	if(convertedData.length > colList.size()) {
        		//需要传入的参数大于新增传入的参数 则为修改
        		modifyData.add(convertedData);
        		updateCount++;
        	}else {
        		insertData.add(convertedData);
        		insertCount++;
        	}
        	
            if ((modifyData.size()+insertData.size()) >= batchSize) {
                saveBatch();
                modifyData.clear();
                insertData.clear();
            }
        }
    }

    private Object[] convertData(Map<Integer,Object> rawData, int rowIndex) {
    	//给colList中的元素放入excel导入数据
    	int colIndex = 0;  
    	TblConfigColumnInfo column = null;
    	int i = 0;
    	boolean selectFlag = false;
    	boolean updateFlag = false;
    	Object[] primaryObjs = new Object[primaryColList.size()]; //where 条件主键参数
    	Object[] valueObjs = new Object[colList.size()]; //ddl操作列参数
    	Object[] result = null;//返回结果集
    	
    	//生成 新增修改 需要传入的数据和 相关的主键参数
    	StringBuilder sb = new StringBuilder();
    	for (Object key : rawData.keySet()) {
    		column = colMap.get(colIndex);
    		convertType(rawData.get(key), column, rowIndex);
    		if(column.getIsPrimaryKey() == 0) {
    			if(rawData.get(key)!= null) {
    				primaryObjs[i] = column.getQueryData();
        			selectFlag = true;
        			i++;
    			}else {
    				column.setQueryData(RandomUtil.uuStringId());
    			}
    			
    		}
    		valueObjs[colIndex] = column.getQueryData();
    		colIndex++;
    		sb.append(column.getQueryData()).append(",");
        }
    	
    	if(selectFlag) {
    		//主键不为空需要判断数量 
    		Integer count = jdbcTemplate.queryForObject(selectSql, Integer.class,primaryObjs);
    		if(count > 0) {
    			updateFlag = true;
    		}
    	}
    	
    	if(updateFlag) {
    		result = new Object[valueObjs.length + primaryObjs.length];
    	    System.arraycopy(valueObjs, 0, result, 0, valueObjs.length);
    	    System.arraycopy(primaryObjs, 0, result, valueObjs.length, primaryObjs.length);
    	    recordMemo += "\t修改数据："+sb.toString()+"\n";
    	}else {
    		result = valueObjs.clone();
    		recordMemo += "\t新增数据："+sb.toString()+"\n";
    	}
        
        return result;
    }

	private void convertType(Object value, TblConfigColumnInfo column, int rowIndex) {
        try {
        	if(value == null) {
        		column.setQueryData(null);
        	}else {
	            switch (column.getColType()) {
		            case "DATE":
		            	column.setQueryData(LocalDate.parse(value.toString(), DATE_FORMAT));
						break;
					case "TIME":
					case "TIMESTAMP":
						column.setQueryData(LocalDate.parse(value.toString(), DATE_FORMAT_FUll));
						break;
					default: 
						column.setQueryData(value);
						break;
	            }
        	}
        	
        } catch (Exception e) {
        	recordMemo += "\t 第"+(rowIndex+1)+"行"+column.getFname()+"数据格式转换异常...\n";
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        if (!insertData.isEmpty() || !modifyData.isEmpty()) {
        	saveBatch();
            modifyData.clear();
            insertData.clear();
        }
        recordMemo += "6.本次导入共"+(insertCount+updateCount)+"条数据，其中新增："+insertCount+"条数据，修改："+updateCount+"条数据。\n";
        BdImportBatchsRecord record = new BdImportBatchsRecord();
        record.setRecordmemo(recordMemo);
    	record.setIscompleted(2);
    	record.setIsresult(1);
    	record.setEnddate(new Date());
    	record.setImportId(importId);
    	this.bdImportBatchsRecordMapper.updateById(record);
    }

    private void saveBatch() {
    	jdbcTemplate.batchUpdate(insertSql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Object[] row = insertData.get(i);
                for (int j = 0; j < row.length; j++) {
                    ps.setObject(j + 1, row[j]);
                }
            }
            
            @Override
            public int getBatchSize() {
                return insertData.size();
            }
        });
    	
    	jdbcTemplate.batchUpdate(modifySql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Object[] row = modifyData.get(i);
                for (int j = 0; j < row.length; j++) {
                    ps.setObject(j + 1, row[j]);
                }
            }
            
            @Override
            public int getBatchSize() {
                return modifyData.size();
            }
        });
    }
    
    
    // 校验状态枚举
    private enum ValidationStatus {
        PENDING,   // 待校验
        SUCCESS,   // 校验通过
        FAILED     // 校验失败
    }

}
