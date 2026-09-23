package com.huabo.compliance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.JsonBean;
import com.huabo.compliance.entity.TblAssessMarkVo;
import com.huabo.compliance.entity.TblAssessTargetVo;
import org.apache.ibatis.annotations.Param;


import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface PjjgService {


    IPage<TblAssessTargetVo> getOrgList(BigDecimal assId, Integer pageNumber);

    /**
     *  AssessTarget 查一条
     * @param assesstargetid
     * @return
     */
    TblAssessTargetVo  getMyOneTargetVo( BigDecimal assesstargetid);



   IPage<Map<String,Object>> findByPageBean(Integer startIndex,String orgid, String orgtype);


    IPage<TblAssessTargetVo> findMarkByOrgGroupZuPing( BigDecimal assId,  BigDecimal staffid,Integer pageNumber);


     List<TblAssessMarkVo> getAssessMarkByAssIdAndOrgId(BigDecimal assId, BigDecimal orgId);

    /**
     *  计算总得分
     * @param assid
     * @param orgid
     * @return
     */
    Double getSumScoreByAssidAndOrgId(@Param("assid") BigDecimal assid,@Param("orgid")BigDecimal orgid);

    /**
     *   计算业务
     * @param assId
     * @param orgid
     * @return
     */
    JsonBean calculate(BigDecimal assId, BigDecimal orgid,String token) throws Exception;

    JsonBean yaoSuMingXi(BigDecimal assId, BigDecimal orgid);

    Map<String, Object> yaoSuMingXi2(BigDecimal assId, BigDecimal orgid);


    List<Map<String,Object>> getPingJiaXiangQing(BigDecimal assmarkid,BigDecimal asscatid);

    List<Object[]> ysmxExport(BigDecimal assid);
}
