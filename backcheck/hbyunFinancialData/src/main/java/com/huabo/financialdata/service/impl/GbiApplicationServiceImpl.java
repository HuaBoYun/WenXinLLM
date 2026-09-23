package com.huabo.financialdata.service.impl;

import cn.hutool.extra.pinyin.PinyinUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.FtpUtil;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.financialdata.config.exception.BizException;
import com.huabo.financialdata.entity.dto.gbi.GbiTableDataQuery;
import com.huabo.financialdata.entity.entity.*;
import com.huabo.financialdata.entity.vo.gbi.GbiBatchListVO;
import com.huabo.financialdata.entity.vo.gbi.GbiKnowledgeRequestVO;
import com.huabo.financialdata.entity.vo.gbi.GbiTableListVO;
import com.huabo.financialdata.jdbc.*;
import com.huabo.financialdata.mapper.*;
import com.huabo.financialdata.service.GbiApplicationService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 大模型数据 服务实现类
 * </p>
 *
 * @author 
 * @since 2024-04-20
 */
@Service
public class GbiApplicationServiceImpl extends ServiceImpl<GbiApplicationConfigMapper, GbiApplicationConfig> implements GbiApplicationService {

    private static final String UPLOAD_PATH = "/gbi/excel";

    //excel导入文件不能超过10MB
    private static final long maxSize = 10 * 1024 * 1024;
    //sheet不能超过1万行
    private static final int maxRowNumber = 10000;
    //sheet不能超过40列
    private static final int maxCellNumber = 400;

    @Autowired
    private GbiApplicationConfigMapper gbiApplicationConfigMapper;
    @Autowired
    private GbiKnowledgeMapper gbiKnowledgeMapper;
    @Autowired
    private GbiBatchMapper gbiBatchMapper;
    @Autowired
    private GbiTableMapper gbiTableMapper;
    @Autowired
    private GbiTableColumnsMapper gbiTableColumnsMapper;

    @Autowired
    private JdbcExecuteFactory jdbcExecuteFactory;

