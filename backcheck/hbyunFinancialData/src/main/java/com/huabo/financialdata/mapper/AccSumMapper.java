package com.huabo.financialdata.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.financialdata.entity.entity.AccSum;
import com.huabo.financialdata.entity.entity.AccSumQuery;
import com.huabo.financialdata.entity.vo.accBkpf.AccBkpfRequestVo;
import com.huabo.financialdata.entity.vo.accSumTotal.AccSumTotalRequestVo;
import com.huabo.financialdata.entity.vo.accSumTotal.AccSumTotalResponseVo;
import com.huabo.financialdata.entity.vo.export.ExportRequestVo;

import java.util.List;

/**
 * 科目余额表 - Mapper接口层
 *
 * @author lee
 * @version 1.0.0
 **/
public interface AccSumMapper extends BaseMapper<AccSum> {

    /**
     * 条件查询
     *
     * @param query 查询请求参数
     * @return 返回结果集合
     */
    List<AccSum> selectByCondition(AccSumQuery query);

    //查询月数
    Integer selectMaxMonth(AccBkpfRequestVo accBkpfRequestVo);

    //查询总分类账信息
    List<AccSumTotalResponseVo> findAccSumTotal(AccSumTotalRequestVo accSumTotalRequestVo);

    //查询总共条数
    Integer findTotalCount(AccSumTotalRequestVo accSumTotalRequestVo);

	List<AccSum> selectListByExportKmyeb(ExportRequestVo exportRequestVo);

}
