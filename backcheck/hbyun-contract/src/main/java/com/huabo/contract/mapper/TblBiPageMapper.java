package com.huabo.contract.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.contract.entity.TblBiPage;

import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2022-04-08
 */
public interface TblBiPageMapper extends BaseMapper<TblBiPage> {

    @Select("select * from TBL_BI_PAGE TBP WHERE TBP.UNIT=#{orgid} AND TBP.PAGEBODY  is not null AND TBP.TREEID=#{staffid} AND TYPE != 1 order by TBP.SORT")
    List<TblBiPage> findByOrgid(BigDecimal staffid, BigDecimal orgid);

    @Select("select * from TBL_BI_PAGE  WHERE PAGEBODY  is not null and PAGEID in (#{pageid})   order by SORT")
    List<TblBiPage> findBypageid(String pageid);
}
