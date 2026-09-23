package com.huabo.monitor.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.monitor.entity.TblAssessMark;
import com.huabo.monitor.entity.TblAssessMarkVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yhr
 * @since 2022-08-26
 */
public interface TblAssessMarkMapper extends BaseMapper<TblAssessMark> {

	List<TblAssessMarkVo> findAssessMarkIpage(@Param("queryParam")TblAssessMarkVo queryParam);

	List<TblAssessMarkVo> findMarkByOrgGroupNew(@Param("assId")BigDecimal assId);

	List<Map<String,Object>> findBypersonNew(@Param("assid")BigDecimal assid,@Param("orgid") BigDecimal orgid);

	
	
    /**
     * 项目跟踪---评价对象列表
     *
     * @param
     * @return
     */

    @Select("select * from TBL_ASSESS_MARK o where o.assmarkid in(select max(t.assmarkid) from TBL_ASSESS_MARK t left join tbl_Assess_Staff a on (t.assmarkid = a.assmarkid)  where t.ASSID =#{assId} group by assorgid)")
    <p extends IPage<TblAssessMark>>  p  findMarkByOrgGroup(p page , @Param("assId") BigDecimal assId);





}
