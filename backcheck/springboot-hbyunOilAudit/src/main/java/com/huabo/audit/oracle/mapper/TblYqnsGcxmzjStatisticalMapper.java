package com.huabo.audit.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.dto.*;
import com.huabo.audit.oracle.entity.TblYqnsAuditWorkRecordsEntity;
import com.huabo.audit.oracle.entity.TblYqnsGcxmzjZjb;

import java.util.List;

import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;

/**
 * @Classname TblYqnsGcxmzjStatisticalMapper
 * @Description TODO 工程项目造价 - 统计表
 * @Date 2024/5/30 22:26
 * @Created by GJ.C
 */
public interface TblYqnsGcxmzjStatisticalMapper  {

    /**
     * 工程项目造价表-建设单位统计表
     * @param entity
     * @return
     */
    List<TblYqnsGcxmzjJsdwStatisticalDto> selectTblYqnsGcxmzjJsdwStatisticalList(TblYqnsGcxmzjJsdwStatisticalDto entity);





    /**
     * 工程项目造价表-施工单位统计表
     * @param entity
     * @return
     */
    List<TblYqnsGcxmzjSgdwStatisticalDto> selectTblYqnsGcxmzjSgdwStatisticalList(TblYqnsGcxmzjSgdwStatisticalDto entity);


    /**
     * 工程项目造价表-内外部 统计表
     * @param entity
     * @return
     */
    List<TblYqnsGcxmzjNwbStatisticalDto> selectTblYqnsGcxmzjNwbStatisticalList(TblYqnsGcxmzjNwbStatisticalDto entity);


    /**
     * 工程项目造价表-抽审表(按施工单位及额度) 统计表
     * 按照建设单位和内外部分类
     * @param entity
     * @return
     */
    List<TblYqnsGcxmzjSampleStatisticalDto> selectTblYqnsGcxmzjSampleStatisticalList(TblYqnsGcxmzjSampleStatisticalDto entity);



    /**
     * 工程项目造价表-抽审表(按施工单位及额度) 合计 统计表
     *  按照内外部分类
     * @param entity
     * @return
     */
    List<TblYqnsGcxmzjSampleStatisticalDto> selectSampleNwbCountStatisticalList(TblYqnsGcxmzjSampleStatisticalDto entity);




    /**
     * 工程项目造价表-建设单位统计表
     * 单一详情表
     * @param entity
     * @return
     */
    List<TblYqnsGcxmzjJsdwStatisticalToOneDto> selectTblYqnsGcxmzjJsdwStatisticalToOne(TblYqnsGcxmzjJsdwStatisticalToOneDto entity);


}
