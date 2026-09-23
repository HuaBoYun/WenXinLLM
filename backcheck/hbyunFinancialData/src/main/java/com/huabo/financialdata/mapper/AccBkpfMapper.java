package com.huabo.financialdata.mapper;


import com.huabo.financialdata.entity.entity.TblAccBkpf;
import com.huabo.financialdata.entity.vo.accBkpf.AccBkpfRequestVo;
import com.huabo.financialdata.entity.vo.accBkpf.AccBkpfResponseVo;
import com.huabo.financialdata.entity.vo.export.ExportRequestVo;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 凭证库 - Mapper接口层
 *
 * @author lee
 * @version 1.0.0
 **/
public interface AccBkpfMapper {

    /**
     * 查询辅助账簿列表
     * @param accBkpfRequestVo 辅助账簿请求对象
     * @return 辅助账簿响应对象列表
     */
    List<AccBkpfResponseVo> selectAuxiliaryBookList(AccBkpfRequestVo accBkpfRequestVo);

    /**
     * 通过条件查询凭证实体类
     * @param pzh 凭证编号
     * @param pzDate 凭证日期
     * @param book
     * @param year
     * @return
     */
    List<TblAccBkpf> findAccBkpfAmonth(@Param("pzh") String pzh,
                                       @Param("pzDate") String pzDate,
                                       @Param("book") String book,
                                       @Param("year") String year);

    Integer selectAuxiliaryBookCount(AccBkpfRequestVo accBkpfRequestVo);

	List<AccBkpfResponseVo> selectListByExportPzk(ExportRequestVo exportRequestVo);
}
