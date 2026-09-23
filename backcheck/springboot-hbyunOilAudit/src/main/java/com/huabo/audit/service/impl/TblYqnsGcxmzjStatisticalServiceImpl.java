package com.huabo.audit.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.dto.*;
import com.huabo.audit.oracle.entity.TblYqnsAuditWorkRecordsEntity;
import com.huabo.audit.oracle.entity.TblYqnsGcxmzjZjb;
import com.huabo.audit.oracle.mapper.TblYqnsGcxmzjStatisticalMapper;
import com.huabo.audit.oracle.vo.TblYqnsGcxmzjSampleStatisticalVo;
import com.huabo.audit.service.TblYqnsGcxmzjStatisticalService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Classname TblYqnsGcxmzjStatisticalServiceImpl
 * @Description TODO  工程项目造价表-统计表 serviceImpl
 * @Date 2024/05/29 21:56
 * @Created by GJ.C
 */
@Service
public class TblYqnsGcxmzjStatisticalServiceImpl implements TblYqnsGcxmzjStatisticalService {

    @Resource
    private TblYqnsGcxmzjStatisticalMapper tblYqnsGcxmzjStatisticalMapper;
    
    @Resource
    private UserProvider userProvider;


    /**
     * 工程项目造价表-建设单位统计表
     *
     * @return
     */
    @Override
    public JsonBean selectTblYqnsGcxmzjJsdwStatisticalList(String token, Integer queryYear) throws Exception {
//         验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        
        if(queryYear == null) {
        	queryYear = Year.now().getValue();
        }
        
        HashMap<String, Object> result = new HashMap<>();
        List<TblYqnsGcxmzjJsdwStatisticalDto> tblYqnsGcxmzjJsdwStatisticalDtoList = this.selectTblYqnsGcxmzjJsdwStatisticalList(queryYear);
        result.put("listInto", tblYqnsGcxmzjJsdwStatisticalDtoList);

        return ResponseFormat.retParam(1, "查询成功", result);

    }

    /**
     * 工程项目造价表-建设单位统计表- 详情信息
     *
     * @return
     */
    @Override
    public JsonBean selectTblYqnsGcxmzjJsdwStatisticalToOne(String token, String jsdw, Integer queryYear) throws Exception {
//         验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

        if(queryYear == null) {
            queryYear = Year.now().getValue();
        }

        HashMap<String, Object> result = new HashMap<>();
        List<TblYqnsGcxmzjJsdwStatisticalToOneDto>  tblYqnsGcxmzjJsdwStatisticalToOneDtoList= this.selectTblYqnsGcxmzjJsdwStatisticalToOne(jsdw,queryYear);
        result.put("listInfo", tblYqnsGcxmzjJsdwStatisticalToOneDtoList);

        return ResponseFormat.retParam(1, "查询成功", result);

    }


    /**
     * 工程项目造价表-施工单位统计表
     *
     * @return
     */
    @Override
    public JsonBean selectTblYqnsGcxmzjSgdwStatisticalList(String token, Integer queryYear) throws Exception {
//         验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        HashMap<String, Object> result = new HashMap<>();
        if(queryYear == null) {
        	queryYear = Year.now().getValue();
        }
        List<TblYqnsGcxmzjSgdwStatisticalDto> tblYqnsGcxmzjSgdwStatisticalDtoList = this.selectTblYqnsGcxmzjSgdwStatisticalList(queryYear);
        result.put("listInto", tblYqnsGcxmzjSgdwStatisticalDtoList);

        return ResponseFormat.retParam(1, "查询成功", result);

    }


    /**
     * 工程项目造价表-内外部 统计表
     *
     * @return
     */
    @Override
    public JsonBean selectTblYqnsGcxmzjNwbStatisticalList(String token, Integer queryYear) throws Exception {
//         验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        HashMap<String, Object> result = new HashMap<>();
        if(queryYear == null) {
        	queryYear = Year.now().getValue();
        }
        List<TblYqnsGcxmzjNwbStatisticalDto> selectTblYqnsGcxmzjNwbStatisticalList = this.selectTblYqnsGcxmzjNwbStatisticalList(queryYear);
        result.put("listInto", selectTblYqnsGcxmzjNwbStatisticalList);

        return ResponseFormat.retParam(1, "查询成功", result);

    }

