package com.huabo.monitor.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.pagehelper.PageInfo;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.huabo.monitor.entity.TblAssessMark;
import com.huabo.monitor.entity.TblAssessMarkVo;
import com.huabo.monitor.entity.TblAssessTargetVo;
import org.apache.ibatis.annotations.Param;


import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface PjjgService {


    IPage<TblAssessTargetVo> getOrgList(BigDecimal assId, Integer pageNumber);
    
    PageInfo<TblAssessTargetVo> getOrgListNew (BigDecimal assId, Integer pageNumber)  throws Exception;


    /**
     *  AssessTarget 查一条
     * @param assesstargetid
     * @return
     */
    TblAssessTargetVo  getMyOneTargetVo( BigDecimal assesstargetid);



   IPage<Map<String,Object>> findByPageBean(Integer startIndex,String orgid, String orgtype);


    IPage<TblAssessTargetVo> findMarkByOrgGroupZuPing( BigDecimal assId,  BigDecimal staffid,Integer pageNumber,TblStaffUtil staff);

     PageInfo<TblAssessTargetVo> findMarkByOrgGroupZuPingNew( BigDecimal assId,  BigDecimal staffid,Integer pageNumber) throws Exception;

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