    @Override
    public GbiApplicationConfig getApplicationConfig(BigDecimal userId){
        if (userId == null && userId.equals(BigDecimal.ZERO)) {
            throw new BizException("参数错误");
        }
        QueryWrapper<GbiApplicationConfig> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(GbiApplicationConfig::getUserId,userId).eq(GbiApplicationConfig::getDeleted,0);
        return gbiApplicationConfigMapper.selectOne(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveApplicationConfig(TblStaffUtil staff, GbiApplicationConfig requestVO){
        //有id则根据id修改
        String configId = requestVO.getId();
        if (StringUtils.isNoneBlank(configId)) {
            gbiApplicationConfigMapper.updateById(requestVO);
            return true;
        }

        //无id根据用户查询应用配置，根据查询到的记录id更新
        GbiApplicationConfig applicationConfig = getApplicationConfig(staff.getStaffid());
        if (Objects.nonNull(applicationConfig)) {
            applicationConfig.setContinuousQuestioning(requestVO.getContinuousQuestioning());
            gbiApplicationConfigMapper.updateById(applicationConfig);
            return true;
        }

        //无用户配置记录则新增
        requestVO.setId(RandomUtil.uuStringId());
        requestVO.setUserId(staff.getStaffid());
        requestVO.setCreateTime(new Date());
        gbiApplicationConfigMapper.insert(requestVO);
        return true;
    }

    /**
     * 数据配置-知识列表
     * @param userId 用户id
     * @return
     */
    @Override
    public List<GbiKnowledge> getKnowledgeList(BigDecimal userId) {
        if (userId == null && userId.equals(BigDecimal.ZERO)) {
            throw new BizException("参数错误");
        }
        QueryWrapper<GbiKnowledge> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(GbiKnowledge::getUserId,userId).eq(GbiKnowledge::getDeleted,0);
        return gbiKnowledgeMapper.selectList(queryWrapper);
    }

    /**
     * 数据配置-新增知识
     * @return
     */
    @Override
    public Boolean addKnowledge(TblStaffUtil staff, GbiKnowledgeRequestVO param) {
        GbiKnowledge addGbiKnowledge = new GbiKnowledge();
        addGbiKnowledge.setId(RandomUtil.uuStringId());
        addGbiKnowledge.setKnowledgeKey(param.getKnowledgeKey());
        addGbiKnowledge.setKnowledgeValue(param.getKnowledgeValue());
        addGbiKnowledge.setCreateTime(new Date());
        addGbiKnowledge.setUserId(staff.getStaffid());
        addGbiKnowledge.setDeleted(0);
        int count = gbiKnowledgeMapper.insert(addGbiKnowledge);
        return count == 1;
    }

    /**
     * 数据配置-修改知识
     * @return
     */
    @Override
    public Boolean updateKnowledge(GbiKnowledgeRequestVO param) {
        if (StringUtils.isBlank(param.getId())) {
            throw new BizException("ID不能为空");
        }

        GbiKnowledge gbiKnowledge = gbiKnowledgeMapper.selectById(param.getId());
        if (Objects.isNull(gbiKnowledge)) {
            throw new BizException("知识数据不存在["+param.getId()+"]");
        }

        if (Integer.valueOf(1).equals(gbiKnowledge.getDeleted())) {
            throw new BizException("已删除知识数据不能再次编辑["+param.getId()+"]");
        }

        GbiKnowledge upd = new GbiKnowledge();
        upd.setId(param.getId());
        upd.setKnowledgeKey(param.getKnowledgeKey());
        upd.setKnowledgeValue(param.getKnowledgeValue());
        upd.setUpdateTime(new Date());
        int result = gbiKnowledgeMapper.updateById(upd);
        return result == 1;
    }

    /**
     * 数据配置-删除知识
     * @return
     */
    @Override
    public Boolean deleteKnowledge(String knowledgeId) {
        GbiKnowledge knowledge = gbiKnowledgeMapper.selectById(knowledgeId);
        if (Objects.isNull(knowledge)) {
            throw new BizException("数据不存在");
        }
        if (knowledge.getDeleted().equals(1)) {
            throw new BizException("数据删除状态错误："+knowledge.getId()+","+knowledge.getDeleted());
        }
        
        knowledge.setDeleted(1);
        int count = gbiKnowledgeMapper.updateById(knowledge);
        return count == 1;
    }

    // -----大模型数据导入----------

    /**
     * 根据userId分页查询上传批次
     * @param userId
     * @return
     */
    @Override
    public IPage<GbiBatchListVO> queryBatch(BigDecimal userId, Page<GbiBatch> page) {
        QueryWrapper<GbiBatch> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(GbiBatch::getUserId,userId).orderByDesc(GbiBatch::getCreateTime);
        Page<GbiBatch> gbiBatchPage = gbiBatchMapper.selectPage(page, queryWrapper);

        //实体装换
        IPage<GbiBatchListVO> result = new Page<>(gbiBatchPage.getCurrent(),gbiBatchPage.getSize(),gbiBatchPage.getTotal());
        if (CollectionUtils.isNotEmpty(gbiBatchPage.getRecords())) {
            List<GbiBatchListVO> records = gbiBatchPage.getRecords().stream().map(e -> convertGbiBatchListVO(e)).collect(Collectors.toList());
            result.setRecords(records);
        }

        return result;
    }

    @Override
    public List<GbiBatchListVO> getBatchList(BigDecimal userId) {
        //根据userId查询所有上传记录 暂定500条
        QueryWrapper<GbiBatch> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(GbiBatch::getUserId,userId).orderByDesc(GbiBatch::getCreateTime);
        Page<GbiBatch> page = new Page<>(1, 500);
        Page<GbiBatch> gbiBatchPage = gbiBatchMapper.selectPage(page, queryWrapper);
        List<GbiBatch> gbiBatches = gbiBatchPage.getRecords();
        if (CollectionUtils.isEmpty(gbiBatches)) {
            return Collections.emptyList();
        }

        //根据批次id批量查询导入数据的表信息
        List<String> batchIds = gbiBatches.stream().map(GbiBatch::getId).collect(Collectors.toList());
        List<GbiTable> tableListByUserIds = this.getTableListByUserIds(batchIds);
        Map<String, List<GbiTable>> batchTableGroup = new HashMap<>();
        if (CollectionUtils.isNotEmpty(tableListByUserIds)) {
            batchTableGroup = tableListByUserIds.stream().collect(Collectors.groupingBy(GbiTable::getBatchId));
        }

        List<GbiBatchListVO> result = new ArrayList<>();
        for (GbiBatch gbiBatch : gbiBatches) {
            GbiBatchListVO gbiBatchListVO = convertGbiBatchListVO(gbiBatch);

            List<GbiTable> gbiTables = batchTableGroup.get(gbiBatch.getId());
            if (CollectionUtils.isNotEmpty(gbiTables)) {
                gbiBatchListVO.setTableList(gbiTables.stream().map(e -> convert2GbiTableListVO(e)).collect(Collectors.toList()));
            }

            result.add(gbiBatchListVO);
        }

        return result;
    }

    /**
     * batch 实体转换
     * @param gbiBatch
     * @return
     */
    private static GbiBatchListVO convertGbiBatchListVO(GbiBatch gbiBatch) {
        GbiBatchListVO gbiBatchListVO = GbiBatchListVO.builder()
                .id(gbiBatch.getId())
                .attname(gbiBatch.getAttname())
                .attsize(gbiBatch.getAttsize())
                .appId(gbiBatch.getAppId())
                .batchNo(gbiBatch.getBatchNo())
                .createTime(gbiBatch.getCreateTime())
                .userId(gbiBatch.getUserId())
                .build();
        return gbiBatchListVO;
    }

    @Override
    public List<GbiTableListVO> getTableList(BigDecimal userId, String batchId) {
        // 如果参数batchId不存在，查询最近一条batch数据
        if (StringUtils.isBlank(batchId)) {
           GbiBatch lastBatch = this.getLastBatch(userId);
            if (Objects.isNull(lastBatch)) {
                throw new BizException("导入数据批次不存在");
            }
            batchId = lastBatch.getId();
        }

        // 根据批次id查询表信息
        List<GbiTable> tableList = this.getTableListByBatchId(batchId);
        if (CollectionUtils.isEmpty(tableList)) {
            return Collections.emptyList();
        }

        List<String> tableIds = tableList.stream().map(GbiTable::getId).collect(Collectors.toList());

        QueryWrapper<GbiTableColumns> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().in(GbiTableColumns::getTableId,tableIds).orderByAsc(GbiTableColumns::getSort);
        List<GbiTableColumns> tableColumns = gbiTableColumnsMapper.selectList(queryWrapper);

        //根据tableId分组 转map
        Map<String, List<GbiTableColumns>> groupByTableId = tableColumns.stream().collect(Collectors.groupingBy(GbiTableColumns::getTableId));
        // 构建返回结果
        List<GbiTableListVO> gbiTableListVOS = new ArrayList<>();
        for (GbiTable table : tableList ) {
            GbiTableListVO gbiTableListVO = convert2GbiTableListVO(table);

            gbiTableListVO.setSettings(groupByTableId.get(table.getId()));
            gbiTableListVOS.add(gbiTableListVO);
        }
        return gbiTableListVOS;
    }

    private GbiTableListVO convert2GbiTableListVO(GbiTable table) {
        return GbiTableListVO.builder()
                .tableId(table.getId())
                .appId(table.getAppId())
                .batchId(table.getBatchId())
                .batchNo(table.getBatchNo())
                .customTable(table.getCustomTable())
                .description(table.getDescription())
                .tableName(table.getTableName())
                .rowNum(table.getRowNum())
                .cellNum(table.getCellNum()).build();
    }

    @Override
    public List<GbiTableColumns> getTableColumns(String tableId) {
        QueryWrapper<GbiTableColumns> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(GbiTableColumns::getTableId,tableId)
                .eq(GbiTableColumns::getDeleted,0)
                .orderByAsc(GbiTableColumns::getSort);
        return gbiTableColumnsMapper.selectList(queryWrapper);
    }

    @Override
    public IPage<Map> getTableData(TblStaffUtil staffUtil, GbiTableDataQuery query) {
        //查询table表
        GbiTable gbiTable = gbiTableMapper.selectById(query.getTableId());
        if (Objects.isNull(gbiTable)) {
            throw new BizException("未查询到表记录："+query.getTableId());
        }

        Page page = new Page(query.getPageNo(), query.getPageSize());
        IPage<Map> iPage = gbiTableMapper.selectTableData(gbiTable.getTableName(), page);
        return iPage;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateTableAndSettings(List<GbiTableListVO> gbiTableListVOS) {
        if (CollectionUtils.isEmpty(gbiTableListVOS)) {
            return false;
        }

        JdbcExecuteTemplate currentJdbcExecute = jdbcExecuteFactory.getCurrentJdbcExecute();

        for (GbiTableListVO gbiTableListVO : gbiTableListVOS) {
            GbiTable gbiTable = gbiTableMapper.selectById(gbiTableListVO.getTableId());
            gbiTable.setDescription(gbiTableListVO.getDescription());
            gbiTable.setUpdateTime(new Date());
            gbiTableMapper.updateById(gbiTable);
            List<GbiTableColumns> gbiTableSettings = gbiTableListVO.getSettings();
            if (CollectionUtils.isNotEmpty(gbiTableSettings)) {
                for (GbiTableColumns settings : gbiTableSettings) {
                    GbiTableColumns dbColumn = gbiTableColumnsMapper.selectById(settings.getId());
                    Boolean dbIndexd = dbColumn.getIndexed();
                    Boolean indexed = settings.getIndexed();
                    if (!dbIndexd.equals(indexed)) { //判断是否修改索引
                        if (dbIndexd) {
                            String dropIndexSql = currentJdbcExecute.dropIndexSql(gbiTable.getTableName(), dbColumn.getIndexName());
                            gbiTableMapper.updateTable(dropIndexSql);
                            dbColumn.setIndexName("");
                        } else {
                            //新增索引
                            String indexName = gbiTable.getTableName() + dbColumn.getMappingColumn();
                            String createIndexSql = currentJdbcExecute.createIndexSql(gbiTable.getTableName(), indexName, dbColumn.getMappingColumn());
                            gbiTableMapper.updateTable(createIndexSql);
                            dbColumn.setIndexName(indexName);
                        }
                    }
                    dbColumn.setIndexed(settings.getIndexed());
                    dbColumn.setParentColumn(settings.getParentColumn());
                    dbColumn.setColumnComment(settings.getColumnComment());

                    //修改字段注释
                    JdbcColumnVO jdbcColumnVO = new JdbcColumnVO();
                    jdbcColumnVO.setName(dbColumn.getMappingColumn());
                    jdbcColumnVO.setComment(settings.getColumnComment());
                    jdbcColumnVO.setFieldType(dbColumn.getFieldType());
                    String updateCommentSql = currentJdbcExecute.updateCommentSql(gbiTable.getTableName(), jdbcColumnVO);
                    gbiTableMapper.updateTable(updateCommentSql);

                    //修改字段记录
                    gbiTableColumnsMapper.updateById(dbColumn);
                }
            }
        }
        return true;
    }

    /**
     * 根据userId查询最后一条batch数据
     * @param userId
     * @return
     */
    private GbiBatch getLastBatch(BigDecimal userId) {
        QueryWrapper<GbiBatch> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(GbiBatch::getUserId,userId).orderByDesc(GbiBatch::getCreateTime);
        List<GbiBatch> gbiBatches = gbiBatchMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(gbiBatches)) {
            return null;
        }
        return gbiBatches.get(0);
    }

    /**
     * 根据batchId获取tableList（根据批次id查询表信息集合）
     * @param batchId
     * @return
     */
    private List<GbiTable> getTableListByBatchId(String batchId) {
        QueryWrapper<GbiTable> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(GbiTable::getBatchId,batchId).orderByAsc(GbiTable::getSort);
        return gbiTableMapper.selectList(queryWrapper);
    }

    /**
     * 根据batchIds批量获取tableList（根据批次id查询表信息集合）
     * @param batchIds
     * @return
     */
    private List<GbiTable> getTableListByUserIds(List<String> batchIds) {
        QueryWrapper<GbiTable> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().in(GbiTable::getBatchId,batchIds).orderByAsc(GbiTable::getSort);
        return gbiTableMapper.selectList(queryWrapper);
    }

    /**
     * 1.单次可上传一个文件，多次上传默认使用最后一个文件
     * 2.单个文件内容不得超过40列，1万行、文件大小不超过10m
     * 3.若文件中包含多个sheet页，会创建多个表格，一个文件最多解析前5个sheet页
     *
     * @param staff
     * @param file
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String uploadData(TblStaffUtil staff, MultipartFile file) throws Exception{
        //上传文件
        long size = file.getSize();
        if (size > maxSize) {
            throw new BizException("文件大小不能超过10MB");
        }

        //本次上传batchId
        String batchId = RandomUtil.uuStringId();

        InputStream inputStream = null;
        Workbook workbook = null;
        try {
            //文件上传ftp1
            String originalFilename = file.getOriginalFilename();
            String uniqueFileName = uniqueFileName(originalFilename);
//            FtpUtil.ftpUploadFile(UPLOAD_PATH,uniqueFileName,file.getInputStream());

            //保存batch
            GbiBatch batch = new GbiBatch();
            batch.setId(batchId);
            batch.setBatchNo(batchId);
            batch.setUserId(staff.getStaffid());
            batch.setAttname(originalFilename);
            batch.setAttpath(UPLOAD_PATH + "/" + uniqueFileName);
            batch.setAttsize(new BigDecimal(size));
            batch.setCreateTime(new Date());
            gbiBatchMapper.insert(batch);

            inputStream = file.getInputStream();
            workbook = WorkbookFactory.create(inputStream);

            //目前只处理前5个sheet页
            int numberOfSheets = workbook.getNumberOfSheets();
            if (numberOfSheets > 5) {
                numberOfSheets = 5;
            }
            for (int sheetIndex = 0; sheetIndex < numberOfSheets; sheetIndex++) {
                //表头
                List<String> headList = new ArrayList<>();
                //样例
                List<String> demoList = new ArrayList<>();
                //字段类型
                Map<Integer,Set<FieldTypeEnum>> columnMap = new HashMap<>();
                //数据
                List<List<String>> dataList = new ArrayList<>();

                Sheet sheet = workbook.getSheetAt(sheetIndex);
                // 遍历所有的工作表(Sheet)
                // 遍历工作表中的所有行(Row)
                int lastRowNum = sheet.getLastRowNum();
                if (lastRowNum <= 0) {
                    throw new BizException("sheet页空数据");
                }
                if (lastRowNum > maxRowNumber) {
                    throw new BizException("文件内容不能超过1万行");
                }

                Row head = sheet.getRow(0);
                short totalCellNum = head.getLastCellNum();
                for (Cell cell : head) {
                    String cellHead = cell.getStringCellValue();
                    headList.add(cellHead);
                }

                for (int rowIndex = 1; rowIndex <= lastRowNum; rowIndex++) {
                    // 遍历行中的所有单元格(Cell)
                    Row row = sheet.getRow(rowIndex);
                    List<String>  rowDataList = new ArrayList<>();
                    short lastCellNum = row.getLastCellNum();
                    if (lastCellNum>maxCellNumber) {
                        throw new BizException("文件内容不能超过40列");
                    }
                    for (int i1 = 0; i1 < lastCellNum; i1++) {
                        String cellValue = String.valueOf(getCellValueAsString(row.getCell(i1)));
                        if (rowIndex == 1) {
                            demoList.add(cellValue);
                        }
                        //用于收集列的类型
                        FieldTypeEnum cellValueType = ColumnTypeUtil.getCellValueType(cellValue);
                        Set<FieldTypeEnum> cellValueTypeOracleEnums = columnMap.get(i1);
                        if (CollectionUtils.isEmpty(cellValueTypeOracleEnums)) {
                            cellValueTypeOracleEnums = new HashSet<>();
                            columnMap.put(i1, cellValueTypeOracleEnums);
                        }
                        cellValueTypeOracleEnums.add(cellValueType);
                        if (cellValueType.equals(FieldTypeEnum.STRING) || cellValueType.equals(FieldTypeEnum.TEXT)) {
                            cellValue  = "'" + cellValue +"'";
                        }
                        rowDataList.add(cellValue);
                    }
                    dataList.add(rowDataList);
                }

                //表信息
                GbiTable gbiTable = new GbiTable();
                gbiTable.setBatchId(batchId);
                gbiTable.setBatchNo(batchId);
                String tableId = RandomUtil.uuStringId();
                gbiTable.setId(tableId);
                gbiTable.setCreateTime(new Date());
                gbiTable.setCustomTable(sheet.getSheetName());
                gbiTable.setSort(sheetIndex);
                gbiTable.setRowNum(lastRowNum);
                gbiTable.setCellNum((int)totalCellNum);

                String tableName = "GBI_"+this.getMappingName(sheet.getSheetName());
                gbiTable.setTableName(tableName);
                gbiTable.setUserId(staff.getStaffid());
                gbiTableMapper.insert(gbiTable);

                //字段信息
                List<JdbcColumnVO> tableColumns = new ArrayList<>();
                List<String> mappingColumns = new ArrayList<>();
                for (int cellIndex = 0; cellIndex < headList.size(); cellIndex++) {
                    GbiTableColumns gbiTableColumns = new GbiTableColumns();
                    gbiTableColumns.setBatchId(batchId);
                    gbiTableColumns.setBatchNo(batchId);
                    gbiTableColumns.setCreateTime(new Date());
                    gbiTableColumns.setSort(cellIndex);
                    gbiTableColumns.setId(RandomUtil.uuStringId());
                    gbiTableColumns.setIndexed(false);
                    gbiTableColumns.setTableId(tableId);
                    gbiTableColumns.setDemo(demoList.get(cellIndex));

                    gbiTableColumns.setColumnName(headList.get(cellIndex));
                    String mappingColumn = this.getMappingName(headList.get(cellIndex));
                    gbiTableColumns.setMappingColumn(mappingColumn);
                    mappingColumns.add(mappingColumn);

                    Set<FieldTypeEnum> cellValueTypeOracleEnums = columnMap.get(cellIndex);
                    FieldTypeEnum fieldTypeEnum = FieldTypeEnum.getColumnType(cellValueTypeOracleEnums);
                    gbiTableColumns.setCellType(fieldTypeEnum.getCellVal());
                    gbiTableColumns.setFieldType(fieldTypeEnum.name());
                    gbiTableColumns.setUserId(staff.getStaffid());
                    gbiTableColumnsMapper.insert(gbiTableColumns);

                    //收集创建表字段信息
                    JdbcColumnVO jdbcColumnVO = new JdbcColumnVO();
                    jdbcColumnVO.setName(mappingColumn);
                    jdbcColumnVO.setFieldType(fieldTypeEnum.name());
                    tableColumns.add(jdbcColumnVO);
                }

                //动态创建表结构
                JdbcExecuteTemplate currentJdbcExecute = jdbcExecuteFactory.getCurrentJdbcExecute();
                String tableSql = currentJdbcExecute.createTableSql(tableName, tableColumns);
                gbiTableMapper.createTable(tableSql);

                //保存数据
                String batchInsertSql = currentJdbcExecute.batchInsertSql(tableName, mappingColumns, dataList);
                gbiTableMapper.batchInsert(batchInsertSql);
            }

            workbook.close();
            inputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        }
        return batchId;
    }

    /**
     * 用表头汉字首字母 + 随机6位字母作为真实名称 （大写）
     * @param name
     * @return
     */
    private String getMappingName(String name) {
        //去除特殊字符（去除特殊符号、数字和空格）
        String regex = "[^a-zA-Z\u4e00-\u9fa5]";
        name = name.replaceAll(regex, "");

        //名称为空的情况
        if(StringUtils.isEmpty(name)) {
            return RandomUtil.enUuId().toUpperCase() + "_" +RandomUtil.enUuId().toUpperCase();
        }

        return PinyinUtil.getFirstLetter(name, "").toUpperCase() + "_" +RandomUtil.enUuId().toUpperCase();
    }

    private static Object getCellValueAsString(Cell cell) {
    	if (Objects.isNull(cell)) {
            return "";
        }
        
        CellType cellType = cell.getCellType();
        
        switch (cellType) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return cell.getNumericCellValue();
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case FORMULA:
                // 使用 DataFormatter 可以更好地处理各种格式
                DataFormatter formatter = new DataFormatter();
                return formatter.formatCellValue(cell);
                // 或者使用带 FormulaEvaluator 的版本
                // FormulaEvaluator evaluator = cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
                // return formatter.formatCellValue(cell, evaluator);
            case BLANK:
                return "";
            case ERROR:
                return cell.getErrorCellValue();
            case _NONE:
            default:
                return "";
        }
    }

    /**
     * 生成唯一文件名
     * @param originalFilename
     * @return
     */
    private String uniqueFileName(String originalFilename) {
        String extension = ""; // 文件扩展名
        String baseName = ""; // 文件基础名

        // 分割文件名以得到基础名和扩展名
        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex != -1) {
            baseName = originalFilename.substring(0, dotIndex);
            extension = originalFilename.substring(dotIndex);
        } else {
            baseName = originalFilename;
        }

        // 组合新的文件名
        return baseName + "-" + RandomUtil.uuStringId() + extension;
    }

}
