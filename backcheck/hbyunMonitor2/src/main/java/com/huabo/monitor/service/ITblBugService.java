package com.huabo.monitor.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.monitor.entity.TblBug;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.monitor.entity.TblTestplan;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yhr
 * @since 2022-08-26
 */
public interface ITblBugService extends IService<TblBug> {

	IPage<TblBug> findALLHy(TblBug tblBug, Integer pageNumber, String startdate, String enddate, String orgid, String orgtype);
	
    List<TblBug> findByCriterionId(BigDecimal criterionId);
    
    TblBug findById(BigDecimal bigDecimal);
    
    TblBug findByCode(String code, String type,String orgid);
    
    void add(TblBug tblBug);
}
