package com.huabo.financialdata.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.financialdata.entity.entity.AccBseg;
import com.huabo.financialdata.entity.entity.AccBsegQuery;
import com.huabo.financialdata.entity.entity.Account;
import com.huabo.financialdata.entity.vo.accBseg.AccBsegQueryInfoVO;
import com.huabo.financialdata.entity.vo.detailedBook.DetailedBookRequestVo;
import com.huabo.financialdata.entity.vo.detailedBook.DetailedBookResponseVo;
import com.huabo.financialdata.entity.vo.diaryBook.DiaryBookRequestVo;
import com.huabo.financialdata.entity.vo.diaryBook.DiaryBookResponseVo;
import com.huabo.financialdata.entity.vo.export.ExportRequestVo;

import java.util.List;

/**
 * 科目余额表 - Mapper接口层
 *
 * @author lee
 * @version 1.0.0
 **/
public interface AccBsegMapper extends BaseMapper<AccBseg> {

    /**
     * 条件查询
     *
     * @param query 查询请求参数
     * @return 返回结果集合
     */
    List<AccBsegQueryInfoVO> selectByCondition(AccBsegQuery query);

    //明细账查询
    List<DetailedBookResponseVo> selectAccBseg(DetailedBookRequestVo detailedBookRequestVo);

    Integer selectAccBsegCount(DetailedBookRequestVo detailedBookRequestVo);

    //获取accid
    Account selectAccid(DiaryBookRequestVo diaryBookRequestVo);

    List<DiaryBookResponseVo> selectDiaryBookList(DiaryBookRequestVo diaryBookRequestVo);

    Integer selectDiaryBookCount(DiaryBookRequestVo diaryBookRequestVo);

	String selectDefaultAccid(DetailedBookRequestVo detailedBookRequestVo);

	DetailedBookResponseVo selectCurrentMonthyMdc(DetailedBookRequestVo detailVO);

	DiaryBookResponseVo selectPreDiaryBookRequestPreVo(DiaryBookRequestVo preBookRequestVo);

	DiaryBookResponseVo selectSumMonthyMdc(DiaryBookRequestVo preBookRequestVo);

	DiaryBookResponseVo selectSumMdcByDayNo(DiaryBookRequestVo preBookRequestVo);

	List<DiaryBookResponseVo> selectListByExportRjz(ExportRequestVo exportRequestVo);

	List<DetailedBookResponseVo> selectListByExportMxz(ExportRequestVo exportRequestVo);

}