    /**
     * 工程项目造价表-抽审表(按施工单位及额度) 统计表
     *
     * @return 
     */
    @Override
    public JsonBean selectTblYqnsGcxmzjSampleStatisticalList(String token, Integer queryYear) throws Exception {
//         验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        HashMap<String, Object> result = new HashMap<>();
        try {
        	if(queryYear == null) {
            	queryYear = Year.now().getValue();
            }
            // 基础数据
            List<TblYqnsGcxmzjSampleStatisticalDto> sampleStatisticalDtoList = this.selectTblYqnsGcxmzjSampleStatisticalList(queryYear);
            // 合计基础数据
            List<TblYqnsGcxmzjSampleStatisticalDto> sampleNwbCountStatisticalList = this.selectSampleNwbCountStatisticalList(queryYear);

            //处理基础数据 获取小计
            List<TblYqnsGcxmzjSampleStatisticalDto> sampleStatisticalList = this.handleTblYqnsGcxmzjSampleStatisticalList(sampleStatisticalDtoList);

            // 处理 [合计] 基础数据 获取小计
            List<TblYqnsGcxmzjSampleStatisticalDto> sampleNwbCountList = this.handleSampleNwbCountStatisticalList(sampleNwbCountStatisticalList);

            sampleNwbCountList.addAll(sampleStatisticalList);
            // 分组
            Map<String, List<TblYqnsGcxmzjSampleStatisticalDto>> groupedByJsdw = sampleNwbCountList.stream().collect(Collectors.groupingBy(TblYqnsGcxmzjSampleStatisticalDto::getJsdw));
            List<TblYqnsGcxmzjSampleStatisticalVo> groupedSampleList = new ArrayList<>();
            for (Map.Entry<String, List<TblYqnsGcxmzjSampleStatisticalDto>> entry : groupedByJsdw.entrySet()) {
                groupedSampleList.add(new TblYqnsGcxmzjSampleStatisticalVo(entry.getKey(), entry.getValue()));
            }
            result.put("listInto", groupedSampleList);
        }catch (Exception e){
            e.printStackTrace();
        }


        return ResponseFormat.retParam(1, "查询成功", result);

    }


    /**
     * 对抽审表(按施工单位及额度) 进行处理 达到预期页面
     * 对合计之外的基础数据 计算小计
     *
     * @param tblYqnsGcxmzjSampleStatisticalDtoList
     * @return
     * @throws Exception
     */
    public List<TblYqnsGcxmzjSampleStatisticalDto> handleTblYqnsGcxmzjSampleStatisticalList(List<TblYqnsGcxmzjSampleStatisticalDto> tblYqnsGcxmzjSampleStatisticalDtoList) throws Exception {

        List<TblYqnsGcxmzjSampleStatisticalDto> listAll = new ArrayList<>();
        Map<String, List<TblYqnsGcxmzjSampleStatisticalDto>> jsdwMap = tblYqnsGcxmzjSampleStatisticalDtoList.stream().collect(
                Collectors.groupingBy(TblYqnsGcxmzjSampleStatisticalDto::getJsdw)
        );
        for (Map.Entry<String, List<TblYqnsGcxmzjSampleStatisticalDto>> entry : jsdwMap.entrySet()) {
            String key = entry.getKey();
            List<TblYqnsGcxmzjSampleStatisticalDto> valueList = entry.getValue();
            TblYqnsGcxmzjSampleStatisticalDto tblYqnsGcxmzjSampleStatisticalDto = this.getSampleCount(key, valueList);
            valueList.add(0,tblYqnsGcxmzjSampleStatisticalDto);
             getSampleSummed(valueList);
            listAll.addAll(valueList);
        }
        return listAll;
    }

