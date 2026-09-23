package com.huabo.compliance.mapper;

import com.huabo.compliance.entity.TblTask;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yhr
 * @since 2022-08-30
 */
public interface TblTaskMapper extends BaseMapper<TblTask> {



     @Select("\n" +
             "select   DISTINCT tas.staffid from\n" +
             "      TBL_ASSESS_MARK  am  left join TBL_ASSESS_STAFF tas on am.assmarkid = tas.assmarkid\n" +
             "          left join TBL_ASSESS ae on am.assid = ae.ASSID\n" +
             "where ae.ASSID = #{assid}")
     List<BigDecimal> queryStaffIdsByAssid(@Param("assid") BigDecimal assid);

}
