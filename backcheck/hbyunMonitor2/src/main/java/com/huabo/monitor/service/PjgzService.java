package com.huabo.monitor.service;
import java.math.BigDecimal;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.hbfk.entity.TblStaffUtil;
import com.huabo.monitor.entity.TblAssess;
import com.huabo.monitor.entity.TblAssessMarkVo;

public interface PjgzService extends IService<TblAssess> {
    /**
     * 评价管理-评价跟踪-主页查询
     * @param pageNumber
     * @param assNumnber value = "评价编号") r 评价编号
     * @param assName    value = "项目名称")
     * @param startDate value = "开始时间:格式
     * @param startDates value = "--开始时间"s
     * @param endDate    value = "结束日期")
     * @param endDates  value = "--结束日期"
     * @param endDates  value = "--结束日期"
     *
     * @return
     */
    IPage<TblAssess> initiatePjgl ( Integer pageNumber,String assNumnber, String assName, String startDate, String startDates, String endDate, String endDates,TblStaffUtil staff);

    /**
     * 项目跟踪---评价对象列表
     *
     * @param
     * @return
     */

    IPage<TblAssessMarkVo> findMarkByOrgGroup( IPage<TblAssessMarkVo> iPage, BigDecimal assId);


    IPage<Map<String,Object>>   findByperson(IPage<Map<String,Object>> page, @Param("assid") BigDecimal assid, @Param("orgid")BigDecimal orgid);

    PageInfo<TblAssessMarkVo> findMarkByOrgGroupNew(Integer pageNumber, BigDecimal assId);

    PageInfo<Map<String,Object>>   findBypersonNew(Integer pageNumber, @Param("assid") BigDecimal assid, @Param("orgid")BigDecimal orgid);

}