    /**
     * 对抽审表(按施工单位及额度) 进行处理 达到预期页面
     * 对合计进行处理
     *
     * @param tblYqnsGcxmzjSampleStatisticalDtoList
     * @return
     * @throws Exception
     */
    public List<TblYqnsGcxmzjSampleStatisticalDto> handleSampleNwbCountStatisticalList(List<TblYqnsGcxmzjSampleStatisticalDto> tblYqnsGcxmzjSampleStatisticalDtoList) throws Exception {

        List<TblYqnsGcxmzjSampleStatisticalDto> listAll = new ArrayList<>();
        // 获取合计的小计汇总数据
        TblYqnsGcxmzjSampleStatisticalDto entityCount = this.getSampleCount("合计", tblYqnsGcxmzjSampleStatisticalDtoList);
        // 计算逻辑处理 小计数据
        tblYqnsGcxmzjSampleStatisticalDtoList.add(0,entityCount);
        getSampleSummed(tblYqnsGcxmzjSampleStatisticalDtoList);
        listAll.addAll(tblYqnsGcxmzjSampleStatisticalDtoList);
        return listAll;
    }

    /**
     * 计算小计 - 按照逻辑进行计算 (所有列 都按照抽审规则计算)
     * 待考证是否正确 抽审项目数量=项目数-工程建设公司项目数
     * 抽审比例 （项目数-工程建设公司项目数）/项目数
     * @param tblYqnsGcxmzjSampleStatisticalDtoList
     * @param
     * @return
     */
    private void getSampleSummed(List<TblYqnsGcxmzjSampleStatisticalDto> tblYqnsGcxmzjSampleStatisticalDtoList) {
        // 处理小计的 抽审比例/抽审项目数量/抽审项目金额
        // 获取工程建筑公司 小计
        List<TblYqnsGcxmzjSampleStatisticalDto> engineeringConstructionList = tblYqnsGcxmzjSampleStatisticalDtoList.stream().filter(x -> x.getNwb().equals("工程建设公司")).collect(Collectors.toList());
        TblYqnsGcxmzjSampleStatisticalDto engineeringConstruction = new TblYqnsGcxmzjSampleStatisticalDto();

        // 判断是否有工程建筑公司
        if (engineeringConstructionList != null && engineeringConstructionList.size() > 0) {
            engineeringConstruction = engineeringConstructionList.get(0);
        } else {
            // 抽审合计个数
            engineeringConstruction.setEsscjeCount(new BigDecimal(0));
            // 抽审合计金额合计
            engineeringConstruction.setEsscjeSum(new BigDecimal(0));
            engineeringConstruction.setHtbhCount(new BigDecimal(0));
        }
        TblYqnsGcxmzjSampleStatisticalDto finalEngineeringConstruction = engineeringConstruction;
        tblYqnsGcxmzjSampleStatisticalDtoList.stream().forEach(entity->{
            // 合计（小计 所有） 抽审比例 =  (合计小计 - 工程建筑公司小计)/合计小计
            BigDecimal esscjeCount = entity.getHtbhCount().subtract(finalEngineeringConstruction.getHtbhCount());
            BigDecimal samplingRatio = esscjeCount.divide(entity.getHtbhCount(),2, BigDecimal.ROUND_UP);
            // 抽审项目金额 抽审金额-工程建筑公司抽审金额
            BigDecimal esscjeSum = entity.getEsscjeSum().subtract(finalEngineeringConstruction.getEsscjeSum());
            entity.setSamplingRatio(samplingRatio);
            entity.setEsscjeCount(esscjeCount);
            entity.setEsscjeSum(esscjeSum);
        });

    }


