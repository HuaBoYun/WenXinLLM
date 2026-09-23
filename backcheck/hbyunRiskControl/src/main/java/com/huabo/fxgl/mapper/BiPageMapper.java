package com.huabo.fxgl.mapper;

import com.huabo.fxgl.entity.BiPage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author LiYe
 * @since 2022-08-11
 */
@Repository
public interface BiPageMapper extends BaseMapper<BiPage> {
    @Select("select * from TBL_BI_PAGE TBP WHERE 1=1" +
            " AND TBP.UNIT=#{orgid} AND TBP.PAGEBODY  is not null AND TBP.TREEID=#{userid} AND TYPE != 1" +
            " order by TBP.SORT")
    List<BiPage> selectList(@Param("orgid") BigDecimal orgid, @Param("userid") BigDecimal userid);


    @Select("select * from TBL_BI_PAGE  WHERE PAGEBODY  is not null and PAGEID in (${pageids}) order by SORT")
    List<BiPage> selectListInPageid(@Param("pageids") String pageids);
    
    
    @Select("select PAGEID from TBL_YY_COMPANY_PAGE WHERE 1=1" +
            " AND companyid=#{companyid} ")
    List<String> getBiPageByCompanyid(@Param("companyid") BigDecimal companyid);
}
