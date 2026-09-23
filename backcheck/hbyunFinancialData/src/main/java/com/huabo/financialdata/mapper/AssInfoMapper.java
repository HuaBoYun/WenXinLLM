package com.huabo.financialdata.mapper;

import com.huabo.financialdata.entity.vo.auxiliaryBook.AuxiliaryBookRequestVo;
import com.huabo.financialdata.entity.vo.auxiliaryBook.AuxiliaryBookResponsetVo;
import com.huabo.financialdata.entity.vo.export.ExportRequestVo;

import java.util.List;

/**
 * 辅助账 - Mapper接口层
 *
 * @author lee
 * @version 1.0.0
 **/
public interface AssInfoMapper {

    //获取分页辅助账数据 辅助信息表
    List<AuxiliaryBookResponsetVo> selectAuxiliaryBookList(AuxiliaryBookRequestVo auxiliaryBookRequestVo);

    //查询总共条数 辅助信息表
    Integer selectAuxiliaryBookCount(AuxiliaryBookRequestVo auxiliaryBookRequestVo);

    //辅助账 辅助余额表
    List<AuxiliaryBookResponsetVo> findAssInfoByBalanceList(AuxiliaryBookRequestVo auxiliaryBookRequestVo);

    //辅助账 辅助余额表
    Integer findAssInfoByBalanceCount(AuxiliaryBookRequestVo auxiliaryBookRequestVo);

    //辅助账  总账
    List<AuxiliaryBookResponsetVo> findAssSumByTotalList(AuxiliaryBookRequestVo auxiliaryBookRequestVo);

    //辅助账  总账条数
    Integer findAssSumByTotalCount(AuxiliaryBookRequestVo auxiliaryBookRequestVo);

	List<String> selectAllAssTypeList(AuxiliaryBookRequestVo auxiliaryBookRequestVo);

	List<AuxiliaryBookResponsetVo> selectListByExportFzxxb(ExportRequestVo exportRequestVo);

	List<AuxiliaryBookResponsetVo> selectListByExportFzyeb(ExportRequestVo exportRequestVo);

	List<AuxiliaryBookResponsetVo> selectListByExportFzzz(ExportRequestVo exportRequestVo);
}