    /**
     * 获取小计 - 获取合计值
     *
     * @param key
     * @param valueList
     * @return
     */
    private TblYqnsGcxmzjSampleStatisticalDto getSampleCount(String key, List<TblYqnsGcxmzjSampleStatisticalDto> valueList) {


        TblYqnsGcxmzjSampleStatisticalDto tblYqnsGcxmzjSampleStatisticalDto = new TblYqnsGcxmzjSampleStatisticalDto();

        try {
//        BigDecimal esscjeCount = BigDecimal.valueOf(valueList.stream().collect(Collectors.summingDouble(x -> x.getEsscjeCount().doubleValue())));
        BigDecimal esscjeSum = BigDecimal.valueOf(valueList.stream().collect(Collectors.summingDouble(x -> x.getEsscjeSum().doubleValue())));
        BigDecimal htbhCount = BigDecimal.valueOf(valueList.stream().collect(Collectors.summingDouble(x -> x.getHtbhCount().doubleValue())));
        BigDecimal edjeSum = BigDecimal.valueOf(valueList.stream().collect(Collectors.summingDouble(x -> x.getEdjeSum().doubleValue())));
//        BigDecimal samplingRatio = esscjeCount.divide(htbhCount,2, BigDecimal.ROUND_UP);

        BigDecimal countOverNineHund = BigDecimal.valueOf(valueList.stream().collect(Collectors.summingDouble(x -> x.getCountOverNineHund().doubleValue())));
        BigDecimal sumOverNineHund = BigDecimal.valueOf(valueList.stream().collect(Collectors.summingDouble(x -> x.getSumOverNineHund().doubleValue())));
        BigDecimal countHundToNineHund = BigDecimal.valueOf(valueList.stream().collect(Collectors.summingDouble(x -> x.getCountHundToNineHund().doubleValue())));
        BigDecimal sumHundToNineHund = BigDecimal.valueOf(valueList.stream().collect(Collectors.summingDouble(x -> x.getSumHundToNineHund().doubleValue())));
        BigDecimal countFiftyToHund = BigDecimal.valueOf(valueList.stream().collect(Collectors.summingDouble(x -> x.getCountFiftyToHund().doubleValue())));
        BigDecimal sumFiftyToHund = BigDecimal.valueOf(valueList.stream().collect(Collectors.summingDouble(x -> x.getSumFiftyToHund().doubleValue())));
        BigDecimal countTwentyToFifty = BigDecimal.valueOf(valueList.stream().collect(Collectors.summingDouble(x -> x.getCountTwentyToFifty().doubleValue())));
        BigDecimal sumTwentyToFifty = BigDecimal.valueOf(valueList.stream().collect(Collectors.summingDouble(x -> x.getSumTwentyToFifty().doubleValue())));
        BigDecimal countUnderTwenty = BigDecimal.valueOf(valueList.stream().collect(Collectors.summingDouble(x -> x.getCountUnderTwenty().doubleValue())));
        BigDecimal sumUnderTwenty = BigDecimal.valueOf(valueList.stream().collect(Collectors.summingDouble(x -> x.getSumUnderTwenty().doubleValue())));

        tblYqnsGcxmzjSampleStatisticalDto.setJsdw(key);
        tblYqnsGcxmzjSampleStatisticalDto.setNwb("小计");
        tblYqnsGcxmzjSampleStatisticalDto.setSort(0);
//        tblYqnsGcxmzjSampleStatisticalDto.setEsscjeCount(esscjeCount);
        tblYqnsGcxmzjSampleStatisticalDto.setEsscjeSum(esscjeSum);
        tblYqnsGcxmzjSampleStatisticalDto.setHtbhCount(htbhCount);
        tblYqnsGcxmzjSampleStatisticalDto.setEdjeSum(edjeSum);
//        tblYqnsGcxmzjSampleStatisticalDto.setSamplingRatio(samplingRatio);

        tblYqnsGcxmzjSampleStatisticalDto.setCountOverNineHund(countOverNineHund);
        tblYqnsGcxmzjSampleStatisticalDto.setSumOverNineHund(sumOverNineHund);
        tblYqnsGcxmzjSampleStatisticalDto.setCountHundToNineHund(countHundToNineHund);
        tblYqnsGcxmzjSampleStatisticalDto.setSumHundToNineHund(sumHundToNineHund);
        tblYqnsGcxmzjSampleStatisticalDto.setCountFiftyToHund(countFiftyToHund);
        tblYqnsGcxmzjSampleStatisticalDto.setSumFiftyToHund(sumFiftyToHund);
        tblYqnsGcxmzjSampleStatisticalDto.setCountTwentyToFifty(countTwentyToFifty);
        tblYqnsGcxmzjSampleStatisticalDto.setSumTwentyToFifty(sumTwentyToFifty);
        tblYqnsGcxmzjSampleStatisticalDto.setCountUnderTwenty(countUnderTwenty);
        tblYqnsGcxmzjSampleStatisticalDto.setSumUnderTwenty(sumUnderTwenty);
        }catch (Exception e){
            e.printStackTrace();
        }
        return tblYqnsGcxmzjSampleStatisticalDto;

    }


    // ============================= 基础方法 ==============================

    /**
     * 工程项目造价表-建设单位统计表
     * @param queryYear 
     *
     * @param
     * @return
     */
    private List<TblYqnsGcxmzjJsdwStatisticalDto> selectTblYqnsGcxmzjJsdwStatisticalList(Integer queryYear) {
        TblYqnsGcxmzjJsdwStatisticalDto entity = new TblYqnsGcxmzjJsdwStatisticalDto();
        entity.setQueryYear(queryYear);
        return tblYqnsGcxmzjStatisticalMapper.selectTblYqnsGcxmzjJsdwStatisticalList(entity);
    }



    /**
     * 工程项目造价表-建设单位统计表 - 详情信息
     * @param queryYear
     *
     * @param
     * @return
     */
    private List<TblYqnsGcxmzjJsdwStatisticalToOneDto> selectTblYqnsGcxmzjJsdwStatisticalToOne(String jsdw, Integer queryYear) {
        TblYqnsGcxmzjJsdwStatisticalToOneDto entity = new TblYqnsGcxmzjJsdwStatisticalToOneDto();
        entity.setQueryYear(queryYear);
        entity.setJsdw(jsdw);
        return tblYqnsGcxmzjStatisticalMapper.selectTblYqnsGcxmzjJsdwStatisticalToOne(entity);
    }



    /**
     * 工程项目造价表-施工单位统计表
     * @param queryYear 
     *
     * @return
     */
    private List<TblYqnsGcxmzjSgdwStatisticalDto> selectTblYqnsGcxmzjSgdwStatisticalList(Integer queryYear) {
        TblYqnsGcxmzjSgdwStatisticalDto entity = new TblYqnsGcxmzjSgdwStatisticalDto();
        entity.setQueryYear(queryYear);
        return tblYqnsGcxmzjStatisticalMapper.selectTblYqnsGcxmzjSgdwStatisticalList(entity);
    }

    /**
     * 工程项目造价表-内外部 统计表
     * @param queryYear 
     *
     * @return
     */
    private List<TblYqnsGcxmzjNwbStatisticalDto> selectTblYqnsGcxmzjNwbStatisticalList(Integer queryYear) {
        TblYqnsGcxmzjNwbStatisticalDto entity = new TblYqnsGcxmzjNwbStatisticalDto();
        entity.setQueryYear(queryYear);
        return tblYqnsGcxmzjStatisticalMapper.selectTblYqnsGcxmzjNwbStatisticalList(entity);
    }


    /**
     * 工程项目造价表-抽审表(按施工单位及额度) 统计表
     * @param queryYear 
     *
     * @return
     */
    private List<TblYqnsGcxmzjSampleStatisticalDto> selectTblYqnsGcxmzjSampleStatisticalList(Integer queryYear) {
        TblYqnsGcxmzjSampleStatisticalDto entity = new TblYqnsGcxmzjSampleStatisticalDto();
        entity.setQueryYear(queryYear);
        return tblYqnsGcxmzjStatisticalMapper.selectTblYqnsGcxmzjSampleStatisticalList(entity);
    }


    /**
     * 工程项目造价表-抽审表(按施工单位及额度) 合计 统计表
     * 按照内外部分类
     *
     * @return
     */
    private List<TblYqnsGcxmzjSampleStatisticalDto> selectSampleNwbCountStatisticalList(Integer queryYear) {
        TblYqnsGcxmzjSampleStatisticalDto entity = new TblYqnsGcxmzjSampleStatisticalDto();
        entity.setQueryYear(queryYear);
        return tblYqnsGcxmzjStatisticalMapper.selectSampleNwbCountStatisticalList(entity);
    }

}
